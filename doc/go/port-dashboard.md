# Tableau de bord du portage Tom+Go (T2)

Généré par `mage portGo` (`go test ./ -args -write-dashboard` dans `test/go`).
Chaque `test/**/*.t` hors `error/` ; un cas porté est OK quand son port
Tom+Go, compilé par `tom -go`, se comporte comme l'original compilé par
`legacy/` (mêmes tests JUnit réussis, ou même sortie et même statut).
Un fichier « support » n'a pas de test propre : ses classes sont compilées
avec le cas qui les utilise (comme `test/build.xml` compile tout le
répertoire), et c'est le statut de ce cas qui les valide.

- total : 103
- OK : 19
- divergence : 0
- échec go test : 0
- échec go build : 0
- échec tom : 0
- oracle invalide : 0
- support : 1
- non porté : 83

| Cas | Statut | Notes |
|---|---|---|
| `Array.t` | OK |  |
| `Peano.t` | OK |  |
| `Record.t` | non porté |  |
| `Test.t` | non porté |  |
| `TestAlgebraic.t` | OK |  |
| `TestArray.t` | OK |  |
| `TestArray2.t` | non porté |  |
| `TestArrayNonLinear.t` | non porté |  |
| `TestBackQuote.t` | OK |  |
| `TestBuiltin.t` | non porté |  |
| `TestConstraintCombination.t` | non porté |  |
| `TestConstraintCombinationWithSubtype.t` | non porté |  |
| `TestList.t` | OK |  |
| `TestList2.t` | non porté |  |
| `TestListNonLinear.t` | non porté |  |
| `TestMatch.t` | non porté |  |
| `TestMatchInference.t` | non porté |  |
| `TestNonVarSubjects.t` | OK |  |
| `TestOptimizer.t` | non porté |  |
| `TestPeano.t` | OK |  |
| `TestRecord.t` | non porté |  |
| `TestReflectiveStrategy.t` | non porté |  |
| `TestStrategy.t` | OK |  |
| `TestSublists.t` | OK |  |
| `TestVarVarStar.t` | OK |  |
| `TomList.t` | non porté |  |
| `acmatching/TestAC.t` | non porté |  |
| `acmatching/TestBool3.t` | non porté |  |
| `andor/TestAndOr.t` | non porté |  |
| `andor/TestAndOrConstraintOnly.t` | non porté |  |
| `antipatterns/TestAntiPattern.t` | non porté |  |
| `antipatterns/TestAntiPattern2.t` | non porté |  |
| `antipatterns/TestAntiPattern3.t` | non porté |  |
| `antipatterns/TestAntiPatternAssociative.t` | non porté |  |
| `antipatterns/TestDoubleList.t` | non porté |  |
| `backquote/TestBackquoteGom.t` | non porté |  |
| `backquote/TestBackquoteWithBracket.t` | non porté |  |
| `backquote/TestDefaultValue.t` | non porté |  |
| `cfib1.t` | non porté |  |
| `gom/BenchConsGom.t` | non porté |  |
| `gom/TestACU.t` | non porté |  |
| `gom/TestAU.t` | non porté |  |
| `gom/TestBool.t` | non porté |  |
| `gom/TestBuiltin.t` | non porté |  |
| `gom/TestCollection.t` | non porté |  |
| `gom/TestDotted.t` | non porté |  |
| `gom/TestFL.t` | non porté |  |
| `gom/TestFrom.t` | non porté |  |
| `gom/TestFromMethods.t` | non porté |  |
| `gom/TestGomMultithread.t` | non porté |  |
| `gom/TestGomWithoutMaximalSharing.t` | non porté |  |
| `gom/TestImport.t` | non porté |  |
| `gom/TestJavaHook.t` | non porté |  |
| `gom/TestList.t` | non porté |  |
| `gom/TestMakeEmpty.t` | non porté |  |
| `gom/TestMultiHook.t` | non porté |  |
| `gom/TestPeanoRule.t` | non porté |  |
| `gom/TestRuleBool.t` | non porté |  |
| `gom/TestRuleCond.t` | non porté |  |
| `gom/TestRuleList.t` | non porté |  |
| `gom/TestRuleSortList.t` | non porté |  |
| `gom/TestSortHook.t` | non porté |  |
| `gom/TestStratMapping.t` | non porté |  |
| `gom/TestTermgraph.t` | non porté |  |
| `gom/TestTomHook.t` | non porté |  |
| `gom/TestYingYang.t` | non porté |  |
| `gom/fromterm/TestFromTerm.t` | non porté |  |
| `loulou.t` | non porté |  |
| `numeric/TestNumericConditions.t` | OK |  |
| `regress/BadBackquote.t` | non porté |  |
| `regress/BadOp.t` | non porté |  |
| `regress/Disjunction1.t` | non porté |  |
| `regress/Disjunction2.t` | non porté |  |
| `regress/Include.t` | non porté |  |
| `regress/IncludeSl.t` | non porté |  |
| `regress/SuccessiveErrors.t` | non porté |  |
| `regress/TestNonLinearity.t` | non porté |  |
| `regress/UndeclaredExplicityType.t` | non porté |  |
| `regress/UnknownSymbol1.t` | non porté |  |
| `regress/UnknownSymbol2.t` | non porté |  |
| `regress/VariableStar.t` | non porté |  |
| `regress/WrongSymbol.t` | non porté |  |
| `rule/Array.t` | non porté |  |
| `rule/Peano.t` | non porté |  |
| `rule/TestArray.t` | non porté |  |
| `rule/TestPeano.t` | non porté |  |
| `rule/TestWhen.t` | non porté |  |
| `rule/TomList.t` | non porté |  |
| `sl/CommonStrat.t` | support | compilé avec `sl/TestImportStrat.t`, couvert par son statut |
| `sl/IntrospectorBehaviour.t` | non porté |  |
| `sl/NewBehaviour.t` | non porté |  |
| `sl/OldBehaviour.t` | non porté |  |
| `sl/TestEnvironment.t` | OK |  |
| `sl/TestGetSubterm.t` | OK |  |
| `sl/TestHandWrittenMapping.t` | non porté |  |
| `sl/TestImportStrat.t` | OK |  |
| `sl/TestOnceTopDownId.t` | OK |  |
| `sl/TestPosition.t` | OK |  |
| `sl/TestReduceAll.t` | OK |  |
| `sl/TestRef.t` | non porté |  |
| `sl/TestReflective.t` | non porté |  |
| `sl/TestSL.t` | non porté |  |
| `sl/TestSLModes.t` | OK |  |
