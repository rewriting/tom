CP=${TOM_HOME}/../../examples/build:${CLASSPATH}

all: Test.class
	java Test

%.tom: %.mml
	@printf "compiling $<: " 
	@java -cp ${CP} miniml.Main < $< > $@ 

Test.t: plus.tom fib10.tom apply_n_times.tom fold_left.tom

Test.java: Test.t
	tom Test.t

Test.class: Test.java
	javac Test.java

clean:
	rm -r test *.class *.tom Test.java

