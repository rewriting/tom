# Backend Go pour le compilateur Tom+Java — design

Date : 2026-10-22
Branche : `tom-go`
Statut : validé en brainstorming, en attente de relecture du spec

## 1. Contexte et objectif

Objectif long terme : un compilateur Tom écrit en **Tom+Go**, compilé par
lui-même (point fixe), qui compile du Tom+Go vers du Go.

Le dépôt contient déjà `tomgo/`, un compilateur Tom écrit à la main en Go
pur (~26k LOC hors AST générés) : parser, typer simplifié, compilateur de
motifs, `backend_go`, plus un générateur Gom→Go, `sharedobjects`, `sl`,
`aterm`. Sa correction n'est pas établie (typer simplifié, expander vide,
11 fixtures de bout en bout seulement).

**Décision** : construire la chaîne Tom+Go → Go **dans le compilateur
Tom+Java existant** (bootstrap croisé classique) plutôt que de continuer à
étendre tomgo. Le front-end et le middle-end Java sont éprouvés ; seul le
backend Go et le branchement Gom sont nouveaux. Cette chaîne, validée sur
le corpus de tests porté en Tom+Go, servira d'amorce pour réécrire le
compilateur en Tom+Go.

Décisions prises :

1. Le Tom Java **peut être modifié**.
2. **Gom reste écrit en Go** (le générateur de tomgo). Le Tom Java lui
   délègue les blocs `%gom` quand la cible est Go. Gom n'est pas réécrit
   en Tom+Go dans ce projet.
3. **tomgo est conservé** ; ses parties réutilisables (parser Go, Gom,
   `sharedobjects`, `sl`, mappings Go, et `backend_go` comme référence de
   conception) seront reprises, notamment au bootstrap.
4. Le compilateur Tom+Java d'origine est **copié intact dans `legacy/`**
   avant toute modification ; il sert de référence et d'oracle.

## 2. Jalons

| Jalon | Contenu | Critère de sortie |
|---|---|---|
| **J−1** | Snapshot `legacy/` | `cd legacy && ./build.sh stable` réussit ; commit dédié |
| **J0** | Cible Go dans Tom Java (§3) | `tom --go` produit du Go ; T0 vert |
| **J1** | Les 11 fixtures Tom+Go de `tomgo/tests/tom/compile/fixtures/go/` passent via la chaîne Java | T1 vert sur ces 11 fixtures |
| **J2** | Portage de `test/` en Tom+Go | Tableau de bord T2 ; chaque cas porté au statut OK |
| **J3** | Couverture des constructions utilisées par le compilateur | Checklist T3 entièrement cochée |
| **J4** | Bootstrap : compilateur réécrit en Tom+Go | stage1 (compilé par Tom Java `--go`) et stage2 (compilé par stage1) produisent un `.go` identique sur le compilateur lui-même |

J4 fera l'objet d'un spec séparé, rédigé à l'issue de J3. Ce document
détaille J−1 à J3.

## 3. Composants

### 3.1 J−1 — snapshot `legacy/`

`legacy/` est le **témoin du travail humain** : il contient la version
écrite à 100 % par des humains, la racine contenant la version étendue
par l'IA pour la cible Go.

- Source : commit `2918e95c7`, pointe de `v3` (dernier commit humain,
  29/08/2019) — **pas** `HEAD` : la branche `tom-go` a modifié
  `build.xml` et les `tom-common.xml` (`fork=yes`, commit `d1d8e5f38`).
- Contenu : `git archive 2918e95c7` de `src/`, `stable/`, `share/`,
  `utils/` (modèles de lanceurs), `build.sh`, `build.xml`,
  `local.properties_template`, `README`, `NEWS`, `AUTHORS`, `LICENCE`.
- Construction : JDK 8, `unset TOM_HOME`, `./build.sh stable` puis
  `./build.sh src.dist`. Aucune modification n'est autorisée pour la
  faire fonctionner ; un échec est rapporté à l'utilisateur.
- `legacy/README.md` (en anglais) : procédure d'installation complète
  pour un utilisateur GitHub (prérequis, construction, `TOM_HOME`/`PATH`,
  vérification sur un exemple). Le `README.md` racine renvoie vers elle.
- Après ce commit, `legacy/` n'est plus jamais modifié.

### 3.2 J0 — sélection de la cible Go

Nouvelle option `goCode` (alias `-go`, à utiliser avec `-np`, le parser îlot ANTLR4), calquée sur `pCode`/`aCode` :

| Fichier (`src/tom/engine/…`) | Changement |
|---|---|
| `backend/BackendPlugin.t` (l. 82-134) | déclarer `goCode` ; l'activer désactive `jCode` ; instancier `GoGenerator` |
| `TomStreamManager.java` (l. 124) | suffixe de sortie `.go` ; sous-répertoire de mappings `go` |
| `tools/SymbolTable.t` (l. 84) | types builtin Go (`int`, `string`, `bool`, …) ; `int` aligné sur le mapping Go existant (`int64`) |
| `tools/OutputCode.java` | indentation comme Java (pas le mode Python) |
| `parser/TomParserPlugin.t` (l. 120) | examiner les usages de `jCode` et y traiter `goCode` comme Java lorsque c'est pertinent |

### 3.3 J0 — `backend/GoGenerator.t`

Nouvelle classe héritant de `GenericGenerator`, sur le modèle de
`JavaGenerator.t`. Elle reprend les solutions déjà trouvées dans
`tomgo/stable/tom/backend_go` (la conception, pas le code) :

- déclarations `var x T = …` suivies de `_ = x` (Go refuse les variables
  inutilisées) ;
- pas d'opérateur ternaire : expressions conditionnelles via des
  fonctions anonymes ;
- labels et `break`/`continue` étiquetés ; aucun `goto` qui sauterait
  une déclaration ;
- `%strategy` → `struct` qui embarque la base `sl` (équivalent de
  `AbstractStrategyBasic`), avec méthodes `VisitLight`/`Visit` ;
- fonctions `tom_is_fsym_*`, `tom_make_*`, `tom_get_slot_*`, etc. émises
  comme fonctions Go.

Le `package` et les `import` du fichier produit viennent du code hôte
écrit par l'utilisateur ; le générateur ne les synthétise pas.

### 3.4 J0 — expander

`expander/ExpanderPlugin.t` est le seul composant du middle-end qui
contient du code cible en dur (34 `ITL("…")`, avec une branche Ada
existante ligne 596). Changements :

- **obligatoire** : branche Go pour les `visitLight`/`visit` générés
  pour `%strategy` ;
- **conditionnel** : branche Go pour la génération d'introspecteur
  (`-gi`, l. 228-500) seulement si un cas porté l'exige ; sinon, avec
  `--go`, `-gi` produit une erreur `TomMessage` explicite.

Le compilateur et l'optimizer ne contiennent aucun fragment cible ; ils
ne sont pas modifiés.

### 3.5 J0 — délégation Gom

Dans `parser/TomParserTool.t` (l. 231), quand `goCode` est actif, le Tom
Java remplace l'appel réflexif à `tom.gom.Gom.exec` par l'exécution d'un
processus `tomgo gom-block` (nouvelle sous-commande) sur le fichier `.gom`
temporaire ; le package Go est celui de la première ligne `package X` du `.t`.

Contrat :

- entrée : chemin du `.gom` temporaire, répertoire de sortie, option
  demandant l'émission du mapping ;
- sortie : le package Go généré et le mapping `.tom` (code hôte Go)
  écrits sur disque ; le chemin du mapping imprimé sur stdout ;
- ce chemin remplace la valeur `KEY_LAST_GEN_MAPPING` ;
- code de sortie non nul → `TomMessage.gomFailure`, comme aujourd'hui ;
- binaire résolu par `$TOMGO_BIN`, puis `tomgo` dans le `PATH` ;
  introuvable → erreur `TomMessage` explicite.

Côté tomgo, `gom-block` écrit `<module>_gen.go` et le mapping à code hôte
Go produit par `EmitTomMappingGo` (le `tomgo gom` existant produit un
mapping à code hôte Java).

### 3.6 J0 — mappings Go

Les mappings Go (`int`, `boolean`, `string`, `sl.tom`, `Strategy.tom`)
sont copiés depuis `tomgo/stable/library/mapping/go/` vers le
sous-répertoire `go` à côté des mappings `java` que consulte
`TomStreamManager`, puis complétés au fil de J1–J3 (notamment
`Collection.tom`). Le runtime qu'ils supposent (`sl`, `sharedobjects`)
reste dans le module Go `tom/tomgo`, que le code généré importe.

### 3.7 J0 — reconstruction

Le compilateur modifié est reconstruit depuis `src/` (amorcé par
`stable/`) via les cibles de `build.xml` (`src`, `build.stable`), puis
promu dans `stable/`. Le plan d'implémentation fixe la séquence exacte de
commandes.

## 4. Stratégie de test

### T0 — non-régression du backend Java

- Pour les 103 `.t` de `test/` hors `error/` : le `.java` produit par le
  nouveau Tom est identique octet pour octet à celui produit par
  `legacy/`.
- Pour les 18 `.t` de `test/error/` : mêmes messages d'erreur.
- Cible `mage portJava`. Exécutée à chaque reconstruction de `stable/`.

### T1 — sorties Go de référence

- Les 11 fixtures Go existantes, puis un cas minimal par construction
  (`%match` simple, liste, anti-pattern, `%strategy`, …), ont un `.go` de
  référence versionné.
- Tout changement de sortie de `GoGenerator.t` fait échouer le test ;
  la mise à jour est explicite (`-update`).
- Chaque référence doit aussi passer `go build` et `go test`.

### T2 — équivalence comportementale avec `legacy/`

Emplacement : `tomgo/tests/port/`, arborescence miroir de `test/`
(`test/sl/TestSequence.t` → `tomgo/tests/port/sl/TestSequence.t`).

Règles de portage :

- `%match`, `%strategy`, `%gom` et motifs inchangés ; seul le code hôte
  Java est traduit en Go ;
- `testFoo` (JUnit) → `TestFoo` (Go `testing`) ;
- les tests commentés dans l'original restent commentés dans le port.

Harnais, pour chaque cas porté :

1. **Oracle** : `legacy/` compile l'original ; `javac` ; JUnit ;
   l'ensemble des tests réussis est enregistré dans un fichier de cache
   versionné (la CI n'a pas besoin de reconstruire `legacy/`).
2. **Nouveau** : `tom --go` compile le port en `<Name>_test.go` dans un
   répertoire temporaire du module `tom/tomgo` ; `go test -json` ;
   ensemble des tests réussis.
3. **Verdict** : les deux ensembles sont égaux après le renommage
   `testFoo` → `TestFoo`.

Tableau de bord `tomgo/reports/port-dashboard.md` : un statut par cas
(`non porté`, `échec tom`, `échec go build`, `échec go test`,
`divergence`, `OK`) et les totaux. Cible `mage portGo` (T1 + T2).

### T3 — couverture des constructions

- Un script inventorie les constructions Tom utilisées par
  `src/tom/engine` et `src/tom/platform` : `%strategy`, opérateurs
  variadiques, anti-patterns, hooks Gom, `%include` de mappings (`sl`,
  `Collection`, …), contraintes de `%match`, etc.
- Résultat : une checklist dans `tomgo/reports/coverage.md` ; chaque
  construction est rattachée à au moins un cas T2 au statut OK. Une
  construction absente de `test/` est couverte en portant un exemple
  choisi dans `examples/`.
- Cible `mage coverage`.

### Critère d'entrée en J4

Checklist T3 entièrement cochée, T0 vert, et tous les cas T2 portés au
statut OK.

### Environnement

Sans JDK, tout ce qui dépend de Java (T0, oracle T2, construction) est
sauté proprement, comme dans les harnais existants. L'oracle T2 mis en
cache permet d'exécuter le côté Go seul.

## 5. Risques

| Risque | Atténuation |
|---|---|
| Le middle-end Java contient d'autres hypothèses Java que les `ITL` de l'expander (casts, `null`, boxing via `builtinToWrapper`) | les découvrir en J1 sur les 11 fixtures ; traiter chaque cas par une branche `goCode` localisée |
| Le générateur Gom de tomgo a des bugs | T2 l'exerce sur `test/gom/` (28 cas) ; corrections faites dans tomgo |
| Les tests `test/` utilisent des API Java sans équivalent direct | traduction au cas par cas ; un cas intraduisible est marqué et justifié dans le tableau de bord |
| La reconstruction de `stable/` casse le compilateur | `legacy/` reste intact ; T0 détecte toute régression Java |

## 6. Hors périmètre

- Réécriture de Gom en Tom+Go.
- Templates Go dans le Gom Java.
- Backends C/Caml/Ada/Python (inchangés, non testés au-delà de T0).
- Portage exhaustif d'`examples/` : seuls les exemples nécessaires à T3.
- Détail du bootstrap J4 (spec séparé).
