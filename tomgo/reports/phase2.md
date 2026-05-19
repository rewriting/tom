# Phase 2 — Rapport de campagne (jalon final)

## Objectif

Outil Go autonome capable, pour chaque `.gom` (sans hook) du corpus
Phase 1, de :

1. parser le module,
2. générer un package Go correspondant,
3. dont les constructeurs reposent sur un portage Go de **shared-objects**
   (hash-consing, partage maximum des structures de données).

## Périmètre livré

| Sous-étape | Package Go | Contenu |
| ---------- | ---------- | ------- |
| 2.a — shared-objects | `tom/tomgo/library/sharedobjects` | Interface `Term` ; `Factory.Build` (hash-cons thread-safe) ; `Stats` ; mixer `OneAtATime`, `MixSymbol`, `StringHash` |
| 2.b — parser `.gom`  | `tom/tomgo/internal/gom`          | Lexer (`lexer.go`), parser descente récursive (`parser.go`), AST (`ast.go`). Refuse les hooks |
| 2.c — backend `.go`  | `tom/tomgo/internal/backend`      | `Generate`, `GenerateToDir` : un package Go par module, gofmt'd, autosuffisant (go.mod + replace vers `tomgo/`) |
| CLI                   | `tom/tomgo/cmd/tomgo`             | `tomgo gom -o <out-dir> [--pkg <name>] <file.gom>` |

## Métriques (preuves)

### Tests unitaires

```text
$ go test ./...
ok  	tom/tomgo/cmd/tomgo
ok  	tom/tomgo/internal/backend
ok  	tom/tomgo/internal/gom
ok  	tom/tomgo/library/sharedobjects
```

| Package                                    | Tests | Couverture qualitative                                              |
| ------------------------------------------ | ----- | ------------------------------------------------------------------- |
| `library/sharedobjects`                    | 6     | Sharing, distinction de symboles, Contains, Stats, concurrence, hash stable |
| `internal/gom` (scanner + parser)          | 13    | Hooks +/-, scope, slot vs hook, module dotted, imports, variadiques, leading `\|`, corpus complet |
| `internal/backend`                         | 11    | 10 fichiers du corpus passent `go build` ; 1 test de partage end-to-end |

### Chaîne `.gom → .go → go build → go test` (corpus Phase 1)

`TestGenerate_BuildsCorpus` exécute pour chaque `.gom` :

```text
PASS  Builtin
PASS  Dotted
PASS  Imported
PASS  Importing
PASS  Leaf
PASS  List
PASS  Minimal
PASS  Yang
PASS  Ying
PASS  foo (fromterm/foo.gom)
```

Score : **10 / 10**.

### Preuve de partage (TestGenerate_ProvesSharing)

Sur un Peano généré :

```go
z1 := MakeZero(); z2 := MakeZero()
// z1 == z2 (identité de pointeur)

s1 := MakeSuc(MakeSuc(z1))
s2 := MakeSuc(MakeSuc(MakeZero()))
// s1 == s2 (sharing maximal — sous-termes équivalents fusionnés)

Factory().Stats().NumTerms == 3  // zero, suc(zero), suc(suc(zero))
```

## Commandes de reproduction

```bash
cd tomgo
go test ./...

# Génération manuelle d'un fichier
go run ./cmd/tomgo gom -o /tmp/gen-minimal testdata/corpus/gom-nohooks/Minimal.gom
(cd /tmp/gen-minimal && go build ./...)
```

## Mapping de types Gom → Go

| Gom                | Go              |
| ------------------ | --------------- |
| `int`, `long`      | `int64`         |
| `boolean`          | `bool`          |
| `String`           | `string`        |
| `char`             | `rune`          |
| `float`            | `float32`       |
| `double`           | `float64`       |
| `ATerm`/`ATermList`| `any` (V1)      |
| Sort propre        | interface Go générée pour ce sort |
| Import d'autre module | `any` (V1, déféré) |

## Limites connues / non-fait

- **Cross-module references** : les types importés (ex. `imports Leaf`)
  sont rendus en `any` côté Go. Conséquence : un constructeur comme
  `MakeLeafSlot(l any) Inner` accepte n'importe quelle valeur. La
  résolution croisée (vrai typage Go d'un import à un autre package
  généré) est différée à une itération ultérieure.
- **Hooks** : aucun support (par design Phase 2). Les 48 `.gom` avec
  hooks sont écartés du corpus.
- **`ATerm`/`ATermList`** : mappés en `any`. Le portage Go d'`aterm.jar`
  est différé.
- **Égalité physique des valeurs scalaires** : les `int64`/`string`/etc.
  sont comparés par valeur dans `Equivalent`, comme attendu. Le partage
  Go porte sur les termes sort-typés ; le tag scalaire est inclus dans
  le hash via `StringHash(fmt.Sprintf("%v", v))`, ce qui est correct mais
  pas optimal (allocation). À optimiser plus tard.
- **`SharedObjectWithID`, `SingletonSharedObjectFactory`, cleanup à la
  WeakReference** : non portés en V1. Cf. `reports/phase2a-sharedobjects-survey.md`.
- **Pas de `weak.Pointer` (Go ≥ 1.24)** : les termes vivent autant que la
  Factory. Acceptable pour un compilateur en phase d'amorce.

## Critère de sortie — atteint ✅

- [x] `shared-objects` Go : interface, factory, hash-cons thread-safe,
      6 tests verts.
- [x] Parser `.gom` Go (sans hooks) : 100 % du corpus parsé.
- [x] Backend `.gom → .go` : 10/10 du corpus passent `go build`.
- [x] Preuve de partage exécutée sur code généré.
- [x] CLI `tomgo gom -o <dir> <file>` opérationnelle.
- [x] Rapport publié.
