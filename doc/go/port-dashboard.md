# Tableau de bord du portage Tom+Go (T2)

Généré par `mage portGo` (`go test ./parity/ -args -write-dashboard` dans `test/tools`).
Chaque `test/java/**/*.t` hors `error/` ; un cas porté est OK quand son port
Tom+Go, compilé par `tom -go`, se comporte comme l'original compilé par
`legacy/` (mêmes tests JUnit réussis, ou même sortie et même statut).
Un fichier « support » n'a pas de test propre : ses classes sont compilées
avec le cas qui les utilise (comme `test/java/build.xml` compile tout le
répertoire), et c'est le statut de ce cas qui les valide.
Un original « échoue dans legacy » ne se compile pas avec `legacy/` :
pas d'oracle possible, il n'est pas porté (`TestLegacyFails` le vérifie).
Un cas « OK (diagnostics) » est un test de non-régression des diagnostics
(`test/java/regress/`) : `tom -go` rejette le port avec les mêmes erreurs que
`legacy/` sur l'original ; rien n'est exécuté (la couverture T3 ne le
compte pas).

- total : 103
- OK : 77
- OK (diagnostics) : 11
- divergence : 0
- échec go test : 0
- échec go build : 0
- échec tom : 0
- oracle invalide : 0
- non supporté : 2
- support : 4
- échoue dans legacy : 9
- non porté : 0

| Cas | Statut | Notes |
|---|---|---|
| `Array.t` | OK |  |
| `Peano.t` | OK |  |
| `Record.t` | échoue dans legacy | the typer rejects the %op hierarchy of Java classes (Incompatible types 'TomBinaryOperator' and 'TomExp' for symbol 'BinaryOperator', Cannot guess the type of 'e1'), with both parsers; with --oldtyper, javac rejects the generated Java (Exp cannot be converted to CstExp); test/java/build.xml excludes it (its commented-out --lazyType option no longer exists) |
| `Test.t` | OK |  |
| `TestAlgebraic.t` | OK |  |
| `TestArray.t` | OK |  |
| `TestArray2.t` | OK |  |
| `TestArrayNonLinear.t` | OK |  |
| `TestBackQuote.t` | OK |  |
| `TestBuiltin.t` | OK |  |
| `TestConstraintCombination.t` | OK |  |
| `TestConstraintCombinationWithSubtype.t` | OK |  |
| `TestList.t` | OK |  |
| `TestList2.t` | OK |  |
| `TestListNonLinear.t` | OK |  |
| `TestMatch.t` | OK |  |
| `TestMatchInference.t` | OK |  |
| `TestNonVarSubjects.t` | OK |  |
| `TestOptimizer.t` | OK |  |
| `TestPeano.t` | OK |  |
| `TestRecord.t` | échoue dans legacy | the typer rejects the %op hierarchy of Java classes (Incompatible types 'TomBinaryOperator' and 'TomExp' for symbol 'BinaryOperator', Cannot guess the type of 'e1'), with both parsers; with --oldtyper, javac rejects the generated Java (Exp cannot be converted to CstExp); test/java/build.xml excludes it (its commented-out --lazyType option no longer exists) |
| `TestReflectiveStrategy.t` | OK |  |
| `TestStrategy.t` | OK |  |
| `TestSublists.t` | OK |  |
| `TestVarVarStar.t` | OK |  |
| `TomList.t` | OK |  |
| `acmatching/TestAC.t` | OK |  |
| `acmatching/TestBool3.t` | OK |  |
| `andor/TestAndOr.t` | OK |  |
| `andor/TestAndOrConstraintOnly.t` | OK |  |
| `antipatterns/TestAntiPattern.t` | OK |  |
| `antipatterns/TestAntiPattern2.t` | OK |  |
| `antipatterns/TestAntiPattern3.t` | OK |  |
| `antipatterns/TestAntiPatternAssociative.t` | OK |  |
| `antipatterns/TestDoubleList.t` | OK |  |
| `backquote/TestBackquoteGom.t` | OK |  |
| `backquote/TestBackquoteWithBracket.t` | OK |  |
| `backquote/TestDefaultValue.t` | OK |  |
| `cfib1.t` | OK |  |
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
| `gom/fromterm/TestFromTerm.t` | OK |  |
| `loulou.t` | OK |  |
| `numeric/TestNumericConditions.t` | OK |  |
| `regress/BadBackquote.t` | OK (diagnostics) | erreurs : incompatibleTypes, incompatibleTypes ; le `.nrt` attend incompatibleTypes (diffère déjà dans `legacy/`) |
| `regress/BadOp.t` | OK (diagnostics) | erreurs : symbolCodomainError ; parser par défaut de `legacy/` : aucune |
| `regress/Disjunction1.t` | OK (diagnostics) | erreurs : freeVarNotPresentInOr |
| `regress/Disjunction2.t` | OK (diagnostics) | erreurs : freeVarNotPresentInOr |
| `regress/Include.t` | échoue dans legacy | includes the nonexistent titi.tom on purpose (non-regression test of the includedFileNotFound error), but legacy/'s tom crashes instead of reporting it (TomRuntimeException "convert: strange term: Cst_IncludeFile" after printing the TomIncludeException), with both parsers |
| `regress/IncludeSl.t` | OK (diagnostics) | erreurs : symbolCodomainError, missingIncludeSL |
| `regress/SuccessiveErrors.t` | OK (diagnostics) | erreurs : incompatibleTypes, incompatibleTypes ; le `.nrt` attend invalidCodomain (diffère déjà dans `legacy/`) |
| `regress/TestNonLinearity.t` | échoue dans legacy | non-regression test of typing errors, but legacy/'s typer crashes (TyperPlugin: Unhandled exception, findOriginTracking: not found: concOption()) with both parsers; a fix of the typer belongs to J4 |
| `regress/UndeclaredExplicityType.t` | OK (diagnostics) | erreurs : unknownType |
| `regress/UnknownSymbol1.t` | OK (diagnostics) | erreurs : unknownSymbol |
| `regress/UnknownSymbol2.t` | OK (diagnostics) | erreurs : unknownSymbol |
| `regress/VariableStar.t` | OK (diagnostics) | erreurs : incorrectVariableStarInMatch |
| `regress/WrongSymbol.t` | OK (diagnostics) | erreurs : invalidVariableStarArgument |
| `rule/Array.t` | OK |  |
| `rule/Peano.t` | échoue dans legacy | obsolete %rule construct: legacy/'s tom crashes on it (NullPointerException in the CstBuilder of the default parser), and with -np it leaves %rule as host code, which javac rejects |
| `rule/TestArray.t` | échoue dans legacy | obsolete %rule construct: legacy/'s tom crashes on it (NullPointerException in the CstBuilder of the default parser), and with -np it leaves %rule as host code, which javac rejects |
| `rule/TestPeano.t` | échoue dans legacy | obsolete %rule construct: legacy/'s tom crashes on it (NullPointerException in the CstBuilder of the default parser), and with -np it leaves %rule as host code, which javac rejects |
| `rule/TestWhen.t` | échoue dans legacy | obsolete %rule construct: legacy/'s tom crashes on it (NullPointerException in the CstBuilder of the default parser), and with -np it leaves %rule as host code, which javac rejects; its %match rules also use the obsolete `when` guards, which -np leaves as host code too (javac stops on the first one, before the %rule) |
| `rule/TomList.t` | échoue dans legacy | obsolete %rule construct: legacy/'s tom crashes on it (NullPointerException in the CstBuilder of the default parser), and with -np it leaves %rule as host code, which javac rejects |
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
| `sl/TestReflective.t` | OK |  |
| `sl/TestSL.t` | OK |  |
| `sl/TestSLModes.t` | OK |  |
