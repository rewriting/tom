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
4.F.2 (`%match` avec pattern variable nommée — `x -> { }`),
4.F.3 (`%match` avec application nullaire — `Foo() -> { }`),
4.F.4 (`%match` avec application + sous-patterns —
`Foo(x, Bar()) -> { }`), 4.F.5 (`%match` multi-sujets —
`(a, b) { x, y -> { } }`, dépend de 4.A.1), 4.F.6 (`%match` avec
plusieurs rules — `{ _ → {} x → {} }`), 4.F.7 (`%match` avec body
non-vide — `_ -> { doSomething(); }`, body lowered via la pipeline
water 4.F.0), 4.F.8 (`%match` avec sujet explicite —
`Foo() << t -> { }`, RHS bare ID), 4.F.9 (variable-star `x*`/`_*`
sur patterns), 4.F.10 (annotation `name@pat` → contrainte
`AliasTo`), 4.F.11 (anti-pattern `!pat` → `AntiTerm(pat)`),
4.F.12 (OR-pattern `(Foo|Bar)(args)` → `TermAppl` avec multi-name
list), 4.F.13 (backquote constant `\`Foo()` sur RHS de `<<` —
premier pas sur les bqterms, contexte contenu), 4.F.14 (backquote
variable dans le body d'une action rule — switch en mode-island via
sub-parser, `BQTermToInstruction(bqterm)` dans `AbstractBlock`) et
4.F.15 (water entourant un backquote dans le body : `{ return \`x; }`
→ 3 instructions TL / BQTermToInstruction / TL avec positions
correctes), 4.F.16 (backquote application avec sous-terme dans le
body : `{ return \`Foo(x); }` → BQTermToInstruction(BQAppl(...,
concBQTerm(BQVariable(x))))) et 4.F.17 (backquotes nested dans args
d'un bqterm body : `{ return \`Foo(\`a, \`b); }` → chaque inner `\``
wrappé en `Composite(CompositeTL(ITL("\140")), CompositeBQTerm(...))`
côté Java, reproduit côté Go ; introduit `sharedobjects.JavaEscape`
pour aligner le print des strings sur la convention AT/aterm) livrées.
La phase **4.A.1** corrige le hook AU généré par `emitAUPrologue`
pour absorber l'unité (`AndConstraint(MC, TrueConstraint()) → MC`),
en accord avec `HookTypeExpander.java:569`.
**30 fixtures** sous `tomgo/tests/testdata/parse/` valident byte-pour-byte
l'AST Go contre la référence Java. tomgo est un outil Go autonome qui :

- lit un fichier `.gom` (avec ou sans hooks),
- génère un package Go reposant sur un portage Go de **shared-objects**
  (hash-consing, partage maximum des structures de données),
- supporte le mode **batch** : N `.gom` qui s'importent mutuellement
  compilent en un seul package Go,
- **utilise ses propres AST générés** (`stable/library/gomast/` pour le sous-
  langage Gom, `stable/library/tomast/` pour le langage TOM) — auto-amorce.

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
| `tomgo/src/gom/adt/`              | 5 `.gom` décrivant l'AST de Gom (Code, Gom, Objects, Rule, SymbolTable) |
| `tomgo/src/engine/adt/`           | 15 `.gom` décrivant l'AST de TOM (TomTerm, TomInstruction, TomSignature, TomOption, TomName, TomType, TomConstraint, TomDeclaration, TomExpression, TomSlot, CST, Code, Il, Theory, TypeConstraints) |
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
  src/                           # sources haut-niveau (.gom, plus tard .t)
    gom/adt/                     # 5 .gom AST Gom (Code, Gom, Objects,
                                 #   Rule, SymbolTable) — copie depuis
                                 #   /src/tom/gom/adt/
    engine/adt/                  # 15 .gom AST TOM engine — copie depuis
                                 #   /src/tom/engine/adt/
  stable/                        # bootstrap Go (100 % Go), généré ou
                                 # auto-amorcé depuis src/
    gom/
      parser/                    # parser .gom (descente récursive) +
                                 #   accesseurs gomast + hookscanner
      backend/                   # générateur .gom → .go natif gomast
                                 #   + knownHookTable (10 entrées)
      equiv/                     # harnais d'équivalence Gom Go ⇄ Java
                                 #   (3 cibles : minimal/leaf/list)
    tom/parser/
      parser/                    # parser TOM hand-rolled (Phases 4.D+)
                                 #   + water tokeniseur ANTLR-fidèle
      equiv/                     # harnais d'équivalence parser TOM Go ⇄ Java
                                 #   (TomParseDump.java mini-runner)
    platform/                    # Plugin interface + chain runner
                                 #   (Phase 5 — analogue de tom.platform.PluginPlatform)
      plugins/                   # 1 fichier par plugin (Phase 5+)
                                 #   parser.go = wrapper du parser TOM
    library/
      sharedobjects/             # runtime public — hash-cons + max-sharing
                                 #   (interface Term, Factory.Build, mixers)
      gomast/                    # AST Gom GÉNÉRÉ depuis tomgo/src/gom/adt/
                                 #   (auto-amorce, byte-stable via TestSelfBootstrap)
      tomast/                    # AST TOM GÉNÉRÉ depuis tomgo/src/engine/adt/
                                 #   (Phase 4.A, byte-stable via TestSelfBootstrap_Tomast)
  tests/                         # tous les artefacts liés aux tests
    share/                       # mappings Tom utilisés par le harnais (tom-mappings/)
    testdata/
      corpus/gom-nohooks/        # 10 .gom de test/gom/ sans hooks
      equiv/{minimal,leaf,list}/ # scénarios paired Go/Java pour stable/gom/equiv
      parse/                     # fixtures .t du parser TOM (Phases 4.D+)
      java-ast/                  # dumps Java cachés (tom --intermediate)
  reports/                       # rapports par phase
  README.md
  go.mod                         # module tom/tomgo, Go 1.22
```

> Idée générale : `src/` = développement (langages de haut niveau —
> .gom, .t, .go) ; `stable/` = amorce bootstrap, 100 % Go, généré
> depuis `src/` puis copié.

Packages livrés cette itération :
- `stable/tom/parser/parser/` — parser TOM hand-rolled (descente récursive
  100 % Go, pas d'ANTLR). Couvre `%typeterm` (+`extends`), `%op` (+slots),
  `%oplist`, `%oparray`, `%include`, `%match` avec un ou plusieurs sujets,
  body vide ou non-vide (host-code → `TL` via pipeline water), patterns
  `_`, variable nommée, ou application `Foo(p1, …, pN)` (sous-patterns
  parsés récursivement). `water.go` contient le tokeniseur
  ANTLR-fidèle + `buildHostblocks` + `mergeHostblocks`.
- `stable/tom/parser/equiv/` — harnais d'équivalence AST Go ⇄ Java :
  mini-runner `java/TomParseDump.java` qui instancie `TomParserPlugin`
  directement (sans `tom.engine.Tom`/`Tom.config`), `tomparseq.go`
  pour la résolution JDK et la normalisation des paths
  (`__INPUT__`/`__DIR__`).
- `tests/testdata/parse/<name>/scenario.t` — 30 fixtures actuellement.

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
- Corpus copié dans `tests/testdata/corpus/gom-nohooks/` : Builtin, Dotted,
  Imported, Importing, Leaf, List, Minimal, Yang, Ying, fromterm/foo.
- Rapports : `phase1-inventory.md`, `phase1.md`.

### Phase 2 — `.gom → .go` avec shared-objects ✅
- **2.a** `stable/library/sharedobjects/` : interface `Term`, `Factory.Build`,
  hash-cons thread-safe (`sync.RWMutex`), mixers `OneAtATime` /
  `MixSymbol` / `StringHash`.
- **2.b** `stable/gom/parser/` : lexer + parser descente récursive.
- **2.c** `stable/gom/backend/` : un fichier `.go` par module Gom dans le
  package cible. Mode batch (`gom-batch`) → un seul package Go partagé.
- **Hooks** : table `knownHookTable` mappe `(scope, pointcut, kind)` →
  émission Go. Phase 2 livre l'entrée `sort HookList:block()` (Objects.gom)
  → méthode `ContainsTomCode() bool` sur l'interface + chaque alt.
- **Équivalence Go ⇄ Java** (`stable/gom/equiv/`) : 3 cibles
  (minimal/leaf/list) produisent un stdout **byte-identique** au
  compilateur Gom Java de référence (`tom.gom.Gom` invoqué via
  `applications/prototype3D/lib/tom-compiler-full.jar`).
- **ADT Gom** : les 5 `tomgo/src/gom/adt/*.gom` (63 sorts, 1 hook)
  compilent ensemble + 5 tests `ContainsTomCode`.

Rapports : `phase2a-sharedobjects-survey.md`, `phase2.md`, `equiv.md`,
`adt.md`.

### Phase 3 — Auto-amorce de l'AST Gom ✅
- **`stable/library/gomast/`** est généré par tomgo depuis les 5 `.gom` de
  `tomgo/src/gom/adt/`. ~6 273 lignes, 63 sorts.
- Le parser construit directement des `gomast.*` (plus de struct V1
  hand-written). Le backend traverse `gomast.GomModule` nativement via
  type assertions. `internal/gom/ast.go`, `bridge.go`, etc. **supprimés**.
- **`TestSelfBootstrap`** : tomgo regénère `stable/library/gomast/`
  **byte-identique** au commit — preuve de stabilité de l'auto-amorce.

Rapport : `phase3.md`.

### Phase 4.A — Auto-amorce de l'AST TOM + 9 hooks engine ✅
- **`stable/library/tomast/`** généré par tomgo depuis les 15 `.gom` de
  `tomgo/src/engine/adt/`. **107 sorts, ~17 400 lignes**.
- **9 hooks de l'ADT engine** implémentés directement en Go via le
  nouveau mécanisme `emitMakePrologue` (`stable/gom/backend/backend.go`) :

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

- **3 fixes structurels du backend** nécessaires pour que `stable/library/tomast/`
  compile :
  - keyword `else` ajouté à `isGoKeyword` (slot `else` dans `Conditional`),
  - collision `MakeSubterm` détectée pré-emit, suffixée en
    `MakeSubterm<Sort>` (`Subterm` est alt de BQTerm ET de Term),
  - slots `String:String` / `Hash:…` renommés en `<Name>_` pour ne pas
    masquer les méthodes `String()` / `Hash()` du struct.
- **`TestSelfBootstrap_Tomast`** : tomgo regénère `stable/library/tomast/`
  **byte-identique** au commit.
- **14 hook smoke tests** dans `stable/gom/backend/tomast_hooks_test.go`
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
- `stable/tom/parser/equiv/java/TomParseDump.java` : mini-runner Java qui
  contourne `tom.engine.Tom` (et donc `Tom.config`/optimize2 et toutes
  les options inter-plugins) via une `OptionManager` minimaliste
  (HashMap).
- `stable/tom/parser/equiv/tomparseq.go` : détection JDK, compilation
  paresseuse du runner, exécution, normalisation des paths absolus en
  `__INPUT__`, diff byte-pour-byte.
- Variable `TOMGO_STABLE_DIST_LIB` pour pointer vers le build du
  worktree principal (`stable/dist/` est gitignored).
- Tests verts : `TestSkeletonAgainstJava` (AST manuel ↔ Java) et
  `TestSkeletonGoParserAgainstJava` (parser Go ↔ Java).

### Phase 4.D + 4.E.1–4.E.5 — Parser TOM Go hand-rolled ✅
- `stable/tom/parser/parser/parser.go` : descente récursive 100% Go,
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
- `stable/tom/parser/parser/water.go` : tokenise un water en `NL`/`WS`/visible
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
- `stable/tom/parser/parser/parser.go` étendu avec `parseMatch`,
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
- Fixture `match0d_appl`.
- Rapport : `phase4f3-match-appl-nullary.md`.

### Phase 4.F.4 — `%match` avec application + sous-patterns ✅
- Nouvelle fonction `parsePatternArgList` qui consomme une liste
  `pattern (',' pattern)*` entre `(` et `)`, par récursion sur
  `parsePattern`.
- `Foo(x, Bar())` →
  `TermAppl(concOption(), concTomName(Name("Foo")),
  concTomTerm(Variable(...,Name("x"),...), TermAppl(...,Name("Bar"),...)),
  concConstraint())`.
- Couvre gratuitement n'importe quelle imbrication (`Foo(_, Bar(x, _))`).
- Fixture `match0e_appl_args`.
- Rapport : `phase4f4-match-appl-args.md`.

### Phase 4.A.1 — correction du hook AU (absorption de l'unité) ✅
- `emitAUPrologue` (`stable/gom/backend/backend.go`) émet maintenant un
  filtre qui supprime les éléments égaux à l'unité après aplatissement,
  puis short-circuit sur 0 args (→ unit) ou 1 arg (→ bare element).
- Mirroir du comportement Java cons-time (`HookTypeExpander.java:569` :
  `if (head == userNeutral) return tail; if (tail == userNeutral) return head;`).
- Effets observables :
  - `MakeAndConstraint(MC, TrueConstraint()) == MC`
  - `MakeAndConstraint(unit, unit, unit) == unit`
  - `MakeOrConstraint(MC, FalseConstraint()) == MC`
- `MakeOrConstraintDisjunction` (AU sans unité) inchangé.
- Tests `TestHook_AndConstraint_AbsorbsTrueConstraintUnit` /
  `TestHook_OrConstraint_AbsorbsFalseConstraintUnit` ajoutés.
- Rapport : `phase4a1-au-hook-unit-absorption.md`.

### Phase 4.F.5 — `%match` multi-sujets ✅
- `parseActionRule` consomme désormais `pattern (',' pattern)* '->'
  '{' BALANCED '}'` ; valide que `len(patterns) == len(subjects)`.
- Pour N sujets : `MakeAndConstraint(MC1, …, MCN)` ; pour N==1 le
  hook AU corrigé renvoie le bare MC (compat 4.F.1–4.F.4 préservée).
- Fixture `match0f_multi`.
- Rapport : `phase4f5-match-multi-subject.md`.

### Phase 4.F.6 — `%match` avec plusieurs rules ✅
- Aucun code parser à modifier : la boucle de `parseMatch` consommait
  déjà N action-rules — il manquait juste la fixture pour le prouver
  byte-pour-byte.
- Fixture `match0g_rules` exerce `_ → {} x → {}` ; chaque
  `ConstraintInstruction` porte son OriginTracking propre (ligne 4
  pour le `_`, ligne 5 pour le `x`).
- Rapport : `phase4f6-match-multi-rules.md`.

### Phase 4.F.7 — `%match` avec body non-vide ✅
- Nouvelle méthode `captureBalancedBlock` qui retourne le contenu
  entre `{` et `}` + la position post-`{` ; nouveau helper
  `lowerActionBody(content, start)` qui réutilise la pipeline water
  de 4.F.0 (`tokenizeWater` → `buildHostblocks` → `mergeHostblocks`)
  pour produire `CodeToInstruction(TargetLanguageToCode(TL(…)))`.
- `parseActionRule` consomme désormais le body via ces helpers.
- Body purement whitespace → zéro hostblock → `concInstruction()`
  vide (compatibilité 4.F.1–4.F.6 préservée). Body avec visibles
  → un `CodeToInstruction(…)` dans `concInstruction(…)`.
- Limites : pas d'îlots TOM imbriqués dans le body ; pas de
  string-literal/comment awareness.
- Fixture `match0h_body`.
- Rapport : `phase4f7-match-body.md`.

### Phase 4.F.8 — `%match` avec sujet explicite (`<<`) ✅
- `parseActionRule` reconnaît `pattern '<<' bqterm` après chaque
  pattern de la liste séparée par `,`. Le bqterm RHS **remplace**
  le sujet implicite venant des parenthèses du `%match(…)` pour
  cette position-là (cf. `AstBuilder.java:683-698`).
- RHS bqterm limité à un bare ID (`BQVariable(..., Name(id), ...)`)
  pour ce premier jet ; pas de type annotation, pas d'application.
- Fixture `match0i_explicit`.
- Rapport : `phase4f8-match-explicit-subject.md`.

### Phase 4.F.9 — variable-star patterns `x*` / `_*` ✅
- `parsePattern` reconnaît un suffixe `*` après `_` ou un ID et
  produit `VariableStar(concOption(), Name(id_or_empty), unknownType,
  concConstraint())` au lieu de `Variable(...)`.
- Suffixe `*` interdit sur les applications `Foo(...)` (conforme à
  la grammaire ANTLR).
- Fixture `match0j_star`.
- Rapport : `phase4f9-match-variable-star.md`.

### Phase 4.F.10 — annotated patterns `name@pat` ✅
- Grammaire (`TomIslandParser.g4:151`) : `pattern : ID '@' pattern` —
  le **nom d'annotation est AVANT le `@`**, le sous-pattern AVRES.
  Pour `x@a` : `x` = nom d'alias, `a` = sous-pattern. *Première
  livraison de la 4.F.10 avait inversé cette priorité ; corrigé après
  cross-validation Java.*
- `parsePattern` détecte par lookahead un `ID '@'` au début, consomme
  les deux et rappelle récursivement pour le sous-pattern.
- `name @ pat` → `pat` avec `concConstraint(AliasTo(Variable(
  concOption(OT(Name(name), 0, "unknown file")), Name(name),
  unknownType, concConstraint())))`. Les valeurs `0` / `"unknown file"`
  sont les placeholders du Java (`ASTFactory.java:285`), remplacés
  ultérieurement par le typer.
- Helper `addPatternConstraint` qui reconstruit le pattern via type
  assertion (Variable / VariableStar / TermAppl) ; ne préserve pas
  encore les contraintes pré-existantes (à étendre quand on aura
  des cas `a@b@pat`).
- Fixture `match0k_annot` exerce une annotation sur sous-position
  `Foo(x@a)` (= `x` alias pour le sous-pattern `a`).
- Rapport : `phase4f10-match-annotated-pattern.md`.

### Phase 4.F.11 — anti-pattern `!pat` ✅
- `parsePattern` reconnaît un préfixe `!` (avec garde `peek(1) != '='`
  pour ne pas capturer `!=`), consomme et rappelle `parsePattern`
  récursivement, puis wrappe le résultat dans `AntiTerm(...)`.
- `!Foo()` → `AntiTerm(TermAppl(concOption(), concTomName(Name("Foo")),
  concTomTerm(), concConstraint()))` (cf. `AstBuilder.java:780-792`).
- La récursion via `parsePattern` (et non `parseBasePattern`) permet
  les combinaisons `!!pat`, `!(name@pat)`, etc.
- Fixture `match0l_anti`.
- Rapport : `phase4f11-match-anti-pattern.md`.

### Phase 4.F.12 — OR-pattern `(Foo|Bar)(args)` ✅
- `parseBasePattern` reconnaît un `(` initial comme tête de pattern
  disjoncté : `(F1|F2|…)`, suivi d'une arg-list explicite.
- `(Foo|Bar)()` → `TermAppl(concOption(), concTomName(Name("Foo"),
  Name("Bar")), concTomTerm(), concConstraint())` (multi-name list,
  cf. `AstBuilder.java:793-804` via `Cst_Appl`).
- Limites : `(Foo|Bar)` SANS args (Cst_ConstantOr) non supporté ;
  symboles à théorie `?`/`??` non parsés (pas de `MatchingTheory`).
- Fixture `match0m_or`.
- Rapport : `phase4f12-match-or-pattern.md`.

### Phase 4.F.13 — backquote constant sur RHS de `<<` ✅
- Premier pas sur les **backquote terms**. Nouveau `parseBQTerm` :
  consomme un `` ` `` optionnel, lit un ID, dispatch sur `(` (BQAppl)
  ou rien (BQVariable). Sub-args parsés récursivement via
  `parseBQTermArgList`.
- `parseActionRule` route le RHS de `<<` vers `parseBQTerm` à la
  place de `parseSubject`. `parseSubject` reste utilisé pour les
  sujets parens-implicites de `%match(...)`.
- `` `Foo() `` → `BQAppl(concOption(OT(Name("Foo"), <line>, file),
  ModuleName("default")), Name("Foo"), concBQTerm())` (cf.
  `AstBuilder.java:551-554`).
- Fixture `match0n_bqappl`.
- Rapport : `phase4f13-bqterm-constant.md`.

### Phase 4.F.14 — backquote variable dans le body d'action rule ✅
- `lowerActionBody` ne traite plus le body comme un water opaque
  uniforme. Nouveau sub-parser (`newSubParser(content, filename,
  start)`) qui parcourt le contenu byte par byte ; à chaque `` ` ``
  rencontré, le water accumulé est flush via la pipeline
  `tokenizeWater + buildHostblocks + mergeHostblocks`, et le bqterm
  est consommé via `parseBQTerm` puis emballé en
  `BQTermToInstruction(bqterm)`.
- Mappage Java : `CstBuilder.exitBlock` (lignes 236-256) →
  `AstBuilder.convert(CstBlock)` (lignes 101-544). `HOSTBLOCK` →
  `CodeToInstruction(TargetLanguageToCode(TL(...)))` (ligne 109) ;
  `Cst_BQTermToBlock` → `BQTermToInstruction(...)` (ligne 119).
- Pour `{ \`x }` (water purement whitespace autour) : l'`AbstractBlock`
  contient un seul `BQTermToInstruction(BQVariable(..., Name("x"),
  ...))`. Les water-chunks vides ne produisent aucun TL.
- Compatibilité 4.F.7 : les fixtures sans backquote (match0b…match0n)
  restent byte-stables — la boucle ne déclenche jamais la branche `\``,
  et le `flushWater` final émet exactement le même TL qu'avant.
- Limites : nested `%match` etc. dans le body restent traités comme
  water opaque ; pas de scope étendu via parenthèses (`\`(...)`).
- Fixtures `match0o_bqbody` (water purement whitespace) et
  `match0p_bqbody_water` (water non-trivial `{ return \`x; }` → 3
  instructions TL / BQTermToInstruction / TL).
- Rapport : `phase4f14-match-bqbody.md`.

---

## 5. Phase 4 — suite (à dérouler quand on rouvre une session)

**Stratégie générale** : porter le compilateur Java de `stable/tom/engine/`
phase par phase, en commençant par le parser. À chaque phase, vérifier
que le pipeline Go produit le même AST `tomast.*` que la référence Java
sur les mêmes entrées. Comparaison via le print du term (déjà prouvée
byte-portable en Phase 2 pour Gom).

### 4.E + 4.F.0 + … + 4.F.14 (livrés) — Constructeurs `%typeterm`/`%op`/`%oplist`/`%oparray`/`%include`/`%match` (`_`/`x`/`Foo(...)`/`(Foo|Bar)(...)`/`x*`/`_*`/`name@pat`/`!pat` + multi-sujets + multi-rules + body non-vide avec bqterm interne + sujet explicite `<<` avec RHS bqterm) + water ANTLR-fidèle

Voir §4 ci-dessus. **21 fixtures** validées contre Java :
`skeleton`, `op_noargs`, `op_slots`, `typeterm_extends`,
`oplist_oparray`, `include_local`, `water_multi`, `match0b`,
`match0c_named`, `match0d_appl`, `match0e_appl_args`, `match0f_multi`,
`match0g_rules`, `match0h_body`, `match0i_explicit`, `match0j_star`,
`match0k_annot`, `match0l_anti`, `match0m_or`, `match0n_bqappl`,
`match0o_bqbody`, `match0p_bqbody_water`, `match0q_bqappl_body`,
`match0r_bq_multi`, `match0s_bqstar`, `strategy0_minimal`,
`strategy1_visit`, `strategy2_visit_body`, `meta0_minimal`,
`match0t_and`.
Pattern à réutiliser pour chaque nouveau constructeur :

1. un `.t` minimal dans `tests/testdata/parse/<nom>/`,
2. 1 ligne dans la slice `fixtures` de `TestGoParserAgainstJava`
   (`stable/tom/parser/equiv/dump_skeleton_test.go`),
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
3. ~~**Pattern application `Foo(x, y)`** avec sous-patterns~~ ✅
   livré en 4.F.4 → `parsePatternArgList` récursif, argList non vide.
4. ~~**Multi-subjects** `%match(a, b) { p1, p2 -> { … } }`~~ ✅ livré
   en 4.F.5 → `parseActionRule` consomme N patterns ; combinés par
   `MakeAndConstraint` (hook AU corrigé en 4.A.1 → N==1 ⇒ bare MC,
   N≥2 ⇒ `AndConstraint(MC1, …, MCN)`).
5. ~~**Plusieurs rules** dans un même `%match`~~ ✅ livré en 4.F.6 —
   la boucle de `parseMatch` était déjà en place ; fixture
   `match0g_rules` ajoutée pour prouver l'invariant byte-pour-byte.
6. ~~**Body non vide** dans l'action rule~~ ✅ livré en 4.F.7 —
   nouveau `captureBalancedBlock` + `lowerActionBody` qui réutilise
   la pipeline water 4.F.0 pour produire un `CodeToInstruction(
   TargetLanguageToCode(TL(content, start, end)))`. Limites :
   pas d'îlots TOM imbriqués dans le body, pas de string-awareness.
7. ~~**Sujet explicite `pattern << bqterm`**~~ ✅ livré en 4.F.8 —
   chaque slot d'une rule peut porter une override `<< bqterm` qui
   remplace le sujet implicite des parens. RHS bqterm = bare ID
   pour ce premier jet (`BQVariable(...,Name(id),...)`).
8. ~~**Variable-star `x*` / `_*`**~~ ✅ livré en 4.F.9 — suffixe `*`
   après `_` ou un `ID` → `VariableStar(...)` au lieu de `Variable(...)`.
9. ~~**Pattern annoté `name @ pat`**~~ ✅ livré en 4.F.10 — ajoute
   une `AliasTo(Variable(...Name(name)...))` constraint au pattern.
10. ~~**Pattern anti `!pat`**~~ ✅ livré en 4.F.11 — `parsePattern`
    consomme `!` puis rappelle récursivement, wrappe dans `AntiTerm`.
11. ~~**OR-pattern `(F1|F2)(args)`**~~ ✅ livré en 4.F.12 —
    `parseBasePattern` accepte une tête parenthésée multi-nommée,
    produit `TermAppl` avec `concTomName(Name(F1), Name(F2), …)`.
12. ~~**Backquote constant `\`Foo()` sur RHS de `<<`**~~ ✅ livré
    en 4.F.13 — nouveau `parseBQTerm` (récursif) qui produit `BQAppl`
    ou `BQVariable` selon présence de `(`. Premier pas sur les
    bqterms, limité au RHS d'un constraint explicite.
13. ~~**Backquote dans le body d'action rule**~~ ✅ livré en 4.F.14 —
    `lowerActionBody` switche en mode-island à chaque `` ` `` via un
    sub-parser, émet `BQTermToInstruction(bqterm)` au milieu de la
    séquence water/island. Compat byte-stable pour les bodies sans
    backquote.
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

Extension d'`stable/gom/equiv/` (ou nouveau `internal/parsereq/`) :

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
hand-rolled dans le style de `stable/gom/parser/`. Bénéfices : 100 % Go,
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

L'orchestrateur est **`mage`** (installation : `go install
github.com/magefile/mage@latest`). Toutes les commandes se lancent
depuis le dossier `tomgo/`.

```bash
cd tomgo

# Aide : lister les cibles
mage -l

# Build + tests + vet (la chaîne complète + self-bootstrap)
mage all

# Tests / build / vet individuels
mage test
mage build
mage vet

# Régénérer src/library/{gomast,tomast}/ et vérifier que ça compile.
# stable/library/ n'est PAS touché — c'est le rail de sécurité demandé :
# on régénère d'abord en zone scratch, on inspecte, on promote ensuite.
mage regen

# Inspecter la différence avec le bootstrap actuel
diff -r src/library/gomast stable/library/gomast
diff -r src/library/tomast stable/library/tomast

# Promouvoir le résultat dans stable/library/ (lance regen d'abord, puis
# copie src/library/* → stable/library/*, puis fait tourner les tests).
# doc.go et *_test.go de stable/library/<pkg>/ sont préservés.
mage promote

# Self-bootstrap (byte-stabilité de stable/library/{gomast,tomast})
mage selfBootstrap

# Harnais d'équivalence Go ⇄ Java (Gom + TOM parser) ; skip si pas de JDK
mage equivJava

# Nettoyage du scratch
mage clean
```

Cibles bas niveau (en dessous de mage) si besoin :

```bash
# Recensement des hooks .gom du dépôt
go run ./cmd/tomgo scan-hooks ..

# Compiler un .gom isolé en Go (mode single)
go run ./cmd/tomgo gom -o /tmp/out tests/testdata/corpus/gom-nohooks/Minimal.gom
(cd /tmp/out && go build ./... && go test ./...)

# Compiler plusieurs .gom cross-importants en UN SEUL package Go (batch)
go run ./cmd/tomgo gom-batch --pkg adt -o /tmp/adt src/gom/adt/*.gom
(cd /tmp/adt && go build ./...)

# Vérifier que les 9 hooks de l'ADT engine produisent les bons termes
go test ./stable/gom/backend/... -v -run TestHook

# Parser Go hand-rolled (Phases 4.D + 4.E + 4.F.0…4.F.13)
go test ./stable/tom/parser/parser/...

# Harnais d'équivalence parser TOM Java depuis un autre worktree
# (stable/dist/ est gitignored) :
TOMGO_STABLE_DIST_LIB=/path/to/main/stable/dist/lib \
    go test ./stable/tom/parser/equiv/...

# Ajouter une fixture (nouveau constructeur) :
#   1) tomgo/tests/testdata/parse/<nom>/scenario.t  — minimal .t qui exerce le constructeur
#   2) tomgo/stable/tom/parser/equiv/dump_skeleton_test.go  — ajouter "<nom>" à la slice `fixtures`
#   3) (optionnel) tomgo/stable/tom/parser/parser/parser_test.go  — un TestParse<Nom> direct
#   4) lancer Java pour récupérer l'AST de référence :
#      java -cp $(find /path/to/stable/dist/lib -name '*.jar' | paste -sd: -):tomgo/stable/tom/parser/equiv/java \
#           TomParseDump tomgo/tests/testdata/parse/<nom>/scenario.t
```

**Outillage externe attendu sur la machine** (sinon les tests
correspondants skippent) :
- Go 1.22+ (`go version` ⇒ 1.26.3 testé).
- `mage` 1.17+ pour l'orchestration (`go install github.com/magefile/mage@latest`).
- OpenJDK pour les tests d'équivalence Java — JDK 25 testé via
  `brew install openjdk`. Variables détectées : `$JAVA_HOME` puis
  `/opt/homebrew/opt/openjdk` (qui pointe sur la Cellar courante)
  puis `java` dans `$PATH`.
- `stable/dist/lib/` doit exister avec les jars Java construits :
  `cd /tom && ./build.sh stable` (~5 s).

Quand ces trois éléments sont en place, `mage equivJava` valide
**byte-pour-byte** les 22 fixtures contre le parser Java de référence.
Sans JDK ou `stable/dist/lib/`, le harnais skip proprement et seuls
les tests Go-vs-Go (parser_test.go) tournent comme pin de non-régression.

---

## 8.bis. Pipeline Platform (Phase 5+)

Le compilateur Tom Java enchaîne 11 plugins via `tom.platform.PluginPlatform`.
L'ordre canonique (de `BootstrapPluginsList.java`) est :

  1. **Starter**      — pré-traitement, gestion des includes
  2. **Parser**       — `.t`/`.tom` → CST → AST (livré côté Go en Phase 4)
  3. **Transformer**  — re-écriture du code (avant typage)
  4. **SyntaxChecker** — vérifications syntaxiques (constraints, sorts)
  5. **Desugarer**    — abaisse les sucre syntaxiques
  6. **Typer**        — résout les `unknown type`, remplit la SymbolTable
  7. **TypeChecker**  — vérifications de types
  8. **Expander**     — expansion des `%match` en `If`/`Switch`
  9. **Compiler**     — génère le code de matching
 10. **Optimizer**    — opt. du code généré
 11. **Backend**      — émission du code Java/Go cible

Le Go reproduit cette architecture via `stable/platform/` :

```go
type Plugin interface {
    Name() string
    Run(in State) (State, error)
}
```

Chaque plugin transforme une `State{Filename, Source, Code}` (à
enrichir au fil des phases). La `Platform` les chaîne dans l'ordre.

État (Phase 5) :
- ✅ Platform infrastructure (`stable/platform/platform.go`).
- ✅ Plugin **Parser** (`stable/platform/plugins/parser.go`) — wrappe
  `stable/tom/parser/parser.Parse` ; le test smoke `parser_test.go`
  prouve qu'un Platform à 1 plugin produit l'AST attendu.
- ✅ Plugin **Starter** + 9 stubs identity pour les phases suivantes
  (`stable/platform/plugins/{starter,stubs,pipeline}.go`).
- ☐ Transformer / SyntaxChecker / Desugarer / Typer / TypeChecker /
  Expander / Compiler / Optimizer / Backend — à porter l'un après
  l'autre, chacun avec son jeu de tests d'équivalence Java contre le
  pipeline officiel.

**Harnais de validation cross-phase (Phase 5.2)** : le runner
`stable/tom/parser/equiv/java/TomPipelineDump.java` étend le
`TomParseDump` historique pour chaîner les plugins
Parser → Transformer → SyntaxChecker → Desugarer → Typer, et dumper
l'AST après n'importe quelle phase. Côté Go :
`JavaToolchain.DumpJavaPhase(input, phase)` (`Phase{Parsed,
Transformed, Synchecked, Desugared, Typed}`) retourne l'AST normalisé,
prêt à être comparé byte-pour-byte avec un plugin Go.

`TestPipelineDump_MutationMatrix` pin l'observation suivante sur les
30 fixtures actuelles : Transformer et SyntaxChecker sont **identité**
partout ; Desugarer mute 13 fixtures (essentiellement `_` →
`Name("_f_r_e_s_h_v_a_r_N")`) ; Typer mute 7 fixtures supplémentaires
(backquote application + `%strategy`). Pas besoin de porter le
Transformer et le SyntaxChecker pour démarrer — l'identity stub est
correct sur tout le corpus. **Premier vrai plugin à porter** :
`Desugarer`.

---

## 8.ter. Strategy library (Phase 6+)

Pré-requis avant le portage des plugins en Go : porter
`tom.library.sl` (37 fichiers Java / ~3900 LOC, autonome — pas de
dépendance sur jjtraveler). C'est la bibliothèque de strategies que
chaque visiteur `%strategy` / `%visit` généré utilise comme runtime.

Surface portée à `stable/library/sl/` (Phases 6.0 → 6.4) :
- ✅ **6.0** — interfaces `Strategy`/`Visitable`/`Introspector`,
  `ErrVisitFailure` sentinel, combinateurs `Identity`/`Fail`,
  `VisitableIntrospector` helper.
- ✅ **6.1** — combinateurs primitifs : `Sequence`, `Choice`, `All`,
  `One`, `SequenceId`, `ChoiceId`, `OneId` (avec le shortcut
  null-tail Java `NewSequence(s, nil) == s`).
- ✅ **6.2** — récursion fixed-point : `Mu` + `MuVar` avec expansion
  en place du graph de strategies (équivalent du `Mu.expand` Java).
- ✅ **6.3** — `AbstractStrategyBasic` : helper que les visiteurs
  générés étendent. Expose `Any()` comme slot 0 pour la fallback.
- ✅ **6.4** — built-in walks : `MakeTry`, `MakeRepeat`, `MakeRepeatId`,
  `MakeTopDown`, `MakeTopDownIdStopOnSuccess`, `MakeOnceTopDown`,
  `MakeOnceTopDownId`, `MakeBottomUp` (portage byte-équivalent des
  `tom_make_*` Java).
- ✅ **6.5** — Introspector tomast généré par le backend GOM
  (`emitVisitableMethods` dans `stable/gom/backend/backend.go`).
  Chaque alt expose `ChildCount/ChildAt/SetChildAt/Children/SetChildren`
  ; variadics utilisent `len(t.Slots)` comme `ChildCount`. Slots
  primitifs (`string`, `int64`, …) retournés bruts via `any` (pas de
  boxing `VisitableBuiltin`). SetChildAt/SetChildren reconstruisent
  via `Make<Op>` pour préserver le hash-cons.
- ✅ **6.6** — Plugin `Desugarer` (`stable/platform/plugins/desugarer.go`)
  porte le `DesugarerPlugin.java` :
  - **Pass #1** `MakeTopDown(desugarUnderscore)` — `_` / `_*` →
    `Name("_f_r_e_s_h_v_a_r_N")` (counter pré-incrémenté, reset par
    Run).
  - **Pass #2** `MakeTopDownIdStopOnSuccess(replaceTermApplTomSyntax)`
    — `TermAppl(opts, names, args, constraints)` →
    `RecordAppl(opts, names, concSlot(PairSlotAppl(EmptyName, arg)*),
    constraints)`. Recursivement applique le walker sur chaque arg
    (la version symbol-table-aware sera activée après le Typer).
  - **Pass #3** (`replaceBQRecordApplTomSyntax`) ☐ à porter quand
    une fixture exerce `` `Foo[a=v] ``.

  `TestDesugarer_ParityWithJava` épingle l'équivalence byte-pour-byte
  côté Java sur les **30 fixtures** via `DumpJavaPhase(...,
  PhaseDesugared)`.
- ✅ **6.7** — Plugin `Typer` (version minimale,
  `stable/platform/plugins/typer.go`, ~200 LOC) :
  - `BQAppl(opts, name, args)` → `FunctionCall(name, contextType, args)`
    quand symbole inconnu, avec contextType propagé depuis le parent
    (MatchConstraint.aType pour les subjects).
  - `Variable.AstType` ("unknown type") → contextType depuis
    `MatchConstraint.aType`.
  - `BQVariable.AstType` propagé par nom depuis le scope des patterns.
  - `TestTyper_ParityWithJava` byte-équivalent sur les **30 fixtures**
    de `tests/testdata/parse/`. Le port complet du `NewKernelTyper.java`
    (constraint solver Hindley-Milner + subtyping, ~1700 LOC) est
    reporté jusqu'à ce que des fixtures plus riches exercent la
    machinery (cf. 6.8).
- 🟡 **6.8** — Exploration du corpus `test/` (121 `.t`). Premières
  extensions parser :
  - **Subjects typés** : `%match(term t1, term t2) { … }` —
    `parseSubject` reconnaît un `codomain var` quand un second ID
    suit le premier (lookahead byte-level).
  - **OR-pattern + record-args** : `(zero|zero)[]` →
    `RecordAppl(opts, [Name(zero), Name(zero)], concSlot(), …)`.
  - **Record-pattern** : `Foo[slot=value, …]` →
    `RecordAppl` direct (parsé par `parsePatternSlotList`).
  - **IncludeSearchPath** : variable globale + env var
    `TOMGO_TOM_INCLUDE` pour résoudre `%include` hors du dossier
    source (équivalent Go de `TomStreamManager.getImportList`).

  `cmd/parsecheck` est l'utilitaire qui rapporte le compte sur le
  corpus `test/`. `TestCorpus_ParityWithJava` est un dashboard
  non-bloquant (t.Logf) qui surface dans le test verbose les
  comptes parseOK/parityOK/parseFail/parityDiff.

  État actuel du corpus `test/` (28 fixtures racine, .t directs sans
  les sous-répertoires) : **parseOK=20, parityOK=1, parityDiff=19,
  parseFail=8, javaFail=0**. La seule fixture parity-OK est `Test.t`
  (juste un `%gom { ... }` vide, pas de %match).

  Extensions parser ajoutées cette itération :
  - Patterns constantes : entiers (`0`, `-2`, `1L`, `1.23d`),
    chars (`'a'`), strings (`"foo"`) → `TermAppl(opts,
    concTomName(Name(literal)), concTomTerm(), concConstraint())`.
  - `%gom { … }` reconnu et émis comme
    `InstructionToCode(AbstractBlock(concInstruction()))` (corps
    consommé en water).
  - `%typeterm` hooks parsés : `is_sort(t) { body }` →
    `IsSortDecl(BQVariable(Type(codomain)), Code(abstractCode(body)),
    OT)` ; `equals(t1,t2) { body }` → `EqualTermDecl` ; `implement
    { body }` → stocké dans `signature.Sorts[codomain]` (utilisé
    par le Typer pour propager le TLType).
  - Réécriture `abstractCode($var → {N})` dans les bodies de hooks
    (mirror `tom.engine.tools.ASTFactory.abstractCode`).
  - Ordre des decls dans `concDeclaration` inversé pour matcher
    l'ordre LIFO de `CstBuilder`.
  - Harnais Java `TomPipelineDump` corrigé : option `X` (chemin
    `Tom.xml`) seedée pour le `%gom` path, `userImportList` étendue
    avec les mappings Tom standards (`utils/eclipse-plugin/
    plugin/include/{java,}/`) — la référence Java passe désormais
    sur tout fixture acceptée par le parser (javaFail=0).

  **Architecture SymbolTable** (Phase 6.8.A) :
  - `tomparser.ParseAll(src, filename)` retourne `*ParseResult` :
    `{Code, Sorts map[string]string, Symbols map[string]TomSymbol}`.
  - `parseOp` consomme le slot list `(p:Nat, q:Nat)` proprement et
    construit un `tomast.MakeSymbol(name, TypesToType(domain,
    codomain), PairNameDeclList, options)` via `buildTomSymbol`.
  - `parseTypeterm` capture le body de `implement` dans
    `signature.Sorts[codomain]`.
  - `platform.SymbolTable` étendu pour porter
    `Sorts map[string]string` (TLType par sort) et
    `Symbols map[string]TomSymbol`.
  - Le **Parser plugin** appelle `ParseAll`, merge les deux maps
    dans `State.Symbols`.
  - Le **Desugarer** (`replaceTermApplTomSyntax`) prend la
    SymbolTable, consulte `Symbols[opName]` pour récupérer les
    slot names via `PairNameDeclList` — émet
    `PairSlotAppl(Name("p"), …)` au lieu d'`EmptyName()` quand le
    symbole est connu (Case B-defined de DesugarerPlugin.java).
  - Le **Typer** (`newKernelTyper`) prend la SymbolTable, fait :
    - `BQAppl(name, args)` → `BuildTerm(name, args, moduleName)` si
      symbole connu, sinon `FunctionCall(name, contextType, args)`.
    - Propage subject's Type ↓ pattern via `MatchConstraint.aType`
      relevé quand `unknown type`.
    - Type les slots de `RecordAppl(name, slots)` via
      `domainTypesFor(name)` (les types de domaine du symbole).
    - Phase finale `substituteTLTypes` : remplace partout
      `Type(_, sort, EmptyTL)` par `Type(_, sort, TLType(body))`
      quand sort.implement est connu.

  Le dashboard est passé à **parityOK=3 (Test.t, Peano.t,
  TestOptimizer.t)** après ces ajouts incrémentaux :
  - **Comment stripping** dans `tokenizeWater` : `// …\n` et `/* …
    */` sont sautés (Java les route en `-> skip`), avec la position
    line/col qui avance toujours à travers les bytes.
  - **Variable.Constraints recursion** : `AliasTo(boundVar)` est
    typé avec le contextType de la Variable hôte (équation
    `Equation(boundVar.Type, hostVar.Type)` du Java résolue
    eagerly).
  - **RecordAppl.Constraints recursion** : pareil pour les
    contraintes d'un RecordAppl (utilisé par `pat@x@x@...`).
  - **BQAppl args symbol-directed** :
    `inferBQTermListWithDomain` passe le slot type comme
    contextType pour chaque arg (e.g. `suc(plus(x,y))` →
    `BuildTerm(suc, [FunctionCall(plus, Type("Nat"), …)])`).
  - **%include propagation** : `parseInclude` utilise
    désormais `ParseAll` et fait un merge no-collision des
    `Sorts`/`Symbols` du fichier inclus dans le `signature` du
    parser englobant.
  - **%oplist/%oparray** : `parseVariadicOp` construit un
    `TomSymbol(name, TypesToType(concTomType(elemType),
    codomain), ConcPairNameDecl(), [DeclarationToOption(
    MakeEmptyList|MakeEmptyArray(...))])` et le stocke dans
    `signature.Symbols`. Le marker dans Options est ce que
    `TomBase.isListOperator`/`isArrayOperator` regarde côté Java.

  **Phase 6.9 — parser test/ exhaustif** (cumul d'itérations) :
  - `parseOK=27/28` ; `parityOK=6` (Peano.t, Test.t, TestNonVarSubjects.t,
    TestOptimizer.t, cfib1.t, loulou.t).
  - **Comparaison AST via `astcmp`** (`stable/tom/parser/equiv/astcmp/`)
    qui parse les deux dumps, applique des règles de simplification
    (`Composite(CompositeBQTerm(t))` → `t`), puis compare
    structurellement. Plus tolérant que `strings.Equal` mais
    sémantiquement équivalent.
  - **Constraints** complets : NumericConstraint (`!=`, `==`, `<`,
    `<=`, `>`, `>=`), `&&`, `||`, parens, `pat << bqterm`. LHS de
    `<<` reconnu comme pattern (lookahead `@`/`[`) ou bqterm.
  - **Subjects non-variables** (`%match(5)`, `%match(f(x))`, `%match(int 5)`).
  - **Visit rule body** : bare bqterm (`a() -> b()`) → `Return(bqterm)`.
  - **Backquote** : `\`(x)` (parens sans fsym), `\`S9(3)` (arg entier),
    char/string literals dans bqterm.
  - **Comment stripping** dans body de `%match`, `%strategy`, water.
  - **`%op` hooks parsés** (Phase 6.9.A) : `is_fsym`, `get_slot`,
    `get_default`, `make`, `equals` pour `%op` ; `make_empty`,
    `make_append`, `make_insert`, `get_size`, `get_element` pour
    `%oplist`/`%oparray`. Chaque hook devient un `Declaration`
    attaché au `TomSymbol.Options` via `DeclarationToOption(...)`.
    `$var` → `{N}` réécriture appliquée aux bodies. Permet à
    `TomBase.isListOperator`/`isArrayOperator` de fonctionner
    correctement côté Typer (via la présence de
    `MakeEmptyList`/`MakeEmptyArray`/`MakeAddList`/`MakeAddArray`
    dans les options).
  - **Annotation sur AntiTerm** : `name@!pat` ; la constraint AliasTo
    descend à l'intérieur de l'AntiTerm.

  Reste à porter pour les autres fixtures du corpus :
  - **`BuildConsList` / `BuildConsArray`** dans le Typer
    pour les BQAppl dont le symbole est un list/array
    operator (avec `WithSymbol(name)` ajouté à la TypeOption
    du codomain).
  - **Top-level backquote islands** (`\`conc(...)` hors d'un
    %match) : `bqcomposite` dans la grammaire ANTLR4, qui
    devient `InstructionToCode(BQTermToInstruction(…))` côté
    AST.
  - **Composite wrapping** des args dans les bqterms
    backquotés (loulou reste à 28 octets — un seul wrap
    `Composite(CompositeBQTerm(BQVariable(x)))` manquant
    sur l'arg `x` de `plus(x,y)`).
  - **%op hooks** (is_fsym, get_slot, make, get_default) à
    parser pour peupler les Options du TomSymbol et ajouter
    les marqueurs qui changent le comportement Java
    (e.g. `MakeAddList` pour les list-ops, `MakeDecl` pour
    les builtin make hooks).
  - Et au fond le port complet de **`NewKernelTyper.java`**
    (résolveur de contraintes pour la cohérence
    pattern-subject avec polymorphisme).

  Gaps qui ressortent du parityDiff :
  - **Body de `%typeterm`/`%op` non parsé** côté Go (les hooks
    `implement {…}`, `is_fsym {…}`, `get_slot {…}`, `make {…}`,
    `equals {…}`, etc. sont actuellement consommés comme water
    opaque). Java parse ces hooks et en émet des `Declaration`s
    (IsSortDecl, IsFsymDecl, GetSlotDecl, MakeDecl, EqualTermDecl)
    qui peuplent la `SymbolTable`. Sans cela, le Desugarer
    `replaceTermAppl` et le Typer `TransformBQAppl` ne trouvent
    aucun symbole → ils ne savent pas que `suc(p:Nat)` a un slot
    nommé `p`, par exemple, et émettent `PairSlotAppl(EmptyName(),
    …)` au lieu de `PairSlotAppl(Name("p"), …)`.
  - **SymbolTable absente** dans `State.Symbols` (allouée vide par
    le Starter, jamais peuplée par le Parser).
  - **Constraint solver Typer** : Variable.AstType ne se met pas à
    jour à partir du subject's Type quand celui-ci est concret
    (e.g. `BQVariable(t1, Type("Nat"))` → la pattern Variable(x)
    devrait devenir Type("Nat")). Le typer simplifié ne propage
    pas dans cette direction (pattern←subject).
  - **Plus de constructions parser à porter** : patterns
    constantes (entiers, strings), contraintes numériques
    (`<`, `>=`, `==`, …), subjects non-variables (`%match(f(x))`),
    backquote arg avec entiers (`\`S9(3)`), action body bare
    bqterm (sans `{ }`), annotation sur AntiTerm.

Différences API vs Java :
- `error` au lieu d'exceptions cochées (`ErrVisitFailure` sentinel,
  branching via `errors.Is`).
- Pas de `Visit(Introspector)` (Environment-style) ; tous les plugins
  downstream n'utilisent que `VisitLight`. À ajouter quand on
  rencontrera Omega/Path.
- `abstractCombinator` embedded fournit `args []Strategy` +
  `ChildCount/At/SetAt` à tous les combinateurs (analogue du
  Java `AbstractStrategyCombinator`).

25 tests verts sur un AST jouet (`node{label, kids}` implémentant
`Visitable`) — la suite épingle Identity/Fail, chaque combinateur,
`Mu` à deux niveaux de récursion, et chaque built-in walk
(TopDown/TopDownIdStopOnSuccess/etc.).

---

## 9. Pour reprendre une session terminal claude-code

1. Lire ce fichier en entier (tu y es).
2. `cd tomgo && go test ./...` doit passer — si non, regarder le rapport
   de la phase concernée dans `tomgo/reports/`.
3. Ouvrir le dernier rapport (`phase4a-tom-hooks-survey.md` à ce jour)
   pour le détail technique de l'étape qu'on vient de finir.
4. Étape suivante = section 5 (4.B → 4.D → 4.E) de ce fichier.
