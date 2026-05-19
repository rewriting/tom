# Phase 4.B — Survey du parser TOM Java

Objectif : cartographier le parser Java de référence (`stable/tom/engine/parser/`),
identifier la commande permettant d'obtenir un AST byte-stable à comparer
avec celui que produira le parser Go (Phase 4.D), et préciser le sous-
ensemble de grammaire à viser pour la première cible.

---

## 1. Trois pipelines coexistent dans `TomParserPlugin.java`

Le plugin `tom.engine.parser.TomParserPlugin` (raccordé par `Tom.xml`,
deuxième plugin du pipeline après `StarterPlugin`) distingue trois modes
via les options déclarées dans `PLATFORM_OPTIONS` :

| Option        | altName | défaut    | Pipeline activé                                                                 |
| ------------- | ------- | --------- | ------------------------------------------------------------------------------- |
| `parse`       | —       | **false** | mode legacy `antlr2` (`HostLexer` + `TomLexer` + `BackQuoteLexer` + `HostParser`) |
| `newparser`   | `np`    | **false** | mode antlr4 « island » (`TomIslandLexer/Parser`, host = water opaque)            |
| `tomjava`     | `tj`    | **true**  | mode antlr4 « tomjava » (`TomJavaLexer/Parser`, host Java parsé entier)         |
| `printcst`    | `cst`   | false     | print du CST post-`CstBuilder`/post-`CstConverter`                              |
| `printast`    | `ast`   | false     | print du `Code` final (post-`AstBuilder`)                                       |

Les 3 chemins produisent **le même type de sortie** : un `Code` (sort de
`src/tom/engine/adt/Code.gom`), placé dans `getWorkingTerm()` du plugin
puis transmis aux phases suivantes (Transformer → Checker → … → Backend).

### 1.a Pipeline antlr2 (legacy, `parse=true`)

```
Reader  →  HostLexer + TomLexer + BackQuoteLexer (TokenStreamSelector)
        →  HostParser.input()
        →  Visitable  (déjà au format AST cible)
```

Sources : `stable/tom/engine/parser/antlr2/{HostLexer,HostParser,
TomLexer,BackQuoteLexer,BackQuoteParser,TomJavaParser,TomLanguage}.java`.
**Pas pertinent pour le port Go** — code legacy basé sur ANTLR 2, conservé
en compatibilité.

### 1.b Pipeline antlr4 « newparser » (`np=true`)

Le plus simple et le plus proche de ce qu'on va faire à la main en Go.

```
Reader  →  ANTLRInputStream
        →  TomIslandLexer  (icebergs : .t = water* (island water*)*)
        →  CommonTokenStream
        →  TomIslandParser.start()      (PredictionMode.SLL avec fallback LL)
        →  ParseTree  (ANTLR)
        →  CstBuilder  (ParseTreeListener; walker.walk(builder, tree))
        →  CstProgram  (ADT — sort de src/tom/engine/adt/CST.gom)
        →  CstConverter.convert(cst)    (simplifications post-CST)
        →  AstBuilder.convert(cst)
        →  Code        (sort de src/tom/engine/adt/Code.gom)
```

Sources : `stable/tom/engine/parser/antlr4/{TomIslandLexer,
TomIslandParser,CstBuilder,CstConverter,AstBuilder,TomParser}.java`.
Grammaires : `src/tom/engine/parser/antlr4/{TomIslandLexer.g4,
TomIslandParser.g4}` — **126 + 277 = 403 lignes** au total. Petite, lisible,
exactement le genre de surface qu'on peut hand-roller en Go.

### 1.c Pipeline antlr4 « tomjava » (`tj=true`, **mode par défaut**)

Identique au 1.b sauf que le lexer/parser host **comprend** Java
(détection précise des classes, méthodes, expressions hôtes au lieu de les
traiter comme du water opaque). Sources :
`stable/tom/engine/parser/tomjava/*`. Grammaires :
`src/tom/engine/parser/tomjava/{TomJavaLexer.g4,TomJavaParser.g4}` —
**433 + 1 268 = 1 701 lignes** de grammaire ; le code Java généré pèse
**~17 000 lignes** rien que pour `TomJavaParser.java`. Beaucoup trop pour
un hand-roll en 4.D.

Différence d'AST entre 1.b et 1.c : essentiellement les nœuds qui
encapsulent le host code (`TL`, `ITL`, `Comment` dans `Code.gom`) — le
mode `tj` produit une découpe plus fine du host, le mode `np` colle de
gros blocs `water`. Pour la 1ʳᵉ cible (skeleton class + `%typeterm` + `%op`),
on n'a besoin que de ce qui est *island* (les statements TOM eux-mêmes) ;
le host n'apparaît que comme blocs opaques de toute façon. **Le mode
`np` est donc adéquat pour 4.D.**

### 1.d Conséquence pour le harnais 4.C

Le pipeline Java par défaut, c'est `tomjava` (`tj=true`). Pour comparer
le hand-roll Go (qui visera la sémantique `np`) avec la référence Java,
**il faut invoquer Tom avec `-np` explicitement** (et désactiver `-tj`).
Le double switch est nécessaire car `TomParserPlugin.run()` enchaîne des
`if/else if` (cf. lignes 172/247/308 de `TomParserPlugin.java`).

---

## 2. Forme de l'AST produit (cible de comparaison)

### 2.a Type racine

`tom.engine.adt.code.types.Code` — équivalent Go (déjà bootstrappé en 4.A) :
`tomast.Code` (interface, alts dans `internal/tomast/Code.go`).

### 2.b API `AstBuilder` (`stable/tom/engine/parser/antlr4/AstBuilder.java`)

```java
public Code convert(CstProgram cst)                  // ligne  92
public Instruction convert(CstBlock cst)             // ligne 101
public BQTerm convert(CstBQTerm cst)                 // ligne 546
public BQTerm convert(CstTerm cst)                   // ligne 602
public TomTerm convert(CstPattern cst)               // ligne 750
public Constraint convert(CstConstraint …)           // ligne 664
public TomVisit convert(CstVisit cst)                // ligne 847
public TomName convert(CstSymbol cst)                // ligne 860
public Option convert(CstOption cst, String subject) // ligne 639
public Slot convert(CstPairPattern cst)              // ligne 838
public BQSlot convert(CstPairSlotBQTerm cst)         // ligne 630
public InstructionList convert(CstBlockList cst)     // ligne 908
public CodeList convertToCodeList(CstBlockList cst)  // ligne 896
// + une vingtaine de listes typées (TomList, OptionList, …)
```

Tous les types de retour sont des sorts définis dans
`src/tom/engine/adt/*.gom`, donc déjà présents côté Go via `internal/tomast/`.

### 2.c Format de print Java

`TomParserTool.printTree(Visitable)` (ligne 266) applique une stratégie
bottom-up `ToSingleLineTargetLanguage` qui aplatit les `TL`/`ITL`/`Comment`
en `[code-sur-une-ligne]`, puis appelle `tom.library.utils.Viewer.toTree(tree)`.
Format produit : print indenté à la make-tree (`Op(\n  arg1,\n  arg2\n)`
avec backslash-escape des sauts de ligne dans les blocs host).

Ce format **n'est pas trivialement reproductible** depuis Go sans porter
`Viewer.toTree`. Stratégie recommandée pour 4.C : **ne pas réutiliser ce
format**. À la place, écrire les *deux* côtés (Go et Java) sur un format
de notre choix, plus pratique à differ (voir §4).

---

## 3. Comment lancer « parser seul » côté Java

### 3.a Approche naïve : lancer Tom complet avec `-i`

```bash
java -cp <classpath> tom.engine.Tom \
     -X utils/eclipse-plugin/plugin/config/Tom.xml \
     -np -i input.t
```

- `-np` : active newparser.
- `-i` (`intermediate`) : sérialise `getWorkingTerm()` en ATerm sur disque
  → `input.tfix.parsed` + `input.tfix.parsed.table` (`SymbolTable`).
- Le pipeline complet (Transformer → Checker → …) tournera après et
  risque d'échouer sur des `.t` minimaux mal formés — pas grave si on
  s'arrête à la sortie `.tfix.parsed` (écrite *avant* la suite). À
  confirmer en exécutant.

Inconvénient : `Tom.java` ne sait pas s'arrêter après le Parser. Pour un
`.t` qui passe le checker, c'est OK ; pour un fragment minimal, ça peut
casser.

### 3.b Approche propre : un mini-runner Java dédié

Comme on l'a fait en Phase 2 pour Gom (`testdata/equiv/<target>/Scenario.java`),
on écrit une petite classe Java qui :

1. instancie `TomStreamManager` sur le `.t` cible,
2. instancie `TomOptionManager` (lecture des options + chargement de la
   `SymbolTable` par défaut, sans forcer `-tj`),
3. instancie `TomParserPlugin`, lui passe `setArgs(streamManager)`,
4. appelle `plugin.run(...)` ,
5. récupère `plugin.getWorkingTerm()` (un `Code`),
6. imprime via un printer Java *de notre choix* (format `(Op a b c)` ou
   JSON-ish — à fixer en 4.C, voir §4).

C'est ce qui sera utilisé par le harnais `internal/tomparseq/` (ou
extension d'`equivtest/`) en Phase 4.C. Skip propre si le JDK est absent.

Classpath nécessaire (à valider en 4.C) :
- `stable/` (compilé) — pas livré ; on a `applications/prototype3D/lib/tom-compiler-full.jar`
- `stable/lib/runtime/*.jar` : `shared-objects.jar`, `aterm.jar`, `jjtraveler.jar`, `TNode.jar`
- `stable/lib/tools/*.jar` : `antlr.jar`, `antlr-runtime.jar`, `antlr-4.x.jar`, `args4j.jar`, `asm-*.jar`
- la classe parserOnly runner qu'on écrit, à compiler à la volée comme
  pour `Scenario.java` (cf. `internal/equivtest/equivtest.go`).

**À vérifier en 4.C** : le `tom-compiler-full.jar` contient-il déjà
`TomParserPlugin` et toutes ses dépendances ? (Probable, vu son nom.)

---

## 4. Stratégie de comparaison AST proposée pour 4.C

### 4.a Choisir un format de print commun

Le format `Viewer.toTree` du runtime Java n'est pas idéal. Proposition :
**format S-expression** simple, défini par induction sur les sorts TOM :

```
Sort_AltName(slot1=<value>, slot2=<value>, …)
```

avec :
- `<value>` récursif pour les sous-termes,
- listes en `[v1, v2, …]`,
- strings/ints/chars en `"…"`, `123`, `'a'`,
- blocs host (`TL`, `ITL`) → `TL[code-aplati-avec-\\n]`.

Avantage : déterministe, byte-stable, indépendant du runtime sl Java, et
on a déjà 90 % du printer côté Go (le `String()` auto-généré de `tomast.*`).
À ajuster pour que les deux côtés produisent **exactement** la même
chaîne — c'est le diff qui sert de preuve.

### 4.b Pipeline du harnais

```
.t cible
  ├─ Go : tomgo parse <fichier.t> → tomast.Code → printSexp(code) → stdoutG
  └─ Java: javac runner.java + java runner <fichier.t> → printSexp(code) → stdoutJ

assert stdoutG == stdoutJ   (sinon → diff inline)
```

Implémentation Go : extension d'`internal/equivtest/` avec un nouveau
type de scénario `parse_t/<target>/{scenario.t,Runner.java}` OU nouveau
package `internal/tomparseq/`. Skip propre si JDK absent (déjà géré par
equivtest).

### 4.c Critères d'acceptation 4.C

- Au moins **1 fichier `.t` minimal** (probablement synthétisé en
  `testdata/parse/skeleton/`) sur lequel les deux printers produisent la
  même chaîne, byte-pour-byte.
- Le runner Java se compile à la volée, lance Tom en mode `np`, capture
  l'AST, l'imprime.
- Le harnais skippe gracieusement si JDK absent (comme equivtest).

---

## 5. Sous-ensemble cible pour 4.D (1ʳᵉ cible)

Choisi à partir de `TomIslandParser.g4`. Vise un `.t` synthétique
contenant **uniquement** :

```tom
public class Hello {
  %typeterm Foo {
    implement { Object }
  }
  %op Foo a()        // pas de slots, pas de hook
  %op Foo b(x:Foo)   // un slot, pas de hook

  public static void main(String[] args) {
    System.out.println("hello");
  }
}
```

### 5.a Surface grammaticale requise

D'après `TomIslandParser.g4` :

- `start : (island | water)*?` — toujours.
- `island` réduit à `typeterm | operator` pour la 1ʳᵉ cible.
- `water : .` — *un seul token* hôte, donc le parser hand-rolled doit
  juste savoir reconnaître les bornes `%typeterm`/`%op`/`{`/`}` et
  recracher tout le reste comme un blob.
- `typeterm` : `TYPETERM ID (EXTENDS ID)? LBRACE implement isSort? equalsTerm? RBRACE`.
- `operator` : `OP codomain=ID opname=ID LPAREN slotList? RPAREN LBRACE (isFsym|make|getSlot|getDefault)* RBRACE`.
- `slotList` : `slot (COMMA slot)*` ; `slot : ID COLON? ID`.
- `implement` : `IMPLEMENT block` ; `block : LBRACE (island|block|water)*? RBRACE`.

Aucun `%match`, `%strategy`, `%include`, `%gom`, backquote, contrainte,
`pattern`, `bqterm`, `bqcomposite` n'est requis pour la cible — ce sont
des étapes ultérieures (4.E+).

### 5.b Tokens nécessaires (`TomIslandLexer.g4`)

Mots-clés island : `%typeterm` (TYPETERM), `%op` (OP), `%include`
(INCLUDE, pour plus tard), `implement` (IMPLEMENT), `extends` (EXTENDS),
`is_sort` (IS_SORT), `equals` (EQUALS).

Ponctuation : `(` `)` `{` `}` `[` `]` `,` `:` `:=` … (cf. lexer pour la
liste exhaustive — court).

Identifiants : `ID = [A-Za-z_][A-Za-z0-9_]*`.

**Tout le reste** = water. Le hand-roll Go peut donc, en mode water,
copier les caractères tels quels jusqu'à apercevoir un marqueur island
(début de ligne ou contexte adéquat, à confirmer en lisant le lexer).

### 5.c Stratégie d'implémentation (pour 4.D)

1. **Lexer** (`internal/tomparser/lexer.go`) : 100-200 lignes. Reproduit
   le minimum de `TomIslandLexer.g4` pour les tokens island ci-dessus.
   Tout le reste émis comme `TokWater` (un blob de caractères).
2. **Parser** (`internal/tomparser/parser.go`) : 300-500 lignes en
   descente récursive, style `internal/gom/parser.go`. Produit
   directement des `tomast.*` via `Make…`.
3. **Convertisseur CST→AST** : on saute le CST intermédiaire (utilisé en
   Java pour gérer les contextes ANTLR). En descente récursive, on peut
   construire l'AST `Code` directement.
4. **Tests** : `internal/tomparser/parser_test.go` avec ≥1 `.t` synthétique.

---

## 6. Commandes utiles validées en cours de survey

```bash
# Inventaire grammaire / parser Java
wc -l src/tom/engine/parser/antlr4/*.g4         # 126 + 277 + 39 = 442
wc -l src/tom/engine/parser/tomjava/*.g4        # 433 + 1268 = 1701
wc -l stable/tom/engine/parser/antlr4/*.java    # ~9 400
wc -l stable/tom/engine/parser/tomjava/*.java   # ~28 900

# Localisation Tom.xml de référence
cat utils/eclipse-plugin/plugin/config/Tom.xml

# Surface du plugin Parser
grep -n '^  public ' stable/tom/engine/parser/antlr4/AstBuilder.java
```

---

## 7. Décisions à acter avant 4.C / 4.D

| Sujet                                         | Recommandation                                  |
| --------------------------------------------- | ----------------------------------------------- |
| Mode Java à utiliser comme référence          | `-np` (newparser, antlr4 island)                |
| Mode Go à reproduire                          | équivalent newparser : water + island           |
| Mini-runner Java                              | dédié, à la manière de `equivtest/Scenario.java` |
| Format de print AST                           | S-expression maison (cf. §4.a), Go et Java identiques |
| Localisation harnais 4.C                      | extension `internal/equivtest/` ou nouveau `internal/tomparseq/` |
| Première cible 4.D                            | `Hello.t` synthétique avec `%typeterm` + 2 `%op` |
| Tests de non-régression                       | `go test ./internal/tomparser/...` (1 cas vert) + harnais 4.C vert si JDK |

---

## 8. Questions ouvertes (à trancher au début de 4.C)

1. Le `tom-compiler-full.jar` (`applications/prototype3D/lib/`) contient-il
   bien `tom.engine.Tom` + `TomParserPlugin` (et toutes les deps runtime) ?
   → à vérifier par `jar -tf` puis un `java -cp … tom.engine.Tom -h`.
2. `Tom.java` accepte-t-il un `-X` explicite pour `Tom.xml`, ou faut-il
   passer par une autre voie ? → relire `PluginPlatformFactory.extractConfigFileName`.
3. Le pipeline complet va-t-il échouer sur un `.t` minimal avant que
   `.tfix.parsed` soit écrit ? (Probable : le Transformer / Checker
   peuvent fail sur un `Code` partiel.) → mitigation : mini-runner dédié
   (§3.b) qui n'invoque que le Parser.

---

## 9. Erratum — découverte d'exécution

Découvertes faites en testant la chaîne Java *réelle* (utilisateur a
exécuté `./build.sh stable`, ce qui produit `stable/dist/lib/` avec un
jar complet incluant **les `*Plugin` wrappers**) :

1. **Config attendue** : `tom.engine.Tom -X <fichier>` veut un fichier
   **ATerm `.config`**, pas du XML. Le bon fichier est
   `stable/dist/Tom.config` (au format `PlatformConfig(concPlugin(Plugin("Starter","tom.engine.starter.StarterPlugin"),…))`).
   `utils/eclipse-plugin/plugin/config/Tom.xml` n'est PAS chargeable
   directement — c'était une fausse piste du §3.a.

2. **Le mode `-np` (newparser, antlr4 island) fonctionne** : sortie
   `antlr4: <file> parsing + building cst:28 ms  building ast:6 ms`.
   L'AST est construit correctement. On peut donc bien comparer
   l'island Go (4.D) à cette référence.

3. **Bug `aterm.jar` sur JDK récent** : `Tools.generateOutput`
   (`writer.write(ast.toString())`) déclenche un
   `IllegalArgumentException: Null charset name` quand l'objet à
   sérialiser est un `aterm.ATerm` (issu de `symbolTable.toTerm().toATerm()`).
   La classe `aterm.stream.BufferedOutputStreamWriter` (livrée dans
   `aterm.jar`) appelle `Charset.forName(null)` sur JDK 11+. Le `--add-opens
   java.base/sun.nio.cs=ALL-UNNAMED` ne suffit pas (le `null` vient
   d'aterm, pas du JDK). Conséquence : **l'option `-i` produit le bug
   sur le `.tfix.<phase>.table`** ; donc on évite cette option pour le
   harnais.

4. **`Code.toString()` Java fonctionne** parfaitement : implémenté via
   `StringBuilder` + `toStringBuilder(StringBuilder)` (cf. dump javap
   sur `tom.engine.adt.code.CodeAbstractType`), **aucune dépendance à
   `aterm.jar`**. C'est le format qu'on va utiliser comme référence.

5. **Print AST via `--printast`** produit un format `Viewer.toTree`
   (arbre Unicode box-drawing) côté `printTree` — joli mais non
   reproductible byte-pour-byte côté Go sans porter `Viewer`. Inutile.

### Stratégie révisée pour 4.C

- **Côté Java** : un mini-runner `TomParseDump.java` (sous
  `tomgo/internal/tomparseq/java/`) qui :
  - instancie `TomStreamManager`, `TomOptionManager`, `TomParserPlugin`,
  - force le mode `np` via `OptionManager`,
  - appelle `plugin.setArgs(...)`, `plugin.run(...)`,
  - imprime sur stdout `((Code) plugin.getWorkingTerm()).toString()`,
  - exit 0 si le parser n'a pas levé d'exception, exit 1 sinon.
- **Côté Go** : un harnais `internal/tomparseq/` qui :
  - compile et lance le mini-runner Java (classpath = `stable/dist/lib/*.jar`),
  - lance le parser Go (Phase 4.D — à coder),
  - diff `String()` Java vs `String()` Go.
- **Format** : `Code.toString()` Gom natif côté Java ⇄
  `tomast.Code.String()` côté Go. Tous les deux suivent la même
  convention `Op(arg1,arg2,…)` héritée de shared-objects. Les petites
  divergences (échappement, listes vides, format des positions) se
  résolvent en alignant les deux printers — sans réinventer un format
  intermédiaire.

### Plan d'exécution 4.C (commits prévus)

| # | Description                                                      | Critère                                                         |
|---|------------------------------------------------------------------|-----------------------------------------------------------------|
| 1 | `internal/tomparseq/java/TomParseDump.java` + harnais Go         | `go test ./internal/tomparseq/...` skipe sans JDK              |
| 2 | Synthétiser `testdata/parse/skeleton/scenario.t` minimal         | Mini-runner Java imprime un term `Tom(concCode(…))` non-vide   |
| 3 | Construire le même `tomast.Code` à la main côté Go               | `tomast.String()` produit la même chaîne (itérer si écart)     |
| 4 | Aligner les printers Java/Go si divergence (commit séparé)       | Diff byte-vide                                                  |

### Plan d'exécution 4.D (après 4.C)

| # | Description                                                      | Critère                                                         |
|---|------------------------------------------------------------------|-----------------------------------------------------------------|
| 1 | `internal/tomparser/lexer.go` (island lexer hand-rolled)         | Tokens sur `scenario.t` ≡ ceux qu'on attend                    |
| 2 | `internal/tomparser/parser.go` (descente récursive → tomast)     | `parse(scenario.t)` → `tomast.Code` non-nil                    |
| 3 | Brancher 4.D sur le harnais 4.C, swap construction manuelle      | Harnais vert, diff byte-vide                                    |
| 4 | Étendre à 2-3 autres `.t` (un `%op`, un `%typeterm extends`, …)  | Idem par cible                                                  |

---

## 10. Conclusion

Survey + erratum livrés. Toutes les ambiguïtés résolues :
- Référence Java = `stable/dist/lib/` (produit par `./build.sh stable`),
  utiliser `-X stable/dist/Tom.config -np`.
- Format de comparaison = `Code.toString()` côté Java ⇄
  `tomast.Code.String()` côté Go.
- Hand-roll Go = grammaire `TomIslandParser.g4` (water + island).
- Mécanisme `-i` natif **désactivé** à cause du bug `aterm.jar` ;
  mini-runner Java direct utilisé à la place. Quand on portera le
  Transformer/Checker/… en Go, on rajoutera un dump à chaque phase et
  on étendra le mini-runner Java avec la même logique (instanciation
  manuelle du plugin suivant).

Prochain pas concret : §9 « Plan d'exécution 4.C » étape 1.
