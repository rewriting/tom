# J4 — compilateur Tom écrit en Tom+Go, de la fin vers le début — design

Date : 2026-10-24
Branche : `tom-go`
Statut : proposition, en attente des décisions de la §8 et de la relecture

Références : `docs/superpowers/specs/2026-10-22-tom-go-backend-design.md`
(J−1…J3), `docs/superpowers/specs/2026-10-23-repo-layout-design.md`,
`doc/go/README.md` (« Orientation pour la suite », « Points à régler au
début de J4 »), `.superpowers/sdd/progress.md` (décisions de
l'utilisateur). Les expériences citées ont été faites dans un répertoire
de travail hors dépôt (`…/scratchpad/j4/`) ; le dépôt n'a pas été modifié
(sauf ce fichier). Détail : `.superpowers/sdd/j4-spec-report.md`.

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
  environ 5 000 à 5 800 lignes de Tom+Go. Le backend Java en ajouterait
  1 100 à 1 300.
- **Préalable bloquant** : un paquetage Go ne peut aujourd'hui contenir
  qu'**un seul** `.t` (expérience : deux `.t` d'un même paquetage
  redéclarent `tom_equal_term_int`, `tom_is_sort_int`, `tom_stringHead`…).
  Le backend s'étend sur au moins trois `.t` d'un même paquetage : il faut
  trancher (§8, D1) avant d'écrire du Tom+Go.
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
   (goldens T1 : 77 ports et cas smoke). En option : le backend Java
   (comparé par T0). Ensuite k recule (optimizer, compilateur de motifs,
   expander, typer, desugarer…). Le **parser vient en dernier** : on reprend
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
  MatchConstraint(…) is not an ATermList ») : l'opérateur AU
  `AndConstraint`, dont le hook aplatit et absorbe l'unité, n'est pas
  converti en liste. Le pont n'utilise pas `ToATerm` ; à corriger dans
  gomgo avant que du code Tom+Go ne s'en serve.
- **Écrire du Tom+Go sur l'ADT du moteur fonctionne.** Expérience : un `.t`
  qui inclut `adt.tom`, fait un `%match` de liste sur `Code` et une
  `%strategy` `TopDown` qui visite les `TL` lit un dump Java ; `tom -go`
  puis `go build` réussissent et il s'exécute (13 ms). Trois écueils :
  1. **Bug de gomgo** : `adt.tom` écrit `get_slot(String,t) {
     ($t).(*NameTomName).String }`, alors que le champ s'appelle
     `String_` (renommage de `--gohooks`). Quatre occurrences
     (`adt.tom:3111,3309,3330,4016`) ; contourné dans l'expérience.
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
| `JavaGenerator.t` + `CFamilyGenerator.t` | 652 + 537 | option ; la partie EMF (environ 150 lignes) est à exclure |

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
  - **(ii)** les fonctions écrites en dur dans deux mappings Go
    (`string.tom:25,27`, `intarray.tom:25`) ;
  - **(iii)** les aides du filtrage AC émises par le compilateur en `ITL`
    (`next_minimal_extract`, `__getMultiplicities_<op>`…).
- **Contre-épreuve** : renommer `tom_` en `tomB_` dans `B.go` suffit : le
  paquetage se construit et s'exécute (`zero 3 A`).
- **L'option existante `-D` (`noDeclaration`) ne résout rien** : avec
  `-go`, elle n'est que partiellement honorée (`GoGenerator.t:662`), et les
  `tom_equal_term_*` et `tom_is_sort_*` restent déclarés.

## 4. Design

### 4.1 Le pont Java → Go

**Côté Java.**

- Une option globale `--goFrom <phase>` (nom d'un plugin de
  `Tom.config`, par ex. `Backend`, puis `Optimizer`…) est déclarée dans les
  options de plateforme de `Tom.config`. Les options des plugins (`-go`,
  `-O`, `--genIntrospector`…) restent déclarées, car tous les plugins
  restent dans la chaîne.
- Dans `PluginPlatform.run`, avant d'exécuter le plugin nommé par
  `--goFrom`, la plateforme appelle `tom.engine.bridge.GoBridge.handOver
  (args, optionManager)` puis sort de la boucle **avec succès**. Le
  changement est d'environ 10 lignes dans la plateforme, plus une classe
  `tom/engine/bridge/GoBridge.java`.
- Variante possible (D3) : brancher le pont seulement dans `BackendPlugin`
  pour J4.1, puis le généraliser.

**`GoBridge.handOver`.**

1. Écrit le **paquet** (§4.2) sur l'entrée standard d'un processus Go.
2. Le binaire est résolu comme gomgo : `$TOMGO_BIN`, puis `tomgo` dans le
   `PATH`. Introuvable → erreur `TomMessage` explicite.
3. Lit la sortie standard du processus (le code généré) et l'écrit dans le
   fichier de sortie habituel (`getStreamManager().getOutputFile()`, avec
   l'option `encoding`), comme le fait `BackendPlugin` aujourd'hui.
4. Relaie chaque ligne `ERROR <nom> <fichier> <ligne> <message>` de la
   sortie d'erreur du processus en `TomMessage.error`, et chaque ligne
   `WARNING …` en avertissement. Un code de sortie non nul est une erreur.

Ainsi `tom -go [--goFrom Backend]` reste la seule commande des harnais. La
bascule Java/Go se fait par une option, que T1, T2 et smoke peuvent
ajouter par une variable d'environnement (`TOMGO_GOFROM=Backend`), sans
toucher aux goldens.

**Côté Go.** Une commande `tomgo` (nom : D2) dans le module `tom`. Pour
J4.1, `tomgo suffix` lit le paquet sur l'entrée standard, reconstruit
`Code`, la table et les marques, exécute les phases à partir de celle
indiquée par l'en-tête, puis écrit le code sur la sortie standard. Le
suffixe refuse une phase qu'il ne connaît pas encore (code de sortie 2,
message explicite).

### 4.2 Le paquet (format d'échange)

Le format est texte, une donnée par ligne : chaque terme Gom tient sur une
ligne (§3.1).

```
tom-bridge 1
from Backend
input /chemin/absolu/X.t
output /chemin/absolu/X.go
option goCode true
option pretty false
…                                   (toutes les options de plateforme et de plugins, nom valeur)
used UsedSymbolConstructor(Symbol(…))   (une ligne par KeyEntry, triées par toString)
type Type(…)                        (une ligne par entrée de mapTypeName, dès que k < Expander)
table Table(concTomEntry(…))
code Tom(concCode(…))
```

- Les termes sont écrits par `toString()` (format éprouvé ci-dessus) et
  relus par `XFromString` ; il n'y a rien à écrire côté Go.
- Le paquet n'est pas une sorte Gom, afin que les ADT Java et Go restent
  indépendants (décision 4). Il est versionné par sa première ligne.
- Options : exporter la liste complète plutôt qu'un sous-ensemble, pour que
  les étapes suivantes n'aient pas à toucher au pont.
- Débogage : avec `--intermediate`, `GoBridge` écrit aussi le paquet dans
  `<sortie>.tfix.bridge`.

### 4.3 Sources, construction et amorce du Tom+Go

**Où vivent les sources.** `src/go/tom/engine/<phase>/*.t` reproduit
`src/tom/engine/<phase>/` : `backend`, `tools` (SymbolTable, TomBase,
OutputCode), `platform` (options, messages), puis `optimizer`, `compiler`…
La commande est dans `src/go/tom/engine/cmd/tomgo/`. Du Go pur (sans
construction Tom) peut voisiner en `.go` : ce sont des sources.

**Construction.**

- Une nouvelle cible `mage regenTomGo` compile chaque `.t` de
  `src/go/tom/engine/**` avec la dist **`src/dist`** (`tom -go -np -I
  src/go/tom/engine/adt`), avec gomgo pour les blocs `%gom`. Le `.go`
  produit est écrit à côté du `.t`, ignoré par git, comme les ADT.
- `mage promoteGo` (existant) copie ces `.go` dans `stable/go/tom/…`, où
  ils sont commités comme amorce.
- `mage bootstrap` continue de tout construire depuis l'amorce **sans
  Java**. Seule la régénération demande une dist Java, jusqu'au point fixe
  (D4).

**Inclusion de l'ADT.** Le code Tom+Go fait `import . "tom/engine/adt"` et
`import "tom/library/aterm"`, et inclut `adt.tom` (point « le mapping de
l'ADT du moteur est à inclure » de `doc/go/README.md`). Avant d'écrire du
Tom+Go, corriger dans gomgo :

- le `get_slot` des champs renommés (§3.2, écueil 1) ;
- `ToATerm` des opérateurs AU.

**Plusieurs `.t` par paquetage.** C'est un prérequis : voir D1, option
recommandée « préfixe par fichier », qui s'applique d'abord au
`GoGenerator` Java.

### 4.4 Structure du code porté

- **Héritage.** Pour J4.1, une seule cible (Go) : la hiérarchie
  `AbstractGenerator → GenericGenerator → GoGenerator` est aplatie en un
  type `goGenerator`. Chaque méthode Java devient une méthode Go de même
  nom, dans le même ordre, avec un commentaire `// Java: Fichier.t:ligne`,
  ce qui facilite la relecture côte à côte.
  - Si le backend Java est porté aussi (D5), les méthodes redéfinissables
    passent par une interface `generator` et un champ `self` (répartition
    virtuelle explicite).
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
| **P0 pont** | relecture du paquet (code, table, marques, types) par l'ADT Go puis réimpression | le paquet lui-même | identité stricte ; corpus : T1 + smoke (121 entrées) + **T0 en `-go`** quand c'est possible |
| **P1 phase** | une phase portée P, en isolation : `Go_P(dump avant P)` | dump Java après P (`--intermediate`, plus table et marques) | identité stricte du `toString()` |
| **P2 bout en bout** | `tom -go --goFrom X` sur les cas T1 et smoke | goldens T1 existants | identité stricte ; T2 (comportement) inchangé par construction |
| **P3 auto-application** | le compilateur hybride compile les sources Tom+Go du suffixe | sortie de `tom -go` 100 % Java sur les mêmes sources | identité stricte (« self-T1 ») |
| **P4 Java** (option D5) | backend Java en Go, `--goFrom Backend` sans `-go` | T0 (legacy) | identité stricte |
| **Parser** | AST Go comparé au `.tfix.parsed` Java | voir §2.3 | identité visée ; tolérances de position séparées, comptées |

Remarques :

- P1 est presque gratuit : `--intermediate` donne déjà les paires
  (`.compiled` → `.optimized` pour l'optimizer, `.expanded` → `.compiled`
  pour le compilateur, `.typed` → `.expanded` pour l'expander…). Il
  manque la table aux frontières autres que le typer, d'où le dump du
  paquet par `--intermediate` (§4.2).
- L'optimizer et le compilateur de motifs agissent aussi sur des AST à
  cible Java. Leurs ports peuvent donc être éprouvés en P1 sur les dumps du
  corpus T0 (102 entrées), ce qui élargit la couverture au-delà des 121
  entrées Go. Cela suppose de garder leurs branches `jCode` (D5).

### 5.2 Application mécanique de la règle d'équivalence

- Une seule fonction de comparaison par niveau, dans
  `test/tools/j4/` : `bytes.Equal` pour P0–P4. Elle n'a pas de paramètre
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

Chaque jalon est un plan d'implémentation séparé et se termine par un
commit vert (`mage all`, T1, T2, smoke, T0 inchangés). (U) marque une
dépendance à une décision de la §8.

| Jalon | Contenu | Critère de sortie |
|---|---|---|
| **J4.0a — préalables gomgo** | `get_slot` des champs renommés ; `ToATerm` des opérateurs AU ; test Tom+Go minimal (le programme de §3.2) dans `test/go` | le `.t` d'essai se compile sans contournement ; `ToATerm` relu sur les 675 dumps |
| **J4.0b — plusieurs `.t` par paquetage** (U : D1) | mise en œuvre de l'option choisie dans `GoGenerator`, `Compiler` (aides AC) et les mappings `string`/`intarray` ; goldens régénérés | deux `.t` d'un paquetage se construisent ; diff des goldens limité aux renommages (vérifié par script) ; T2 inchangé |
| **J4.0c — pont** (U : D2, D3) | `--goFrom`, `GoBridge`, paquet ; `tomgo suffix` en mode `--check` (relit le paquet, le réimprime, compare) ; `mage regenTomGo` | P0 vert sur T1 + smoke (121 entrées) et T0 en `-go` ; un `--goFrom Backend` avec un suffixe absent échoue proprement |
| **J4.1a — fondations Go** | `OutputCode`, sous-ensembles `SymbolTable`/`TomBase`, options et messages, en Tom+Go, avec tests unitaires | tests unitaires verts ; compilés par `regenTomGo` |
| **J4.1b — backend Go** | `Collector`, parcours `generate…`, `GenericGenerator`, `GoGenerator` ; portage par familles d'instructions, en suivant le compteur P2 | P2 : 100 % des goldens T1 + smoke identiques avec `--goFrom Backend` ; budget §5.3 tenu |
| **J4.1c — amorce** | `promoteGo` du backend ; P3 sur ses propres sources | self-T1 identique ; `mage bootstrap` sans Java vert |
| **J4.1j — backend Java en Go** (U : D5) | `CFamilyGenerator` + `JavaGenerator` sans EMF | T0 identique avec `--goFrom Backend` |
| **J4.2 — optimizer** | `OptimizerPlugin` + `sl/builtin.tom` Go | P1 `.compiled` → `.optimized` identique ; P2 avec `--goFrom Optimizer` |
| **J4.3 — compilateur de motifs** | `compiler/*` (16 fichiers), dont `collectHostSubjects` et les aides AC Go | P1 `.expanded` → `.compiled` ; P2 |
| **J4.4 — expander** | export des types ; `ExpanderPlugin`, dont l'introspecteur Go | P1 `.typed` → `.expanded` ; P2 |
| **J4.5 — type checker, typer** | diagnostics en mode Regress ; `NewKernelTyper` | P1 ; cas d'erreur T2 identiques par nom |
| **J4.6 — desugarer, syntax checker, transformer** | | P1 ; P2 avec `--goFrom Transformer` |
| **J4.7 — parser** | parser du prototype reciblé sur `tom/engine/adt`, complété ; règle des accolades `ITL` | AST identique, ou tolérances de position comptées (§5.2) ; P2 |
| **J4.8 — orchestration** | starter, `%include`, gomgo en bibliothèque, CLI `tomgo` autonome | T1/T2/smoke verts avec `tomgo` seul, sans Java |
| **J4.9 — point fixe** | stage1 (`.go` produit par Java `-go`) compile les sources : stage2 ; stage2 recompile : stage3 | sortie de stage2 identique à celle de stage1 (et stage3 identique à stage2) |

## 7. Risques

| Risque | Atténuation |
|---|---|
| Volume du backend (environ 5 000 lignes) pour une seule étape | J4.1 découpé en a/b/c ; compteur P2 par cas ; portage méthode par méthode avec renvoi `// Java:` |
| Héritage et exceptions Java mal traduits (répartition virtuelle, flot par exception) | aplatissement pour une seule cible ; `panic` limité aux erreurs, retours explicites ailleurs (§4.4) |
| Sortie qui dépend de l'ordre d'itération d'un `HashSet`/`HashMap` Java (middle-end) | aide `javaHashOrder` ; repéré par P1, phase par phase ; 53 boucles à examiner |
| Limites du parser îlot sur le Tom+Go du compilateur (paramètres de stratégie de type non identifiant, règles étiquetées, `$var` dans des hooks avec accolades) | contournements locaux notés `// port:` ; liste tenue au tableau de bord |
| Fabrique `sharedobjects` sans références faibles, mutex global : mémoire et temps sur de gros fichiers | mesure à J4.1 (budget §5.3) ; paquet Go `weak` si dépassé |
| La table exportée devient incomplète quand k recule (types, état des plugins) | le paquet porte déjà `type` ; chaque jalon vérifie par `grep` les champs d'état lus par la phase portée |
| Les sorties d'erreur passent par un autre processus | protocole `ERROR …` sur stderr, comparé par nom (Regress) |
| La régénération dépend d'une dist Java jusqu'au point fixe | `bootstrap` depuis l'amorce sans Java ; seule `regenTomGo` exige Java |
| Chemins absolus dans `OriginTracking` (fichier d'entrée temporaire des harnais) | sans effet tant que les deux côtés lisent le même paquet ; à traiter au parser (tolérance « fichier ») |

## 8. Décisions ouvertes (à trancher par l'utilisateur)

Chaque décision donne les options, la recommandation, et sa raison.

- **D1 — plusieurs `.t` dans un paquetage Go.**
  - (a) Un seul `.t` par paquetage ; le reste en `.go` pur.
  - (b) Préfixe par fichier pour toutes les déclarations de niveau
    paquetage générées (`tom_<Fichier>_make_x`…), aussi dans les aides AC
    du compilateur, et fonctions des mappings `string`/`intarray`
    réécrites en fermetures ou préfixées.
  - (c) Rendre `-D` complet en Go (tout en ligne, aucune déclaration).

  **Recommandation : (b)**, appliquée d'abord au `GoGenerator` Java
  (J4.0b), sans condition. Raisons : l'expérience montre que le renommage
  suffit ; (a) obligerait à fondre `AbstractGenerator`, `GenericGenerator`
  et `GoGenerator` en un seul `.t` ; (c) change la forme du code produit et
  rend le portage plus délicat. Coût : les 94 goldens changent, une seule
  fois, par un diff vérifié « renommages seuls ». Variante possible :
  préfixe activé seulement par une option, pour ne pas toucher aux goldens.
- **D2 — nom et emplacement de la commande Go.** `tomgo` dans
  `src/go/tom/engine/cmd/tomgo` (sous-commande `suffix`), ou
  `tom-go-suffix`.
  **Recommandation : `tomgo`**, qui deviendra le compilateur complet
  (J4.8). Le prototype a aussi un `tomgo` (`prototype/tomgo/cmd/tomgo`), mais il est gelé et dans un autre module : seuls les binaires installés pourraient se confondre. Variable d'environnement
  `TOMGO_BIN`.
- **D3 — point d'insertion du pont.** Crochet générique dans
  `PluginPlatform` avec `--goFrom <plugin>`, ou branchement dans
  `BackendPlugin` seulement pour J4.1.
  **Recommandation : crochet générique dès J4.0c** (environ 10 lignes) :
  il sert tel quel à chaque recul de k, et les options de tous les plugins
  restent déclarées.
- **D4 — dist Java utilisée pour compiler le Tom+Go.** `src/dist` (le
  compilateur en développement) ou `stable/dist` (promu).
  **Recommandation : `src/dist`**, vérifiée fraîche par la garde existante
  (`CheckFresh`). Le `GoGenerator` de référence évolue encore en J4.0b, et
  c'est sa sortie que le suffixe doit reproduire.
- **D5 — jusqu'où porter la cible Java.**
  - (a) Go seulement (branches `jCode` des phases omises).
  - (b) Backend Java en Go en plus (J4.1j, environ 1 200 lignes).
  - (c) Toutes les branches Java du middle-end conservées, pour éprouver
    les ports sur T0.

  **Recommandation : (c) pour le middle-end et (a) pour le backend dans un
  premier temps.** Garder les branches Java de l'optimizer et du
  compilateur ne coûte presque rien et double à peu près le corpus de P1 (102 entrées T0 en plus des 121). Le
  `JavaGenerator` en Go n'apporte rien au point fixe d'un compilateur
  Tom+Go → Go. À reconsidérer après J4.3.
- **D6 — format du paquet.** Texte ligne par ligne (§4.2), une sorte Gom
  dédiée, ou plusieurs fichiers (`.tfix.*` existants).
  **Recommandation : texte ligne par ligne sur stdin/stdout.** Rien à
  écrire côté Go pour lire les termes, pas de fichier temporaire, et les
  ADT Java et Go restent indépendants.
