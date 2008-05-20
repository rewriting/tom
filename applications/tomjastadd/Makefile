ANTPATH=./tools/ant.jar:./tools/ant-launcher.jar
ASPECTJ=java -classpath tools/aspectj-1.5.3/aspectjlib.jar:tools/aspectj-1.5.3/aspectjrt.jar:tools/aspectj-1.5.3/aspectjtools.jar:tools/aspectj-1.5.3/aspectjweaver.jar:$(ANTPATH) org.aspectj.tools.ajc.Main -source 1.4
JAVACC=java -classpath tools/JavaCC.jar org.javacc.parser.Main -JDK_VERSION=1.4
JJTREE=java -classpath tools/JavaCC.jar org.javacc.jjtree.Main -JDK_VERSION=1.4
JASTADD=java -jar tools/jastadd2.jar

JRAGFILES=ast/ClassRelations.jrag ast/ComponentsUtil.jrag ast/Errorcheck.jrag ast/JaddCodeGen.jrag ast/NameBinding.jrag \
 jrag/Attributes.jrag jrag/Circular.jrag jrag/CollectionAttributes.jrag jrag/Errorcheck.jrag jrag/JragCodeGen.jrag jrag/NameBinding.jrag

all : ast/AST/Grammar.java ast/AST/Ast.java jrag/AST/JragParser.java jastadd/JastAdd.java
	$(ASPECTJ) `find ast jastadd jrag -name "*.java"`

ast/AST/Ast.java : ast/AST/Ast.jj
	$(JAVACC) -OUTPUT_DIRECTORY=ast/AST ast/AST/Ast.jj

ast/AST/Ast.jj : ast/Ast.jjt
	$(JJTREE) -OUTPUT_DIRECTORY=ast/AST -NODE_PREFIX=\"\" ast/Ast.jjt

ast/AST/Grammar.java : ast/Ast.ast $(JRAGFILES)
	$(JASTADD) --jjtree --rewrite --grammar=Ast --package=ast.AST $(JRAGFILES) ast/Ast.ast
  
jrag/AST/JragParser.java : jrag/AST/Jrag.jj
	$(JAVACC) -OUTPUT_DIRECTORY=jrag/AST jrag/AST/Jrag.jj

jrag/AST/Jrag.jj : jrag/Jrag.jjt
	$(JJTREE) -OUTPUT_DIRECTORY=jrag/AST jrag/Jrag.jjt

jar : all
	cp tools/aspectj-1.5.3/aspectjlocal.jar tomjastadd.jar
	jar -umf manifest tomjastadd.jar ast/*.class ast/AST/*.class jastadd/*.class jrag/*.class jrag/AST/*.class

clean :
	rm -rf jastadd/*.class ast/*.class ast/AST jrag/*.class jrag/AST/*.class jrag/AST/AST* jrag/AST/Jrag.jj jrag/AST/JragParser* jrag/AST/JJT* jrag/AST/Node.* jrag/AST/JavaCharStream* jrag/AST/ParseException* tomjastadd.jar
