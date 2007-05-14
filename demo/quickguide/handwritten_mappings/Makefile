all: HandMapping.class
	java HandMapping

HandMapping.java: HandMapping.t
	tom HandMapping.t

HandMapping.class: HandMapping.java
	javac HandMapping.java

clean:
	rm -rf handmapping HandMapping*.java HandMapping*.class

