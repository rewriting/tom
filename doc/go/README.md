# Cible Go de Tom — où sont les choses

| Quoi | Où | Statut |
|---|---|---|
| Compilateur Tom+Java d'origine (humain) | `legacy/` | témoin, gelé |
| Compilateur Tom+Java étendu (`-go`) | `src/tom/`, promu dans `stable/` | validé |
| Runtime Go `sl`, `sharedobjects` | `src/go/tom/library/` (module `tom`) | validé |
| Runtime Go `aterm` (lecture/écriture ATerm) | `src/go/tom/library/aterm` (module `tom`) | validé |
| Gom écrit en Go (`gomgo`) | `src/go/tom/gom/` | validé |
| ADT du compilateur Go : sources `.gom` (hooks en Go) / amorce générée | `src/go/tom/{gom,engine}/adt` / `stable/go/tom/{gom,engine}/adt` | validé |
| Mappings Go | `src/tom/library/mapping/go/` | validé |
| Tests Tom+Go (ports de `test/`), T0–T3, smoke | `test/go/` (module `tomtest`) | validé |
| Rapports | `doc/go/` : J0–T3 [`j0-go-backend.md`](j0-go-backend.md), J2 [`j2-report.md`](j2-report.md), [tableau de bord](port-dashboard.md), [couverture](coverage.md) | à jour |
| Prototype tomgo (compilateur Go écrit à la main) | `prototype/tomgo/` | gelé, réservoir de code |

Utiliser le runtime dans un projet : `require tom v0.0.0` et
`replace tom => $TOM_HOME/lib/go/tom` dans `go.mod`, puis
`import sl "tom/library/sl"`.

## Démarrage rapide

```bash
unset TOM_HOME && ./build.sh src.dist
mage -d magefiles -w . bootstrap   # génère les ADT de src/go/tom (voir « Sources et amorce »)
go -C src/go/tom build -o /tmp/gomgo ./gom/cmd/gomgo
GOMGO_BIN=/tmp/gomgo TOM_HOME=$PWD/src/dist src/dist/bin/tom -go -d out X.t
```

Puis dans `out/`, un `go.mod` avec `go 1.26` (Go ≥ 1.26 requis),
`require tom v0.0.0` et `replace tom => $TOM_HOME/lib/go/tom`.

Orchestration : `go.work` à la racine (modules `src/go/tom`, `test/go`,
`magefiles`, `prototype/tomgo`) et `magefiles/` ; cibles `mage -d magefiles
-w . <cible>` (ou `cd magefiles && mage <cible>`), cf. `mage -d magefiles
-w . -l`.
Les cibles `portGo`, `portJava`, `smoke` et `coverage` (et `-write-dashboard`)
posent `TOMGO_REQUIRE_TOOLS=1` : un outil absent (java, javac, cc, une dist)
fait échouer le test au lieu de le sauter ; une `src/dist` plus ancienne
que `src/tom/**/*.{t,java,tom,config}` ou `src/tom/library/mapping/**` échoue toujours.

## Différences connues avec Java

Termes générés par gomgo (modules utilisateur : blocs `%gom` et `.gom`
autonomes), comparés à ceux de Java Gom (`legacy/`) :

- **Ordre des éléments AC/ACU.** Un opérateur `op:AC()` ou `op:ACU()`
  garde ses éléments triés par `compareTo`, qui compare d'abord les valeurs de hachage.
  Celles de Go (`sharedobjects` : un « one-at-a-time » sur le nom de
  l'opérateur et les hachages des enfants) ne sont pas celles de Java
  (mélange lookup2 de Bob Jenkins dans `hashFunction()`, graine calculée
  par Gom). L'ordre des éléments diffère donc : affichage (`String()`),
  `ToATerm`, `ToArray`/`GetHead…`, `CompareTo`, et ordre d'énumération
  des solutions d'un filtrage AC. L'égalité des termes et l'ensemble des
  solutions sont les mêmes (test/gom/TestACU.t, test/acmatching).
- **Coût des listes.** Comme en Java, une liste d'un opérateur variadique
  (de tout module compilé par gomgo) est une chaîne de cellules `Cons<op>(tête, queue)` / `Empty<op>()`
  partagées au maximum (`gom/backend/cells.go`) : `Cons<Op>`,
  `GetTail<Op>`, `IsEmpty<Op>` sont en O(1), les queues sont partagées, et
  une liste peut finir par une queue qui n'est pas une liste (par ex.
  `lm(a(),m0())`, résultat d'un hook `make_empty`). La fabrique de
  `sharedobjects` garde cependant tous les termes construits (pas de
  références faibles comme celle de Java) : un programme qui construit
  beaucoup de termes passe plus de temps dans le ramasse-miettes
  (test/acmatching/TestBool3.t : 2,9 s en Go, 1,2 s en Java ; 12,4 s avec
  l'ancienne représentation en tranches plates).
- **Hooks dans le mapping.** Les hooks `make`, `make_insert`,
  `make_empty`, les règles (`module M:rules()`) et les blocs (`block`)
  sont du code Tom+Go : gomgo les écrit dans le mapping généré (`.tom`),
  qui les installe à l'initialisation du paquetage qui l'inclut. Du code
  Go qui construit des termes sans inclure le mapping (paquetage qui ne
  l'inclut pas, `Make<Op>` appelé hors d'un programme Tom) ne les exécute
  pas ; les théories (FL, AU, ACU, Free) sont, elles, générées en Go et
  toujours appliquées. Avec l'option `--gohooks` de gomgo (propre à
  gomgo : `%gom(--gohooks)`, `gom-batch --gohooks`), les hooks sont du Go
  pur, sans code Tom, écrits dans le fichier Go généré : ils s'exécutent
  sans Tom ; les règles (traduites en code Tom) sont alors refusées, et
  une méthode qu'un `block` de sorte déclare sur chaque alternative de
  la sorte est ajoutée à l'interface de la sorte (Java : méthode de la
  classe de la sorte). C'est le mode des ADT du compilateur.
- **Ordre des hooks d'un opérateur variadique.** Java Gom les exécute
  dans l'ordre de déclaration ; Go exécute la théorie avant les hooks
  `make_insert`/`make_empty` et les règles, et refuse un module qui
  déclare la théorie après eux.
- **Aides du filtrage AC.** Les fonctions que le compilateur ajoute pour
  un motif `f??(…)` (`next_minimal_extract`, `__getMultiplicities_<op>`,
  `__getTermForMult_<op>`, `__computeLength_<op>`, …) sont, en Java, des
  méthodes de la classe générée ; en Go, ce sont des fonctions de niveau
  paquetage, émises dans chaque fichier généré qui en a besoin. Deux
  fichiers `.t` d'un même paquetage Go qui utilisent le filtrage AC les
  déclareraient donc deux fois (refusé par Go) ; à régler en J4 (comme
  les `tom_*` des mappings, qui imposent déjà un fichier Tom par paquetage).
- **Code hôte d'un `%strategy`.** Dans une action de `visit`, le code hôte
  Java lit un paramètre de la stratégie comme un champ (`param`) et
  appelle `getEnvironment()` ; en Go, il doit écrire
  `tom__strat.<param>` et `tom__strat.GetEnvironment()` (`tom__strat` est
  le récepteur de la méthode générée). C'est une limite du compilateur,
  qui ne réécrit pas ces noms dans le code hôte ; les ports le signalent
  par une note `// port:` (par ex. `test/go/sl/TestGetSubterm.t`).
- **Exceptions.** Les exceptions Java de l'API Gom sont des paniques
  `*aterm.GomError` (`IllegalArgument` vrai pour une
  `IllegalArgumentException` : `Length`/`Reverse` d'un terme qui n'est pas
  une liste, ATerm qu'aucun opérateur ne convertit). Les enfants
  `Visitable` d'une liste (`ChildCount`/`ChildAt`/`Children`) dont la
  dernière queue n'est pas une liste et n'est pas de la sorte des éléments
  (`conc(a(),nil())`, `lm(a(),m0())` d'un hook `make_empty`) paniquent
  ainsi, comme le `toArray` typé de Java lève `ArrayStoreException`.

## Sources et amorce

Comme pour Java (`stable/` contient le code Java qui amorce le
compilateur), `src/` ne contient que des sources et `stable/go/tom`
l'amorce Go :

- `src/go/tom/gom/adt/*.gom` (AST de Gom, sur lequel gomgo est
  construit) et `src/go/tom/engine/adt/*.gom` (AST de Tom, les 15
  modules dans un paquetage) sont les copies Go des `.gom` des ADT du
  compilateur Java : mêmes sortes, opérateurs et champs que les
  originaux de `src/tom` (`TestSignatureParity`), hooks réécrits en Go
  (les règles des originaux deviennent des hooks `make`) ; les AST Go
  peuvent ainsi évoluer indépendamment de ceux de Java. Seuls ces
  `.gom` sont commités dans ces répertoires ; le code Go et le mapping
  `adt.tom` générés y sont ignorés par git.
- `stable/go/tom` est une copie autonome complète du module `tom` (Go
  non-test de `library/` et `gom/`, `gom/adt` généré compris), hors de
  `go.work`, construite par `GOWORK=off go build ./...`.
- `mage -d magefiles -w . bootstrap` construit gomgo depuis l'amorce,
  génère avec lui les deux ADT depuis leurs `.gom` dans `src/go/tom`
  (`gomgo gom-batch --gohooks`), construit gomgo depuis `src/go/tom` sur
  ce code et régénère les ADT avec lui : les deux générations doivent
  être identiques octet pour octet (point fixe : le code généré est mis
  à l'épreuve dans gomgo lui-même). Toutes les cibles mage en dépendent :
  `git clone` puis `mage -d magefiles -w . all` suffit.
- Après une évolution voulue de gomgo ou des `.gom` qui change le code
  généré : `mage -d magefiles -w . promoteGo` (vérifie le point fixe de
  `src/go/tom`, le copie dans `stable/go/tom`, vérifie que l'amorce se
  construit seule et régénère son `gom/adt` à l'identique), puis
  `bootstrap`. `TestSelfBootstrap` (`gom/backend`) compare la
  régénération au code généré par l'amorce ; les tests de comportement
  des ADT sont dans `test/go/adt`.
- La dist (`<dist>/lib/go/tom`) ne contient que le runtime
  (`library/**`, `go.mod`) ; gomgo se construit depuis `src/go/tom`.

## Limites connues de `-go` (parser îlot `-np`)

- **Règles étiquetées** (`l: motif -> action`, `break l`) : seul le parser
  par défaut (tomjava, `CstBuilder.exitLabelledpattern`) les reconnaît ;
  la grammaire îlot (`TomIslandParser.g4`) n'a pas de règle étiquetée, et
  `-go` impose `-np` : le `%match` reste du code hôte. Aucun test de
  `test/` ne les utilise, ni le compilateur (T3 : 0 utilisation, les
  exemples en commentaire de `GeneralPurposePropagator.t` ne comptent pas).
- **Accolades du code hôte** : sous `-go`, les `{ … }` du code hôte d'une
  action (que `-np` analyse comme des blocs) restent du texte hôte
  (`AstBuilder`), et le compilateur écrit `_ = s` (une fois par nom) pour
  chaque sujet variable d'un `%match` qui n'est pas lié par un motif de la
  même règle, c'est-à-dire une variable du code hôte ou d'une action
  englobante (une telle variable que les motifs n'inspectent pas
  resterait inutilisée, ce que Go refuse).

## Orientation pour la suite

- **J4 — compilateur en Tom+Go, stratégie hybride « de la fin vers le
  début » (décidée le 2026-10-23).** On écrit le nouveau compilateur en
  Tom+Go à côté du compilateur Tom+Java, en remplaçant les phases **en
  partant de la dernière** :
  - le compilateur Tom+Java exécute les phases 1…k, puis exporte **une
    fois** le terme de travail **et** la table des symboles (formats
    aterm déjà produits par `--intermediate` : `.tfix.*` et `.table` ;
    vérifier d'abord que la table contient tout ce que le backend
    consulte) ; le pont ne va que de Java vers Go ;
  - un « suffixe » écrit en Tom+Go (compilé par la chaîne `-go`)
    exécute les phases k+1…fin et produit le code ;
  - premier jalon : le backend Go écrit en Tom+Go, branché après
    l'optimizer, dont la sortie doit être identique octet pour octet à
    celle de `GoGenerator` (goldens T1 : ports et cas smoke) ; en option
    le backend Java, comparé à `legacy/` (T0) ;
  - puis on recule k phase par phase (optimizer, compilateur de motifs,
    expander, typer, desugarer…) ; le **parser vient en dernier**
    (reprise du parser Go du prototype, complété) ; l'orchestration Go
    se construit au fil des étapes ; on termine par le point fixe (le
    compilateur Tom+Go se compile lui-même).
  Un spec J4 dédié sera rédigé avant de commencer.
- **Règle d'équivalence pour J4.** Identité stricte (octet pour octet)
  pour toutes les étapes qui partent de l'AST exporté par Java, c'est-à-
  dire toutes sauf le parser. À l'étape du parser, viser l'identité ; si
  des écarts portant **uniquement** sur les positions (ligne, colonne,
  fichier de l'`OriginTracking`) résistent, les accepter explicitement et
  étroitement : comparateur d'AST qui masque seulement ces champs,
  comparaison du code généré qui ignore seulement les retours à la ligne
  de remplissage, diagnostics comparés par nom (mode `Regress`) ; chaque
  tolérance est un test séparé avec un compteur visible, et la ligne
  d'une erreur doit continuer à désigner le bon endroit du `.t`.
- **Points à régler au début de J4.**
  - Le pont doit exporter l'AST produit **avec `-go`** : les branches
    propres à Go (`_ = s` des sujets non liés, accolades des `ITL`,
    `LocalIntrospector` Go, `next_minimal_extract` Go) sont déjà dans
    l'AST avant le backend ; les phases portées plus tard devront
    reproduire `collectHostSubjects` et la règle des accolades.
  - Décider comment plusieurs `.t` partagent un paquetage Go : les aides
    `tom_*` des mappings et les aides AC de niveau paquetage imposent
    aujourd'hui un `.t` par paquetage.
  - Le mapping de l'ADT du moteur (`adt.tom`, généré, voir « Sources
    et amorce ») est à inclure par le compilateur Tom+Go.
  - Fabrique `sharedobjects` : références faibles (paquetage Go `weak`)
    et, peut-être, découpage (sharding) du mutex global.
  - Vérifier que la `.table` porte le code hôte Go des mappings générés
    par gomgo.
- **Reporté :** `src/tom` → `src/java/tom` (et `stable/java`) ; à faire
  seulement quand un compilateur Tom+Go fonctionnera.
