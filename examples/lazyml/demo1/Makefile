CP=${TOM_HOME}/../../examples/build:${CLASSPATH}

all: Main.class
	@printf "running\n"
	@java -Xss128M Main

lazy1.code: lazy1.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main --lazy < $< > $@ 

lazy2.code: lazy2.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} lazyml.Main --lazy < $< > $@ 

Main.java: Main.pre.java lazy1.code lazy2.code
	@printf "including generated code\n" 
	@cpp -P Main.pre.java > Main.java

Main.class: Main.java
	@printf "compiling $<" 
	@javac Main.java
	@printf "\n" 

clean:
	rm -rf *.class *.code Main.java *.orig

