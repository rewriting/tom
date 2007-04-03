.SUFFIXES: .t .java

.t.java:
	tom $<

all: antlrgrammar/AntlrGrammar.tom antlrgrammar/antlrcommons/AntlrCommons.tom antlrgrammar/antlrrules/AntlrRules.tom antlrgrammar/antlrelement/AntlrElement.tom Gomantlr.java
	cd gomantlr;make
	cd aterm2antlrgrammar;make
	cd utils;make
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

antlrgrammar/AntlrGrammar.tom: AntlrGrammar.gom
	gom AntlrGrammar.gom
	rm -rf antlrcommons
	rm -rf antlrrules
	rm -rf antlrelement
	./subst antlrgrammar antlrcommons
	./subst antlrgrammar antlrrules
	./subst antlrgrammar antlrelement

antlrgrammar/antlrcommons/AntlrCommons.tom: AntlrCommons.gom
	gom -p antlrgrammar AntlrCommons.gom
	./modif AntlrGrammar AntlrCommons

antlrgrammar/antlrrules/AntlrRules.tom: AntlrRules.gom
	gom -p antlrgrammar AntlrRules.gom
	./modif AntlrGrammar AntlrRules

antlrgrammar/antlrelement/AntlrElement.tom: AntlrElement.gom
	gom -p antlrgrammar AntlrElement.gom
	./modif AntlrGrammar AntlrElement
