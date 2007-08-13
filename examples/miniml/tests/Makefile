all: TestPlus.class TestFold.class
	@echo ""
	@echo "running examples :"
	@echo ""
	java TestPlus
	@echo ""
	java TestFold

clean:
	rm -r testfold testplus *.class *.java *.tom 

TestFold.class: TestFold.java
	javac TestFold.java

TestPlus.class: TestPlus.java
	javac TestPlus.java

TestFold.java: TestFold.t
	tom TestFold.t

TestPlus.java: TestPlus.t
	tom TestPlus.t

TestFold.t: fold.tom plus.tom mult.tom

TestPlus.t: plus.tom

fold.tom: fold.ml
	cd .. \
	&& java Compiler tests/fold.ml > tests/fold.tom \
	&& cd tests

plus.tom: plus.ml
	cd .. \
	&& java Compiler tests/plus.ml > tests/plus.tom \
	&& cd tests

mult.tom: mult.ml
	cd build \
	&& java Compiler tests/mult.ml > tests/mult.tom \
	&& cd tests

