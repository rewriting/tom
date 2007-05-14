all: Mapping.class
	java Mapping

Mapping.java: Mapping.t
	tom Mapping.t

Mapping.class: Mapping.java
	javac Mapping.java

clean:
	rm -rf mapping Mapping.java Mapping.class

