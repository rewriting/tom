all: cps/FreshLambda.class
	java cps.FreshLambda < test.ml

Lambda.g.t: cps/lambda/Lambda.tom 

Printer.t: cps/lambda/Lambda.tom 

FreshLambda.t: cps/lambda/Lambda.tom 

cps/Lambda.g: Lambda.g.t
	tom -o cps/Lambda.g -I cps Lambda.g.t

cps/LambdaParser.java cps/LambdaLexer.java: cps/Lambda.g
	java org.antlr.Tool cps/Lambda.g

cps/LambdaParser.class: cps/LambdaParser.java
	javac cps/LambdaParser.java

cps/LambdaLexer.class: cps/LambdaLexer.java
	javac cps/LambdaLexer.java

cps/Printer.java: Printer.t
	tom -I cps Printer.t

cps/FreshLambda.java: FreshLambda.t
	tom -I cps FreshLambda.t

cps/Printer.class: cps/Printer.java
	javac cps/Printer.java

cps/FreshLambda.class: cps/FreshLambda.java cps/LambdaParser.class cps/LambdaLexer.class cps/Printer.class
	javac cps/FreshLambda.java

cps/lambda/Lambda.tom: lambda.gom
	gom --fresh --package cps lambda.gom

clean:
	rm -rf cps/

