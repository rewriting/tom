.SUFFIXES: .g.t .g .t .java

.g.t.g:
	rm -f $@
	tom $< --output $@

.t.java:
	rm -f $@
	tom $<

all: Gomantlr_JavaParser.java Main.java
	make classclean
	javac Main.java
	java Main HelloWorld.java

Gomantlr_Java.g: Gomantlr_Java.g.t gomantlr_java/Gomantlr_Java.tom
	rm -f $@
	tom $< --output $@

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
