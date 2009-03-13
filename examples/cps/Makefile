all: cps/OnePassTransformation.class cps/TwoPassTransformation.class
	java cps.TwoPassTransformation < test1.ml
	java cps.OnePassTransformation < test1.ml
	java cps.TwoPassTransformation < test2.ml

Lambda.g.t: cps/lambda/Lambda.tom 

Printer.t: cps/lambda/Lambda.tom 

OnePassTransformation.t: cps/lambda/Lambda.tom

TwoPassTransformation.t: cps/lambda/Lambda.tom 

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

cps/OnePassTransformation.java: OnePassTransformation.t
	tom -I cps OnePassTransformation.t

cps/TwoPassTransformation.java: TwoPassTransformation.t
	tom -I cps TwoPassTransformation.t

cps/Printer.class: cps/Printer.java
	javac cps/Printer.java

cps/OnePassTransformation.class: cps/OnePassTransformation.java cps/LambdaParser.class cps/LambdaLexer.class cps/Printer.class
	javac cps/OnePassTransformation.java

cps/TwoPassTransformation.class: cps/TwoPassTransformation.java cps/LambdaParser.class cps/LambdaLexer.class cps/Printer.class
	javac cps/TwoPassTransformation.java

cps/lambda/Lambda.tom: lambda.gom
	gom --fresh --package cps lambda.gom

clean:
	rm -rf cps/
