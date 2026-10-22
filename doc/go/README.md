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
