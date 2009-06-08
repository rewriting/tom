CP=${TOM_HOME}/../../examples/build:${CLASSPATH}

all: Test.class
	@printf "running\n"
	@java -Xss128M Test

main.code: main.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main < $< > $@ 

Test.java: Test.pre.java main.code
	@printf "including generated code\n" 
	@cpp -P Test.pre.java > Test.java

Test.class: Test.java
	@printf "compiling $<" 
	@javac Test.java
	@printf "\n" 

clean:
	rm -rf *.class *.code Test.java *.orig

