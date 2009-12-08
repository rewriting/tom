.SUFFIXES: .g.t .g .t .java

.g.t.g:
	rm -f $@
	tom $< --output $@

.t.java:
	rm -f $@
	tom $<

all: gomantlr_java/Gomantlr_Java.tom Gomantlr_JavaParser.java Test.java
	make classclean
	javac Test.java
	java Test HelloWorld.java

Gomantlr_JavaParser.java: Gomantlr_Java.g
	make antlrclean
	java org.antlr.Tool Gomantlr_Java.g

gomantlr_java/Gomantlr_Java.tom: Gomantlr_Java.gom
	make gomclean
	gom Gomantlr_Java.gom

classclean:
	rm -f *.class

antlrclean:
	rm -f Gomantlr_JavaLexer.java
	rm -f Gomantlr_JavaParser.java
	rm -f Gomantlr_Java.tokens
	rm -f Gomantlr_Java__.g

gomclean:
	rm -rf `echo $(foreach v,$(wildcard *.gom),$(shell echo $(basename $(v)))) | tr A-Z a-z`

clean:
	make classclean
	make antlrclean
	make gomclean
	rm -f *~
	rm -f $(foreach v,$(wildcard *.t),$(shell echo $(basename $(v)).java))
	rm -f Gomantlr_Java.g
