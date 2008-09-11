CP=..:${CLASSPATH}

all: Test.class
	java Test

Test.t: fib10.tom plus.tom apply_n_times.tom fold_left.tom

Test.java: Test.t
	tom Test.t

Test.class: Test.java
	javac Test.java

fib10.tom: fib10.mml
	cd ..; java Main < test/fib10.mml > test/fib10.tom; cd test

plus.tom: plus.mml
	cd ..; java Main < test/plus.mml > test/plus.tom; cd test

fold_left.tom: fold_left.mml
	cd ..; java Main < test/fold_left.mml > test/fold_left.tom; cd test

apply_n_times.tom: apply_n_times.mml
	cd ..; java Main < test/apply_n_times.mml > test/apply_n_times.tom; cd test

clean:
	rm -r test *.class *.tom Test.java

