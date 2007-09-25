all: Preprocess.class
	java Preprocess test/Essai_template.t > test/Essai.t
	cd test; tom Essai.t; javac Essai.java ; java Essai; cd ..

Preprocess.class: Preprocess.java GenericParser.class GenericLexer.class
	javac Preprocess.java

GenericParser.class: GenericParser.java
	javac GenericParser.java

GenericLexer.class: GenericLexer.java
	javac GenericLexer.java

GenericParser.java: Generic.g.t generics/generics.tom
	tom -o Generic.g Generic.g.t 
	java org.antlr.Tool Generic.g

.PHONY: clean

clean:
	-rm -r generics *.g *.tokens *.java *.class test/*.java test/Essai.t test/essai test/Essai.class

generics/generics.tom: generics.gom
	gom generics.gom

Preprocess.java: Preprocess.t generics/generics.tom
	tom Preprocess.t

