# TOM → Go : portage du compilateur

Ce fichier décrit l'objectif, l'état du dépôt, la stratégie de portage,
et les conventions de travail. Il est lu en début de session.

---

## 1. Objectif

**Trajectoire long terme** : porter le compilateur TOM (actuellement en
Java dans `stable/`) vers Go, puis le réécrire en Tom+Go et l'auto-compiler
avec l'amorce.

**État actuel** : Phases 0 → 3 livrées. tomgo est un outil Go autonome qui :

- lit un fichier `.gom` (avec ou sans hooks),
- génère un package Go reposant sur un portage Go de **shared-objects**
  (hash-consing, partage maximum des structures de données),
- supporte le mode **batch** : N `.gom` qui s'importent mutuellement
  compilent en un seul package Go,
- **utilise son propre AST généré** (`internal/gomast/` produit par
  tomgo lui-même à partir de `src/tom/gom/adt/*.gom`) — auto-amorce.

**Itération en cours (Phase 4)** : porter le **compilateur TOM** phase
par phase (parser → checker → typer → …), à partir de la référence Java
`stable/tom/engine/`. Chaque phase Go doit produire le même AST que la
version Java sur les mêmes entrées — comparable via le print du term.

> Le code Java de référence est `stable/` (compilateur déjà bootstrappé,
> généré à partir des sources Tom+Java de `src/`). C'est cette implémentation
> Java qui sert de spécification exécutable pour le portage.

---

## 2. Cartographie du dépôt

| Chemin                  | Contenu                                                         |
| ----------------------- | --------------------------------------------------------------- |
| `src/`                  | Sources Tom+Java du compilateur (nécessite un TOM existant)     |
| `src/tom/gom/adt/`      | 5 `.gom` décrivant l'AST de Gom (Code, Gom, Objects, Rule, SymbolTable) |
| `src/tom/engine/adt/`   | **15 `.gom` décrivant l'AST de TOM** (TomTerm, TomInstruction, TomSignature, TomOption, TomName, TomType, TomConstraint, TomDeclaration, TomExpression, TomSlot, CST, Code, Il, Theory, TypeConstraints) |
| `src/tom/engine/parser/` | Sources Tom+Java du parser TOM (ANTLR4) — référence pour Phase 4 |
| `stable/`               | Compilateur Java **généré** (251 fichiers `.java`) — spec exécutable |
| `stable/tom/engine/`    | Phases : starter → parser → checker → typer → desugarer → transformer → expander → compiler → optimizer → backend → prettyprinter |
| `stable/tom/engine/parser/` | TomParserPlugin, TomParserTool, sous-dossiers antlr2/antlr4/tomjava |
| `stable/tom/gom/`       | Compilateur Gom (signatures algébriques) en Java                |
| `stable/tom/library/`   | Runtime/bibliothèques (sl, mapping, bytecode, …)                |
| `stable/tom/platform/`  | Plateforme/plugin manager                                       |
| `stable/lib/runtime/`   | `.jar` runtime : `TNode.jar`, `jjtraveler.jar`, shared-objects, aterm  |
| `stable/lib/tools/`     | `.jar` outils : antlr 2/3/4, asm, emf, args4j, …                |
| `applications/prototype3D/lib/` | `tom-compiler-full.jar`, `tom-runtime-full.jar` — pré-builts |
| `test/`                 | 121 `.t` + 18 `.gom` (corpus de tests des compilateurs TOM)     |
| `examples/`             | 402 `.t` + 51 `.gom`                                            |
| `share/`                | Mappings prédéfinis (`$TOM_HOME/share/tom`)                     |

---

## 3. État livré (Phases 0 → 3) — résumé

### Phase 0 — Bootstrap du dépôt Go ✅

Arborescence `tomgo/` minimale, `go.mod`, CLI squelette.

### Phase 1 — Corpus `.gom` sans hooks ✅

Outil `tomgo scan-hooks <dir>` ; copie des 10 `test/gom/*.gom` sans hooks
dans `testdata/corpus/gom-nohooks/`. Rapports `phase1-inventory.md`,
`phase1.md`. Inventaire dépôt : **176 `.gom` total, 128 sans hooks, 48 avec**.

### Phase 2 — `.gom → .go` reposant sur shared-objects ✅

- **2.a** `library/sharedobjects/` : interface `Term`, factory `Build`,
  hash-cons thread-safe, mixers `OneAtATime` / `MixSymbol` / `StringHash`.
- **2.b** `internal/gom/` : lexer + parser descente récursive.
- **2.c** `internal/backend/` : génération d'un package Go par module
  ou plusieurs modules → un seul package (mode batch).
- **Hooks** : table `knownHookTable` mappe `(scope, pointcut, kind)` vers
  une lowering Go. Une entrée à ce jour : `sort HookList:block()` d'Objects.gom
  → `ContainsTomCode() bool`. Hooks inconnus → commentaire « unsupported »,
  corps perdu.
- **Validation** : 10/10 corpus `go build` ; preuve de partage exécutée
  sur code généré.
- **Équivalence Go ⇄ Java** (`internal/equivtest/`) : 3 cibles
  (minimal/leaf/list) produisent un stdout **byte-identique** au
  compilateur Gom Java de référence (`tom.gom.Gom` invoqué via
  `applications/prototype3D/lib/tom-compiler-full.jar`).
- **ADT** : les 5 `src/tom/gom/adt/*.gom` (63 sorts, 1 hook) compilent
  en un seul package + 5 tests `ContainsTomCode`.

Rapports : `phase2a-sharedobjects-survey.md`, `phase2.md`, `equiv.md`,
`adt.md`.

### Phase 3 — Auto-amorce de l'AST Gom ✅

- **`internal/gomast/` est généré par tomgo** à partir des 5 `.gom` de
  `src/tom/gom/adt/`. ~6 273 lignes Go, 63 sorts.
- Le parser construit directement des `gomast.*` (plus aucun struct
  Go hand-written). Le backend traverse `gomast.GomModule` nativement
  via type assertions.
- `internal/gom/ast.go`, `bridge.go`, `bridge_test.go`,
  `cross_v1v2_test.go` **supprimés**.
- **`TestSelfBootstrap`** : le nouveau tomgo regénère
  `internal/gomast/` **byte-identique** au commit → preuve forte de
  stabilité de l'auto-amorce.
- ~60 sous-tests verts pour la conservation comportementale.

Rapport : `phase3.md`.

---

## 4. Disposition actuelle du code Go

Module Go : `tom/tomgo` (local).

```
tomgo/
  cmd/
    tomgo/                       # CLI : scan-hooks, gom, gom-batch
  internal/
    gom/                         # parser .gom + accesseurs gomast + hookscanner
    gomast/                      # AST Gom GÉNÉRÉ par tomgo (auto-amorce)
    backend/                     # générateur .gom → .go (V2 natif gomast)
    equivtest/                   # harnais d'équivalence Go ⇄ Java
  library/
    sharedobjects/               # runtime public — hash-cons / max sharing
  testdata/
    corpus/gom-nohooks/          # 10 .gom de test/gom/ sans hooks
    equiv/{minimal,leaf,list}/   # scénarios paired Go/Java pour equivtest
  reports/                       # 6 rapports (phase1, phase2, phase2a, equiv, adt, phase3)
  README.md
  go.mod
```

Packages encore à matérialiser pour les phases suivantes :
- `internal/tomast/` (Phase 4.A — AST TOM généré depuis `src/tom/engine/adt/`),
- `internal/tomparser/` (Phase 4.D — parser TOM en Go),
- `internal/tomengine/` (Phases 4.F+ — checker, typer, desugarer, …),
- `internal/library/sl/` (stratégies, jjtraveler-like, plus tard).

---

## 5. Phase 4 — Auto-amorce du parser TOM (itération suivante)

**Stratégie générale** : porter le compilateur Java de `stable/tom/engine/`
phase par phase, en commençant par le parser. À chaque phase, vérifier
que le pipeline Go produit le même AST `tomast.*` que la référence Java
sur les mêmes entrées. Comparaison via print du term (déjà prouvée
byte-portable en Phase 2 pour Gom).

### 4.A — Auto-amorce de l'AST TOM
- `tomgo gom-batch --pkg tomast -o internal/tomast ../src/tom/engine/adt/*.gom`.
- 15 modules, ~150-200 sorts attendus, 9 hooks à analyser.
- Certains hooks vont demander d'étendre `knownHookTable` ; les autres
  produiront un commentaire et compileront sans leur logique (acceptable
  tant que le code Go généré reste utilisable comme AST passif).
- Critère : `internal/tomast/` compile, smoke test (construction d'un
  `TermAppl` simple, sharing OK).

### 4.B — Survey du parser Java
- Lire `stable/tom/engine/parser/TomParserPlugin.java`, `TomParserTool.java`
  pour l'entrée.
- Inspecter `stable/tom/engine/parser/antlr4/` :
  `TomIslandLexer`/`TomIslandParser` (l'« île » TOM dans le host),
  `TomJavaLexer`/`TomJavaParser` (host Java),
  `CstBuilder` → CST, `AstBuilder`/`CstConverter` → AST TomTerm.
- Trouver comment invoquer côté Java un dump du term parsé (option
  `--parse-only` ou équivalent), ou écrire un petit driver Java qui
  affiche l'AST sous forme `Op(arg1,arg2)`.
- Documenter dans `reports/phase4a-parser-survey.md`.

### 4.C — Harnais de comparaison AST
- `internal/parsereq/` (ou extension d'`equivtest/`) : pour chaque `.t`
  cible, faire tourner le parser Java de référence et le parser Go,
  imprimer les ASTs et `diff`.
- Pipe Java : `java -cp <classpath> tom.engine.Tom --parse-only -dump-ast t.t`
  (à confirmer après 4.B).

### 4.D — Parser TOM en Go — **hand-roll 100 % Go natif**

Décision utilisateur : on n'utilise pas ANTLR. Lexer + descente récursive
hand-rolled, dans le style de `internal/gom/`. Bénéfices : 100 % Go,
zéro dépendance externe, contrôle total de l'AST produit (et donc de
l'équivalence avec la version Java).

Approche :

- **Lexer** : tokenise un mélange host (Java) + TOM. Une stratégie
  d'« île » à la TomIslandParser : par défaut on consomme du *water*
  (texte hôte opaque) ; les marqueurs `%match`, `%op`, `%typeterm`,
  `%include`, backquote `` ` ``, etc. enclenchent un mode TOM avec ses
  propres règles.
- **Parser** : descente récursive, produit directement des `tomast.*`
  via les `Make*` constructeurs.
- **Construction incrémentale** : on n'écrit que le sous-ensemble
  nécessaire pour la première cible (`%typeterm` + `%op` simple), on
  prouve l'équivalence AST avec Java sur cette cible, puis on étend
  pour la suivante (`%match`, backquote, etc.).
- **Référence Java pour la sémantique** :
  `src/tom/engine/parser/antlr4/TomIslandParser.g4` + le code généré
  dans `stable/tom/engine/parser/antlr4/{TomIslandParser,CstBuilder,
  AstBuilder,CstConverter}.java`. On lit ces fichiers comme spec, on
  ne les exécute pas — sauf via le harnais 4.C pour valider.

### 4.E — Corpus initial de validation
- 3 à 5 `.t` minimaux **synthétisés à la main** ou **choisis dans `test/`**
  pour exercer progressivement les constructions : skeleton de classe,
  `%typeterm`, `%op`, puis `%match`, backquote, etc.
- Pour chaque cible : print AST côté Java ≡ print AST côté Go.

### 4.F → 4.Z — Phases compilateur suivantes
Après le parser, on porte phase par phase, dans cet ordre (calque du
pipeline Java) :

`starter → syntax-checker → desugarer → typer → type-checker → expander
→ compiler → optimizer → backend → prettyprinter`

Pour chaque phase :
- AST en entrée et en sortie typés en `tomast.*` (l'auto-amorce
  garantit la même structure que côté Java).
- Comparaison Go ⇄ Java sur le print du term à la sortie de la phase.
- Tests sur le corpus 4.E.

---

## 6. Exigences de qualité et transparence

1. **Transparence stricte** :
   - Ne jamais déclarer « terminé » si la sémantique n'est pas couverte.
   - Distinguer explicitement : *compile* vs *équivalent sémantique*.
2. **Preuves obligatoires** par jalon : commandes, fichiers modifiés,
   extraits avant/après, métriques (total / OK / KO), limites.
3. **Code Go idiomatique** : `gofmt`, `go vet`, packages courts, pas de
   sur-abstraction prématurée.
4. **Tests automatiques** : `go test ./...` doit passer ; les tests Java
   externes (equivtest) skippent proprement si le JDK est absent.
5. **Documentation** : `tomgo/README.md` à jour + un rapport par sous-phase
   (`reports/phaseN<...>.md`).

---

## 7. Conventions de travail

- **Branche de travail** : `tom-go` (worktree), main = `v3` non touché.
- **Commits** : un commit par sous-étape vérifiable. Messages :
  `tomgo phase<N>: <verbe> <objet>`.
- **Rapports** : `tomgo/reports/phase<N>.md`, format markdown stable.
- **Non-objectifs (jusqu'à nouvel ordre)** : performance, optimisations
  avancées, parité 100 % des options Java, backends C/Caml/Ada.
