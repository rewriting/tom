# Réorganisation du dépôt : version validée dans src/ et test/, prototype à part — design

Date : 2026-10-23
Branche : `tom-go`
Statut : validé en brainstorming, en attente de relecture du spec

## 1. Problème

Après J−1…J3 (spec `2026-10-22-tom-go-backend-design.md`), le dépôt mélange :

- le compilateur Tom+Java étendu (`src/`, `stable/`) ;
- du code Go **validé et utilisé** par la chaîne Tom+Java → Go, mais rangé
  dans `tomgo/` (runtime `sl`, `sharedobjects`, Gom écrit en Go, CLI
  `gom-block`, harnais T0–T3, ports Tom+Go) ;
- le **prototype** tomgo (compilateur Tom écrit à la main en Go, non
  terminé), dans le même `tomgo/`.

On ne sait plus ce qui est correct, validé et utilisable — par Tom+Java
aujourd'hui et par le futur compilateur Tom+Go — de ce qui est un
prototype dont on peut seulement récupérer du code.

## 2. Principes

1. `legacy/` reste le témoin humain, inchangé.
2. Tout ce qui est validé vit dans `src/` (sources) et `test/` (tests),
   la version Go dans un **arbre parallèle** qui reproduit l'arborescence
   Java : `src/tom/X` (Java) ↔ `src/go/tom/X` (Go) ; `test/X` (Tom+Java) ↔
   `test/go/X` (Tom+Go).
3. Le prototype est déplacé dans `prototype/tomgo/`, **gelé et autonome** :
   il garde son `go.mod` et ses propres copies, compile et passe ses tests,
   n'évolue plus. On y puise du code par copie.
4. Les chemins d'import Go ne contiennent ni `src/` ni `stable/` : le module
   s'appelle `tom` et la copie utilisée se choisit par `replace` (équivalent
   du classpath Java). Le dernier élément d'un chemin d'import est le nom
   du package (`tom/library/sl` → package `sl`).

Raison de l'arbre parallèle plutôt que des `.go` dans les répertoires
Java : le bootstrap (J4) produira des sources Tom+Go du compilateur portant
les mêmes noms que les sources Tom+Java (`Compiler.t`, `GoGenerator.t`…), qui
ne peuvent pas partager un répertoire ; un module Go enraciné dans
`src/tom/` engloberait tout l'arbre Java ; Ant copie des arborescences
entières qui embarqueraient des fichiers Go.

## 3. Disposition cible

```
go.work                              use ./src/go/tom ./test/go
magefiles/                           magefile (module à part) : portJava, portGo,
                                     coverage, regenGomast, selfBootstrap
legacy/                              inchangé
src/tom/…                            compilateur Tom+Java, runtimes Java (inchangés)
src/tom/library/mapping/go/          mappings Go (inchangé)
src/go/tom/go.mod                    module tom
src/go/tom/library/sl/               ← tomgo/stable/library/sl          import "tom/library/sl"
src/go/tom/library/sharedobjects/    ← tomgo/stable/library/sharedobjects
src/go/tom/gom/parser/               ← tomgo/stable/gom/parser          import "tom/gom/parser"
src/go/tom/gom/backend/              ← tomgo/stable/gom/backend (sans --target java)
src/go/tom/gom/adt/                  ← tomgo/stable/library/gomast      import "tom/gom/adt"
src/go/tom/gom/cmd/gomgo/            ← tomgo/cmd/tomgo (gom, gom-batch, gom-block)
test/…                               tests Tom+Java (inchangés)
test/go/go.mod                       module tomtest ; replace tom => ../../src/go/tom
test/go/<rel>.t                      ports Tom+Go, miroir de test/ ← tomgo/tests/port/*.t
test/go/port_test.go                 ← tomgo/tests/port/port_test.go
test/go/testdata/{golden,oracle}/    ← tomgo/tests/port/testdata/*
test/go/aterm/                       ← tomgo/tests/port/aterm
test/go/harness/                     ← tomgo/tests/tomjava
test/go/javaparity/                  ← tomgo/tests/javaparity
test/go/smoke/                       ← tomgo/tests/gosmoke
test/go/coverage/                    ← tomgo/tests/coverage
doc/go/                              j0-go-backend.md, port-dashboard.md, coverage.md
                                     (← tomgo/reports/), README des emplacements
prototype/tomgo/                     tout le reste de tomgo/, gelé (module tom/tomgo)
```

Choix de nommage :

- `gomast` devient le package `tom/gom/adt`, jumeau du package Java
  `tom.gom.adt` ; il est régénéré depuis `src/tom/gom/adt/*.gom` (les
  originaux, identiques à la copie `tomgo/src/gom/adt/`, qui disparaît du
  côté validé).
- Le binaire du Gom écrit en Go s'appelle `gomgo` (pas `gom`, pour ne pas
  masquer la commande Java) ; la variable d'environnement devient
  `GOMGO_BIN`.

## 4. Ce qui est déplacé, ce qui reste

- Tout `tomgo/` est déplacé par `git mv tomgo prototype/tomgo`, puis les
  fichiers validés en sont **copiés** (pas déplacés) vers `src/go` et
  `test/go`, pour que le prototype reste autonome. Le prototype et la copie
  ne peuvent pas tous deux « hériter » du chemin : le renommage suit le
  prototype, et l'historique des copies reste accessible par
  `git log --follow -C` ; le message du commit de copie donne la
  correspondance des chemins d'origine.
- N'est pas copié vers `src/go` : la cible `--target java` du Gom Go (et le
  `gom.jar` embarqué), `scan-hooks`, `tomast`, `aterm` (bibliothèque),
  `stable/tom/*`, `stable/platform`, les autres `cmd/`, `tests/tom/compile`,
  `tests/testdata`, les rapports des phases 1–6.
- Dans `prototype/tomgo/`, les fichiers dont une copie vit désormais dans
  `src/go` ou `test/go` reçoivent un en-tête
  `// Frozen prototype copy — the maintained version is src/go/tom/<…>.`
  et `prototype/tomgo/README.md` explique le statut (gelé, réservoir de
  code, version maintenue ailleurs).

## 5. Conséquences sur le code

| Élément | Changement |
|---|---|
| Imports Go (code validé) | `tom/tomgo/stable/library/sl` → `tom/library/sl`, `…/sharedobjects` → `tom/library/sharedobjects`, `…/gom/parser` → `tom/gom/parser`, `…/gom/backend` → `tom/gom/backend`, `…/library/gomast` → `tom/gom/adt` |
| Backend Gom Go | le code généré importe `tom/library/sharedobjects` et `tom/library/sl` |
| `src/tom/engine/parser/TomParserTool.t` | lance `gomgo gom-block` via `$GOMGO_BIN` ou le `PATH` (code exécuté seulement avec `-go`) |
| Ports `test/go/*.t` | `import "tom/library/sl"` (fin de `tom/tomgo/…`) ; goldens régénérés |
| Harnais `test/go/harness` | racine du dépôt détectée par `build.xml` + `test/go/go.mod` ; module généré : `require tom v0.0.0` + `replace tom => <TOM_HOME>/lib/go/tom` ; construit `gomgo` depuis `src/go/tom/gom/cmd/gomgo` |
| `build.xml` | `src.dist` et `stable.dist` copient `src/go/tom/**` (hors `*_test.go` et `testdata/`) dans `<dist>/lib/go/tom/` |
| `bootinstall` | inchangé : `stable/` n'a pas besoin du Go tant que le compilateur est Java ; la copie de `src/go` vers `stable/go` sera ajoutée en J4 |
| `.gitignore` | ajuster les chemins des binaires (`gomgo`) |
| `CLAUDE.md` | réécrit autour de la carte : `legacy/` témoin, `src/`+`test/` validé, `prototype/` réservoir ; cookbook à jour |
| Spec et plan J−1…J3 | une note en tête renvoie à ce spec pour les nouveaux chemins |

Utilisateur final : son `go.mod` contient
`require tom v0.0.0` et `replace tom => $TOM_HOME/lib/go/tom` ; il importe
`"tom/library/sl"` s'il utilise des stratégies ; le code produit par Gom
importe lui-même `sl` et `sharedobjects`.

## 6. Vérification

La réorganisation ne change aucun comportement :

- mêmes résultats avant/après : T0 (défaut et `-np`, planchers inchangés),
  T2 (13 OK, 0 divergence), T3 (13/17), smoke (8 cas + options), tests de
  `sl` et du Gom Go, auto-amorce de `tom/gom/adt` (régénération
  octet-identique) ;
- goldens : seules les lignes d'import diffèrent (vérifié par diff) ;
  oracles inchangés (leur `source_sha256` porte sur `test/<rel>`) ;
- `cd prototype/tomgo && go test ./...` vert ;
- `go test ./...` depuis `src/go/tom` et `test/go` (ou via `go.work`) vert ;
- `grep -rn "tomgo" src test magefiles doc/go` ne renvoie que des mentions
  historiques volontaires ;
- la distribution contient `lib/go/tom/go.mod` et les packages, et un
  programme généré se construit avec `replace tom => $TOM_HOME/lib/go/tom`.

## 7. Hors périmètre

- Publication `go get` (le module `tom` n'a pas de domaine).
- Copie du Go dans `stable/` (J4).
- Distribution d'un binaire `gomgo` précompilé dans `<dist>/bin`.
- Toute évolution fonctionnelle (le code est déplacé, pas modifié, hors
  chemins d'import et noms).
