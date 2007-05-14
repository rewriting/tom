all: Collect.class
	java Collect

Collect.java: Collect.t
	tom Collect.t

Collect.class: Collect.java
	javac Collect.java

clean:
	rm -rf collect Collect.java Collect*.class

