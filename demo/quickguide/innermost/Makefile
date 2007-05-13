all: Eval.class
	java Eval

Eval.java: Eval.t
	tom Eval.t

Eval.class: Eval.java
	javac Eval.java

clean:
	rm -rf eval Eval.java Eval*.class

