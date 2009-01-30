CP=${TOM_HOME}/../../examples/build:${CLASSPATH}

all: Test.class
	java Test

minisl.tom: minisl.mml
	@printf "compiling $<: " 
	@java -Xss128M -cp ${CP} miniml.Main < $< > $@ 

Test.t: minisl.tom
	@touch Test.t

Test.java: Test.t
	tom  Test.t

Test.class: Test.java
	javac Test.java

clean:
	rm -r test *.class *.tom Test.java

