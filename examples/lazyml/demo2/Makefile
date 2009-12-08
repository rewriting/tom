CP=${TOM_HOME}/../../examples/build:${CLASSPATH}

all: Main.class
	@printf "running\n"
	@java -Xss128M Main

main.code: main.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main < $< > $@ 

pretty.code: pretty.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main < $< > $@ 

Main.java: Main.pre.java main.code pretty.code
	@printf "including generated code\n" 
	@cpp -P Main.pre.java > Main.java

Main.class: Main.java
	@printf "compiling $<" 
	@javac Main.java
	@printf "\n" 

clean:
	rm -rf *.class *.code Main.java *.orig

