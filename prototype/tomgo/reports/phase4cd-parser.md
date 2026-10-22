# Phase 4.C + 4.D + 4.E — Harnais d'équivalence + parser TOM Go

Livré : un parser TOM hand-rolled en Go qui produit, sur 6 fixtures
représentatives, un AST `tomast.Code` **byte-identique** à celui que
produit le parser antlr4 (mode `-np`) de référence Java. Couverture
actuelle des constructeurs d'îlot :

| Constructeur            | Cible (`testdata/parse/<name>/scenario.t`) | AST émis (extrait)                              |
| ----------------------- | ------------------------------------------ | ----------------------------------------------- |
| `%typeterm` minimal     | `skeleton`                                 | `TypeTermDecl(Name, concDeclaration(), OT)`     |
| `%op` sans slots        | `op_noargs`                                | `SymbolDecl(Name)`                              |
| `%op` avec slots        | `op_slots`                                 | `SymbolDecl(Name)` (slots → SymbolTable)        |
| `%typeterm extends`     | `typeterm_extends`                         | `TypeTermDecl(Name, ..., OT)` (subtype → opts)  |
| `%oplist` / `%oparray`  | `oplist_oparray`                           | `ListSymbolDecl(Name)` / `ArraySymbolDecl(Name)`|
| `%include`              | `include_local`                            | `TomInclude(concCode(InstructionToCode(AbstractBlock(concInstruction(CodeToInstruction(…))))))` |

Tous validés via `TestGoParserAgainstJava/<name>` qui compare la sortie
`fmt.Sprintf("%v", code)` du parser Go avec le `code.toString()` Java,
byte-pour-byte, après normalisation des chemins (`__INPUT__` pour
`scenario.t`, `__DIR__` pour son dossier — permet de référencer un
fichier `%include` au même endroit sans absoluty-leak).

---

## 1. Cible originelle (MVP 4.D — `skeleton`)

```tom
// tomgo/testdata/parse/skeleton/scenario.t
public class Skeleton {
  %typeterm Foo {
    implement { Object }
  }
}
```

AST produit, **identique côté Go et Java** :

```
Tom(concCode(
  TargetLanguageToCode(TL("public class Skeleton {\n  ",
                          TextPosition(1,1), TextPosition(2,3))),
  DeclarationToCode(TypeTermDecl(
      Name("Foo"),
      concDeclaration(),
      OriginTracking(Name("Foo"), 2, "__INPUT__"))),
  TargetLanguageToCode(TL("\n}\n",
                          TextPosition(4,4), TextPosition(5,1)))
))
```

Les placeholders `__INPUT__` (scenario.t) et `__DIR__` (son dossier)
sont appliqués des deux côtés (Go et Java) avant comparaison, par
`tomparseq.normalizeAST`.

---

## 2. Architecture livrée

### 2.a `tomgo/internal/tomparseq/`

Harnais d'équivalence AST Go ⇄ Java (Phase 4.C).

| Fichier | Rôle |
|---|---|
| `java/TomParseDump.java` | Mini-runner Java direct : instancie `TomParserPlugin` avec une `OptionManager` minimaliste (HashMap), parse en mode `-np`, imprime `code.toString()` |
| `java/TomParseDump.class` | Compilé à la volée par `EnsureCompiled()` |
| `tomparseq.go` | Détection JDK, compilation paresseuse du runner, exécution, normalisation du path absolu en `__INPUT__`, diff |
| `dump_skeleton_test.go` | 3 tests : sanity Go-only, AST manuel ↔ Java, **parser Go ↔ Java** |

Le runner Java **ne passe pas par `tom.engine.Tom`** ni par `Tom.config` :
ces deux exigent toutes les options de tous les plugins du pipeline
(`optimize2`, etc.) — voir `phase4b-parser-survey.md` §9. Il fournit
plutôt une implémentation locale d'`OptionManager` qui stocke des
clés/valeurs dans une map et renvoie `Boolean.FALSE` par défaut. Toutes
les options consultées par `TomParserPlugin` et `TomStreamManager` sont
seedées explicitement (`jCode=true`, `newparser=true`, `encoding=UTF-8`,
etc.).

Format de comparaison : `Code.toString()` côté Java (implémenté par
`CodeAbstractType.toString()` → `StringBuilder` + `toStringBuilder` —
**zéro dépendance à `aterm.jar`**, contournant le bug Charset.forName
sur JDK 11+) ↔ `fmt.Sprintf("%v", code)` côté Go (utilise les `String()`
auto-générés par le backend tomgo, héritiers de la convention
shared-objects). Les deux produisent `Op(arg1,arg2,…)`, séparateurs
identiques, échappement identique.

### 2.b `tomgo/internal/tomparser/`

Parser TOM hand-rolled (Phases 4.D + 4.E.1–4.E.5).

| Fichier | Rôle |
|---|---|
| `parser.go` | Descente récursive 100% Go. Pas d'ANTLR. Construit directement des `tomast.*` via `Make*`. Résout les `%include` relativement à `Parse(_, filename)` |
| `parser_test.go` | Une fonction `parseFixture(t, name)` partagée + un test par fixture (`TestParse*`) |

**Grammaire couverte (sous-ensemble de `TomIslandParser.g4`)** :

```
start    : (water | islandStmt)* EOF
islandStmt : typeterm | op | oplist | oparray | include
typeterm : '%typeterm' ID ('extends' ID)? '{' BALANCED '}'
op       : '%op' ID ID '(' slotList? ')' '{' BALANCED '}'
oplist   : '%oplist'  ID ID '(' ID '*' ')' '{' BALANCED '}'
oparray  : '%oparray' ID ID '(' ID '*' ')' '{' BALANCED '}'
include  : '%include' '{' path '}'
slotList : slot (',' slot)*
slot     : ID ':' ID
water    : byte*?  (anything that is not the start of an island)
```

Pour tous les constructeurs sauf `%include`, le corps `{ … }` est
consommé via `consumeBalancedBlock` (compteur de braces) — son contenu
n'est PAS encodé dans le AST (codomain, slots, options, hooks vont
historiquement dans la `SymbolTable` côté Java, pas dans l'AST `Code`).
C'est exact pour les fixtures actuelles ; à revisiter quand on
attaquera le typer/checker.

**Détail `%include`** : le path est lu entre `{` et `}` (mêmes
caractères que le rule ANTLR : `DOT|SLASH|BACKSLASH|ID`, plus le
whitespace que ANTLR forward via `ctx.getText()` — `strings.TrimSpace`
suffit). Le chemin est résolu via `filepath.Abs(filepath.Join(filepath.Dir(parser.filename), path))` quand il est relatif. Le fichier est lu et re-parsé via
`Parse(...)`, puis ses codes top-level sont enveloppés dans le sandwich
`TomInclude(concCode(InstructionToCode(AbstractBlock(concInstruction(
CodeToInstruction(ci), …)))))` — exactement la forme que produit le
parser Java avant que les hooks `concInstruction:make_insert` aplatissent.

### 2.c Pièges identifiés et résolus

1. **Path absolu dans `OriginTracking`** : Java enregistre l'`fileName`
   absolu. Le harnais le réécrit en `__INPUT__` avant diff ; côté Go,
   l'appelant passe `"__INPUT__"` directement.

2. **Convention bizarre de fin de water trailing** : pour un water qui
   précède l'EOF et termine par `\n`, Java retourne la position
   *avant* le `\n` final (i.e. ligne du `\n`, col 1), pas
   `(line+1, 1)` comme on s'y attendrait. Reproduit par le parser Go :
   si le water finit par `\n` ET on est à EOF, on décrémente `line` et
   met `col = 1`. Vérifié sur 2 cas de test (cf. §2.d).

3. **`Tom.config` minimal écarté** : tentative de réduire la chaîne à
   `Starter + Parser` rejetée par `TomOptionManager.initialize` qui
   exige les options de tous les plugins (`optimize2` en premier). →
   mini-runner direct retenu.

4. **Bug `aterm.jar` JDK 11+ contourné** par usage de `code.toString()`
   au lieu de `Tools.generateOutput(... aterm)`. Format identique en
   pratique (canonique Gom).

### 2.d Quirk de fin de water — preuves expérimentales

Test empirique avec deux `.t` produisant des waters trailing différents,
exécutés par `TomParseDump` :

| Fichier | Water trailing | `start` | `end` Java |
|---|---|---|---|
| `skeleton.t` | `"\n}\n"` (suivi de EOF) | (4,4) | (5,1) ← `\n` final swallowed |
| `skeleton_no_nl.t` | `"\n}"` (pas de `\n` final) | (4,4) | (5,2) ← convention standard |
| `multi.t` (avant un island) | `"a\nb\nc\n"` (suivi de `%typeterm`) | (1,1) | (4,1) ← convention standard |

Conclusion : la "swallow" du `\n` final ne s'applique **que** quand le
water est suivi de l'EOF (pas d'un island). Le parser Go imite ce
comportement strictement.

---

## 3. Tests

```bash
cd tomgo
go test ./internal/tomparser/...                              # parser-only
TOMGO_STABLE_DIST_LIB=/path/to/stable/dist/lib \
  go test ./internal/tomparseq/...                            # harness + Java
```

Sans `TOMGO_STABLE_DIST_LIB` ni `stable/dist/lib` accessible, les tests
qui requièrent Java sont **skipés proprement** (pas marqués FAIL).

Tests verts (`go test ./internal/tomparser/... ./internal/tomparseq/...`) :
- 6 × `TestParse*` (tomparser) — parser Go ↔ chaîne attendue
- `TestSkeletonGoDump` (tomparseq) — AST manuel, pas de Java
- `TestSkeletonAgainstJava` (tomparseq) — harnais avec AST manuel
- `TestGoParserAgainstJava` (tomparseq) — sous-tests par fixture,
  **parser Go ↔ Java** byte-pour-byte

---

## 4. Variable d'environnement

`TOMGO_STABLE_DIST_LIB` : chemin vers `stable/dist/lib/` quand on
travaille depuis un worktree qui ne contient pas son propre build
(`stable/dist/` étant gitignored). Si absent, le harnais cherche
`$repoRoot/stable/dist/lib`.

Pour rebâtir : depuis la racine du dépôt principal,
```bash
./build.sh stable
```

---

## 5. Limites connues (à étendre dans les phases suivantes)

- Statements TOM restants à couvrir : `%match`, `%strategy`, `%gom`,
  backquote terms, metaquote `%[…]%`. Tous nécessitent un sous-parser
  significativement plus large (patterns, contraintes, action rules,
  …).
- Les corps `{ … }` des îlots sont consommés via brace-counting sans
  interpréter les chaînes ni les commentaires : suffisant pour les
  fixtures actuelles, à revoir si une cible insère un `}` dans une
  chaîne hôte.
- `extends`/`is_sort`/`equals` côté typeterm et codomain/slots côté op
  sont tokens consommés mais non encodés dans l'AST — cohérent avec le
  Java sur les fixtures actuelles, à re-vérifier sur des typetermes
  plus riches (en particulier ceux qui activent `Cst_Equals` ou
  `Cst_IsSort` dans AstBuilder, lignes 144–162).
- Pas de gestion des commentaires `/* … */` et `//` en mode water (le
  Java les traite comme du water, ce qu'on fait aussi par défaut).
- Le mini-runner Java ne réinjecte pas le bytecode
  `tom-compiler-full.jar` newer-than-source automatiquement : son
  timestamp-check est sur la date du `.java`, pas du jar. À régénérer
  manuellement après `./build.sh stable` :
  `rm internal/tomparseq/java/TomParseDump.class`.

---

## 6. Conclusion

Phases 4.B, 4.C, 4.D, 4.E.1–4.E.5 verrouillées sur 6 fixtures
(skeleton, op_noargs, op_slots, typeterm_extends, oplist_oparray,
include_local). Le pipeline complet de validation
**Go-parser → Java-parser-reference** est opérationnel :

```
scenario.t
  │
  ├── tomparser.Parse (Go)        → tomast.Code → String() → goSide
  │                                                          │
  │                                                normalizeAST(__INPUT__/__DIR__)
  │
  └── TomParseDump.java (Java)    → Code.toString()       → javaSide
                                              │
                                     normalizeAST(__INPUT__/__DIR__)
                                              │
                       AssertParityWithGo: goSide == javaSide ?
                                              │
                                          ✓ green
```

Prochaines cibles, par ordre d'effort croissant :

1. **`%match(t) { … }`** — patterns, contraintes, action rules. Le
   plus gros morceau — c'est ce qui justifie l'existence de TOM.
2. **`%strategy ... extends ...`** + `visit` blocks.
3. **`%gom`** — gom directives inline.
4. **Backquote terms** (`` `Op(args) ``) dans les action rules.
5. **Metaquote** `%[ … ]%`.

À chaque cible : un `.t` minimal dans `testdata/parse/<nom>/` + une
ligne dans la slice `fixtures` du `TestGoParserAgainstJava` + 1 entrée
dans `parser_test.go` (optionnel).
