CP=${TOM_HOME}/../../examples/build:${CLASSPATH}

all: Demo.class
	@printf "running\n"
	@java -Xss128M Demo

lazy1.code: lazy1.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main --lazy < $< > $@ 

lazy2.code: lazy2.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main --lazy < $< > $@ 

Demo.java: Demo.pre.java lazy1.code lazy2.code
	@printf "including generated code\n" 
	@cpp -P Demo.pre.java > Demo.java

Demo.class: Demo.java
	@printf "compiling $<" 
	@javac Demo.java
	@printf "\n" 

clean:
	rm -rf *.class *.code Demo.java *.orig

