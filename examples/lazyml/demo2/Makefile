CP=${TOM_HOME}/../../examples/build:${CLASSPATH}

all: Demo.class
	@printf "running\n"
	@java -Xss128M Demo

main.code: main.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main < $< > $@ 

pretty.code: pretty.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main < $< > $@ 

Demo.java: Demo.pre.java main.code pretty.code
	@printf "including generated code\n" 
	@cpp -P Demo.pre.java > Demo.java

Demo.class: Demo.java
	@printf "compiling $<" 
	@javac Demo.java
	@printf "\n" 

clean:
	rm -rf *.class *.code Demo.java *.orig

