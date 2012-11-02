all: src/Main.class

src/MOF.java: src/MOF.t
	cd src/ && tom -v -p MOF.t

src/Main.class: src/Main.java src/MOF.java
	cd src/ && javac Main.java

javadoc: src/MOF.java
	[ -e doc ] || mkdir doc/
	cd src/ && javadoc -d ../doc/ `find . -iname "*.java"`

.PHONY: clean test tom

clean:
	find . -name "*.class" -delete
	rm -fr src/term/TSL.java src/MOF.java doc/ src/mof/ out/*

run: src/Main.class
	cd src/ && java Main

test: clean all run
