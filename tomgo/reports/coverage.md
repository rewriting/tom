# Couverture des constructions du compilateur (T3)

Généré par `mage coverage`.

| Construction | Occurrences dans le compilateur | Cas OK qui l'exercent | Couvert |
|---|---|---|---|
| %match | 446 | `Array.t`, `Peano.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`, `TestNonVarSubjects.t`, `TestPeano.t`, `TestSublists.t`, `TestVarVarStar.t`, `numeric/TestNumericConditions.t` | ☑ |
| %strategy | 114 | `TestAlgebraic.t`, `TestStrategy.t` | ☑ |
| %gom | 1 | `TestAlgebraic.t`, `TestBackQuote.t`, `TestNonVarSubjects.t`, `TestStrategy.t`, `TestSublists.t`, `TestVarVarStar.t`, `numeric/TestNumericConditions.t`, `sl/TestEnvironment.t` | ☑ |
| %include sl.tom | 33 | `TestAlgebraic.t`, `TestStrategy.t` | ☑ |
| %include Collection/List/Map mappings | 18 |  | ☐ |
| %op/%oplist/%typeterm declarations | 49 | `Array.t`, `Peano.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`, `TestPeano.t`, `TestStrategy.t` | ☑ |
| variadic patterns (X*) | 1005 | `Array.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`, `TestNonVarSubjects.t`, `TestStrategy.t`, `TestSublists.t`, `TestVarVarStar.t`, `numeric/TestNumericConditions.t` | ☑ |
| anti-patterns (!p) | 32 | `TestSublists.t` | ☑ |
| aliases (x@p) | 306 | `Array.t`, `Peano.t`, `TestArray.t`, `TestList.t`, `TestPeano.t`, `TestSublists.t` | ☑ |
| constraints (<< && ||) | 236 |  | ☐ |
| backquote terms | 3789 | `Array.t`, `Peano.t`, `TestAlgebraic.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`, `TestPeano.t`, `TestStrategy.t`, `TestSublists.t`, `TestVarVarStar.t`, `sl/TestEnvironment.t` | ☑ |
| %strategy with parameters | 81 | `TestStrategy.t` | ☑ |
| getEnvironment() | 23 | `TestStrategy.t` | ☑ |
| getPosition() | 8 | `TestStrategy.t` | ☑ |
| visit rules | 128 | `TestAlgebraic.t`, `TestStrategy.t` | ☑ |
| labelled rules (l:p -> a) | 2 |  | ☐ |

Constructions non couvertes : 3
