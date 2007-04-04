.SUFFIXES: .t .java

.t.java:
	tom $<

all: antlrgrammar/AntlrGrammar.tom Gomantlr.java
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

antlrgrammar/AntlrGrammar.tom: AntlrGrammar.gom AntlrCommons.gom AntlrRules.gom AntlrElement.gom
	gom AntlrGrammar.gom
	rm -rf antlrcommons
	rm -rf antlrrules
	rm -rf antlrelement
	./subst antlrgrammar antlrcommons
	./subst antlrgrammar antlrrules
	./subst antlrgrammar antlrelement
	gom -p antlrgrammar AntlrRules.gom
	./modif AntlrGrammar AntlrRules
	./modif AntlrGrammar AntlrCommons
	./modif AntlrGrammar AntlrElement
