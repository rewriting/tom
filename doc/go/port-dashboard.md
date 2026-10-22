# Tableau de bord du portage Tom+Go (T2)

Généré par `mage portGo` (`go test ./ -args -write-dashboard` dans `test/go`).
Chaque `test/**/*.t` hors `error/` ; un cas porté est OK quand son port
Tom+Go, compilé par `tom -go`, se comporte comme l'original compilé par
`legacy/` (mêmes tests JUnit réussis, ou même sortie et même statut).
Un fichier « support » n'a pas de test propre : ses classes sont compilées
avec le cas qui les utilise (comme `test/build.xml` compile tout le
répertoire), et c'est le statut de ce cas qui les valide.

- total : 103
- OK : 48
- divergence : 0
- échec go test : 0
- échec go build : 0
- échec tom : 0
- oracle invalide : 0
- non supporté : 2
- support : 4
- non porté : 49

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
| `gom/BenchConsGom.t` | OK |  |
| `gom/TestACU.t` | OK |  |
| `gom/TestAU.t` | OK |  |
| `gom/TestBool.t` | OK |  |
| `gom/TestBuiltin.t` | OK |  |
| `gom/TestCollection.t` | OK |  |
| `gom/TestDotted.t` | OK |  |
| `gom/TestFL.t` | OK |  |
| `gom/TestFrom.t` | OK |  |
| `gom/TestFromMethods.t` | OK |  |
| `gom/TestGomMultithread.t` | OK |  |
| `gom/TestGomWithoutMaximalSharing.t` | OK |  |
| `gom/TestImport.t` | OK |  |
| `gom/TestJavaHook.t` | OK |  |
| `gom/TestList.t` | OK |  |
| `gom/TestMakeEmpty.t` | OK |  |
| `gom/TestMultiHook.t` | OK |  |
| `gom/TestPeanoRule.t` | OK |  |
| `gom/TestRuleBool.t` | OK |  |
| `gom/TestRuleCond.t` | OK |  |
| `gom/TestRuleList.t` | OK |  |
| `gom/TestRuleSortList.t` | OK |  |
| `gom/TestSortHook.t` | OK |  |
| `gom/TestStratMapping.t` | OK |  |
| `gom/TestTermgraph.t` | non supporté | `TestExpand` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestExpandCycle` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules1` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules2` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules3` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules4` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules51` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules52` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules6` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules7` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules8` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo ; `TestGraphRules9` non supporté : needs Gom's --termgraph (term-graph operators LabTerm/RefTerm/PathTerm, expand(), graphrules hooks compiled into term-graph rewriting strategies), not implemented by gomgo |
| `gom/TestTomHook.t` | OK |  |
| `gom/TestYingYang.t` | OK |  |
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
| `sl/IntrospectorBehaviour.t` | support | compilé avec `sl/TestSL.t`, couvert par son statut |
| `sl/NewBehaviour.t` | support | compilé avec `sl/TestSL.t`, couvert par son statut |
| `sl/OldBehaviour.t` | support | compilé avec `sl/TestSL.t`, couvert par son statut |
| `sl/TestEnvironment.t` | OK |  |
| `sl/TestGetSubterm.t` | OK |  |
| `sl/TestHandWrittenMapping.t` | OK |  |
| `sl/TestImportStrat.t` | OK |  |
| `sl/TestOnceTopDownId.t` | OK |  |
| `sl/TestPosition.t` | OK |  |
| `sl/TestReduceAll.t` | OK |  |
| `sl/TestRef.t` | non supporté | `TestRef1` non supporté : needs Gom's --termpointer (RefTerm/LabTerm/PathTerm/VarTerm, expand()) and the term-graph strategies of sl/graph.tom (DeRef, StrictDeRef, AllSeq), not implemented by gomgo and the Go sl library ; `TestRef2` non supporté : needs Gom's --termpointer (RefTerm/LabTerm/PathTerm/VarTerm, expand()) and the term-graph strategies of sl/graph.tom (DeRef, StrictDeRef, AllSeq), not implemented by gomgo and the Go sl library ; `TestRef3` non supporté : needs Gom's --termpointer (RefTerm/LabTerm/PathTerm/VarTerm, expand()) and the term-graph strategies of sl/graph.tom (DeRef, StrictDeRef, AllSeq), not implemented by gomgo and the Go sl library ; `TestRef4` non supporté : needs Gom's --termpointer (RefTerm/LabTerm/PathTerm/VarTerm, expand()) and the term-graph strategies of sl/graph.tom (DeRef, StrictDeRef, AllSeq), not implemented by gomgo and the Go sl library |
| `sl/TestReflective.t` | OK | `TestMake` non supporté : gomgo represents the variadic l(Term*) of testsl.gom as a flat list, without Java Gom's Consl/Emptyl constructors, hence without Make_Consl/Make_Emptyl |
| `sl/TestSL.t` | OK |  |
| `sl/TestSLModes.t` | OK |  |
