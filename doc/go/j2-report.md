# J2 — portage de `test/` en Tom+Go

Chemins : depuis la réorganisation du 2026-10-24, les originaux `test/<rel>` cités ci-dessous sont dans `test/java/<rel>` et les harnais `test/go/{harness,javaparity,coverage,smoke}` et `port_test.go` dans `test/tools/{harness,javaparity,coverage,smoke,parity}` ; les ports restent dans `test/go`.

Date : 2026-10-23. Branche `tom-go`.
Plan : `docs/superpowers/plans/2026-10-23-j2-port.md`.
Spec de référence : `docs/superpowers/specs/2026-10-22-tom-go-backend-design.md`
(règles de portage, T0–T3).

Résultat : tous les fichiers de `test/` hors `error/` (103) ont un
statut autre que « non porté ». 77 cas portés en Tom+Go se comportent
comme leur original compilé par `legacy/`, 11 tests de non-régression
des diagnostics sont rejetés par `tom -go` avec les mêmes erreurs, et
aucun cas ne diverge. Les 17 constructions de la checklist T3 sont
couvertes. Les changements de `src/` sont promus dans `stable/`, et le
point fixe du bootstrap est vérifié.

## 1. Ce qui a été fait, lot par lot

Commits : `git log --oneline d8bb0cf1d..HEAD` (31 commits).

| Lot | Commits | Fichiers | Contenu principal |
|---|---|---|---|
| Plan, Tasks 1–2 | `6fe6d7bac`, `54eead4c3`, `da055e6a9`, `772b1e704` | — | Les plantages `Cst_IncludeFile` ne venaient pas du parser `-np`. 21 entrées de `test/gom` et `test/sl` incluent un mapping que `test/build.xml` fait d'abord générer par Gom. T0 lance donc le Gom de chaque distribution, avec les options de `build.xml`, puis passe `-I`. T0 exige aussi du Java pour chaque entrée de `test/gom` et `test/sl`. `TestNonLinearity.t` plante aussi dans `legacy/` (bug du typer), d'où le statut « échoue dans legacy ». |
| `test/sl/` | `fd04fbb68`, `e492b152f`, `7b5911393`, `73778ed2c`, `b058067a0` | 16 : 10 OK, 4 support, 1 non supporté (`TestRef.t`), plus `TestEnvironment.t`, déjà porté | **gomgo** : `--withCongruenceStrategies` (`_op`, `When_op`, `Is_op`, `Make_op`), hooks `make` utilisateur, refus explicite des hooks non traduits. **Compilateur** : `-gi` pris en charge sous `-go` (`LocalIntrospector` Go, `ExpanderPlugin.t`/`GoGenerator.t`), paramètres `Strategy` exposés comme enfants, options de `%gom(...)` transmises à gomgo. **Runtime sl** : `IfThenElse`, `Not`, `ThrowVisitFailure`, comparaison par identité `same`, stratégies vues comme termes. **Mappings** `sl/basic.tom`, `sl/composed.tom` complétés. |
| `test/gom/` | `c16e254ed`, `f6d9c74a2`, `8dc2ee9ae`, `677327c43`, `8546c05d3`, `b9fff7eea` | 28 : 27 OK, 1 non supporté (`TestTermgraph.t`) | **gomgo** : imports entre modules, `--nosharing`, `--multithread`, règles Gom (`backend/rules.go`), théories FL/AU/ACU/Free et hooks dans l'ordre de Java Gom (`backend/userhooks.go`), API Java des sortes et des listes (`backend/javaapi.go`), `toString`/ATerm/`fromTerm` comme Java. **Runtime** : `sharedobjects.Factory` protégée par un verrou, conversions Gom de `aterm`. **Compilateur** : suffixe `l` des littéraux long supprimé en Go. **Mappings** `char`, `double`, `float`, `aterm`, `atermlist`. |
| `antipatterns/`, `andor/`, `backquote/`, `rule/`, `acmatching/` | `3ad90ec92`, `d1ec518c4`, `674ff034a`, `dcf46ba7f`, `9d97a89a9`, `885b3323d` | 18 : 13 OK, 5 échoue dans legacy (`rule/`, `%rule` obsolète) | **`OutputCode.java`** : en Go seulement, les points-virgules sont gardés quand le code hôte est mis sur une ligne. **`Compiler.t`** : version Go de `next_minimal_extract`. **gomgo** : théorie AC, hooks sans arglist. **Mappings** `util/LinkedList`, `intarray`. **Harnais** : statut « échoue dans legacy » vérifié par `TestLegacyFails`, jumeaux Java des fixtures smoke (`TestJavaTwins`). |
| `test/regress/` | `90a71bc1e`, `8ecc30dcb` | 13 : 11 OK (diagnostics), 2 échoue dans legacy (`Include.t`, `TestNonLinearity.t`) | Mode `Regress` du harnais. L'oracle est le journal `RegressFormatter` de `legacy/ -np`, et `tom -go` doit rejeter le port avec le même journal. |
| Listes Gom en cellules | `6fe42c57c`, `9fccbbcd4`, `21aee43af` | — | Modules utilisateur de gomgo : une liste est une chaîne de cellules `Cons<op>`/`Empty<op>` partagées au maximum, comme en Java Gom (`gom/backend/cells.go`). `GetTail`/`Cons` en O(1), queue non-liste représentable, enfants `Visitable` mis en cache, hachage direct des têtes builtin. |
| Fichiers racine | `6c2fd73ed`, `d79277636`, `23783c4e7`, `ca2c40d71`, `eebdd6bd4` | 17 : 15 OK (dont 2 programmes C, `cfib1.t` et `loulou.t`, portés en Go), 2 échoue dans legacy (`Record.t`, `TestRecord.t`) | **`GoGenerator.t`** : égalité entre sous-types (conversion vérifiée). **`AstBuilder.t`** : sous `-go`, les `{ … }` du code hôte restent du texte hôte. **`Compiler.t`** (`collectHostSubjects`) : `_ = s` pour chaque sujet variable non lié par un motif de la règle. **gomgo** : module sans sorte, `BuildCons<Op>`/`BuildEmpty<Op>` et `Make_Cons<op>`/`Make_Empty<op>`, qui réactivent `sl/TestReflective.testMake`. **Mappings** `concString` (string.tom), `util/ArrayList`. **Harnais** : mode C. **Couverture** : commentaires retirés aussi côté compilateur. |
| Clôture | `d407d2e83`, `a6582a618`, ce commit | — | Promotion dans `stable/` ; `seen` → `HashSet` dans `collectHostSubjects` ; précision dans le README (`_ = s`) ; ce rapport. |

Volume (`git diff --stat d8bb0cf1d..eebdd6bd4`) :

| Arbre | Fichiers | Insertions | Suppressions |
|---|---|---|---|
| `src/tom` (compilateur et mappings) | 20 | 680 | 49 |
| `src/go` (gomgo et runtime) | 35 | 3 521 | 53 |
| `test/go` (ports, goldens, oracles, harnais) | 316 | 32 920 | 246 |

Fichiers de `src/tom` modifiés :
- `Compiler.t`, `ExpanderPlugin.t`, `GoGenerator.t` ;
- `AstBuilder.t` (antlr4), `TomParserTool.t`, `OutputCode.java` ;
- les mappings Go `aterm`, `atermlist`, `char`, `double`, `float`, `intarray`, `string`, `sl/basic`, `sl/composed`, `util/{ArrayList,LinkedList}`, `util/types/{ArrayList,LinkedList,Object}`.

## 2. Tableau de bord et couverture finals

Source : `doc/go/port-dashboard.md` (`mage portGo`), 103 fichiers.

| Statut | Nombre |
|---|---|
| OK | 77 |
| OK (diagnostics) | 11 |
| non supporté | 2 (`sl/TestRef.t` : `--termpointer` et stratégies de graphes de `sl/graph.tom` ; `gom/TestTermgraph.t` : `--termgraph`) |
| support | 4 (`sl/CommonStrat.t`, `sl/{Old,New,Introspector}Behaviour.t`) |
| échoue dans legacy | 9 (`Record.t`, `TestRecord.t`, `regress/Include.t`, `regress/TestNonLinearity.t`, 5 fichiers `rule/`) |
| divergence, échec go test / go build / tom, oracle invalide | 0 |
| non porté | 0 |

Évolution du nombre de cas OK : 13 au départ (fin de J0/J1), puis 22 après `sl/`, 49 après `gom/`, 62 après les petits répertoires, et 77 après les fichiers racine.

**Couverture T3** (`doc/go/coverage.md`, `mage coverage`) : 16
constructions listées, 0 non couverte (un premier décompte indiquait 17 ;
le tableau actuel compte 16 lignes). La ligne « règles étiquetées » compte 0
utilisation dans le compilateur. Les seules occurrences sont des exemples
en commentaire de `GeneralPurposePropagator.t`, et le comptage retire
désormais les commentaires. Elle est donc couverte à vide.

**T0** (`mage portJava`) : la sortie Java de la racine reste identique à
celle de `legacy/`, avec le parser par défaut et avec `-np`. Depuis la
Task 1, il y a 102 entrées avec sortie en mode par défaut (764 fichiers
`.java`) et 103 en `-np` (765), au lieu de 81 et 82 avant. Les
planchers sont relevés à ces valeurs.

## 3. Découvertes notables

- **T0 passait à vide sur 21 entrées.** Les entrées de `test/gom` et
  `test/sl` qui incluent un mapping généré par Gom plantaient à
  l'identique dans les deux compilateurs : `TomIncludeException`, puis
  `convert: strange term: Cst_IncludeFile`. Le harnais lance maintenant
  Gom et passe `-I` comme `test/build.xml`. Ces 21 entrées produisent du
  Java identique à `legacy/` dans les deux modes. Le défaut supposé du
  parser `-np` n'existait pas.
- **Affirmations de revue réfutées en exécutant `legacy/`.**
  - Une revue jugeait que la congruence `_l(sub)` divergeait de Java sur
    les listes. Mesuré sur un programme Tom+Java compilé par `legacy/` :
    `getChildren()` d'une liste Gom Java renvoie tous les éléments, et
    `_l(sub)` les visite tous, comme en Go.
  - De même, un ordre supposé des hooks ACU (« Java trie avant
    d'aplatir ») a été corrigé après lecture du Java généré par le Gom de
    `legacy/` : neutre, puis aplatissement, puis échange.
  - Règle suivie : chaque sortie attendue d'une fixture smoke est mesurée
    sur son jumeau Tom+Java compilé par `legacy/` (`TestJavaTwins`).
- **Listes en cellules partagées.** Les modules utilisateur de gomgo
  représentaient d'abord les listes comme des tranches plates. Elles sont
  maintenant des cellules `Cons`/`Empty` partagées au maximum, comme en
  Java Gom. Benchmarks sur 10 000 éléments (`gom/backend/testdata/listcells`,
  rapport de lot) :

  | Benchmark | Avant | Après |
  |---|---|---|
  | `Cons` répété | 429,6 ms/op, 1 051 Mo/op | 0,49 ms/op, 0,54 Mo/op |
  | parcours par `GetTail` | 393,0 ms/op | 0,022 ms/op, 0 allocation |

  `TestConsIsLinear` échoue sur l'ancien générateur. `acmatching/TestBool3.t`
  passe de 12,4 s à 2,9 s d'exécution (1,2 s en Java).
- **Bootstrap : faux négatif dû à un résidu.** Lors de la promotion, le
  premier `./build.sh bootstrap` n'a pas affiché « BOOTSTRAP PERFORM
  SUCCESSFULLY ». Les classes ne différaient que par la numérotation
  `tomMatchN`. La cause : `performbootstrap` ne vide pas `src/bootstrap`
  avant la première étape. Un `src/bootstrap/gen` resté d'une exécution
  antérieure rendait donc cette étape incrémentale, avec une
  numérotation par fichier. Après suppression de `src/bootstrap` et
  `src/bootcheck`, le point fixe réussit.
- **Différences connues avec Java.** Elles sont détaillées dans
  `doc/go/README.md`, sections « Différences connues avec Java » et
  « Limites connues de `-go` » :
  - ordre des éléments AC/ACU, qui dépend des fonctions de hachage
    (affichage, `ToATerm`, `ToArray`, `CompareTo`, ordre d'énumération
    des solutions AC ; l'égalité et l'ensemble des solutions sont les
    mêmes) ;
  - modules compilés par `gomgo gom-batch` : listes en tranches plates,
    sans API Java ni théories utilisateur ;
  - hooks utilisateur installés par le mapping, donc exécutés seulement
    dans un paquetage qui l'inclut ;
  - théorie exécutée avant les hooks `make_insert`/`make_empty` et les
    règles, et refus d'une théorie déclarée après eux ;
  - aides du filtrage AC émises au niveau du paquetage ;
  - exceptions Java devenues des paniques `*aterm.GomError` ;
  - règles étiquetées non reconnues par `-np` ;
  - accolades du code hôte gardées comme texte, et `_ = s` pour les
    sujets non liés.

## 4. Points ouverts pour J3/J4

- **ADT du compilateur en tranches plates.** L'ADT engine est généré par
  `gom-batch`, donc ses listes restent des tranches plates. Dans le
  mapping, `get_tail` et `make_insert` copient la liste (O(n)), et le
  filtrage de listes devient quadratique. Le passage aux cellules est une
  décision de J4 : soit par la voie des modules utilisateur, quand les
  corps de hooks seront en Tom+Go, soit en réexprimant les hooks de
  `knownHookTable` au niveau des cellules.
- **La fabrique ne libère jamais la mémoire.** `sharedobjects.Factory`
  garde tous les termes, alors que Java utilise des références faibles.
  C'est l'essentiel de l'écart de performance restant : `TestBool3` prend
  2,9 s en Go contre 1,2 s en Java, et le profil est dominé par le
  ramasse-miettes et la table de la fabrique. Piste : le paquetage `weak`
  de Go.
- **Graphes de termes non pris en charge.** `--termpointer` et
  `--termgraph` (gomgo), ainsi que les stratégies de `sl/graph.tom`,
  bloquent `sl/TestRef.t` et `gom/TestTermgraph.t`.
- **Règles étiquetées.** Elles ne sont pas prises en charge par `-np`,
  donc pas par `-go` (`TomIslandParser.g4` n'a pas de règle étiquetée).
- **Aides AC de niveau paquetage.** Deux `.t` d'un même paquetage Go qui
  utilisent le filtrage AC redéclareraient `next_minimal_extract`, etc.
  C'est à régler avant que le compilateur en Tom+Go ne compte plusieurs
  fichiers par paquetage.
- **Divers.**
  - Le Go généré est plus bruité qu'avant (`{ ; }`, `; ` après chaque
    action).
  - L'heuristique de `OutputCode.goSingleLine` ne voit ni les
    commentaires `//` ni les chaînes brutes multi-lignes.
  - Les originaux qui « échouent dans legacy » (`%rule`, typer) ne sont
    pas portables sans corriger `legacy/`.
- **Écart minime dans `stable/`.** `stable/` a été promu avant le commit
  `a6582a618` : son `Compiler.java` garde la boucle `seen` linéaire au
  lieu du `HashSet`. Le comportement est identique, et le prochain
  `bootinstall` alignera les deux.

## 5. Tout relancer

Prérequis : JDK 8, Go ≥ 1.26, `mage`, `legacy/src/dist` construit
(`cd legacy && ./build.sh stable && ./build.sh src.dist`). `cc` ne sert
qu'avec `-update-oracle` (recalcul des oracles C de `cfib1.t` et
`loulou.t`) : T2 lit sinon l'oracle C mis en cache dans
`test/go/testdata/oracle/`. Les cibles `mage` posent
`TOMGO_REQUIRE_TOOLS=1` : un outil manquant (java, javac, cc, une dist)
fait échouer au lieu de sauter, et une `src/dist` plus ancienne que les
sources du compilateur échoue toujours (« rebuild ./build.sh src.dist »).

```bash
unset TOM_HOME
./build.sh src.dist                               # compilateur de src/
mage -d magefiles -w . all                        # vet, tests unitaires, auto-amorce, smoke (dont TestJavaTwins)
mage -d magefiles -w . portJava                   # T0
mage -d magefiles -w . portGo                     # T1 + T2, réécrit doc/go/port-dashboard.md
mage -d magefiles -w . coverage                   # T3, réécrit doc/go/coverage.md
# oracles et goldens (écrits dans test/go/testdata, lancés depuis test/tools) :
(cd test/tools && go test ./parity/ -run TestSameBehaviourAsLegacy -args -update-oracle)
(cd test/tools && go test ./parity/ -run TestGoBackend -args -update)
```

Promotion dans `stable/` (faite le 2026-10-23, commit `d407d2e83`) :

```bash
unset TOM_HOME
rm -rf src/bootstrap src/bootcheck    # sinon l'étape 1 du bootstrap est incrémentale
./build.sh bootstrap                  # BOOTSTRAP PERFORM SUCCESSFULLY (1 min 38 s)
./build.sh clean.src && ./build.sh src.dist   # src/gen complet avant bootinstall
./build.sh bootinstall
./build.sh clean.stable && ./build.sh stable  # 237 sources Java
./build.sh clean.src && ./build.sh src.dist
```

Résultat de la promotion :
- 78 fichiers modifiés sous `stable/` : 67 `.java`, 8 `.jar`, 3 mappings ;
- 11 mappings Go nouveaux (`aterm`, `atermlist`, `char`, `double`, `float`, `intarray`, `util/…`), identiques à ceux de `src/` ;
- les `.jar` sont réécrits par `bootinstall`, à taille égale, comme lors de la promotion précédente `3764cb591` ;
- `stable/dist/lib/go/tom` est identique à `src/go/tom`, hors fichiers de test.

Vérification « utilisateur » :
- `test/go/TestAlgebraic.t` compilé par `stable/dist/bin/tom -go`, avec un gomgo construit depuis `src/go/tom` ;
- `go test` (`GOWORK=off`, `replace tom => stable/dist/lib/go/tom`) donne `ok`.

Après la promotion, `mage all`, `portGo` (239 s) et `portJava` (243 s)
sont verts.
