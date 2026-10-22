# Cible Go de Tom — où sont les choses

| Quoi | Où | Statut |
|---|---|---|
| Compilateur Tom+Java d'origine (humain) | `legacy/` | témoin, gelé |
| Compilateur Tom+Java étendu (`-go`) | `src/tom/`, promu dans `stable/` | validé |
| Runtime Go `sl`, `sharedobjects` | `src/go/tom/library/` (module `tom`) | validé |
| Runtime Go `aterm` (lecture/écriture ATerm) | `src/go/tom/library/aterm` (module `tom`) | validé |
| Gom écrit en Go (`gomgo`) | `src/go/tom/gom/` | validé |
| Mappings Go | `src/tom/library/mapping/go/` | validé |
| Tests Tom+Go (ports de `test/`), T0–T3, smoke | `test/go/` (module `tomtest`) | validé |
| Rapports | `doc/go/` | à jour |
| Prototype tomgo (compilateur Go écrit à la main) | `prototype/tomgo/` | gelé, réservoir de code |

Utiliser le runtime dans un projet : `require tom v0.0.0` et
`replace tom => $TOM_HOME/lib/go/tom` dans `go.mod`, puis
`import sl "tom/library/sl"`.

## Démarrage rapide

```bash
unset TOM_HOME && ./build.sh src.dist
go -C src/go/tom build -o /tmp/gomgo ./gom/cmd/gomgo
GOMGO_BIN=/tmp/gomgo TOM_HOME=$PWD/src/dist src/dist/bin/tom -go -d out X.t
```

Puis dans `out/`, un `go.mod` avec `go 1.26` (Go ≥ 1.26 requis),
`require tom v0.0.0` et `replace tom => $TOM_HOME/lib/go/tom`.

Orchestration : `go.work` à la racine (modules `src/go/tom`, `test/go`,
`magefiles`, `prototype/tomgo`) et `magefiles/` ; cibles `mage -d magefiles
-w . <cible>` (ou `cd magefiles && mage <cible>`), cf. `mage -d magefiles
-w . -l`.

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
- **Queue d'une liste qui n'est pas une liste.** Java Gom construit
  `Cons<op>(e, x)` avec `x` de la sorte de la liste sans être une liste
  (par ex. le résultat d'un hook `make_empty` : `lm(a(),m0())`). Le terme
  Go d'un opérateur variadique est une tranche plate de ses éléments :
  quand la sorte des éléments diffère de celle de la liste, un tel terme
  n'est pas représentable et `Cons<Op>` panique avec un `*aterm.GomError`
  (« … is not a … list (not representable in Go …) »). (Quand les sortes
  sont égales, cas des théories AU/ACU, `x` est le dernier élément.)
- **Coût des listes.** Une liste Go est une tranche plate : `Cons<Op>`
  copie la queue (O(n), contre O(1) pour une cellule Java). Les hooks FL/AC
  évitent les copies inutiles sans changer le résultat, mais un programme
  qui construit beaucoup de grandes listes reste plus lent qu'en Java
  (test/acmatching/TestBool3.t : 12 s en Go, 1,2 s en Java).
- **Hooks dans le mapping.** Les hooks `make`, `make_insert`,
  `make_empty`, les règles (`module M:rules()`) et les blocs (`block`)
  sont du code Tom+Go : gomgo les écrit dans le mapping généré (`.tom`),
  qui les installe à l'initialisation du paquetage qui l'inclut. Du code
  Go qui construit des termes sans inclure le mapping (paquetage qui ne
  l'inclut pas, `Make<Op>` appelé hors d'un programme Tom) ne les exécute
  pas ; les théories (FL, AU, ACU, Free) sont, elles, générées en Go et
  toujours appliquées.
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
- **Exceptions.** Les exceptions Java de l'API Gom sont des paniques
  `*aterm.GomError` (`IllegalArgument` vrai pour une
  `IllegalArgumentException` : `Length`/`Reverse` d'un terme qui n'est pas
  une liste, ATerm qu'aucun opérateur ne convertit).

## Orientation pour la suite

- **J4 — compilateur en Tom+Go, stratégie hybride (décidée le 2026-10-23).**
  On remplace les phases du compilateur Tom+Java une par une par leur
  version Go, dans la chaîne Java qui marche. Un pont Java ⇄ Go fait
  passer le terme de travail **et** la table des symboles (formats aterm
  déjà produits par `--intermediate` : `.tfix.*` et `.table`) ; un plugin
  « externe » de la plateforme Java lance la phase Go ; une option choisit
  Java ou Go phase par phase. Ordre : (0) pont seul avec une phase
  identité, (1) desugarer, (2) parser (reprise du parser Go du
  prototype, complété), (3) typer, expander, compilateur de motifs,
  optimizer, backend, (4) orchestration Go et point fixe. Chaque étape
  est validée par T0 (sortie Java identique à `legacy/`) et T2. Un spec
  J4 dédié sera rédigé avant de commencer.
- **Reporté :** `src/tom` → `src/java/tom` (et `stable/java`) ; à faire
  seulement quand un compilateur Tom+Go fonctionnera.
