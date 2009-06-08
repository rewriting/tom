CP=${TOM_HOME}/../../examples/build:${CLASSPATH}

all: Test.class
	@printf "running\n"
	@java -Xss128M Test

lazy1.code: lazy1.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main --lazy < $< > $@ 

lazy2.code: lazy2.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main --lazy < $< > $@ 

Test.java: Test.pre.java lazy1.code lazy2.code
	@printf "including generated code\n" 
	@cpp -P Test.pre.java > Test.java

Test.class: Test.java
	@printf "compiling $<" 
	@javac Test.java
	@printf "\n" 

clean:
	rm -rf *.class *.code Test.java *.orig

