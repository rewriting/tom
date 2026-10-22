# Couverture des constructions du compilateur (T3)

Généré par `mage coverage`.

| Construction | Occurrences dans le compilateur | Cas OK qui l'exercent | Couvert |
|---|---|---|---|
| %match | 446 | `Array.t`, `Peano.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`, `TestNonVarSubjects.t`, `TestPeano.t`, `TestSublists.t`, `TestVarVarStar.t`, `numeric/TestNumericConditions.t`, `sl/TestReduceAll.t` | ☑ |
| %strategy | 114 | `TestAlgebraic.t`, `TestStrategy.t`, `sl/TestGetSubterm.t`, `sl/TestOnceTopDownId.t`, `sl/TestReduceAll.t`, `sl/TestSLModes.t` | ☑ |
| %gom | 2 | `TestAlgebraic.t`, `TestBackQuote.t`, `TestNonVarSubjects.t`, `TestStrategy.t`, `TestSublists.t`, `TestVarVarStar.t`, `numeric/TestNumericConditions.t`, `sl/TestEnvironment.t`, `sl/TestGetSubterm.t`, `sl/TestOnceTopDownId.t`, `sl/TestReduceAll.t` | ☑ |
| %include sl.tom | 33 | `TestAlgebraic.t`, `TestStrategy.t`, `sl/TestGetSubterm.t`, `sl/TestImportStrat.t`, `sl/TestOnceTopDownId.t`, `sl/TestReduceAll.t`, `sl/TestSLModes.t` | ☑ |
| %include Collection/List/Map mappings | 18 |  | ☐ |
| %op/%oplist/%typeterm declarations | 49 | `Array.t`, `Peano.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`, `TestPeano.t`, `TestStrategy.t`, `sl/TestGetSubterm.t`, `sl/TestImportStrat.t` | ☑ |
| variadic patterns (X*) | 1005 | `Array.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`, `TestNonVarSubjects.t`, `TestStrategy.t`, `TestSublists.t`, `TestVarVarStar.t`, `numeric/TestNumericConditions.t` | ☑ |
| anti-patterns (!p) | 32 |  | ☐ |
| aliases (x@p) | 306 | `Array.t`, `Peano.t`, `TestArray.t`, `TestList.t`, `TestPeano.t` | ☑ |
| constraints (<< && ||) | 236 |  | ☐ |
| backquote terms | 3789 | `Array.t`, `Peano.t`, `TestAlgebraic.t`, `TestArray.t`, `TestBackQuote.t`, `TestList.t`, `TestPeano.t`, `TestStrategy.t`, `TestSublists.t`, `TestVarVarStar.t`, `sl/TestEnvironment.t`, `sl/TestGetSubterm.t`, `sl/TestImportStrat.t`, `sl/TestOnceTopDownId.t`, `sl/TestReduceAll.t`, `sl/TestSLModes.t` | ☑ |
| %strategy with parameters | 81 | `TestStrategy.t`, `sl/TestGetSubterm.t` | ☑ |
| getEnvironment() | 23 | `TestStrategy.t`, `sl/TestGetSubterm.t` | ☑ |
| getPosition() | 8 | `TestStrategy.t`, `sl/TestEnvironment.t`, `sl/TestGetSubterm.t` | ☑ |
| visit rules | 128 | `TestAlgebraic.t`, `TestStrategy.t`, `sl/TestGetSubterm.t`, `sl/TestOnceTopDownId.t`, `sl/TestReduceAll.t`, `sl/TestSLModes.t` | ☑ |
| labelled rules (l:p -> a) | 2 |  | ☐ |

Constructions non couvertes : 4
