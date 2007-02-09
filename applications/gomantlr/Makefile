all:
	make clean
	gom AntlrGrammar.gom
	rm -rf antlrcommons
	rm -rf antlrrules
	rm -rf antlrelement
	./subst antlrgrammar antlrcommons
	./subst antlrgrammar antlrrules
	./subst antlrgrammar antlrelement
	gom -p antlrgrammar AntlrCommons.gom
	gom -p antlrgrammar AntlrRules.gom
	gom -p antlrgrammar AntlrElement.gom
	./modif AntlrGrammar AntlrCommons
	./modif AntlrGrammar AntlrRules
	./modif AntlrGrammar AntlrElement
	tom Gomantlr.t
	tom gomantlr/*.t
	tom aterm2antlrgrammar/*.t
	tom aterm2antlrgrammar/exceptions/*.t
	tom utils/*.t
	javac *.java
	rm -f Gomantlr_Java.gom
	java Gomantlr Java

clean:
	cd utils;make clean
	cd aterm2antlrgrammar;make clean
	cd gomantlr;make clean
	rm -f $(foreach v,$(wildcard *.t),$(shell echo $(basename $(v)).java))
	rm -f *.class
	rm -f *~
	rm -rf `echo $(foreach v,$(wildcard *.gom),$(shell echo $(basename $(v)))) | tr A-Z a-z`
	rm -rf antlrgrammar
