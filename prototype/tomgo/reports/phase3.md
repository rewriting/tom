# Phase 3 — Self-bootstrap de l'AST Gom

## Objectif

Le parser et le backend de `tomgo` cessent d'utiliser les structures Go
écrites à la main dans `internal/gom/ast.go` et utilisent à la place
les **types Go générés** par `tomgo` lui-même à partir des cinq fichiers
`src/tom/gom/adt/*.gom`. C'est une étape d'auto-amorce : tomgo manipule
exactement l'AST que la référence Java manipule, dans des termes
hash-consés via `library/sharedobjects`.

## Pipeline résultant

```
.gom source
  ├─→ internal/gom/parser.go        (lexer + descente récursive)
  │      │
  │      │   Construit directement :
  │      ▼
  │   gomast.GomModule  ────────────  (canonical AST, types issus de
  │                                    src/tom/gom/adt/Gom.gom)
  │
  └─→ internal/backend/backend.go
         (traversée native de gomast.GomModule via type assertions,
          aucun struct V1 intermédiaire)
         │
         ▼
       <pkg>.go  (compilable, partagé, avec ContainsTomCode pour HookList)
```

## Métriques

| Composant                                  | Lignes |
| ------------------------------------------ | ------ |
| `internal/gomast/` (généré)                | 6 273  |
| `internal/gom/` (parser + helpers + tests) | 1 466  |
| `internal/backend/` (générateur + tests)   | 1 137  |
| `library/sharedobjects/` (runtime)         | inchangé |

**Supprimé par cette phase** :
- `internal/gom/ast.go` (~60 lignes — types V1 hand-written)
- `internal/gom/bridge.go` (~273 lignes — bridge V1↔V2 transitoire)
- `internal/gom/bridge_test.go` (~264 lignes — tests du bridge)
- `internal/backend/cross_v1v2_test.go` (~200 lignes — comparaison V1/V2 transitoire)

## Tests de conservation comportementale

Le user a demandé "plein de tests pour garantir que le comportement
des 2 implantations est bien conservé". Les preuves :

### 1. Tests préservés et passants

| Test                                  | Sous-cas | Ce qu'il prouve                             |
| ------------------------------------- | -------- | ------------------------------------------- |
| `TestGenerate_BuildsCorpus`           | 10       | Tous les .gom du corpus produisent du Go qui compile |
| `TestGenerate_ProvesSharing`          | 1        | Le code généré atteint l'identité de pointeur sur termes équivalents |
| `TestGenerate_ADT`                    | 5        | Les 5 .gom de l'ADT compilent en un seul package + `ContainsTomCode` correct |
| `TestEquivalence` (Go ⇄ Java)         | 3        | Stdout byte-identique avec le Gom Java de référence sur minimal/leaf/list |
| Tests `sharedobjects`                 | 6        | Hash-consing, sharing, concurrence inchangés |

### 2. Tests neufs ajoutés en Phase 3

| Test                                | Sous-cas | Ce qu'il prouve                                |
| ----------------------------------- | -------- | ---------------------------------------------- |
| `TestSmoke_Construction` (gomast)   | 1        | Le package généré gomast est utilisable, hash-cons OK, ContainsTomCode OK |
| `TestParse_*` (parser, V2-natifs)   | 9+10     | La forme V2 produite par le parser est correcte : sorts, alternatives, slots nommés, variadiques, hooks (simple/scopé/avec args), Objects.gom réel, sharing cross-modules |
| `TestParse_DeterministicSharing`    | 10       | Parser le même fichier deux fois → même pointeur GomModule canonique |
| `TestParse_ADTSharing`              | 5        | Idem sur les 5 .gom de l'ADT cross-importants |
| `TestSelfBootstrap`                 | 1        | **Le nouveau tomgo regénère son propre `internal/gomast/` byte-identique au commit** — preuve la plus forte de stabilité |

**Total ≈ 60 sous-tests passants après le refactor.**

### 3. Stabilité auto-amorce

C'est le test clé : `TestSelfBootstrap` prouve que tomgo, dans son
état Phase 3, peut **re-générer le même package gomast** que celui
qui l'alimente. Sortie :

```text
$ go test ./internal/backend/... -run TestSelfBootstrap -v
=== RUN   TestSelfBootstrap
--- PASS: TestSelfBootstrap (0.01s)
```

Si demain quelqu'un change le format de `String()` dans le backend,
ce test casse jusqu'à ce que `internal/gomast/` soit régénéré — la
boucle est bouclée.

## Commande de régénération de `internal/gomast/`

```bash
cd tomgo
rm -rf internal/gomast/{code,gom,objects,rule,symboltable}.go
go run ./cmd/tomgo gom-batch --pkg gomast -o internal/gomast \
    ../src/tom/gom/adt/Code.gom \
    ../src/tom/gom/adt/Gom.gom \
    ../src/tom/gom/adt/Objects.gom \
    ../src/tom/gom/adt/Rule.gom \
    ../src/tom/gom/adt/SymbolTable.gom
rm internal/gomast/go.mod    # gomast est interne au module tomgo
# (doc.go subsiste, il n'est pas écrasé)
go test ./...
```

Cf. également `internal/gomast/doc.go`.

## Limites assumées

- **Cross-module en mode single-file** : `tomgo gom <one.gom>` ne
  résout pas les imports d'autres modules. Le mode `gom-batch` est
  nécessaire pour les .gom qui se référencent.
- **Pas de mapping multi-package Java-style** : le batch émet un seul
  package Go (vs un package Java par module côté référence). Aucun
  conflit observé sur l'ADT (63 sorts uniques).
- **Hooks** : seul le hook `sort HookList:block()` d'Objects.gom est
  reconnu (cf. `knownHookTable`). Tout autre hook émet un commentaire
  « unsupported » et son corps est perdu.
- **Tests d'équivalence Go ⇄ Java** : couvrent 3 cibles
  représentatives (minimal/leaf/list). L'ADT n'est pas comparé à Java
  parce que les API d'accès (cons-list de Hook côté Java vs slice
  côté Go) divergent assez pour mériter un scénario dédié.

## Suite naturelle

- Étendre `knownHookTable` au fur et à mesure que d'autres hooks
  apparaissent dans la base.
- Porter `stable/tom/gom/parser/`, `stable/tom/gom/compiler/`,
  `stable/tom/gom/backend/` en Go (Phase 4 du plan global) — désormais
  ces phases peuvent manipuler des termes `gomast.*`, fidèles à
  l'AST canonique.
- Bootstrap final : réécrire `tomgo` en Tom+Go et auto-compiler.
