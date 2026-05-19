# TOM → Go : portage du compilateur

Ce fichier décrit l'objectif, l'état du dépôt, la stratégie de portage,
et les conventions de travail. Il est lu en début de session.

---

## 1. Objectif

**Itération en cours** : produire un **outil Go autonome** capable de :

- lire un fichier `.gom` (signature algébrique, sans hooks),
- générer un **package Go** correspondant,
- reposant sur un **portage Go de shared-objects** (hash-consing, partage
  maximum des structures de données).

Ce jalon couvre les **phases 0, 1 et 2** du plan global ci-dessous.
**Les phases 3 à 6 sont différées** et ne doivent pas être touchées sans
validation explicite.

> Le code Java de référence est `stable/` (compilateur déjà bootstrappé,
> généré à partir des sources Tom+Java de `src/`). C'est cette implémentation
> Java qui sert de spécification exécutable pour le portage.

Trajectoire long terme (visibilité, hors scope immédiat) : porter
l'ensemble du pipeline TOM en Go, puis réécrire le compilateur en Tom+Go
et l'auto-compiler avec l'amorce.

---

## 2. Cartographie du dépôt (état observé)

| Chemin                  | Contenu                                                         |
| ----------------------- | --------------------------------------------------------------- |
| `src/`                  | Sources Tom+Java du compilateur (nécessite un TOM existant)     |
| `stable/`               | Compilateur Java **généré** (251 fichiers `.java`) — référence  |
| `stable/tom/engine/`    | Phases : starter → parser → checker → typer → desugarer → transformer → expander → compiler → optimizer → backend → prettyprinter |
| `stable/tom/gom/`       | Compilateur Gom (signatures algébriques) en Java                |
| `stable/tom/library/`   | Runtime/bibliothèques (sl, mapping, bytecode, …)                |
| `stable/tom/platform/`  | Plateforme/plugin manager                                       |
| `stable/lib/runtime/`   | `.jar` runtime : `TNode.jar`, `jjtraveler.jar`, shared-objects  |
| `stable/lib/tools/`     | `.jar` outils : antlr 2/3/4, asm, emf, args4j, …                |
| `test/gom/`             | 16 `.gom` (10 sans hooks → corpus Phase 1)                      |
| `test/` (autres)        | 121 `.t` — **hors scope cette itération**                       |
| `examples/`             | 402 `.t`, 51 `.gom` — **hors scope cette itération**            |
| `share/`                | Mappings prédéfinis (`$TOM_HOME/share/tom`)                     |

### Inventaire `.gom` (preuve : `tomgo/internal/gom/hookscanner.go` + `tomgo/reports/phase1-inventory.md`)

- **176** fichiers `.gom` dans le dépôt (hors `tomgo/` lui-même, et hors `.git/build/bin/dist`).
- **128 sans hooks** (corpus exploitable immédiatement).
- **48 avec hooks** (reportés à une sous-phase 2b ultérieure).

Hook détecté par la règle ANTLR :
`(sort|module|operator)? ID ':' ID '(' arglist ')' '{'` au début d'une ligne.

### Mots-clés Gom rencontrés
`module`, `imports`, `abstract syntax`, `*` (variadique), built-in types
(`int`, `boolean`, `String`, `long`, `char`, `float`, `double`, `ATerm`,
`ATermList`).

### Pipeline Java (référence — partiellement utilisée cette itération)
```
starter → parser → syntax-checker → desugarer → typer
        → type-checker → expander → compiler → optimizer
        → backend → prettyprinter
```
Gom suit un pipeline parallèle : `starter → parser → expander → compiler → backend`.
**Cette itération ne touche QUE le pipeline Gom**, et seulement parser + backend.

---

## 3. Disposition du code Go

Module Go : `tom/tomgo` (local, renommable).

```
tomgo/
  cmd/
    tomgo/                       # CLI principal (tomgo gom, tomgo scan-hooks)
  internal/
    gom/                         # parser .gom + AST Gom + hookscanner (phase 1+2b)
    backend/                     # générateur .gom → .go (phase 2c)
  library/
    sharedobjects/               # runtime public — hash-consing / max sharing (phase 2a)
                                 # public car importé par le code généré
  testdata/
    corpus/
      gom-nohooks/               # 10 .gom de test/gom/ sans hooks
  reports/
    phase1-inventory.md          # 129/177 sans hooks, groupés par sous-arbo
    phase1.md                    # campagne phase 1
    phase2a-sharedobjects-survey.md   # cartographie des .jar
    phase2.md                    # campagne finale
  tools/
    scan-hooks/                  # détecteur de hooks en Go
  README.md
  go.mod
```

Packages encore non créés cette itération (laissés pour phases 3-6, à
matérialiser au moment voulu) :
- `internal/parser/` (parser TOM+Go),
- `internal/engine/{starter,checker,typer,desugarer,expander,compiler,optimizer}/`,
- `internal/library/{sl,mapping}/`,
- `internal/runtime/`.

---

## 4. Plan de portage — 6 phases (3-6 différées)

> Discipline stricte : on ne déclare une phase « terminée » qu'avec une
> preuve exécutable (commande, métrique, extrait avant/après).

### Phase 0 — Bootstrap du dépôt Go
- Arborescence `tomgo/` minimale (cf. §3), `go.mod`, CLI squelette.
- **Critère** : `cd tomgo && go build ./... && go test ./... && ./tomgo --help` OK.

### Phase 1 — Corpus `.gom` sans hooks
- Outil Go `tomgo scan-hooks <dir>` reproduit l'inventaire (129/177).
- Copie des **10 `test/gom/*.gom` sans hooks** dans `testdata/corpus/gom-nohooks/`.
- Rapports : `reports/phase1-inventory.md`, `reports/phase1.md`.

**Liste des 10 fichiers du corpus Phase 1** :

| Sans hooks (10)         | Avec hooks (6, exclus)   |
| ----------------------- | ------------------------ |
| Builtin.gom             | Bool.gom                 |
| Dotted.gom              | JavaHook.gom             |
| Imported.gom            | ML.gom                   |
| Importing.gom           | MultiHook.gom            |
| Leaf.gom                | RuleBool.gom             |
| List.gom                | RuleList.gom             |
| Minimal.gom             |                          |
| Yang.gom                |                          |
| Ying.gom                |                          |
| fromterm/foo.gom        |                          |

### Phase 2 — Outil `.gom` → `.go` reposant sur shared-objects (**JALON FINAL**)

**2.a — Portage Go de shared-objects** (fondation) :
- `internal/library/sharedobjects/` : interface `Term`, factory `Make`,
  hash-consing, égalité par identité après partage, stats.
- Survey préalable des `.jar` de `stable/lib/runtime/` documenté dans
  `reports/phase2a-sharedobjects-survey.md`.

**2.b — Parser Gom en Go** (descente récursive, sans support hook) :
- `internal/gom/` : tokeniser + parser.
- AST Gom Go codé à la main, inspiré de `src/tom/gom/adt/Gom.gom`.

**2.c — Backend `.gom` → `.go`** :
- `internal/backend/` : pour chaque sort un type Go ; pour chaque opérateur
  un constructeur `Make…` passant par la factory sharedobjects ; accesseurs,
  `Equals`, `Hash`, `String`.

**Validation Phase 2** :
- `tomgo gom <file.gom> -o out/` produit un package Go.
- `go build ./...` et `go test ./...` dans `out/` réussissent.
- Test généré : deux termes structurellement égaux pointent vers la même
  instance (preuve de partage).
- **Critère** : 10/10 du corpus Phase 1 passent la chaîne.

**Équivalence Go ⇄ Java de référence** (`internal/equivtest/`) :
- Pour 3 cibles (Minimal, Leaf, List), on tourne la même séquence
  d'opérations côté Go (généré par `tomgo`) et côté Java (généré par
  `tom.gom.Gom`), puis on `diff` stdout — **identique byte-à-byte**.
- Skip propre si pas de JDK. Détails et preuves : `tomgo/reports/equiv.md`.

**Mode batch + hooks** (extensions Phase 2'+ pour compiler le vrai ADT) :
- `tomgo gom-batch -o <dir> --pkg <name> <f1.gom> <f2.gom> …` compile N
  `.gom` qui s'importent mutuellement dans **un seul package Go**.
- Parser : tolère les hooks (`[scope] PointCut:Kind(args) { body }`) ;
  corps stocké en texte brut.
- Backend : table `knownHookTable` qui mappe `(scope, pointcut, kind)`
  vers une lowering Go. V1 : `("sort","HookList","block")` →
  `ContainsTomCode() bool` (boucle + switch-type sur les variantes
  `MakeHook|MakeBeforeHook|BlockHook` dont `HasTomCode=true`).
- 5 fichiers `src/tom/gom/adt/*.gom` (63 sorts, 1 hook) compilent et
  passent leurs tests Go. Détails : `tomgo/reports/adt.md`.

**Phase 3 — Auto-amorce de l'AST Gom** (`internal/gomast/`) :
- `internal/gomast/` est **généré par tomgo** à partir des 5 .gom de
  `src/tom/gom/adt/` (Code, Gom, Objects, Rule, SymbolTable).
- Le parser de `internal/gom/parser.go` construit directement des
  `gomast.*` (plus de structs hand-written), le backend les traverse
  nativement. `ast.go` et le bridge V1↔V2 transitoire ont été supprimés.
- `TestSelfBootstrap` vérifie que le nouveau tomgo regénère
  `internal/gomast/` **byte-identique** au commit — preuve forte de
  stabilité de l'auto-amorce. Plus 60+ sous-tests préservés ou ajoutés
  pour la conservation comportementale.
- Détails : `tomgo/reports/phase3.md`.

### Phases 3 → 6 — *différées*

Parser TOM+Go ; pipeline compilateur ; portage des `.jar` restants ;
amorce CLI complète. **Ne pas exécuter sans validation explicite.**

---

## 5. Exigences de qualité et transparence

1. **Transparence stricte** :
   - Ne jamais déclarer « terminé » si la sémantique n'est pas couverte.
   - Distinguer explicitement : *compile* vs *équivalent sémantique*.
2. **Preuves obligatoires** par jalon : commandes, fichiers modifiés,
   extraits avant/après, métriques (total / OK / KO), limites.
3. **Code Go idiomatique** : `gofmt`, `go vet`, packages courts, pas de
   sur-abstraction prématurée.
4. **Tests automatiques** : `go test ./...` doit passer.
5. **Documentation** : `tomgo/README.md` à jour + rapports par phase.

---

## 6. Conventions de travail

- **Branche de travail** : `tom-go` (worktree), main = `v3` non touché.
- **Commits** : un commit par sous-étape vérifiable. Messages :
  `tomgo phase<N>: <verbe> <objet>` (ex. `tomgo phase1: copy hook-free corpus`).
- **Rapports** : `tomgo/reports/phaseN.md`, format markdown stable.
- **Non-objectifs (cette itération)** : tout `.t`, performance,
  optimisations avancées, parité 100 % des options Java, backends C/Java/Caml/Ada.
