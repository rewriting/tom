# J0 → T3 — cible Go dans le compilateur Tom+Java

Chemins mis à jour par la réorganisation du 2026-10-23 (voir docs/superpowers/specs/2026-10-23-repo-layout-design.md).

Date : 2026-10-22. Branche `tom-go`.
Spec : `docs/superpowers/specs/2026-10-22-tom-go-backend-design.md`.
Plan : `docs/superpowers/plans/2026-10-22-tom-go-backend.md` (Tasks 1 → 10).

Résultat : le compilateur Tom+Java accepte `-go` et produit du Go qui
compile et s'exécute ; il est promu dans `stable/` (le compilateur
bootstrappé, `stable/dist/bin/tom -go`, l'accepte aussi). La sortie Java
reste identique à celle du compilateur humain `legacy/`, et 13 cas de
`test/` portés en Tom+Go se comportent comme leur original.

## 1. Ce qui a été fait, tâche par tâche

| Tâche | Commits | Contenu |
|---|---|---|
| 1 (J−1) | `29108b7ad` | `legacy/` = copie intacte de `v3` @ `2918e95c7` (dernier commit humain, 29/08/2019), `legacy/README.md` (installation), section « Two versions » dans `README.md`. `cd legacy && ./build.sh stable && ./build.sh src.dist` réussit. |
| 2 (T0) | `36cc4faf6`, `8682fa34a` | Harnais `test/go/harness` (compile un `.t` avec une distribution Tom) et `test/go/javaparity` : chaque `test/**/*.t` compilé par `legacy/` et par la racine, en mode par défaut et en `-np`, doit donner les mêmes diagnostics et des arbres `.java` identiques. `mage portJava`. |
| 3 | `130f82a1f` | Côté tomgo : `sl.MustVisit` / `sl.RecoverVisitFailure`, commande `tomgo gom-block` (un bloc `%gom` → `<m>_gen.go` + mapping `.tom`). |
| 4 (J0) | `d83564cc1`, `32ae918ca` | Option `goCode` (`-go`) dans `BackendPlugin.t`, `GoGenerator.t`, types builtin Go dans `SymbolTable.t`, extension `.go` dans `TomStreamManager.java`, mappings `src/tom/library/mapping/go/` copiés dans `dist/share/tom/go`. Conditionnelles typées, valeurs nulles par type (`Bottom`), corps de `make` parenthésés. |
| 5 (J1, T1) | `ccbdbc085`, `7e6ec3631` | Correctif du parser `-np` (`CstBuilder.t`) : `$var` substitué dans les corps de hooks contenant des accolades (sinon `make(p){ &T{x:$p} }` gardait `$p` et `make_empty() { []*T{} }` était perdu). Harnais `test/go` (T1 : sortie Go figée dans `testdata/golden/`, build et exécution). |
| 6 (J1) | `d5adfde2d`, `c100a48c2`, `5b2c00cbf`, `cac018b7a` | `%gom` délégué à `tomgo gom-block` sous `-go` (`TomParserTool.t`, `parseGomFileGo`) ; dix cas de `test/` portés fidèlement (seul le code hôte est traduit, écarts notés `// port:`). |
| 7 (J1) | `e772d6ab7`, `74499dbe4`, `a7809b123` | `%strategy` en Go (`ExpanderPlugin.t` branche Go, `CstConverter.t` `goTarget`) : une struct par stratégie sur la bibliothèque `sl` Go ; paramètres lus via `tom__strat.<nom>` ; `-go` force `-np` ; `-gi` refusé d'emblée (pris en charge depuis J2, cf. §5). |
| 8 (T2) | `87d4e1afe`, `6ffbe2cfe` | Oracle `legacy/` : chaque original est compilé par `legacy/` et exécuté (JUnit via un `RunListener`, ou programme `main`) ; résultat mis en cache dans `test/go/testdata/oracle/` avec `source_sha256` et l'ensemble des `@Test` (détection de péremption, garde contre un oracle vide). Tableau de bord `doc/go/port-dashboard.md` (`mage portGo`). |
| 8b (J1) | `95b1775f4`, `20e17024b`, `5f13f63e0` | Stratégies avec environnement dans `sl` Go (`Environment`, `Position`, `Visit`/`VisitWith`/`VisitEnv`) et chemin d'environnement généré pour les `%strategy` ; `TestStrategy.testPosition` et `sl/TestEnvironment.t` portés. |
| 9 (T3) | `98229a43a`, `776d330c9` | Inventaire des constructions Tom utilisées par le compilateur (`src/tom/engine`, `src/tom/platform`) et de leur couverture par un cas OK : `doc/go/coverage.md` (`mage coverage`). |
| 10 | `4590420b6`, ce commit | Promotion dans `stable/`, ce rapport, `CLAUDE.md`. |

## 2. Fichiers modifiés dans `src/`

`git diff --stat 2918e95c7 -- src` : 20 fichiers, +1312 / −38. Le
changement de `src/lib/tom-common.xml` (fork javac, 2 lignes) est
antérieur au projet ; les 19 autres sont du projet :

```
 src/tom/engine/TomMessage.java              |   2 +
 src/tom/engine/TomStreamManager.java        |   4 +
 src/tom/engine/backend/BackendPlugin.t      |  15 +
 src/tom/engine/backend/GoGenerator.t        | 769 +++++
 src/tom/engine/expander/ExpanderPlugin.t    |  75 ++-
 src/tom/engine/parser/TomParserPlugin.t     |   6 +
 src/tom/engine/parser/TomParserTool.t       |  85 +++
 src/tom/engine/parser/antlr4/CstBuilder.t   |  85 ++-
 src/tom/engine/parser/antlr4/CstConverter.t |  22 +-
 src/tom/engine/tools/SymbolTable.t          |  15 +-
 src/tom/library/mapping/go/{Position,Strategy,boolean,int,long,sl,string}.tom
 src/tom/library/mapping/go/sl/{basic,composed}.tom
```

## 3. Métriques (valeurs observées le 2026-10-22, après promotion)

- **T0** (`mage portJava`) : les 121 `test/**/*.t` compilés par `legacy/`
  et par la racine donnent des diagnostics et des sorties `.java`
  identiques, en mode par défaut et en `-np` (242 comparaisons, 0
  différence, aucune exception dans la liste `npAllowed`). Les entrées
  qui échouent (dont les 25 qui plantent en `-np` sur `Cst_IncludeFile`)
  échouent pareil dans les deux compilateurs.
  *Mise à jour 2026-10-23 (J2, Task 1) :* les plantages `Cst_IncludeFile`
  ne venaient pas du parser `-np` : 21 entrées de `test/gom` et `test/sl`
  incluent un mapping que `test/build.xml` fait d'abord générer par Gom
  (`testsl/testsl.tom`, etc.) ; sans ce mapping, les deux compilateurs
  imprimaient `TomIncludeException` puis `convert: strange term:
  Cst_IncludeFile` à l'identique, et T0 passait à vide. T0 lance
  désormais le Gom de chaque distribution comme `build.xml` et passe `-I`
  vers les mappings générés : ces 21 entrées produisent du Java,
  identique à `legacy/` dans les deux modes (102 entrées avec sortie en
  mode par défaut, 103 en `-np`, planchers relevés à ces valeurs), et le
  test exige que chaque entrée de `test/gom` et `test/sl` produise du
  Java dans les deux distributions.
- **T1/T2** (`mage portGo`) : 103 cas (`test/**/*.t` hors `error/`) ;
  **13 OK**, 0 divergence, 0 échec go test / go build / tom, 0 oracle
  invalide, 90 non portés. Cas OK : `Array.t`, `Peano.t`,
  `TestAlgebraic.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`,
  `TestNonVarSubjects.t`, `TestPeano.t`, `TestStrategy.t`,
  `TestSublists.t`, `TestVarVarStar.t`, `numeric/TestNumericConditions.t`,
  `sl/TestEnvironment.t`.
- **T3** (`mage coverage`) : 13 constructions couvertes sur 17. Non
  couvertes : `%include` des mappings Collection/List/Map, anti-patterns
  `!p` (ceux de `TestSublists.t` sont dans un bloc commenté de
  l'original), contraintes `<<`/`&&`/`||`, règles étiquetées. Les
  comptes sont textuels (bornes hautes), pas une preuve d'exercice
  sémantique.
- **Régression** : `go test -count=1 ./smoke/` (dans `test/go`) vert (8 fixtures
  + test d'options) :
  - `array_size` : `get_size` d'un `%oparray` renvoie un `int64` (le
    `len($l)` Go, de type `int`, est converti ; un `;` final est toléré) ;
  - `bottom_struct` : le « bottom » d'un sort struct (variables fraîches
    d'un OR-pattern `(wa|wb)(...)`) est la valeur zéro, pas `nil` ;
  - `bq_then_match` : un backquote en fin de ligne suivi d'un `%match` ;
    le backend sépare lui-même les deux instructions Go ;
  - `make_braces` : corps de hooks contenant des accolades (littéraux
    composites) : `$var` substitué, `make_empty() { []*N{} }` reconnu ;
  - `oplist_same_sort` : `%oplist` dont l'élément a le même sort que la liste ;
  - `smoke` : programme Tom+Go de bout en bout (`%include`, `%match`
    sur entiers, backquote) ;
  - `strategy_env` : stratégies à base d'environnement
    (`GetEnvironment()`, exécution d'une stratégie à sa propre position,
    accord de `Visit`/`VisitLight`) ;
  - `strategy_param` : paramètre de `%strategy` utilisé dans une
    partie droite, un backquote et un sujet de `%match` ;
  - test d'options (`options_test.go`) : `-go -gi` est refusé.
    *(J2, 2026-10-23 : supprimé, `-gi` est désormais pris en charge en Go ;
    fixture `gen_introspector`, options supplémentaires lues dans
    `args.txt`.)*

## 4. Promotion dans `stable/` (Task 10)

```
unset TOM_HOME
./build.sh bootstrap      # BOOTSTRAP PERFORM SUCCESSFULLY (1 min 36 s)
./build.sh bootinstall    # BUILD SUCCESSFUL
./build.sh clean.stable && ./build.sh stable   # BUILD SUCCESSFUL (237 sources)
./build.sh clean.src && ./build.sh src.dist    # BUILD SUCCESSFUL (1 min 44 s)
TOM_HOME=$PWD/stable/dist stable/dist/bin/tom -go -d out test/go/smoke/testdata/smoke/smoke.t
(cd out && go run .)      # sortie = expected.txt ; go vet propre
mage -d magefiles -w . all && mage -d magefiles -w . portJava && mage -d magefiles -w . portGo && mage -d magefiles -w . coverage   # tout vert
(cd prototype/tomgo && go test -count=1 ./...)    # 17 paquets ok (sur 47 ; 30 sans test), 0 échec — compté le 2026-10-23 avec `go list ./...` et `go test`
```

- Le point fixe (`./build.sh bootstrap` : `src/` compilé deux fois par
  lui-même, classes comparées) réussit : le compilateur étendu se
  recompile à l'identique.
- `stable/` datait du bootinstall de janvier 2018 ; la promotion apporte
  donc aussi les changements amont de `src/` faits depuis (Java généré
  qui garde les commentaires, helpers réordonnés), et des fichiers de
  `src/` jamais installés (`lib/tools/guava-18.0.jar`, mappings emf
  `StringEList.tom`/`uml2.tom`, `MyECrossReferenceAdapter.java`).
- `stable/tom/engine/parser/antlr2/` (17 fichiers) est supprimé :
  l'amont a retiré le parser antlr2 de `src/` (commit `1dd5124a9`,
  février 2018) et le nouveau `TomParserPlugin` n'a plus
  `createHostParser` ; `bootinstall` ne supprime rien, et une
  reconstruction propre de `stable` échouait sur `HostParser.java`.
  D'autres fichiers de `stable/` sans source dans `src/` restent
  (anciens lexers/parsers Gom antlr2, `tom/library/xml`,
  `OptionParser.java`, `parser/tomjava/TomIsland*`) ; ils compilent et ne
  gênent pas.
- `stable/config/Tom.config` n'a pas besoin de changer : l'option
  `goCode` est déclarée dans `BackendPlugin.t`, et `src/config/Tom.config`
  ne diffère de celui de `stable/` que par la mise en page.

## 5. Limites connues

- `%transformation` et `-gi` (`--genIntrospector`) sont refusés avec
  `-go`. *(J2, 2026-10-23 : `-gi` est pris en charge — `LocalIntrospector`
  Go, utilisé comme `&LocalIntrospector{}` ; `%transformation` reste
  refusé.)*
- Les paramètres de type `Strategy` d'un `%strategy` ne sont pas exposés
  comme enfants (`ChildCount`/`ChildAt`), donc invisibles pour `Mu`/`Init`.
  *(J2, 2026-10-23 : levée — ils suivent la stratégie par défaut, comme
  en Java.)*
- Le type d'une conditionnelle est lu sur ses branches ou sur la variable
  affectée, avec repli sur `any`.
- Le Go généré tient sur une ligne logique par construction (lire avec
  `gofmt`) ; les numéros de ligne du `.go` dérivent de ceux du `.t`.
- `ChoiceId.VisitLight`/`OneId.VisitLight` : la divergence avec Java
  (échec du FIRST traité comme succès) a été corrigée dans le commit
  19236dce8 ; les combinateurs `sl` sont alignés sur Java.
- Mappings Go seulement pour `boolean`, `int`, `long`, `string`, plus
  `sl`, `Strategy`, `Position` (pas de `char`/`double`/`float`, pas de
  Collection/List/Map).
- Les corps de hooks qui mêlent accolades et îlot Tom (backquote)
  gardent `$var` littéral.
- Dans le code hôte d'une stratégie, il faut écrire `tom__strat.<param>`
  et `tom__strat.GetEnvironment()`.

Limites de la promotion dans `stable/` :

- **Point fixe au niveau des classes seulement.** `./build.sh bootstrap`
  montre que `src/` compilé deux fois par lui-même donne des `.class`
  identiques. En revanche le `src/gen` produit par le nouveau `stable`
  diffère des sources installées dans `stable/` (générées par l'ancien
  `stable`) : numérotation `tomMatchN` et ordre des déclarations, dans
  19 fichiers une fois la numérotation normalisée. Aucun second
  bootinstall n'a été tenté ; on s'attend à ce qu'il converge (le
  nouveau `stable` et `src/` sont le même compilateur, comme le montre le
  point fixe des classes), mais ce n'est pas vérifié.
- **Dérive amont réelle, pas seulement cosmétique.** Comme `stable/`
  datait du bootinstall de janvier 2018, environ 70 fichiers diffèrent
  sémantiquement (hors commentaires et blancs) de l'ancien `stable/` :
  changements amont de `src/` entre 2018 et `v3` plus ceux de ce projet.
  Ils sont validés indirectement par le point fixe et par les harnais,
  pas revus un par un.
- **Fichiers de `stable/` sans source dans `src/`.** Il en reste :
  anciens lexers/parsers Gom antlr2 (`tom/gom/parser/{Block,GomLanguage,
  SimpleBlock}*`), `tom/gom/expander/rule/Rule{Lexer,Parser}.java`,
  `tom/library/xml/*`, `tom/platform/OptionParser.java`,
  `tom/engine/parser/tomjava/TomIsland*`. Ils compilent encore, mais un
  futur bootinstall qui changerait les API qu'ils appellent les ferait
  casser comme `parser/antlr2/` (supprimé ici) ; un nettoyage de
  `stable/` devra les traiter.
- **`lib/tools/guava-18.0.jar`** a été copié par bootinstall depuis
  `src/lib/tools` mais n'est utilisé par aucune source (aucun import
  `com.google.common`).

## 6. Suite

- **J2** : porter le reste de `test/` par lots (un sous-répertoire par
  lot : `sl/`, `gom/`, `antipatterns/`, …) : copier l'original dans
  `test/go/<rel>`, traduire le code hôte, ajouter le cas à
  `cases`, `-update -update-oracle`, corriger `GoGenerator.t` au besoin,
  un commit par lot.
- **J3** : pour chaque ☐ de `coverage.md`, porter un cas de `test/` ou
  d'`examples/` qui l'exerce.
- **J4** : bootstrap du compilateur réécrit en Tom+Go (spec séparé, à
  rédiger quand la checklist T3 est complète).
