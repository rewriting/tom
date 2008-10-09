all: SystemF.class FreshLambda.class
	java SystemF
	java FreshLambda < test.ml

SystemF.java: SystemF.t
	tom SystemF.t

SystemF.class: SystemF.java
	javac SystemF.java

SystemF.t: systemf/SystemF.tom

systemf/SystemF.tom: systemf.gom
	gom --fresh systemf.gom

Lambda.g.t: lambda/Lambda.tom 

Printer.t: lambda/Lambda.tom 

FreshLambda.t: lambda/Lambda.tom 

Lambda.g: Lambda.g.t
	tom -o Lambda.g Lambda.g.t

LambdaParser.java LambdaLexer.java: Lambda.g
	java org.antlr.Tool Lambda.g

LambdaParser.class: LambdaParser.java
	javac LambdaParser.java

LambdaLexer.class: LambdaLexer.java
	javac LambdaLexer.java

Printer.java: Printer.t
	tom Printer.t

FreshLambda.java: FreshLambda.t
	tom FreshLambda.t

Printer.class: Printer.java
	javac Printer.java

FreshLambda.class: FreshLambda.java LambdaParser.class LambdaLexer.class Printer.class
	javac FreshLambda.java

lambda/Lambda.tom: lambda.gom
	gom --fresh lambda.gom

clean:
	rm -rf systemf lambda *.class *.java Lambda.g Lambda__.g *.tokens 

