all: Rules.class
	java Rules

Rules.java: Rules.t
	tom Rules.t

Rules.class: Rules.java
	javac Rules.java

clean:
	rm -rf rules Rules*.java Rules*.class

