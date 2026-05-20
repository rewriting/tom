# TOM → Go : portage du compilateur

Ce fichier est l'unique source de vérité pour une session sur ce
projet. Tout ce dont une session terminal claude-code a besoin pour
reprendre le travail doit pouvoir se trouver ci-dessous. Les détails
plus longs sont dans `tomgo/reports/phase<N>.md`.

---

## 1. Objectif

**Trajectoire long terme** : porter le compilateur TOM (actuellement en
Java dans `stable/`) vers Go, puis le réécrire en Tom+Go et l'auto-compiler
avec l'amorce.

**État actuel** : Phases 0 → 3 livrées ; Phases 4.A (auto-amorce
tomast + 9 hooks engine), 4.B (survey parser Java), 4.C (harnais
d'équivalence AST Go ⇄ Java), 4.D + 4.E.1–4.E.5 (parser TOM Go
hand-rolled couvrant `%typeterm`, `%typeterm extends`, `%op` avec ou
sans slots, `%oplist`, `%oparray`, `%include`), 4.F.0 (tokeniseur
water ANTLR-fidèle + simulation `buildHostblock`/`mergeString`),
4.F.1 (`%match` minimal — un sujet, pattern `_`, body vide),
4.F.2 (`%match` avec pattern variable nommée — `x -> { }`) et
4.F.3 (`%match` avec application nullaire — `Foo() -> { }`) livrées.
**10 fixtures** sous `tomgo/testdata/parse/` valident byte-pour-byte
l'AST Go contre la référence Java. tomgo est un outil Go autonome qui :

- lit un fichier `.gom` (avec ou sans hooks),
- génère un package Go reposant sur un portage Go de **shared-objects**
  (hash-consing, partage maximum des structures de données),
- supporte le mode **batch** : N `.gom` qui s'importent mutuellement
  compilent en un seul package Go,
- **utilise ses propres AST générés** (`internal/gomast/` pour le sous-
  langage Gom, `internal/tomast/` pour le langage TOM) — auto-amorce.

**Itération en cours (Phase 4)** : porter le **compilateur TOM** phase
par phase (parser → checker → typer → …), à partir de la référence Java
`stable/tom/engine/`. Chaque phase Go doit produire le même AST que la
version Java sur les mêmes entrées — comparable via le print du term.

> La version Java de référence est `stable/` (compilateur déjà bootstrappé,
> généré à partir des sources Tom+Java de `src/`). C'est cette
> implémentation Java qui sert de spécification exécutable pour le portage.

---

## 2. Cartographie du dépôt

| Chemin                          | Contenu                                                         |
| ------------------------------- | --------------------------------------------------------------- |
| `src/`                          | Sources Tom+Java du compilateur (nécessite un TOM existant)     |
| `src/tom/gom/adt/`              | 5 `.gom` décrivant l'AST de Gom (Code, Gom, Objects, Rule, SymbolTable) |
| `src/tom/engine/adt/`           | 15 `.gom` décrivant l'AST de TOM (TomTerm, TomInstruction, TomSignature, TomOption, TomName, TomType, TomConstraint, TomDeclaration, TomExpression, TomSlot, CST, Code, Il, Theory, TypeConstraints) |
| `src/tom/engine/parser/`        | Sources Tom+Java du parser TOM + grammaire ANTLR4 (lecture seule, sert de spec pour 4.D) |
| `stable/`                       | Compilateur Java **généré** (251 fichiers `.java`) — spec exécutable |
| `stable/tom/engine/`            | Phases : starter → parser → checker → typer → desugarer → transformer → expander → compiler → optimizer → backend → prettyprinter |
| `stable/tom/engine/parser/antlr4/` | `TomIslandLexer/Parser`, `TomJavaLexer/Parser`, `CstBuilder`, `AstBuilder`, `CstConverter` — référence pour Phase 4.D |
| `stable/tom/gom/`               | Compilateur Gom (signatures algébriques) en Java                |
| `stable/tom/library/`           | Runtime/bibliothèques Java (sl, mapping, bytecode, …)            |
| `stable/lib/runtime/`           | `.jar` runtime : `TNode.jar`, `jjtraveler.jar`, `shared-objects.jar`, `aterm.jar` |
| `stable/lib/tools/`             | `.jar` outils : antlr 2/3/4, asm, emf, args4j, …                |
| `applications/prototype3D/lib/` | `tom-compiler-full.jar`, `tom-runtime-full.jar` — pré-builts utilisés par `equivtest` pour invoquer le Gom Java de référence |
| `utils/eclipse-plugin/plugin/config/Gom.xml` | Config XML attendue par `tom.gom.Gom -X` |
| `test/`                         | 121 `.t` + 18 `.gom` (corpus tests historique)                  |
| `examples/`                     | 402 `.t` + 51 `.gom`                                            |
| `share/`                        | Mappings prédéfinis (`$TOM_HOME/share/tom`)                     |
| `utils/eclipse-plugin/plugin/include/java/boolean.tom` | Mapping Tom `true()/false()` ↔ Java `boolean` (sert de spec pour les hooks AU) |

---

## 3. Disposition actuelle du code Go

Module Go : `tom/tomgo` (local, branche `tom-go`).

```
tomgo/
  cmd/
    tomgo/                       # CLI : scan-hooks, gom, gom-batch
  internal/
    gom/                         # parser .gom (descente récursive) +
                                 #   accesseurs gomast + hookscanner
                                 #   inspect.go : QualifiedName, Sorts,
                                 #     Hooks, NameParts, Imports…
    gomast/                      # AST Gom GÉNÉRÉ depuis src/tom/gom/adt/
                                 # (auto-amorce, byte-stable via
                                 #  TestSelfBootstrap)
    tomast/                      # AST TOM GÉNÉRÉ depuis
                                 # src/tom/engine/adt/ (Phase 4.A,
                                 # byte-stable via TestSelfBootstrap_Tomast)
    backend/                     # générateur .gom → .go natif gomast
                                 # + knownHookTable (10 entrées : 1 pour
                                 #   Gom HookList:block + 9 pour TOM ADT)
    equivtest/                   # harnais d'équivalence Go ⇄ Java
                                 # (3 cibles : minimal/leaf/list)
  library/
    sharedobjects/               # runtime public — hash-cons + max-sharing
                                 # (interface Term, Factory.Build,
                                 #  mixers OneAtATime/MixSymbol/StringHash)
  testdata/
    corpus/gom-nohooks/          # 10 .gom de test/gom/ sans hooks
    equiv/{minimal,leaf,list}/   # scénarios paired Go/Java pour equivtest
  reports/                       # rapports par phase
  README.md
  go.mod                         # module tom/tomgo, Go 1.22
```

Packages livrés cette itération :
- `internal/tomparser/` — parser TOM hand-rolled (descente récursive
  100 % Go, pas d'ANTLR). Couvre `%typeterm` (+`extends`), `%op` (+slots),
  `%oplist`, `%oparray`, `%include`, `%match` minimal avec pattern
  `_`, variable nommée ou application nullaire `Foo()`. `water.go`
  contient le tokeniseur ANTLR-fidèle + `buildHostblocks` +
  `mergeHostblocks`.
- `internal/tomparseq/` — harnais d'équivalence AST Go ⇄ Java :
  mini-runner `java/TomParseDump.java` qui instancie `TomParserPlugin`
  directement (sans `tom.engine.Tom`/`Tom.config`), `tomparseq.go`
  pour la résolution JDK et la normalisation des paths
  (`__INPUT__`/`__DIR__`).
- `testdata/parse/<name>/scenario.t` — 10 fixtures actuellement.

Packages encore à matérialiser :
- `internal/tomengine/` (phases compilateur suivantes — checker,
  typer, desugarer, …),
- `internal/library/sl/` (stratégies type jjtraveler, plus tard).

---

## 4. État livré — détail par phase

### Phase 0 — Bootstrap du dépôt Go ✅
Arborescence `tomgo/`, `go.mod`, CLI squelette.

### Phase 1 — Corpus `.gom` sans hooks ✅
- `tomgo scan-hooks <dir>` détecte les hooks par la règle ANTLR :
  `(sort|module|operator)? ID ':' ID '(' arglist ')' '{'` au début d'une ligne.
- Inventaire dépôt : **176 `.gom` total, 128 sans hooks, 48 avec hooks**.
- Corpus copié dans `testdata/corpus/gom-nohooks/` : Builtin, Dotted,
  Imported, Importing, Leaf, List, Minimal, Yang, Ying, fromterm/foo.
- Rapports : `phase1-inventory.md`, `phase1.md`.

### Phase 2 — `.gom → .go` avec shared-objects ✅
- **2.a** `library/sharedobjects/` : interface `Term`, `Factory.Build`,
  hash-cons thread-safe (`sync.RWMutex`), mixers `OneAtATime` /
  `MixSymbol` / `StringHash`.
- **2.b** `internal/gom/` : lexer + parser descente récursive.
- **2.c** `internal/backend/` : un fichier `.go` par module Gom dans le
  package cible. Mode batch (`gom-batch`) → un seul package Go partagé.
- **Hooks** : table `knownHookTable` mappe `(scope, pointcut, kind)` →
  émission Go. Phase 2 livre l'entrée `sort HookList:block()` (Objects.gom)
  → méthode `ContainsTomCode() bool` sur l'interface + chaque alt.
- **Équivalence Go ⇄ Java** (`internal/equivtest/`) : 3 cibles
  (minimal/leaf/list) produisent un stdout **byte-identique** au
  compilateur Gom Java de référence (`tom.gom.Gom` invoqué via
  `applications/prototype3D/lib/tom-compiler-full.jar`).
- **ADT Gom** : les 5 `src/tom/gom/adt/*.gom` (63 sorts, 1 hook)
  compilent ensemble + 5 tests `ContainsTomCode`.

Rapports : `phase2a-sharedobjects-survey.md`, `phase2.md`, `equiv.md`,
`adt.md`.

### Phase 3 — Auto-amorce de l'AST Gom ✅
- **`internal/gomast/`** est généré par tomgo depuis les 5 `.gom` de
  `src/tom/gom/adt/`. ~6 273 lignes, 63 sorts.
- Le parser construit directement des `gomast.*` (plus de struct V1
  hand-written). Le backend traverse `gomast.GomModule` nativement via
  type assertions. `internal/gom/ast.go`, `bridge.go`, etc. **supprimés**.
- **`TestSelfBootstrap`** : tomgo regénère `internal/gomast/`
  **byte-identique** au commit — preuve de stabilité de l'auto-amorce.

Rapport : `phase3.md`.

### Phase 4.A — Auto-amorce de l'AST TOM + 9 hooks engine ✅
- **`internal/tomast/`** généré par tomgo depuis les 15 `.gom` de
  `src/tom/engine/adt/`. **107 sorts, ~17 400 lignes**.
- **9 hooks de l'ADT engine** implémentés directement en Go via le
  nouveau mécanisme `emitMakePrologue` (`internal/backend/backend.go`) :

  | Catégorie | Pointcut | Fichier source | Effet Go |
  |---|---|---|---|
  | AU avec unité | `AndConstraint` | TomConstraint.gom | aplatit + unit = `TrueConstraint()` |
  | AU avec unité | `OrConstraint` | TomConstraint.gom | aplatit + unit = `FalseConstraint()` |
  | AU sans unité | `OrConstraintDisjunction` | TomConstraint.gom | aplatit seulement |
  | rewrite-rule | `InstructionToCode` | Code.gom | `InstructionToCode(CodeToInstruction(t)) → t` |
  | rewrite-rule | `CodeToInstruction` | Code.gom (déclaré là, op dans TomInstruction.gom) | inverse |
  | rewrite-rule | `BQTermToExpression` | TomExpression.gom | `BQTermToExpression(ExpressionToBQTerm(t)) → t` |
  | rewrite-rule | `ExpressionToBQTerm` | TomExpression.gom (déclaré là, op dans Code.gom) | inverse |
  | make-guard | `Cast` | TomExpression.gom | `panic("bad cast")` si Type "unknown type" |
  | make-simplify | `NameNumber` | TomName.gom | `NameNumber(PositionName(concTomNumber([Position]))) → Position` |
  | make_insert-splice | `concInstruction` | TomInstruction.gom | splice `AbstractBlock(l1)` dans la liste |
  | make_insert-splice | `concTomNumber` | TomName.gom | splice `NameNumber(PositionName(concTomNumber(p*)))` |

- **3 fixes structurels du backend** nécessaires pour que `internal/tomast/`
  compile :
  - keyword `else` ajouté à `isGoKeyword` (slot `else` dans `Conditional`),
  - collision `MakeSubterm` détectée pré-emit, suffixée en
    `MakeSubterm<Sort>` (`Subterm` est alt de BQTerm ET de Term),
  - slots `String:String` / `Hash:…` renommés en `<Name>_` pour ne pas
    masquer les méthodes `String()` / `Hash()` du struct.
- **`TestSelfBootstrap_Tomast`** : tomgo regénère `internal/tomast/`
  **byte-identique** au commit.
- **14 hook smoke tests** dans `internal/backend/tomast_hooks_test.go`
  qui épinglent l'effet observable de chacun des 9 hooks.

Rapport : `phase4a-tom-hooks-survey.md`.

---

### Phase 4.B — Survey du parser Java ✅
- Rapport `phase4b-parser-survey.md` (incl. erratum) : référence Java =
  `stable/dist/lib/` (produit par `./build.sh stable`), mode `-np`
  (antlr4 island), format de comparaison = `Code.toString()` (zéro
  dépendance `aterm.jar`).
- Bug `aterm.jar` sur JDK 11+ identifié et contourné (mécanisme `-i`
  écarté au profit du print direct).

### Phase 4.C — Harnais d'équivalence AST Go ⇄ Java ✅
- `internal/tomparseq/java/TomParseDump.java` : mini-runner Java qui
  contourne `tom.engine.Tom` (et donc `Tom.config`/optimize2 et toutes
  les options inter-plugins) via une `OptionManager` minimaliste
  (HashMap).
- `internal/tomparseq/tomparseq.go` : détection JDK, compilation
  paresseuse du runner, exécution, normalisation des paths absolus en
  `__INPUT__`, diff byte-pour-byte.
- Variable `TOMGO_STABLE_DIST_LIB` pour pointer vers le build du
  worktree principal (`stable/dist/` est gitignored).
- Tests verts : `TestSkeletonAgainstJava` (AST manuel ↔ Java) et
  `TestSkeletonGoParserAgainstJava` (parser Go ↔ Java).

### Phase 4.D + 4.E.1–4.E.5 — Parser TOM Go hand-rolled ✅
- `internal/tomparser/parser.go` : descente récursive 100% Go,
  pas d'ANTLR. Couvre `%typeterm` (avec/sans `extends`), `%op` (avec/sans
  slots), `%oplist`, `%oparray`, `%include`, `%match` minimal (cf. 4.F.1).
  Body `{ … }` consommé via compteur de braces, contenu non encodé
  (conforme Java : codomain, slots, options et hooks vont dans la
  SymbolTable).
- `%include` : path résolu via `filepath.Abs(filepath.Join(dir(filename), path))`,
  fichier re-parsé récursivement, wrapping
  `TomInclude(concCode(InstructionToCode(AbstractBlock(concInstruction(
  CodeToInstruction(c1), CodeToInstruction(c2), …)))))`.
- Normalisation 2-niveaux dans le harnais : `scenario.t` → `__INPUT__`,
  `dirname(scenario.t)` → `__DIR__` (permet aux `%include` pointant
  vers un fichier voisin de matcher byte-pour-byte sans absoluty-leak).

Rapport : `phase4cd-parser.md`.

### Phase 4.F.0 — Tokeniseur water ANTLR-fidèle ✅
- `internal/tomparser/water.go` : tokenise un water en `NL`/`WS`/visible
  à la `TomIslandLexer`, construit les `HOSTBLOCK` à la `CstBuilder.
  buildHostblock` (avec attribution left-to-right des hidden tokens
  via `usedToken`), puis fusionne avec padding à la
  `CstConverter.simplifyCstBlockList` + `mergeString`.
- Fixture `water_multi/` valide la convention sur un water à 3
  visibles séparés par 1/2/1 `\n` (`start (3,2)`, content reporté
  `\nb\n\nc\n\n\n\nd\n` avec 4 `\n` synthétiques de padding, end
  `(8,1)`).
- Water purement whitespace+`\n` → **aucun HOSTBLOCK émis** (rules
  `NL`/`WS` du lexer sont `-> channel(HIDDEN)`).

### Phase 4.F.1 — `%match` minimal ✅
- `internal/tomparser/parser.go` étendu avec `parseMatch`,
  `parseSubjectList`, `parseSubject`, `parseActionRule`,
  `parsePattern`, `unknownType()`.
- Couvre exactement `%match(t) { _ -> { } }` sur la fixture `match0b` :
  - subject `t` → `BQVariable(concOption(OT, ModuleName("default")),
    Name("t"), unknownType)`
  - pattern `_` → `Variable(concOption(), EmptyName(), unknownType,
    concConstraint())`
  - body `{ }` → `RawAction(If(TrueTL(), AbstractBlock(concInstruction()),
    Nop()))`
  - rule opts → `concOption(OT(Name("ConstraintAction"), ruleLine, file))`
  - Match opts → `concOption(OT(Name("Match"), matchLine, file),
    ModuleName("default"))`

### Phase 4.F.2 — `%match` avec pattern variable nommée ✅
- `parsePattern` accepte désormais aussi `ID` en plus de `_` :
  `x` → `Variable(concOption(), Name("x"), unknownType,
  concConstraint())` (pas d'OriginTracking, identique à
  `AstBuilder.java:752-766`).
- Fixture `match0c_named`.
- Rapport : `phase4f2-match-named.md`.

### Phase 4.F.3 — `%match` avec application nullaire `Foo()` ✅
- `parsePattern` distingue maintenant `ID` (variable) de `ID '(' ')'`
  (application sans argument) : `Foo()` → `TermAppl(concOption(),
  concTomName(Name("Foo")), concTomTerm(), concConstraint())`.
- Suit le chemin `Cst_Appl` du parser Java (`CstBuilder.java:452-453`
  + `AstBuilder.java:793-804`) avec `extractTheory` vide pour les
  symboles sans marqueur `?`/`??`.
- Une application avec arguments non vides est explicitement rejetée
  (à lever en 4.F.4).
- Fixture `match0d_appl` (10 fixtures total).
- Rapport : `phase4f3-match-appl-nullary.md`.

---

## 5. Phase 4 — suite (à dérouler quand on rouvre une session)

**Stratégie générale** : porter le compilateur Java de `stable/tom/engine/`
phase par phase, en commençant par le parser. À chaque phase, vérifier
que le pipeline Go produit le même AST `tomast.*` que la référence Java
sur les mêmes entrées. Comparaison via le print du term (déjà prouvée
byte-portable en Phase 2 pour Gom).

### 4.E + 4.F.0 + 4.F.1 + 4.F.2 + 4.F.3 (livrés) — Constructeurs `%typeterm`/`%op`/`%oplist`/`%oparray`/`%include`/`%match` (`_` + variable nommée + appl nullaire) + water ANTLR-fidèle

Voir §4 ci-dessus. **10 fixtures** validées contre Java :
`skeleton`, `op_noargs`, `op_slots`, `typeterm_extends`,
`oplist_oparray`, `include_local`, `water_multi`, `match0b`,
`match0c_named`, `match0d_appl`. Pattern à réutiliser pour chaque nouveau constructeur :

1. un `.t` minimal dans `testdata/parse/<nom>/`,
2. 1 ligne dans la slice `fixtures` de `TestGoParserAgainstJava`
   (`internal/tomparseq/dump_skeleton_test.go`),
3. (optionnel) un `TestParse<Nom>` direct dans
   `tomparser/parser_test.go` avec la chaîne attendue.

### 4.F (prochaines extensions de `%match`)

Cibles, par ordre d'effort croissant :

1. ~~**Pattern variable nommée `x`**~~ ✅ livré en 4.F.2 →
   `Variable(concOption(), Name("x"), unknownType, concConstraint())`,
   sans OriginTracking (`AstBuilder.java:752-766`).
2. ~~**Pattern application nullaire `Foo()`**~~ ✅ livré en 4.F.3 →
   `TermAppl(concOption(), concTomName(Name("Foo")), concTomTerm(),
   concConstraint())` (chemin `Cst_Appl`, pas `Cst_Constant` —
   cf. `CstBuilder.java:452-453` + `AstBuilder.java:793-804`).
3. **Pattern application `Foo(x, y)`** avec sous-patterns
   (`argList` non vide).
4. **Body non vide** dans l'action rule (instructions Java consommées
   en `TL`/`ITL`).
5. **Multi-subjects** `%match(a, b) { p1, p2 -> { … } }`.
6. **Contraintes `pattern << bqterm`** (AND/OR).
7. **`%strategy ... extends ...`** + `visit` blocks.
8. **`%gom`** — gom directives inline.
9. **Backquote terms** (`` `Op(args) ``).
10. **Metaquote** `%[ … ]%`.

### 4.B — Survey du parser Java *(historique)*

À lire pour comprendre le pipeline parser à reproduire :

- `stable/tom/engine/parser/TomParserPlugin.java`,
  `TomParserTool.java` — entrée et orchestration.
- `stable/tom/engine/parser/antlr4/TomIslandLexer.java`,
  `TomIslandParser.java` — l'« île » TOM dans le host Java.
- `stable/tom/engine/parser/antlr4/TomJavaLexer.java`,
  `TomJavaParser.java` — host Java.
- `stable/tom/engine/parser/antlr4/CstBuilder.java`,
  `AstBuilder.java`, `CstConverter.java` — CST → AST `TomTerm`.
- Grammaire source : `src/tom/engine/parser/antlr4/TomIslandParser.g4`,
  `src/tom/engine/parser/tomjava/{TomJavaLexer,TomJavaParser}.g4`.

À documenter dans `reports/phase4b-parser-survey.md` : entrée CLI Java
de référence (`tom.engine.Tom`), option pour dumper l'AST parsé, et
exemple d'invocation byte-comparable.

### 4.C — Harnais de comparaison AST

Extension d'`internal/equivtest/` (ou nouveau `internal/parsereq/`) :

```
.t source ─┬─ parser Java de référence  → print AST (TomTerm + cie)
           └─ parser Go (tomgo)         → print AST tomast.*
                diff(printJava, printGo) doit être vide
```

Skip propre si JDK absent. Pipe Java attendu :
`java -cp <classpath> tom.engine.Tom --parse-only -dump-ast t.t`
(à confirmer en 4.B).

### 4.D — Parser TOM en Go — hand-rolled 100 % Go natif

**Décision verrouillée** : pas d'ANTLR. Lexer + descente récursive
hand-rolled dans le style de `internal/gom/`. Bénéfices : 100 % Go,
zéro dépendance externe, contrôle total de l'AST produit.

Approche :
- **Lexer** : tokenise un mélange host (Java) + TOM avec une stratégie
  d'« île » à la `TomIslandParser` — par défaut on consomme du *water*
  (texte hôte opaque) ; les marqueurs `%match`, `%op`, `%typeterm`,
  `%include`, backquote `` ` ``, etc. enclenchent le mode TOM.
- **Parser** : descente récursive, produit directement des `tomast.*`
  via les `Make*` constructeurs.
- **Construction incrémentale** : on n'écrit que le sous-ensemble
  nécessaire pour la première cible (skeleton de classe host + `%typeterm`
  + `%op` simple), on prouve l'équivalence AST avec Java sur cette cible,
  puis on étend pour la suivante (`%match`, backquote, …).
- **Référence Java pour la sémantique** : les fichiers cités en 4.B
  sont lus comme spec, jamais exécutés (sauf via le harnais 4.C pour
  valider).

### 4.E — Corpus initial de validation

3 à 5 `.t` minimaux **synthétisés à la main** OU choisis dans `test/`
pour exercer progressivement les constructions. Critère par cible :
print AST côté Java ≡ print AST côté Go, byte-pour-byte.

### 4.F → 4.Z — Phases compilateur suivantes

Après le parser, on porte phase par phase, dans l'ordre du pipeline
Java :

`starter → syntax-checker → desugarer → typer → type-checker → expander
→ compiler → optimizer → backend → prettyprinter`

Pour chaque phase : AST en entrée et en sortie en `tomast.*`, comparaison
Go ⇄ Java sur le print du term en sortie, tests sur le corpus 4.E.

---

## 6. Exigences de qualité

1. **Transparence stricte** : ne jamais déclarer « terminé » si la
   sémantique n'est pas couverte. Distinguer *compile* vs
   *équivalent sémantique*.
2. **Preuves obligatoires** par jalon : commandes, fichiers modifiés,
   extraits avant/après, métriques (total / OK / KO), limites connues.
3. **Code Go idiomatique** : `gofmt`, `go vet`, packages courts.
4. **Tests automatiques** : `go test ./...` doit passer ; les tests Java
   externes (equivtest) skippent proprement si le JDK est absent.
5. **Documentation** : un rapport par sous-phase dans
   `tomgo/reports/phase<N>.md`.

---

## 7. Conventions de travail

- **Branche** : `tom-go` (worktree), main = `v3` non touché.
- **Commits** : un commit par sous-étape vérifiable. Messages :
  `tomgo phase<N>: <verbe> <objet>` (ex. `tomgo phase 4.A: bootstrap
  tomast + lower 9 engine ADT hooks`).
- **Rapports** : `tomgo/reports/phase<N>.md`, format markdown stable.
- **Non-objectifs** (jusqu'à nouvel ordre) : performance, optimisations
  avancées, parité 100 % des options Java, backends C/Caml/Ada.

---

## 8. Cookbook — commandes utiles pour reprendre rapidement

Toutes les commandes se lancent depuis le dossier `tomgo/` sauf mention.

```bash
# Build + tests + vet
cd tomgo
go vet ./...
go test ./...

# Recensement des hooks .gom du dépôt
go run ./cmd/tomgo scan-hooks ..

# Compiler un .gom isolé en Go (mode single)
go run ./cmd/tomgo gom -o /tmp/out testdata/corpus/gom-nohooks/Minimal.gom
(cd /tmp/out && go build ./... && go test ./...)

# Compiler plusieurs .gom cross-importants en UN SEUL package Go (batch)
go run ./cmd/tomgo gom-batch --pkg adt -o /tmp/adt ../src/tom/gom/adt/*.gom
(cd /tmp/adt && go build ./...)

# Régénérer internal/gomast/ (AST Gom auto-amorcé)
find internal/gomast -name '*.go' -not -name 'doc.go' -delete
go run ./cmd/tomgo gom-batch --pkg gomast -o internal/gomast \
    ../src/tom/gom/adt/Code.gom \
    ../src/tom/gom/adt/Gom.gom \
    ../src/tom/gom/adt/Objects.gom \
    ../src/tom/gom/adt/Rule.gom \
    ../src/tom/gom/adt/SymbolTable.gom
rm internal/gomast/go.mod
go test ./...

# Régénérer internal/tomast/ (AST TOM auto-amorcé, Phase 4.A)
find internal/tomast -name '*.go' -not -name 'doc.go' -delete
go run ./cmd/tomgo gom-batch --pkg tomast -o internal/tomast \
    ../src/tom/engine/adt/CST.gom \
    ../src/tom/engine/adt/Code.gom \
    ../src/tom/engine/adt/Il.gom \
    ../src/tom/engine/adt/Theory.gom \
    ../src/tom/engine/adt/TomConstraint.gom \
    ../src/tom/engine/adt/TomDeclaration.gom \
    ../src/tom/engine/adt/TomExpression.gom \
    ../src/tom/engine/adt/TomInstruction.gom \
    ../src/tom/engine/adt/TomName.gom \
    ../src/tom/engine/adt/TomOption.gom \
    ../src/tom/engine/adt/TomSignature.gom \
    ../src/tom/engine/adt/TomSlot.gom \
    ../src/tom/engine/adt/TomTerm.gom \
    ../src/tom/engine/adt/TomType.gom \
    ../src/tom/engine/adt/TypeConstraints.gom
rm internal/tomast/go.mod
go test ./...

# Vérifier l'équivalence Go ⇄ Java (3 cibles ; nécessite JDK + jars)
go test ./internal/equivtest/... -v -run TestEquivalence

# Vérifier que les 9 hooks de l'ADT engine produisent les bons termes
go test ./internal/backend/... -v -run TestHook

# Self-bootstrap (gomast et tomast doivent être byte-stables)
go test ./internal/backend/... -v -run TestSelfBootstrap

# Parser Go hand-rolled (Phases 4.D + 4.E + 4.F.0 + 4.F.1)
go test ./internal/tomparser/...

# Harnais d'équivalence Go ⇄ Java (Phase 4.C+)
# Depuis le worktree principal (qui contient stable/dist/ après ./build.sh stable) :
go test ./internal/tomparseq/...
# Depuis un autre worktree (stable/dist/ est gitignored) :
TOMGO_STABLE_DIST_LIB=/path/to/main/stable/dist/lib \
    go test ./internal/tomparseq/...

# Ajouter une fixture (nouveau constructeur) :
#   1) tomgo/testdata/parse/<nom>/scenario.t  — minimal .t qui exerce le constructeur
#   2) tomgo/internal/tomparseq/dump_skeleton_test.go  — ajouter "<nom>" à la slice `fixtures`
#   3) (optionnel) tomgo/internal/tomparser/parser_test.go  — un TestParse<Nom> direct
#   4) lancer Java pour récupérer l'AST de référence :
#      java -cp $(find /path/to/stable/dist/lib -name '*.jar' | paste -sd: -):tomgo/internal/tomparseq/java \
#           TomParseDump tomgo/testdata/parse/<nom>/scenario.t
```

**Outillage externe attendu sur la machine** (sinon les tests
correspondants skippent) :
- Go 1.22+ (`go version` ⇒ 1.26.3 testé).
- OpenJDK pour les tests d'équivalence Java — chemin par défaut
  `/opt/homebrew/opt/openjdk` (`brew install openjdk` sur macOS).
  Variables détectées : `$JAVA_HOME` puis `/opt/homebrew/opt/openjdk`
  puis `java` dans `$PATH`.

---

## 9. Pour reprendre une session terminal claude-code

1. Lire ce fichier en entier (tu y es).
2. `cd tomgo && go test ./...` doit passer — si non, regarder le rapport
   de la phase concernée dans `tomgo/reports/`.
3. Ouvrir le dernier rapport (`phase4a-tom-hooks-survey.md` à ce jour)
   pour le détail technique de l'étape qu'on vient de finir.
4. Étape suivante = section 5 (4.B → 4.D → 4.E) de ce fichier.
