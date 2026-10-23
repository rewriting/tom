# J4 — compilateur Tom écrit en Tom+Go, de la fin vers le début — design

Date : 2026-10-24
Branche : `tom-go`
Statut : décisions D1–D6 prises par l'utilisateur le 2026-10-24 (§8) ;
plan du premier jalon : `docs/superpowers/plans/2026-10-24-j4-0-bridge.md`

Références : `docs/superpowers/specs/2026-10-22-tom-go-backend-design.md`
(J−1…J3), `docs/superpowers/specs/2026-10-23-repo-layout-design.md`,
`doc/go/README.md` (« Orientation pour la suite », « Points à régler au
début de J4 »), `.superpowers/sdd/progress.md` (décisions de
l'utilisateur). Les expériences citées ont été faites dans un répertoire
de travail hors dépôt (`…/scratchpad/j4/`) ; le dépôt n'a pas été modifié
(sauf ce fichier). Détail : `.superpowers/sdd/j4-spec-report.md` ; mise à
jour après les décisions : `.superpowers/sdd/j4-plan-report.md`.

## 1. Résumé

- **Faisable, et le pont est presque gratuit.** L'export que J4 demande
  existe déjà : avec `--intermediate`, chaque phase écrit son terme de
  travail par `toString()` de Gom (`Tools.generateOutput`), sur une seule
  ligne. Le paquetage Go `tom/engine/adt` (généré par gomgo) relit ces
  fichiers par `CodeFromString` / `TomSymbolTableFromString`, et son
  `String()` les réécrit **à l'identique, octet pour octet : 675 fichiers
  sur 675** (toutes les phases, les 94 cas de T1 et les 27 fichiers des
  cas smoke, jusqu'à 310 Ko, 19 ms pour le plus gros).
- **L'AST exporté avec `-go` contient déjà les constructions propres à Go**
  (`Assign(BQVariable(…,Name("_"),…),…)` des sujets non liés dans 82
  fichiers optimisés, `next_minimal_extract` et les introspecteurs Go en
  `ITL`). La table des symboles porte le code hôte Go des mappings générés
  par gomgo.
- **La table exportée ne suffit pas à elle seule** : `SymbolTable.toTerm()`
  n'exporte que les symboles, ni les types (`mapTypeName`), ni les marques
  « utilisé » (`usedKeyEntry`). Le backend recalcule presque toutes ces
  marques, sauf celles que le compilateur pose sur `concInt` pour le
  filtrage AC (`Compiler.t:613-614`). Le pont exporte donc un **paquet**
  (code + table + marques + options), pas seulement les deux fichiers
  `--intermediate`.
- **Premier jalon (backend Go en Tom+Go)** : environ 4 300 à 4 600 lignes
  de Tom+Java à porter (GoGenerator, la quasi-totalité de GenericGenerator
  et du parcours d'AbstractGenerator, OutputCode, un sous-ensemble de
  SymbolTable/TomBase, la passe `Collector` de BackendPlugin), soit
  environ 5 000 à 5 800 lignes de Tom+Go. Le côté Go ne produit que du Go
  (D5) : le backend Java n'est pas porté.
- **Préalable bloquant** : un paquetage Go ne peut aujourd'hui contenir
  qu'**un seul** `.t` (expérience : deux `.t` d'un même paquetage
  redéclarent `tom_equal_term_int`, `tom_is_sort_int`, `tom_stringHead`…).
  Le backend s'étend sur au moins trois `.t` d'un même paquetage. Décision
  D1 (b) : un préfixe par fichier sur les déclarations générées, appliqué
  d'abord au `GoGenerator` Java (J4.0b).
- **Deux défauts de gomgo** à corriger avant d'écrire du Tom+Go (J4.0a) :
  `adt.tom` lit `.String` au lieu du champ `String_` ; `ToATerm` panique
  sur une liste AU qui finit par un élément (§3.2).
- **Plus gros risque** : la taille et le style du code à porter (héritage
  Java à trois niveaux, exceptions comme flot de contrôle, environ 150
  méthodes qui lèvent `IOException`), puis, pour les phases suivantes,
  l'ordre d'itération des `HashSet`/`HashMap` Java, dont dépend parfois
  l'AST produit (par ex. `Compiler.t:608-618`).

## 2. Décisions déjà prises (contraintes, non rediscutées)

1. **Portage de la fin vers le début.** Le compilateur Tom+Java exécute les
   phases 1…k, puis exporte **une fois** le terme de travail et la table des
   symboles ; le pont ne va que de Java vers Go. Un « suffixe » écrit en
   Tom+Go (compilé par la chaîne `-go`) exécute les phases k+1…fin.
2. Premier jalon : le **backend Go** en Tom+Go, branché après l'optimizer.
   Sa sortie est identique octet pour octet à celle de `GoGenerator`
   (goldens T1 : 77 ports et cas smoke). Le côté Go ne cible que Go
   (D5) : ni backend Java, ni branches Java des phases. Ensuite k recule
   (optimizer, compilateur de motifs, expander, typer, desugarer…). Le **parser vient en dernier** : on reprend
   celui de `prototype/tomgo/stable/tom/parser`, complété. L'orchestration
   Go se construit au fil des étapes, et on termine par le point fixe.
3. **Équivalence.** Identité stricte pour chaque étape qui part de l'AST
   exporté par Java. À l'étape du parser, on vise l'identité ; seules des
   différences de position (ligne, colonne, fichier de l'`OriginTracking`)
   peuvent être acceptées, explicitement et étroitement : comparateur d'AST
   qui ne masque que ces champs, comparaison du code qui n'ignore que les
   retours à la ligne de remplissage, diagnostics comparés par nom. Chaque
   tolérance est un test séparé, avec un compteur visible.
4. Le compilateur Go a **ses propres copies des ADT**
   (`src/go/tom/{gom,engine}/adt/*.gom`, hooks en Go), générées localement,
   avec leur amorce dans `stable/go/tom`. Elles évoluent indépendamment de
   celles de Java.
5. **`src/` ne contient que des sources** ; le code généré est l'amorce, dans
   `stable/`. Les déplacements `src/java` et `stable/java` sont reportés
   jusqu'à ce qu'un compilateur Tom+Go fonctionne.
6. On avance par petites étapes vérifiables.

## 3. Constats (avec preuves)

### 3.1 Ce que Java sait exporter

- **Chaîne des phases.** `src/dist/Tom.config` liste 11 plugins : Starter,
  Parser, Code transformer, Syntax checker, Desugarer, Typer, Type checker,
  Expander, Compiler, Optimizer, Backend.
  - `PluginPlatform.run` (`src/tom/platform/PluginPlatform.t:158-199`)
    passe à chaque plugin le résultat `getArgs()` du précédent, soit
    `[Code, TomStreamManager]` (`TomGenericPlugin.t:111-145`).
  - La table des symboles est **unique**, portée par le `TomStreamManager`
    (`TomGenericPlugin.t:163`). `getSymbolTable(moduleName)` ignore
    `moduleName` (`AbstractGenerator.t:67`, `BackendPlugin.t:235`).
- **`--intermediate`** écrit `ast.toString()` (`Tools.java:38-49`) :
  - `.tfix.parsed`, `.transformed`, `.desugared`, `.typed`, `.expanded`,
    `.compiled` et `.optimized` (ce dernier seulement si `-O`, actif par
    défaut, `OptimizerPlugin.t:88,159`) ;
  - la table, après le parser (`.tfix.parsed.table`, en `toATerm()`, donc
    syntaxe ATerm avec `[…]`, `TomParserPlugin.t:275`) et après le typer
    (`.tfix.typed.table`, en `toString()`, `TyperPlugin.t:211`).

  Rien n'est dumpé après le type checker ni avant le backend avec la table.
- **Format.** C'est le `toString()` de Gom : applications
  `Op(arg,…)`, listes en `concX(…)`, chaînes entre guillemets avec
  échappements Java (`\n`, `\"`, `\t`). Pas de partage, pas d'annotations.
  Les 675 fichiers relevés font **0 ligne** (`cat … | wc -l` → `0`) : un
  terme tient sur une ligne, ce qui permet un paquet ligne par ligne.

  Extrait (`Peano.go.tfix.optimized`) :
  `Tom(concCode(TargetLanguageToCode(TL("package main\n…",TextPosition(1,1),TextPosition(14,29))),DeclarationToCode(TypeTermDecl(Name("term"),…`
- **Contenu de la table** : `Table(concTomEntry(Entry(nom, Symbol(Name,
  TypesToType(domaine, Type(options, sorte, TLType(" aterm.ATerm "))),
  concPairNameDecl(PairNameDecl(slot, GetSlotDecl(… Code(" … ")…))),
  concOption(OriginTracking(…), DeclarationToOption(IsFsymDecl|MakeDecl|
  MakeAddList|…)))))))`. Symboles, types avec TLType, slots, options, hooks
  avec leur code hôte Go et `OriginTracking` y sont.
  - Pour `gom/TestList`, on y lit le code produit par gomgo, par exemple
    `Code(" func() bool { _, ok := ({0}).(*ConcList); return ok }() ")` et
    `Code(" ConsConc({0},{1}) ")` : le point « vérifier que la `.table`
    porte le code hôte Go des mappings générés par gomgo » est réglé.
- **Ce que `toTerm()` omet** (`SymbolTable.t:486-494` : seuls les
  symboles) :
  - `mapTypeName` (types déclarés par `%typeterm`). Le backend Go ne s'en
    sert pas : il appelle `getSymbolFromName`, les accesseurs des types
    builtin, calculés depuis `goCode` (`SymbolTable.t:243-250`), et les
    marques « utilisé ». En revanche, l'expander (`ExpanderPlugin.t:289`,
    `getUsedTypes`) et le typer (`NewKernelTyper.t:427`) en ont besoin :
    l'export devra les inclure quand k reculera avant l'expander.
  - `usedKeyEntry` : marques posées par la passe `Collector` du backend
    (`BackendPlugin.t:270-423`), que le suffixe Go refera, **et** par le
    compilateur pour `concInt` quand un filtrage AC est présent
    (`Compiler.t:609-615`), qu'il faut exporter. `KeyEntry` est une sorte
    Gom (`TomSignature.gom:26`), donc exportable telle quelle.
  - `mapInliner` : rempli par le backend lui-même (`BackendPlugin.t:442-590`).
- **Table au moment du backend.** Après le typer, aucune phase n'ajoute de
  symbole : tous les `putSymbol` sont dans le parser, le transformer, le
  desugarer et le typer (`grep putSymbol`). La table exportée au point de
  passage est donc celle de `.typed.table`, plus les marques ci-dessus. Le
  pont l'exporte néanmoins au point de passage, pour ne pas dépendre de
  cette propriété.
- **Constructions propres à Go présentes dans l'AST optimisé** (dumps des
  cas T1 et smoke) :
  - les `_ = s` des sujets non liés, nœuds
    `Assign(BQVariable(concOption(),Name("_"),EmptyType()),…)` insérés par
    `Compiler.t:265-275` (82 fichiers optimisés) ;
  - `next_minimal_extract` en `ITL` (3 fichiers, comme dans les `.go`) ;
  - les introspecteurs Go (24 fichiers, comme dans les `.go`).

  Les `_ = x` qui suivent les `var` (89 `.go`) sont produits par le
  backend lui-même (`GoGenerator.t:296-304`) : ils feront partie du port.
- **`--intermediate` ne change pas la sortie** : les 83 `.go` produits
  par la dist enveloppée et comparables nom pour nom sont identiques aux
  goldens, et `TestGoBackend` reste vert (100 s).
- **Options lues par le chemin Go du backend** : `goCode`, `cCode`, `jCode`,
  `aCode`, `pCode`, `csCode`, `pretty`, `noDeclaration`, `inline`,
  `inlineplus`, `encoding` et `noOutput`, plus les noms des fichiers
  d'entrée et de sortie (`getStreamManager`). Le paquet doit les porter.

### 3.2 Lecture côté Go

- **Expérience** : un programme Go (`rt`) relit chaque dump avec
  `adt.CodeFromString` ou `adt.TomSymbolTableFromString`, le réimprime par
  `fmt.Sprint(v)` et compare les octets. Résultat sur tous les dumps des
  cas T1 et smoke : `bad=0 total=675`.
  - Ce total couvre 110 `.optimized`, 110 `.compiled`, 110 `.expanded`,
    112 `.typed`, 121 `.parsed` et 112 `.typed.table` (les autres cas sont
    des cas d'erreur, arrêtés avant).
  - Aucun problème d'échappement, de chaîne multiligne ni de liste vide.
    La syntaxe ATerm de `.parsed.table` (`[…]`) est aussi relue.
- **Défaut trouvé (hors chemin du pont)** : `v.ToATerm()` panique sur les
  AST d'avant le compilateur (« ClassCastException: the ATerm of
  MatchConstraint(…) is not an ATermList »). L'opérateur `AndConstraint`
  est AU d'unité `TrueConstraint()` : son hook absorbe l'unité, si bien
  que `AndConstraint(a,b)` est la cellule `ConsAndConstraint(a, b)` dont
  la queue est un **élément**, pas une liste. *Correction après relecture*
  : Java a le même défaut — son `toATerm` généré convertit la queue en
  `ATermList` par un transtypage (`src/gen/tom/engine/adt/tomconstraint/
  types/constraint/AndConstraint.java:126-133`), qui lève la même
  `ClassCastException`. La correction Go (J4.0a) est donc une extension
  voulue, notée dans « Différences connues avec Java » de
  `doc/go/README.md` : pour un opérateur dont la théorie absorbe l'unité
  (`Make<Op>(x)` rend `x`), une queue qui est un élément est écrite comme
  dernier élément de la liste ATerm, et `FromTerm(ToATerm(v)) == v`. Le
  pont n'utilise pas `ToATerm`.
- **Écrire du Tom+Go sur l'ADT du moteur fonctionne.** Expérience : un `.t`
  qui inclut `adt.tom`, fait un `%match` de liste sur `Code` et une
  `%strategy` `TopDown` qui visite les `TL` lit un dump Java ; `tom -go`
  puis `go build` réussissent et il s'exécute (13 ms). Trois écueils :
  1. **Bug de gomgo** : `adt.tom` écrit `get_slot(String,t) {
     ($t).(*NameTomName).String }`, alors que le champ s'appelle
     `String_`. Quatre occurrences (`adt.tom:3111,3309,3330,4016`) ;
     contourné dans l'expérience. *Précision* : le renommage des champs
     réservés (`reservedFieldName`, `gom/backend/backend.go:1090`) est
     appliqué à tous les modules, pas seulement avec `--gohooks`, alors
     que `EmitTomMappingGo` (`backend.go:557`) écrit `exportedField(slot)`
     sans ce renommage : un `%gom` utilisateur dont un champ s'appelle
     `String` ou `Hash` a le même défaut.
  2. `adt.tom` emploie des noms non qualifiés (`Code`, `MakeName`) : le
     code Tom+Go doit faire `import . "tom/engine/adt"`. Il inclut aussi
     `aterm.tom`, ce qui exige `import "tom/library/aterm"`.
  3. Le paquetage `tom/engine/adt` n'est pas dans la dist
     (`lib/go/tom` ne contient que le runtime) : le module du suffixe pointe
     sur `src/go/tom` (ou sur l'amorce `stable/go/tom`).
- **Paramètre de stratégie de type pointeur** (`n:*int`) : non reconnu par
  le parser îlot (le `%strategy` reste du texte hôte). C'est une limite
  connue des paramètres non identifiants ; contournée.

### 3.3 Taille du premier jalon

Mesures (`wc -l`, comptes de `%match`, hiérarchie) :

| Élément | Lignes Tom+Java | Remarque |
|---|---|---|
| `GoGenerator.t` | 887 | `extends GenericGenerator` (`:58`), environ 60 méthodes redéfinies |
| `GenericGenerator.t` | 1 007 | presque tout sur le chemin Go |
| `AbstractGenerator.t` | 1 083 | environ 880 lignes de parcours (`generate…`) |
| `BackendPlugin.t` | 598 | pilote (environ 120 lignes) + `%strategy Collector` (environ 300) |
| `OutputCode.java` | 341 | indentation, options |
| `SymbolTable.t` (sous-ensemble) | environ 530 sur 687 | recherche, marques, types builtin, inliners, `fromTerm` |
| `TomBase.t` (sous-ensemble) | environ 280 | `getTLType`, `getTomType`, `getSymbolCodomain`, `getSlotName`… |
| **Total chemin Go** | **environ 4 300 à 4 600** | environ 5 000 à 5 800 lignes de Tom+Go, colle comprise |
| `JavaGenerator.t` + `CFamilyGenerator.t` | 652 + 537 | **non porté** (D5 : le côté Go ne cible que Go) |

Pour situer les étapes suivantes : optimizer 828 lignes, compiler 3 948
(16 fichiers), expander 750, typer 3 748, checker 2 124, desugarer 345,
transformer 1 015, parser 7 741 (hors code ANTLR généré), tools 3 790.

Ce qui existe déjà côté Go :

- l'ADT du moteur avec `FromString`/`String()`/introspection (`sl`) ;
- `tom/library/sl` et les mappings Go `sl/basic.tom` et `composed.tom`
  (`TopDown`, `BottomUp`, `TopDownCollect`, `Innermost`, `InnermostId`…) ;
- les mappings `int`, `string`, `boolean`, `aterm`, `util/ArrayList.tom` ;
- `tom/library/aterm`.

Ce qui manque :

- des mappings ou petites aides pour `HashMap`, `HashSet`, `TreeMap`,
  `Stack`, `StringBuilder` et `Writer` ;
- un équivalent de `OptionManager`, `TomMessage` et `TomStreamManager` ;
- `sl/builtin.tom` (`BuiltinRepeatId`, `BuiltinBottomUp`, pour
  l'optimizer) ;
- une convention d'erreur à la place des exceptions (§5.4).

Aucun `.t` n'existe encore sous `src/go`.

### 3.4 Plusieurs `.t` dans un paquetage Go

- **Expérience** : deux `.t` (`A.t`, `B.t`, `package main`, chacun avec
  `%include { int.tom }` et `{ string.tom }`) sont compilés par
  `tom -go -np` dans le même répertoire, puis `go build` échoue :
  `B.go:7:6: tom_equal_term_int redeclared in this block`. Même erreur pour
  `tom_is_sort_int`, `_char`, `_String`, `tom_stringHead` et
  `tom_stringTail`.
- **Origine des collisions** :
  - **(i)** les déclarations générées par le backend pour les symboles et
    types utilisés (`tom_make_*`, `tom_is_fun_sym_*`, `tom_get_slot_*`,
    `tom_equal_term_*`, `tom_is_sort_*`…). En Java, ce sont des méthodes
    `private static` de la classe ; en Go, des fonctions du paquetage ;
  - **(ii)** les fonctions écrites en dur dans les mappings Go : neuf
    fonctions dans six fichiers (`string.tom:25,27` ; `intarray.tom:23,25` ;
    `util/ArrayList.tom:18` ; `util/LinkedList.tom:19,21` ;
    `util/types/ArrayList.tom:14` ; `util/types/LinkedList.tom:14`) ;
  - **(iii)** les aides du filtrage AC émises par le compilateur en `ITL`
    (`next_minimal_extract`, `__getMultiplicities_<op>`…).
- **Contre-épreuve** : renommer `tom_` en `tomB_` dans `B.go` suffit : le
  paquetage se construit et s'exécute (`zero 3 A`).
- **Noms écrits par l'utilisateur.** Les types des `%strategy` (nommés
  par l'utilisateur) et `LocalIntrospector` (`-gi`, que le code hôte
  instancie : `&LocalIntrospector{}` dans
  `test/go/sl/TestHandWrittenMapping.t`) sont aussi des déclarations de
  niveau paquetage, mais le code de l'utilisateur les nomme : ils ne sont
  pas préfixés (comme en Java, ce sont des noms de classes visibles).
  Deux `.t` d'un paquetage ne peuvent donc pas déclarer la même
  stratégie, ni utiliser tous deux `-gi`.
- **L'option existante `-D` (`noDeclaration`) ne résout rien** : avec
  `-go`, elle n'est que partiellement honorée (`GoGenerator.t:662`), et les
  `tom_equal_term_*` et `tom_is_sort_*` restent déclarés.

## 4. Design

### 4.1 Le pont Java → Go

**Côté Java.**

- Deux options de plateforme sont déclarées dans `src/config/Tom.config`
  (liste `concPlatformOption`, à côté de `intermediate`) :
  - `goFrom` (`StringValue("")`, argument `phase`) : nom d'un plugin de
    `Tom.config` avant lequel la compilation passe au suffixe Go. Le nom
    est comparé sans tenir compte de la casse ni des espaces (`Backend`,
    `Optimizer`, `TypeChecker` pour « Type checker »…). Un nom inconnu
    est une erreur de la plateforme.
  - `goCheck` (`BooleanValue(False())`) : avec `goFrom`, le pont vérifie
    l'aller-retour du paquet (`tomgo suffix --echo`), puis **la chaîne
    Java continue** comme sans `goFrom` (sortie inchangée). C'est le mode
    de P0 (§5.1).
  Les options des plugins (`-go`, `-O`, `--genIntrospector`…) restent
  déclarées : tous les plugins restent dans la chaîne.
- **Crochet générique (D3)** dans `PluginPlatform.run`
  (`src/tom/platform/PluginPlatform.t:158-199`), environ 10 lignes :
  - `ConfigurationManager` garde, à côté de la liste des plugins, la
    liste de leurs noms (`extractClassPaths` les lit déjà,
    `ConfigurationManager.t:165`) ; `PluginPlatformFactory` les passe à
    `PluginPlatform` avec la valeur de `goFrom`, lue seulement si
    l'option est déclarée (Gom utilise la même plateforme avec
    `Gom.config`, qui ne la déclare pas : `getOptionValue` d'une option
    inconnue lève une erreur, `TomOptionManager.t:191`).
  - Avant d'exécuter le plugin nommé par `goFrom`, la plateforme
    instancie par réflexion `tom.engine.bridge.GoBridgePlugin` (la
    plateforme ne dépend pas de `tom.engine` à la compilation), lui donne
    le gestionnaire d'options et les arguments courants
    (`[Code, TomStreamManager]`), et l'exécute. Sans `goCheck`, elle sort
    ensuite de la boucle **avec succès** (le résultat est celui du
    pont) ; avec `goCheck`, elle continue avec le plugin Java.
  - `goFrom` vide (défaut) : le code de la boucle est inchangé ; la sortie
    est identique octet pour octet (T0, T1).
- `GoBridgePlugin` (`src/tom/engine/bridge/GoBridgePlugin.java`,
  `extends TomGenericPlugin`) refuse de fonctionner sans `-go` (D5 : le
  suffixe Go ne produit que du Go).

**`GoBridgePlugin.run`.**

1. Écrit le **paquet** (§4.2) sur l'entrée standard d'un processus
   `tomgo suffix` (`tomgo suffix --echo` avec `goCheck`), en UTF-8, depuis
   un fil séparé ; la sortie d'erreur est lue par un autre fil.
2. **Binaire (D2)** : `$TOMGO_BIN` s'il est défini (surcharge), sinon
   `<tom.home>/bin/tomgo` (`-Dtom.home` est posé par `bin/tom`), installé
   dans la dist par la construction (§4.3). Introuvable → erreur
   `TomMessage` explicite qui donne le chemin cherché. Pas de recherche
   dans le `PATH` : le prototype gelé a aussi un `tomgo`
   (`prototype/tomgo/cmd/tomgo`). gomgo est trouvé de la même façon par
   `TomParserTool.parseGomFileGo` (`$GOMGO_BIN`, sinon
   `<tom.home>/bin/gomgo`).
3. Sans `goCheck` : lit la sortie standard (le code généré) et l'écrit
   dans le fichier de sortie habituel
   (`getStreamManager().getOutputFile()`, avec l'option `encoding`),
   comme `BackendPlugin` ; `getArgs()` rend `[nom du fichier généré]`.
   Avec `goCheck` : compare la sortie au paquet envoyé, octet pour octet ;
   une différence est une erreur qui nomme la première ligne différente.
4. Relaie chaque ligne `ERROR <nom> <fichier> <ligne> <message>` de la
   sortie d'erreur en `TomMessage.error`, chaque ligne `WARNING …` en
   avertissement ; un code de sortie non nul est une erreur (avec le
   texte de la sortie d'erreur).
5. Avec `--intermediate`, écrit aussi le paquet dans
   `<sortie>.tfix.bridge`.

Ainsi `tom -go [--goFrom Backend]` reste la seule commande des harnais. La
bascule se fait par une option, que T1, T2 et smoke ajoutent par des
variables d'environnement lues par `harness.Dist.Compile`
(`test/tools/harness/harness.go:215`) : `TOMGO_GOFROM=<phase>`,
`TOMGO_GOCHECK=1` (ajoute `--goFrom Backend --goCheck`) et
`TOMGO_DUMPS=<répertoire>` (ajoute `--intermediate` et recueille les
`*.tfix*`). Les goldens ne changent pas.

**Côté Go.** La commande `tomgo` (D2), source
`src/go/tom/engine/cmd/tomgo/`, lit et écrit le paquet par le paquetage
`tom/engine/bridge` (`src/go/tom/engine/bridge/`). `tomgo suffix` lit le
paquet sur l'entrée standard, reconstruit `Code`, la table, les marques
et les types, exécute les phases à partir de celle de la ligne `from`,
puis écrit le code sur la sortie standard. Il refuse une phase qu'il ne
connaît pas encore (code de sortie 2, message explicite) : c'est le cas
de toutes en J4.0. `tomgo suffix --echo` réécrit le paquet relu.

### 4.2 Le paquet (format d'échange, D6)

Le format est texte, UTF-8, une donnée par ligne : chaque terme Gom tient
sur une ligne (§3.1). L'ordre des lignes est fixe.

```
tom-bridge 1
from Backend                        (nom du plugin, sans espaces)
input /chemin/absolu/X.t
output /chemin/absolu/X.go
option destdir string "out"         (toutes les options connues, triées par nom :
option goCode bool true              option <nom> bool|int|string <valeur>)
option verbose bool false
…
used UsedSymbolConstructor(Symbol(…))   (une ligne par KeyEntry, triées par toString)
type Type(…)                        (une ligne par entrée de mapTypeName, dans l'ordre de ses clés)
table Table(concTomEntry(…))
code Tom(concCode(…))
end
```

- Les termes sont écrits par `toString()` (format éprouvé ci-dessus) et
  relus par `XFromString` (`CodeFromString`, `TomSymbolTableFromString`,
  `KeyEntryFromString`, `TomTypeFromString`) ; il n'y a pas de lecteur de
  termes à écrire côté Go.
- Valeur d'une option `string` : littéral entre guillemets, échappements
  `\\`, `\"`, `\n`, `\r`, `\t`, et `\u00XX` pour les autres caractères
  de contrôle, relu par `strconv.Unquote`.
- Les options sont celles de `TomOptionManager` (plateforme et plugins),
  toutes, pour que les étapes suivantes n'aient pas à toucher au pont.
  Les types (`type`) sont exportés dès J4.0, même si le backend ne les
  lit pas.
- La ligne `end` détecte un paquet tronqué. Une ligne de terme qui
  contiendrait un saut de ligne est une erreur du pont (jamais observée :
  0 sur 675 dumps).
- Le paquet n'est pas une sorte Gom, afin que les ADT Java et Go restent
  indépendants (décision 4). Il est versionné par sa première ligne.

### 4.3 Sources, construction et amorce du Tom+Go

**Où vivent les sources.** `src/go/tom/engine/<phase>/*.t` reproduit
`src/tom/engine/<phase>/` : `backend`, `tools` (SymbolTable, TomBase,
OutputCode), `platform` (options, messages), puis `optimizer`, `compiler`…
La commande est dans `src/go/tom/engine/cmd/tomgo/`. Du Go pur (sans
construction Tom) peut voisiner en `.go` : ce sont des sources.

**Binaires installés dans la dist (D2).** « `src/` contient des
sources, pas des binaires » : `gomgo` et `tomgo` sont construits par la
construction de la dist et installés dans `<dist>/bin/gomgo` et
`<dist>/bin/tomgo`.

- `build.xml` (`src.dist` et `stable.dist`) appelle une cible mage
  `installGo <src|stable> <dist>` : pour `src`, elle lance `bootstrap`
  (les ADT générés sont ignorés par git) puis `go build` dans
  `src/go/tom` ; pour `stable`, `GOWORK=off go build` dans l'amorce
  `stable/go/tom`. Elle installe chaque commande présente
  (`gom/cmd/gomgo`, `engine/cmd/tomgo`) et dit lesquelles. Go et mage
  deviennent des prérequis de `./build.sh src.dist` et `stable` ; la
  construction échoue clairement s'ils manquent.
- Le côté Java trouve les binaires depuis `tom.home` (§4.1) ;
  `TOMGO_BIN` et `GOMGO_BIN` ne sont plus que des surcharges.
- Les harnais (`test/tools/harness`) utilisent les binaires de la dist
  testée (`Dist.Bin`) au lieu de construire gomgo ; un binaire absent
  fait échouer le test avec un message qui dit de reconstruire la dist.
  `CheckFresh` vérifie aussi que les binaires ne sont pas plus anciens
  que les sources Go (`src/go/tom/**/*.{go,gom}` hors tests) ni que
  `src/config/*.config`.
- Le compilateur Java de `stable/` lit encore `GOMGO_BIN` ou le `PATH`
  jusqu'à la prochaine promotion de `src/` dans `stable/` ; les harnais
  posent `GOMGO_BIN` vers le binaire de la dist, ce qui marche dans les
  deux cas.

**Compilation du Tom+Go (D4).**

- Une nouvelle cible `mage regenTomGo` (J4.1a, avec le premier `.t`)
  compile chaque `.t` de `src/go/tom/engine/**` avec la dist
  **`src/dist`** (`tom -go -np -I src/go/tom/engine/adt`), vérifiée
  fraîche par `CheckFresh`, avec le gomgo de la dist pour les blocs
  `%gom`. Le `.go` produit est écrit à côté du `.t`, ignoré par git, comme
  les ADT.
- `mage promoteGo` (existant) copie ces `.go` dans `stable/go/tom/…`, où
  ils sont commités comme amorce.
- `mage bootstrap` continue de tout construire depuis l'amorce **sans
  Java**. Seule la régénération demande une dist Java, jusqu'au point fixe.

**Inclusion de l'ADT.** Le code Tom+Go fait `import . "tom/engine/adt"` et
`import "tom/library/aterm"`, et inclut `adt.tom` (point « le mapping de
l'ADT du moteur est à inclure » de `doc/go/README.md`). Avant d'écrire du
Tom+Go, J4.0a corrige dans gomgo :

- le `get_slot` des champs renommés (§3.2, écueil 1) ;
- `ToATerm` des listes AU qui finissent par un élément (§3.2).

**Plusieurs `.t` par paquetage (D1 b, J4.0b).** Le préfixe est
`tom_<F>_`, où `<F>` est le nom de base du fichier d'entrée sans
extension, chaque caractère hors `[A-Za-z0-9_]` remplacé par `_`
(`Peano.t` → `tom_Peano_`). Un nom généré `tom_x` devient `tom_<F>_x`
(`tom_make_suc` → `tom_Peano_make_suc`) ; un autre nom généré de niveau
paquetage `n` devient `tom_<F>_n` (`next_minimal_extract`,
`__getMultiplicities_<op>`…). Les variables locales (`tom_a`, `tom__strat`,
`tom__arg`…) et les noms écrits par l'utilisateur (§3.4) ne changent
pas. Les fonctions des mappings deviennent des fermetures dans le corps
des hooks. Seule la cible Go change : T0 le garantit pour Java.

### 4.4 Structure du code porté

- **Héritage.** Une seule cible (Go, D5) : la hiérarchie
  `AbstractGenerator → GenericGenerator → GoGenerator` est aplatie en un
  type `goGenerator`. Pour chaque méthode, on porte la version la plus
  dérivée sur le chemin Go (celle de `GoGenerator` si elle la redéfinit,
  sinon celle de `GenericGenerator`, sinon celle d'`AbstractGenerator`).
  Chaque méthode Java devient une méthode Go de même nom, dans le même
  ordre, avec un commentaire `// Java: Fichier.t:ligne`, ce qui facilite
  la relecture côte à côte.
- **Branches Java.** Les branches propres aux autres cibles (`jCode`,
  `cCode`…) des phases portées sont omises côté Go ; elles restent
  intactes dans les sources Java, que T0 continue de valider.
- **Exceptions.**
  - `TomRuntimeException` et les erreurs d'E/S deviennent des `panic`
    typées, récupérées une seule fois par le pilote, qui écrit `ERROR …`
    et sort avec le code 1.
  - Les exceptions qui servent de flot de contrôle (`VisitFailure`,
    `EmptyStackException`, le `RuntimeException` rattrapé de
    `GoGenerator.termType`) deviennent des retours explicites, au cas par
    cas.
- **Collections.** `map`/slice Go, plus une petite aide `javaHashOrder`
  partout où la sortie dépend de l'ordre d'itération d'un
  `HashSet<String>`/`HashMap` Java. Le backend n'itère sur aucune
  (`classFields`, `isFsymMap` : appartenance seulement). C'est en revanche
  le cas du compilateur (`Compiler.t:608-618`, `for(String op:bag)`) et de
  53 boucles `for(… : …)` du middle-end, à examiner une par une.

### 4.5 Orchestration Go

Elle émerge par étapes :

- J4.1 : un pilote réduit au backend ;
- à chaque recul de k, le pilote enchaîne une phase de plus, avec la même
  interface `Plugin { Name(); Run(State) }` que `prototype/tomgo/stable/platform`
  (réservoir de conception) ;
- au parser, le pilote Go lit le `.t` lui-même, délègue `%gom` à gomgo en
  processus (bibliothèque, plus besoin d'exécutable) et n'a plus besoin de
  Java.

## 5. Stratégie de test

### 5.1 Niveaux

| Niveau | Quoi | Oracle | Règle |
|---|---|---|---|
| **P0 pont** | relecture du paquet (code, table, marques, types) par `tomgo suffix --echo` (`--goCheck`), et des dumps `--intermediate` de toutes les phases par l'ADT Go (`String()` et `FromTerm(ToATerm(v))`) | le paquet ou le dump lui-même | identité stricte ; corpus : T1 + smoke (121 entrées) |
| **P1 phase** | une phase portée P, en isolation : `Go_P(dump avant P)` | dump Java après P (`--intermediate`, plus table et marques) | identité stricte du `toString()` |
| **P2 bout en bout** | `tom -go --goFrom X` sur les cas T1 et smoke | goldens T1 existants | identité stricte ; T2 (comportement) inchangé par construction |
| **P3 auto-application** | le compilateur hybride compile les sources Tom+Go du suffixe | sortie de `tom -go` 100 % Java sur les mêmes sources | identité stricte (« self-T1 ») |
| **Parser** | AST Go comparé au `.tfix.parsed` Java | voir §2.3 | identité visée ; tolérances de position séparées, comptées |

Remarques :

- P1 est presque gratuit : `--intermediate` donne déjà les paires
  (`.compiled` → `.optimized` pour l'optimizer, `.expanded` → `.compiled`
  pour le compilateur, `.typed` → `.expanded` pour l'expander…). Il
  manque la table aux frontières autres que le typer, d'où le dump du
  paquet par `--intermediate` (`.tfix.bridge`, §4.1).
- **T0 ne valide que le compilateur Java** (sortie `--java` identique à
  `legacy/`). Il garde son rôle à chaque jalon : les changements Java du
  pont et du préfixe (J4.0b, J4.0c) doivent laisser la sortie Java
  inchangée, et les branches Java du middle-end restent dans les sources
  Java, intactes. Le côté Go ne cible que Go (D5) : il est validé par les
  goldens T1, le comportement T2 (oracle `legacy/`) et les cas smoke ; les
  jumeaux Java des cas smoke (`TestJavaTwins`) restent l'oracle du
  comportement du code généré.

### 5.2 Application mécanique de la règle d'équivalence

- Une seule fonction de comparaison par niveau, dans
  `test/tools/j4/` : `bytes.Equal` pour P0–P3. Elle n'a pas de paramètre
  de normalisation.
- Les tolérances du parser sont des fonctions distinctes
  (`maskPositions`, `ignorePaddingNewlines`, `diagByName`), chacune
  utilisée par un test distinct qui imprime son compteur (`identiques
  N / tolérés M / différents K`). Elles n'ont **aucun** usage avant
  l'étape du parser : un test le vérifie (`grep` des appels).
- Tableau de bord `doc/go/j4-dashboard.md` : pour chaque phase portée,
  identiques et différents par niveau. Cible `mage j4`.
- Tant qu'une phase n'est pas entièrement identique, `--goFrom` vers elle
  n'est pas utilisé par défaut ; les cas atteints sont listés au tableau
  de bord.

### 5.3 Budget de performance

- Mesures actuelles :
  - `tom -go -np` sur `Peano.t` : 0,6 s de bout en bout, dont environ
    0,2 s de parsing ;
  - `TestGoBackend` (T1 complet, avec `go build`/`go test`) : 100 s ;
  - relecture Go du plus gros AST optimisé (219 Ko) : 9 ms.
- Coût attendu du pont : un `fork/exec` plus environ 10 à 20 ms par
  fichier.
- **Budget proposé** : `TestGoBackend` avec `--goFrom Backend` ne dépasse
  pas +15 % de la durée tout-Java. La mesure est faite à J4.1 et reportée
  au tableau de bord.

### 5.4 Ce qui n'est pas vérifiable aujourd'hui

- Messages d'erreur du backend : aucun cas T1 n'en produit. On les teste
  par des cas construits.
- Ordre d'itération des collections Java dans les phases suivantes : il
  ne se vérifie que phase par phase, par P1.

## 6. Jalons

Chaque jalon se termine par un commit vert (`mage all`, T1, T2, smoke, T0
inchangés, sauf changement voulu et vérifié des goldens en J4.0b). Les
jalons J4.0a–J4.0d forment un seul plan
(`docs/superpowers/plans/2026-10-24-j4-0-bridge.md`) ; chacun des suivants
aura le sien. La colonne « Décisions » renvoie à la §8 : toutes sont
prises, plus rien n'attend l'utilisateur.

| Jalon | Contenu | Dépend de | Décisions | Critère de sortie |
|---|---|---|---|---|
| **J4.0a — binaires dans la dist, bugs gomgo** | cible `mage installGo` appelée par `build.xml` (`src.dist`, `stable.dist`) : `<dist>/bin/gomgo` ; `TomParserTool` trouve gomgo depuis `tom.home` ; harnais sur les binaires de la dist (`Dist.Bin`, `CheckFresh` étendu) ; `get_slot` des champs renommés ; `ToATerm` des listes AU ; programme Tom+Go d'essai sur l'ADT du moteur (§3.2) | — | D2 | les deux dists contiennent `bin/gomgo` ; harnais sans `go build` de gomgo ; le `.t` d'essai se compile et s'exécute sans contournement ; tests unitaires des deux corrections ; T0, T1, T2, smoke inchangés |
| **J4.0b — préfixe par fichier** | `tom_<F>_` dans `AbstractGenerator`/`GenericGenerator`/`GoGenerator`, noms des aides AC du compilateur, fermetures dans les mappings Go ; goldens T1 régénérés | J4.0a | D1 | deux `.t` (avec mappings `int`/`string` et filtrage AC) d'un paquetage se construisent et s'exécutent ; diff des goldens = renommages seuls (programme de vérification) ; T0, T2, smoke, jumeaux Java inchangés |
| **J4.0c — pont** | options `goFrom`/`goCheck`, crochet de `PluginPlatform`, `GoBridgePlugin` et écriture du paquet ; paquetage Go `tom/engine/bridge` (lecture/écriture), commande `tomgo suffix` (`--echo`, phases refusées) ; `tomgo` installé dans la dist | J4.0a | D2, D3, D6 | sans `goFrom`, sortie identique (T0, T1) ; `--goFrom Backend` échoue proprement (code 2, message) ; `--goFrom` sans `-go` refusé ; `TOMGO_BIN` absent et binaire absent : erreur claire |
| **J4.0d — identité (P0)** | harnais : `TOMGO_GOCHECK`, `TOMGO_DUMPS` ; cible `mage bridgeCheck` ; test des dumps (`String()` et `ToATerm`) | J4.0b, J4.0c | D6 | `--goCheck` vert sur T1 + smoke (121 entrées, goldens inchangés) ; tous les dumps relus à l'identique (675 au relevé de §3.2, plus les `.tfix.bridge`), compteurs imprimés |
| **J4.1a — fondations Go** | `OutputCode`, sous-ensembles `SymbolTable`/`TomBase`, options et messages, en Tom+Go, avec tests unitaires ; `mage regenTomGo` | J4.0 | D4 | tests unitaires verts ; compilés par `regenTomGo` avec `src/dist` |
| **J4.1b — backend Go** | `Collector`, parcours `generate…`, `GenericGenerator`, `GoGenerator` (aplatis, §4.4) ; portage par familles d'instructions, en suivant le compteur P2 | J4.1a | D5 | P2 : 100 % des goldens T1 + smoke identiques avec `--goFrom Backend` ; budget §5.3 tenu |
| **J4.1c — amorce** | `promoteGo` du backend ; P3 sur ses propres sources | J4.1b | D4 | self-T1 identique ; `mage bootstrap` sans Java vert |
| **J4.2 — optimizer** | `OptimizerPlugin` + `sl/builtin.tom` Go | J4.1c | D5 | P1 `.compiled` → `.optimized` identique ; P2 avec `--goFrom Optimizer` |
| **J4.3 — compilateur de motifs** | `compiler/*` (16 fichiers), dont `collectHostSubjects` et les aides AC Go (préfixées) | J4.2 | D1, D5 | P1 `.expanded` → `.compiled` ; P2 |
| **J4.4 — expander** | `ExpanderPlugin`, dont l'introspecteur Go (les types sont déjà dans le paquet) | J4.3 | D5 | P1 `.typed` → `.expanded` ; P2 |
| **J4.5 — type checker, typer** | diagnostics en mode Regress ; `NewKernelTyper` | J4.4 | | P1 ; cas d'erreur T2 identiques par nom |
| **J4.6 — desugarer, syntax checker, transformer** | | J4.5 | | P1 ; P2 avec `--goFrom Transformer` |
| **J4.7 — parser** | parser du prototype reciblé sur `tom/engine/adt`, complété ; règle des accolades `ITL` | J4.6 | | AST identique, ou tolérances de position comptées (§5.2) ; P2 |
| **J4.8 — orchestration** | starter, `%include`, gomgo en bibliothèque, CLI `tomgo` autonome | J4.7 | D2 | T1/T2/smoke verts avec `tomgo` seul, sans Java |
| **J4.9 — point fixe** | stage1 (`.go` produit par Java `-go`) compile les sources : stage2 ; stage2 recompile : stage3 | J4.8 | | sortie de stage2 identique à celle de stage1 (et stage3 identique à stage2) |

J4.0b et J4.0c sont indépendants (fichiers disjoints, hors
documentation) ; J4.0d a besoin des deux.

## 7. Risques

| Risque | Atténuation |
|---|---|
| Volume du backend Go : `GoGenerator` (887 lignes) plus les parties partagées sur le chemin Go de `GenericGenerator` (1 007), d'`AbstractGenerator` (environ 880 de parcours), d'`OutputCode`, de `SymbolTable`/`TomBase` et du `Collector`, soit 4 300 à 4 600 lignes de Tom+Java (pas de backend Java : D5) | J4.1 découpé en a/b/c ; compteur P2 par cas ; portage méthode par méthode avec renvoi `// Java:` |
| Chaîne d'héritage à trois niveaux (`AbstractGenerator → GenericGenerator → GoGenerator`, environ 60 méthodes redéfinies) mal aplatie : méthode de la classe de base portée alors que `GoGenerator` la redéfinit, ou appel virtuel perdu | aplatissement en un seul type, version la plus dérivée par méthode (§4.4) ; liste des méthodes redéfinies relevée au début de J4.1b et cochée ; P2 par famille d'instructions |
| Exceptions comme flot de contrôle, environ 150 méthodes qui lèvent `IOException` | `panic` limité aux erreurs, récupéré une fois par le pilote ; retours explicites ailleurs (§4.4) |
| Sortie qui dépend de l'ordre d'itération d'un `HashSet`/`HashMap` Java (middle-end) | aide `javaHashOrder` ; repéré par P1, phase par phase ; 53 boucles à examiner |
| Préfixe par fichier (J4.0b) : un nom oublié (déclaration ou appel) casse la construction ; un nom de trop casse le code utilisateur | deux `.t` d'un paquetage en test ; programme « renommages seuls » sur les 83 goldens `.go` ; T0 pour Java ; noms écrits par l'utilisateur exclus (§3.4) |
| Fermetures dans les hooks des mappings : le parser îlot laisse parfois `$var` tel quel dans un corps de hook avec accolades | `$var` placé hors des accolades (`func(…){…}($s)`) ; chaque mapping réécrit exercé par T1/T2 ou un cas smoke |
| Binaires de la dist périmés par rapport à `src/go/tom` | `CheckFresh` compare leurs dates aux sources Go ; la construction de la dist reconstruit toujours les binaires |
| La construction de la dist exige maintenant Go et mage | prérequis documentés (`CLAUDE.md`, `doc/go/README.md`) ; échec explicite de `installGo` |
| Limites du parser îlot sur le Tom+Go du compilateur (paramètres de stratégie de type non identifiant, règles étiquetées, `$var` dans des hooks avec accolades) | contournements locaux notés `// port:` ; liste tenue au tableau de bord |
| Fabrique `sharedobjects` sans références faibles, mutex global : mémoire et temps sur de gros fichiers | mesure à J4.1 (budget §5.3) ; paquet Go `weak` si dépassé |
| La table exportée devient incomplète quand k recule (état des plugins) | le paquet porte déjà types, marques et toutes les options ; chaque jalon vérifie par `grep` les champs d'état lus par la phase portée |
| Les sorties d'erreur passent par un autre processus | protocole `ERROR …` sur stderr, comparé par nom (Regress) |
| La régénération dépend d'une dist Java jusqu'au point fixe | `bootstrap` depuis l'amorce sans Java ; seule `regenTomGo` exige Java |
| Chemins absolus dans `OriginTracking` (fichier d'entrée temporaire des harnais) | sans effet tant que les deux côtés lisent le même paquet ; à traiter au parser (tolérance « fichier ») |

## 8. Décisions prises

Décisions de l'utilisateur du 2026-10-24. Chaque entrée donne le choix et
la raison qui compte.

- **D1 — plusieurs `.t` dans un paquetage Go : (b), préfixe par
  fichier.** Toutes les déclarations de niveau paquetage générées
  reçoivent le préfixe `tom_<F>_` (§4.3), y compris les aides AC du
  compilateur ; les fonctions des mappings Go deviennent des fermetures.
  Le préfixe est appliqué d'abord au `GoGenerator` Java (J4.0b), sans
  condition (pas d'option). Raisons : l'expérience montre que le
  renommage suffit ; (a) obligerait à fondre `AbstractGenerator`,
  `GenericGenerator` et `GoGenerator` en un seul `.t` ; (c) (`-D` complet
  en Go) change la forme du code produit. Coût accepté : les goldens T1
  changent une fois, par un diff vérifié « renommages seuls ».
- **D2 — commande `tomgo`, binaires installés dans la dist.** Sources dans
  `src/go/tom/engine/cmd/tomgo` (sous-commande `suffix`) ; `tomgo` deviendra
  le compilateur complet (J4.8). Principe de l'utilisateur : « `src` sont
  des sources, pas des binaires ». `gomgo` et `tomgo` sont donc construits
  par la construction de la dist et installés dans `<dist>/bin` ; le côté
  Java les trouve depuis `tom.home` ; `TOMGO_BIN` et `GOMGO_BIN` ne restent
  que comme surcharges ; les harnais utilisent les binaires installés de
  la dist testée et échouent clairement s'ils manquent.
- **D3 — crochet générique `--goFrom <plugin>` dans `PluginPlatform`,
  dès J4.0c** (environ 10 lignes). Il sert tel quel à chaque recul de k,
  et les options de tous les plugins restent déclarées. Le mode
  `--goCheck` en est une extension de quelques lignes, nécessaire à P0.
- **D4 — `src/dist` compile le Tom+Go**, vérifiée fraîche par
  `CheckFresh`. Le `GoGenerator` de référence évolue encore (J4.0b), et
  c'est sa sortie que le suffixe doit reproduire.
- **D5 — (a) : le côté Go ne cible que Go.** Ni backend Java en Go, ni
  branches Java (`jCode`…) dans les ports du middle-end. Raison de
  l'utilisateur : sinon il faudrait aussi étendre gomgo (Gom écrit en Go)
  pour qu'il génère du Java. Conséquences : pas de jalon « backend Java
  en Go » ; T0 continue de valider seulement le compilateur Java ; le
  côté Go est validé par T1, T2 et smoke (§5.1). (L'utilisateur avait
  d'abord choisi (b), puis est revenu à (a) le même jour.)
- **D6 — paquet texte, un terme par ligne, sur stdin/stdout** (§4.2). Rien
  à écrire côté Go pour lire les termes, pas de fichier temporaire, et
  les ADT Java et Go restent indépendants.
