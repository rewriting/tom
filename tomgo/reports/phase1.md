# Phase 1 — Rapport de campagne

## Objectif

Constituer un corpus stable de `.gom` sans hooks pour servir de référence
au futur parser et générateur Gom de la Phase 2. Pas de touche aux `.t` ni
au pipeline TOM.

## Décisions de scope

- Hôte cible : Go uniquement (pas de portage `.t` cette itération).
- Périmètre du corpus : **strict — uniquement les 10 fichiers `test/gom/*.gom`
  sans hooks**. Pas d'extension à `examples/`.
- Stockage : copie des fichiers dans `tomgo/testdata/corpus/gom-nohooks/`
  (pas de symlink).

## Métriques (preuves)

### Inventaire global (cf. `phase1-inventory.md`)

| Catégorie       | Nombre |
| --------------- | ------ |
| Total `.gom`    | 176    |
| Sans hooks      | 128    |
| Avec hooks      | 48     |

Commande de reproduction :
```bash
cd tomgo
go run ./cmd/tomgo scan-hooks ..                     # totaux
go run ./cmd/tomgo scan-hooks --list-no-hooks ..     # liste détaillée
```

### `test/gom/` (zone d'intérêt Phase 1)

| Catégorie       | Nombre |
| --------------- | ------ |
| Total           | 16     |
| Sans hooks      | 10     |
| Avec hooks      | 6      |

### Corpus retenu (10 fichiers)

Copiés dans `tomgo/testdata/corpus/gom-nohooks/` à partir de `test/gom/` :

| Fichier             | Module Gom        |
| ------------------- | ----------------- |
| `Builtin.gom`       | `gom.b.u.i.l.t.i.n.Builtin` |
| `Dotted.gom`        | `Dotted` (à vérifier) |
| `Imported.gom`      | `Imported`        |
| `Importing.gom`     | `Importing`       |
| `Leaf.gom`          | `Leaf`            |
| `List.gom`          | `List`            |
| `Minimal.gom`       | `Minimal`         |
| `Yang.gom`          | `Yang`            |
| `Ying.gom`          | `Ying`            |
| `fromterm/foo.gom`  | (à vérifier)      |

Vérification post-copie :
```text
$ go run ./cmd/tomgo scan-hooks testdata/corpus/gom-nohooks
Scanned 10 .gom file(s) under testdata/corpus/gom-nohooks
  without hooks: 10
  with hooks:    0
```

## Outillage produit

| Fichier                                       | Rôle                                          |
| --------------------------------------------- | --------------------------------------------- |
| `internal/gom/hookscanner.go`                 | API `HasHookContent`, `HasHookFile`, `ScanDir`, `HookReport` |
| `internal/gom/hookscanner_test.go`            | 6 tests unitaires (Go, scoped, slot field, comment, walk) |
| `cmd/tomgo/main.go` — sous-commande `scan-hooks` | CLI : `tomgo scan-hooks [--list-no-hooks\|--list-with-hooks] <dir>` |

Tests :
```text
$ go test ./...
ok    tom/tomgo/cmd/tomgo
ok    tom/tomgo/internal/gom
?     tom/tomgo/internal/backend                 [no test files]
?     tom/tomgo/library/sharedobjects            [no test files]
```

## Limites connues

- La détection de hooks est une **heuristique de scan** (regexp), pas un
  vrai parser Gom. Elle reproduit fidèlement la grammaire ANTLR au niveau
  d'une ligne logique, mais elle ne déroule pas les commentaires `/* … */`
  multi-lignes : un (très improbable) hook commenté en bloc serait
  faussement détecté. Sur le corpus actuel : 0 occurrence identifiée.
- Les chemins contenant des espaces (ex. `applications/prototype3D/Projet 3A/`)
  sont correctement traversés par le scanner Go (contrairement au script shell
  de débroussaillage initial qui en avait perdu un).

## Non-fait (et pourquoi)

- Pas de portage des `.gom` **avec hooks** (48 fichiers) : reporté à une
  future sous-phase 2b une fois la fondation `.gom → .go` sans hook stable.
- Pas de portage des `.t` : hors scope de l'itération.

## Critère de sortie — atteint ✅

- [x] Outil `tomgo scan-hooks` opérationnel et testé unitairement.
- [x] Inventaire global produit dans `reports/phase1-inventory.md` (128/48/176).
- [x] Corpus de 10 `.gom` (sans hooks) déposé dans
      `testdata/corpus/gom-nohooks/`, ré-validé à 10/0.
