#!/bin/sh
DIRLIST="acmatching addressbook analysis antipattern bdd boulderdash
caml cell cps csmaca debugger deepmatch dom
freshgom gasel gom gomoku integerc labyrinth
lambdacalculi lazyml list master matching meta mgs 
multigraph nspk p3p parser peano poly strategy 
subtyping set structure polygraphes prodrule propp quine
rbtree tactics termgraph tomjastadd  typeinference"

DIRLIST2="bpel builtin  bytecode expression  miniml  modeltrans strategycompiler strings tutorial"

#tom -nt $FILELIST &> logAllFiles.txt

#FILELIST2="Array Peano Test TestAlgebraic TestArray TestArray2 TestArrayNonLinear TestBackQuote TestBuiltinNS TestDoubleSubject TestLabelNS TestList2NS TestListNS TestListNonLinearNS TestMatch TestMatchInference TestNonVarSubjects TestOptimizer TestPeano TestReflectiveStrategy TestStrategy TestSublistsNS TestVarVarStar TomListNS cfib1 loulou"

for f in $DIRLIST; do
	cd $f
	ant -Dnewparser=true >> ../resultTest.log
	cd ..
done

#for f in $FILELIST2; do
#	javac $f.java
#done
