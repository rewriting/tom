all: Eval.class
	java Eval < test.mml

Lambda.g.t: lambda/Lambda.tom 

Printer.t: lambda/Lambda.tom 

Eval.t: lambda/Lambda.tom 

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

Eval.java: Eval.t
	tom Eval.t

Printer.class: Printer.java
	javac Printer.java

Eval.class: Eval.java LambdaParser.class LambdaLexer.class Printer.class Typer.class
	javac Eval.java

Typer.t: lambda/Lambda.tom

Typer.java: Typer.t
	tom Typer.t

Typer.class: Typer.java Eval.java
	javac Typer.java

lambda/Lambda.tom: lambda.gom
	gom lambda.gom

clean:
	rm -rf lambda *.class *.java Lambda.g Lambda__.g *.tokens 

