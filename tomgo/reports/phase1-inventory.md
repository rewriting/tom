# Phase 1 — Inventaire des fichiers `.gom`

Généré par `tomgo scan-hooks ..` puis filtré pour exclure le sous-dossier `tomgo/`
lui-même (le module Go en cours de portage).

## Totaux

| Catégorie       | Nombre |
| --------------- | ------ |
| **Sans hooks**  | **128** |
| Avec hooks      | 48 |
| **Total**       | **176** |

## Détection

Hook = ligne (au début, après stripping d'éventuels espaces, d'un commentaire `//`
et de la portée optionnelle `sort|module|operator`) qui matche
`^[A-Za-z_]\w*\s*:\s*[A-Za-z_]\w*\s*\(`. Cf. `internal/gom/hookscanner.go`.

## Répartition par sous-arborescence

| Sous-arbo | Sans hooks | Avec hooks |
| --------- | ---------- | ---------- |
| applications | 45 | 16 |
| bench | 3 | 2 |
| demo | 3 | 0 |
| examples | 38 | 13 |
| lab | 9 | 4 |
| src | 18 | 7 |
| test | 12 | 6 |


## `applications/` — sans hooks

- `applications/firewall/src/firewall/Ast.gom`
- `applications/gomantlr/AntlrCommons.gom`
- `applications/gomantlr/AntlrElement.gom`
- `applications/gomantlr/AntlrGrammar.gom`
- `applications/gomantlr/AntlrRules.gom`
- `applications/gomantlr/future/Gomantlr_Java.gom`
- `applications/gomantlr/test/Gomantlr_Java.gom`
- `applications/iptables/src/iptables/AddressParser.gom`
- `applications/iptables/src/iptables/Ast.gom`
- `applications/iptables/src/iptables/Firewall.gom`
- `applications/iptables/src/iptables/IptablesCmd.gom`
- `applications/iptables/src/iptables/IptablesList.gom`
- `applications/islandgrammar/src/newparser/miniTom.gom`
- `applications/islandgrammar2/src/newparser/miniTom.gom`
- `applications/jspAnalysisTom/src/test/TNode.gom`
- `applications/lemuridae/src/lemu2/kernel/coc.gom`
- `applications/lemuridae/src/lemu2/kernel/proofterms.gom`
- `applications/logo/src/logo/Ast.gom`
- `applications/minijava/src/parser/Rec.gom`
- `applications/poc/jboss-el/src/EL.gom`
- `applications/poc/mappingInterface/src/base/data.gom`
- `applications/poc/sdfgom/Bool.gom`
- `applications/poc/sdfgom/examples/pico/pico.gom`
- `applications/poc/sdfgom/src/sdfgom/sdf.gom`
- `applications/policyCheck/last/input.gom`
- `applications/policyCheck/last/policy.gom`
- `applications/policyCheck/src/version1/input.gom`
- `applications/prototype3D/Projet 3A/src/tom/Donnees.gom`
- `applications/prototype3D/src/tom/Donnees.gom`
- `applications/quickcheck/examples/binarytree/binarytree.gom`
- `applications/quickcheck/examples/lists/AList.gom`
- `applications/quickcheck/examples/lists/AssortedList.gom`
- `applications/quickcheck/examples/lists/StringList.gom`
- `applications/quickcheck/examples/simplest/Dummy.gom`
- `applications/quickcheck/examples/simplest/Term.gom`
- `applications/quickcheck/propcheck/Propcheck/src/examples/AList.gom`
- `applications/quickcheck/propcheck/Propcheck/src/examples/BList.gom`
- `applications/quickcheck/propcheck/Propcheck/src/examples/types.gom`
- `applications/quickcheck/propcheck/Zipper/src/examples/types.gom`
- `applications/quickcheck/src/tomchecker/PropLang.gom`
- `applications/quickcheck_agata/src/gom/sort.gom`
- `applications/quickcheck_agata/src/gom/system.gom`
- `applications/rho/matching/Lamterm.gom`
- `applications/rho/matching/term.gom`
- `applications/rho/xrho/Rhoterm.gom`

## `applications/` — avec hooks (exclus du corpus Phase 1)

- `applications/iptables/src/iptables/Analyser.gom`
- `applications/javaparser/src/Ast.gom`
- `applications/lemuridae/src/lemu/sequents.gom`
- `applications/lemuridae/src/lemu/urban.gom`
- `applications/poc/sdfgom/src/sdfgom/mept.gom`
- `applications/policyCheck/src/version1/policy.gom`
- `applications/policyCheck/src/version2/accesscontrol.gom`
- `applications/polygraphes/src/adt/PolygraphicProgram.gom`
- `applications/polygraphes/src/gui/PolygraphicProgramgui.gom`
- `applications/refactoring/src/binarynumber/BinaryNumber.gom`
- `applications/refactoring/src/testgen/TinyJava.gom`
- `applications/refactoring/src/testgen2/TinyJava.gom`
- `applications/rho/lambdaparallel/Parallellamterm.gom`
- `applications/strategyAnalyzer/src/sa/Rule.gom`
- `applications/twoscale/src/formula.gom`
- `applications/twoscale/src/formule.gom`

## `bench/` — sans hooks

- `bench/gomterm/gomterm.gom`
- `bench/lemuridae/src/lambdapp/lambda.gom`
- `bench/sl/Term.gom`

## `bench/` — avec hooks (exclus du corpus Phase 1)

- `bench/lemuridae/src/lemu/sequents.gom`
- `bench/lemuridae/src/lemu/urban.gom`

## `demo/` — sans hooks

- `demo/gomANTLRAdaptor/parser/Expression.gom`
- `demo/labyrinth/src/labyrinth/boulder.gom`
- `demo/reach/term.gom`

## `demo/` — avec hooks (exclus du corpus Phase 1)


## `examples/` — sans hooks

- `examples/addressbook/Data.gom`
- `examples/analysis/Ast.gom`
- `examples/analysis/Cfg.gom`
- `examples/analysis/Node.gom`
- `examples/boulderdash/boulder.gom`
- `examples/builtin/term.gom`
- `examples/cps/lambda.gom`
- `examples/csharp/term.gom`
- `examples/enumerator/conslist.gom`
- `examples/enumerator/mutual.gom`
- `examples/freshgom/lambda.gom`
- `examples/freshgom/systemf.gom`
- `examples/generics/generics.gom`
- `examples/gom/Elist.gom`
- `examples/gom/List.gom`
- `examples/gom/Rond.gom`
- `examples/gom/VList.gom`
- `examples/gom/term.gom`
- `examples/gomoku/Gomoku.gom`
- `examples/labyrinth/boulder.gom`
- `examples/lazyml/lambda.gom`
- `examples/mgs/term.gom`
- `examples/miniml/Lambda.gom`
- `examples/multigraph/term.gom`
- `examples/nspk/term.gom`
- `examples/parser/ListInt.gom`
- `examples/parser/Rule.gom`
- `examples/parser/Term.gom`
- `examples/peano/Peano.gom`
- `examples/poly/Poly.gom`
- `examples/poly/expression.gom`
- `examples/propp/Seq.gom`
- `examples/rbtree/tree.gom`
- `examples/set/jgset.gom`
- `examples/strategy/term.gom`
- `examples/subtyping/checking/Definition.gom`
- `examples/subtyping/inference/Language.gom`
- `examples/tactics/Trees.gom`

## `examples/` — avec hooks (exclus du corpus Phase 1)

- `examples/acmatching/Peano.gom`
- `examples/antipattern/Term.gom`
- `examples/antipattern/associative/TermAso.gom`
- `examples/bdd/Bdd.gom`
- `examples/gom/Bool.gom`
- `examples/gom/Peano.gom`
- `examples/lambdacalculi/lambdaterm.gom`
- `examples/poly/polynom.gom`
- `examples/polygraphes/Polygraphes.gom`
- `examples/structure/Structures.gom`
- `examples/termgraph/freshlambda.gom`
- `examples/termgraph/lambdaterm.gom`
- `examples/termgraph/term.gom`

## `lab/` — sans hooks

- `lab/enumerator/src/examples/adt/list/AList.gom`
- `lab/enumerator/src/examples/adt/queue/queue.gom`
- `lab/enumerator/src/examples/adt/stack/stackLanguage.gom`
- `lab/enumerator/src/examples/adt/table/table.gom`
- `lab/enumerator/src/examples/adt/tree/tree.gom`
- `lab/enumerator/src/examples/conslist.gom`
- `lab/enumerator/src/examples/mutual.gom`
- `lab/enumerator/src/examples/parser/Rec.gom`
- `lab/enumerator/src/examples/recursive.gom`

## `lab/` — avec hooks (exclus du corpus Phase 1)

- `lab/enumerator/src/examples/adt/queue/queueLanguage.gom`
- `lab/enumerator/src/examples/adt/stack/stack.gom`
- `lab/enumerator/src/examples/shop/boutique.gom`
- `lab/enumerator/src/examples/shop/shop.gom`

## `src/` — sans hooks

- `src/tom/engine/adt/CST.gom`
- `src/tom/engine/adt/Il.gom`
- `src/tom/engine/adt/Theory.gom`
- `src/tom/engine/adt/TomDeclaration.gom`
- `src/tom/engine/adt/TomOption.gom`
- `src/tom/engine/adt/TomSignature.gom`
- `src/tom/engine/adt/TomSlot.gom`
- `src/tom/engine/adt/TomTerm.gom`
- `src/tom/engine/adt/TomType.gom`
- `src/tom/engine/adt/TypeConstraints.gom`
- `src/tom/gom/adt/Code.gom`
- `src/tom/gom/adt/Gom.gom`
- `src/tom/gom/adt/Rule.gom`
- `src/tom/gom/adt/SymbolTable.gom`
- `src/tom/library/adt/TNode.gom`
- `src/tom/platform/adt/PlatformAlert.gom`
- `src/tom/platform/adt/PlatformConfig.gom`
- `src/tom/platform/adt/PlatformOption.gom`

## `src/` — avec hooks (exclus du corpus Phase 1)

- `src/tom/engine/adt/Code.gom`
- `src/tom/engine/adt/TomConstraint.gom`
- `src/tom/engine/adt/TomExpression.gom`
- `src/tom/engine/adt/TomInstruction.gom`
- `src/tom/engine/adt/TomName.gom`
- `src/tom/gom/adt/Objects.gom`
- `src/tom/library/adt/Bytecode.gom`

## `test/` — sans hooks

- `test/backquote/term.gom`
- `test/gom/Builtin.gom`
- `test/gom/Dotted.gom`
- `test/gom/Imported.gom`
- `test/gom/Importing.gom`
- `test/gom/Leaf.gom`
- `test/gom/List.gom`
- `test/gom/Minimal.gom`
- `test/gom/Yang.gom`
- `test/gom/Ying.gom`
- `test/gom/fromterm/foo.gom`
- `test/sl/testsl.gom`

## `test/` — avec hooks (exclus du corpus Phase 1)

- `test/gom/Bool.gom`
- `test/gom/JavaHook.gom`
- `test/gom/ML.gom`
- `test/gom/MultiHook.gom`
- `test/gom/RuleBool.gom`
- `test/gom/RuleList.gom`
