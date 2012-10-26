all: src/MOF.class

tom: src/MOF.t src/term/TSL.t
	cd src/ && tom -v -p term/TSL.t
	cd src/ && tom -v -p MOF.t

src/MOF.class: src/MOF.java  
	cd src/ && javac MOF.java

src/MOF.java: javadoc

javadoc: tom
	[ -e doc ] || mkdir doc/
	cd src/ && javadoc -d ../doc/ `find . -iname "*.java"`

.PHONY: clean test tom

clean:
	find . -name "*.class" -delete
	rm -fr src/term/TSL.java src/MOF.java doc/

run: src/MOF.class
	cd src/ && java MOF

test: clean all run
