# tomgo — prototype gelé

Ce répertoire est le prototype de compilateur Tom écrit à la main en Go
(phases 0 à 6). Il est **gelé** : il compile et ses tests passent, mais il
n'évolue plus. Il sert de réservoir de code : on y copie ce dont on a
besoin, que l'on intègre ensuite dans l'arbre validé.

La version maintenue de ce qui a été validé est ailleurs :

| Ici (copie gelée) | Version maintenue |
|---|---|
| `stable/library/sl` | `src/go/tom/library/sl` |
| `stable/library/sharedobjects` | `src/go/tom/library/sharedobjects` |
| `stable/library/aterm` | `src/go/tom/library/aterm` |
| `stable/gom/{parser,backend}` | `src/go/tom/gom/{parser,backend}` |
| `stable/library/gomast` | `src/go/tom/gom/adt` |
| `cmd/tomgo` (gom, gom-batch, gom-block) | `src/go/tom/gom/cmd/gomgo` |
| `tests/{port,tomjava,javaparity,gosmoke,coverage}` | `test/go/…` (déplacés) |

Lancer les tests du prototype : `cd prototype/tomgo && go test ./...`.

Le `go.work` de la racine du dépôt inclut ce module (`tom/tomgo`) ; les
cibles d'orchestration de l'arbre validé sont dans `magefiles/` à la
racine (`mage -d magefiles -w . -l`). Le `magefile.go` de ce répertoire
ne concerne que le prototype.

## Historique

### tomgo

Go port of the TOM compiler. Work in progress.

> See [`../CLAUDE.md`](../CLAUDE.md) for the full project plan and scope.
> Current iteration covers phases 0–2: a tool that reads a hook-free `.gom`
> file and produces a Go package backed by a Go port of the shared-objects
> runtime. Phases 3–6 are deferred.

#### Layout

```
cmd/tomgo/                    CLI entry point
internal/
  gom/                        .gom parser + AST (Phase 2b)
  backend/                    .gom → .go generator (Phase 2c)
  library/sharedobjects/      hash-consing runtime (Phase 2a)
testdata/corpus/gom-nohooks/  10 hook-free .gom from test/gom/ (Phase 1)
reports/                      campaign reports (phase1, phase2…)
tools/scan-hooks/             standalone hook inventory tool (Phase 1)
```

#### Build & test

```bash
cd tomgo
go build ./...
go vet ./...
go test ./...
./tomgo --help
```

#### Commands (current state)

- `tomgo scan-hooks <dir>` — Phase 1, not yet wired into the CLI; see
  `tools/scan-hooks/` for the standalone implementation.
- `tomgo gom <file.gom> -o <out>` — Phase 2 entry point, not yet implemented.

#### Out of scope this iteration

- Anything touching `.t` files (host language, TOM parser, compiler pipeline).
- `.gom` files **with** hooks (deferred to sub-phase 2b).
- Backends other than Go.
