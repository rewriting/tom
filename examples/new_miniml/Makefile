all: Main.class Eval.class

test: Eval.class
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

Eval.class: Eval.java Compiler.class LambdaParser.class LambdaLexer.class Printer.class Typer.class 
	javac Eval.java

Main.t: lambda/Lambda.tom

Main.java: Main.t
	tom Main.t

Main.class: Main.java Compiler.class LambdaParser.class LambdaLexer.class Printer.class Typer.class 
	javac Main.java

Typer.t: lambda/Lambda.tom

Typer.java: Typer.t
	tom Typer.t

Typer.class: Typer.java Eval.java
	javac Typer.java

Compiler.t: lambda/Lambda.tom

Compiler.java: Compiler.t
	tom Compiler.t

Compiler.class: Compiler.java 
	javac Compiler.java

lambda/Lambda.tom: lambda.gom
	gom lambda.gom

clean:
	rm -rf lambda *.class *.java Lambda.g Lambda__.g *.tokens 

