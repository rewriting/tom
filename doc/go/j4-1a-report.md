# J4.1a — fondations Go (OutputCode, SymbolTable, TomBase, options, messages, `regenTomGo`)

Date : 2026-10-24. Branche `tom-go`.
Plan : `docs/superpowers/plans/2026-10-24-j4-1a-foundations.md`.
Spec : `docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md`
(§4.3 sources Tom+Go, §6 jalons, §8 décisions D1–D6).

Résultat : les briques dont le futur backend Go a besoin existent côté Go
et sont identiques à Java sur les corpus mesurés : `OutputCode` (407 scripts
d'écriture sur 407 identiques), les sous-ensembles `SymbolTable` et
`TomBase` du backend (115 paquets pont interrogés, 115 identiques), les
options et les messages de la plateforme. Trois sources sont écrites en
Tom+Go (`astfactory.t`, `symboltable.t`, `tombase.t`) et compilées par
`tom -go` **sans aucune modification du compilateur Java** (aucun défaut
du `GoGenerator` rencontré). Rien d'autre n'a bougé : T0 (planchers
102/103), T1/T2 (tableau de bord 77 / 11 / 9 / 2 / 4 / 0 / 0), T3 et smoke
sont inchangés.

## 1. Ce qui a été fait

| Tâche | Commit | Contenu principal |
|---|---|---|
| 1 | `683f66b85` | `tom/engine/platform` : options (`Options`), messages (`Message`, format à la `MessageFormat`), diagnostics, exceptions d'exécution ; `tomgo` les utilise. |
| 2 | `fa87f3e05` | `tom/engine/tools/outputcode.go` + programmes de rejeu Java : `OutputCode` identique à Java sur 407 scripts d'écriture. |
| 3 | `36d13122c` | `mage regenTomGo` (`X.t` → `X.t.go` avec `src/dist`), `CheckFreshCompiler`, `tomGoFresh`, reprise depuis l'amorce par `bootstrap`, `TestTomGoRegen` ; `astfactory.t`. |
| 4 | `652eeb7fd` | `symboltable.t` : sous-ensemble de `SymbolTable` du backend, `FromBundle` (table du pont). |
| 5 | `0419b78ab` | `tombase.t` : sous-ensemble de `TomBase` du backend. |
| 6 | `5e977e5b8` | `TestSymbolTableQueries` : mêmes réponses que Java sur les paquets pont. |
| revue | `5ebf9841a` | Fraîcheur des `X.t.go` par contenu (SHA-256) ; contre-épreuve des requêtes. |

## 2. Disposition retenue

- Paquetages : `src/go/tom/engine/platform` (options, messages, rapports),
  `src/go/tom/engine/tools` (`OutputCode` en Go, `.t` portés).
- Règle `X.t` → `X.t.go` : `mage regenTomGo` compile chaque `X.t` de
  `src/go/tom/engine/**` (hors `testdata`) avec `src/dist` (D4) ; le
  fichier généré est ignoré par git dans `src/`, copié dans l'amorce
  `stable/go/tom` par `promoteGo`, et repris de l'amorce par `bootstrap`
  quand `src` n'en a pas (un clone neuf se construit sans Java).
- **Fraîcheur par contenu** : `regenTomGo` écrit en première ligne
  `// tom-source-sha256: <hex>` (SHA-256 du `.t`) ; `mage tomGoFresh`
  (préalable de `test`, `vet`, `j4`, `bridgeCheck`, et garde de
  `promoteGo`) compare cette valeur au `.t` actuel et refuse sinon
  (« `x.t` changed since its generated `x.t.go`; run mage regenTomGo (needs
  src/dist) then promoteGo »). Les dates ne décident plus : un clone donne
  des dates neuves à tout, et un `.t` modifié sans `promoteGo` aurait
  silencieusement construit depuis un `.t.go` périmé de l'amorce. Test
  unitaire : `magefiles/fresh_test.go` (`go test -tags mage` dans
  `magefiles`).
- Amorce : `stable/go/tom/engine/tools/*.t.go` portent l'en-tête.

## 3. Sous-ensemble porté et écarts

Voir le plan. Écarts `// port:` relevés (`grep -n 'port:' src/go/tom/engine`) :

- `symboltable.t` : tri des marques par octets en Go, par unités UTF-16 en
  Java (même ordre sur le BMP) ; `GetBuiltinType` : variante signalée dans
  le commentaire.
- `tombase.t` : nettoyage de nom de fichier par rune ; `TomNumberListToString` ;
  `GetSlotName` et un autre cas où Java imprime en plus l'erreur sur la
  sortie standard.
- `outputcode.go` : erreur d'écriture (Java imprime « write error » et
  continue) ; contexte de la dernière ligne (Java garde 256 unités UTF-16).
- `platform/messages.go` : séparateur de milliers `,` ; `platform/report.go` :
  `Info` (voir §5).

## 4. Vérifications finales

Sur `src/dist` fraîche (JDK 8), après régénération (3 écrits, puis
`promoteGo` et `bootstrap`) :

| Commande | Résultat |
|---|---|
| `mage -d magefiles -w . all` | vert (`engine/platform`, `engine/tools`, `gom/backend`, `tomtest/adt`, `tomtools/smoke` ok) |
| `mage … j4` | `ok tomtools/j4`, `ok tomtools/j4/renamecheck` ; `TestOutputCodeJava` 407 / 407 ; `TestSymbolTableQueries` 5 paquets, 5 identiques ; `TestTomGoRegen` ok |
| `mage … bridgeCheck` | P0 : 1139 identiques / 0 différents ; requêtes : 110 paquets, 110 identiques, minimum 15 lignes de réponse |
| `mage … portGo` | `ok tomtools/parity` ; tableau de bord inchangé : 77 OK / 11 OK (diagnostics) / 9 échoue dans legacy / 2 non supporté / 4 support / 0 divergence / 0 non porté |
| `mage … portJava` | non relancé : `src/tom` est inchangé depuis le début de J4.1a (aucune modification du compilateur Java) ; planchers T0 102/103 inchangés |

Contre-épreuve des requêtes : le test refuse un paquet dont la sortie a
moins de 10 lignes de réponse (minimum mesuré : 287 sous `j4`, 15 sous
`bridgeCheck`) et `TestQueryComparisonDetectsDifference` vérifie qu'une
valeur fausse, une ligne manquante, une ligne en trop ou une sortie vide
sont détectées.

## 5. Limites connues et points ouverts pour J4.1b

- Options et messages : petits écarts avec Java : texte de la panique
  « option not found » ; `{0,number,integer}` appliqué à une chaîne.
- `NewOutputCode` refuse les cibles `cCode`/`aCode`/`pCode` (décision D5 :
  pas de backend Java ni C/Ada/Caml en Go).
- Les `X.t.go` générés ne sont pas formatés par `gofmt`.
- Groupement des nombres dans les messages : `,` en Go, locale de la
  machine côté Java. À aligner en J4.1b avec une locale fixe dans les
  harnais.
- Les lignes `INFO` de `tomgo` ne sont pas relayées par `GoBridgePlugin`
  (le pilote Go en émettra en J4.1b).
- `inline` reste sans effet avec `-go` ; les binaires de la dist sont à
  reconstruire (`./build.sh src.dist`) après une régénération.

## 6. Tout relancer

```bash
unset TOM_HOME
./build.sh stable && ./build.sh src.dist
mage -d magefiles -w . regenTomGo      # attendu : 0 written
mage -d magefiles -w . all
mage -d magefiles -w . j4 && mage -d magefiles -w . bridgeCheck
mage -d magefiles -w . portGo && mage -d magefiles -w . coverage
mage -d magefiles -w . portJava        # ~4 min, seulement si src/tom change
(cd test/tools && go test -count=1 ./smoke/)
(cd magefiles && go test -tags mage ./...)
```

Prochaine étape : plan J4.1b (backend Go en Tom+Go, branché après
l'optimizer), spec §6.
