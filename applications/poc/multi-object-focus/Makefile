TOMGENERATED=src/MiniML.java
M4GENERATED=src/MiniML.t

GENERATED=${TOMGENERATED} ${M4GENERATED}

all: help

build: src/Main.class

src/MOF.java: src/MOF.t
	cd src/ && tom -v -p MOF.t

src/MiniML.java: src/MiniML.m4
	cd src/ && m4 MiniML.m4 >MiniML.t && tom -v MiniML.t

src/Main.class: src/Main.java ${TOMGENERATED}
	cd src/ && javac Main.java

javadoc: ${TOMGENERATED}
	[ -e doc ] || mkdir doc/
	cd src/ && javadoc -d ../doc/ `find . -iname "*.java"`

.PHONY: clean test tom help

clean:
	find . -name "*.class" -delete
	rm -fr src/term/TSL.java ${GENERATED} doc/ src/mof/ src/miniml out/* out.xml

run: src/Main.class
	cd src/ && java Main > ../out.xml
	xmllint --recover out.xml | xmllint -format - > outtidy.xml
	@echo
	@echo "**************************************"
	@echo "*    OUTPUT WRITTEN IN outtidy.xml   *"
	@echo "*                                    *"
	@echo "*    Open it in any xml viewer       *"
	@echo "*    to see beatiful trace trees     *"
	@echo "**************************************"
	@echo

test: clean run

bench: src/Main.class
	cd src/ && time java Main > /dev/null

help:
	@echo "make build       : build code"
	@echo "make run         : run the test"
	@echo "make test        : build code and run test"
	@echo "make javadoc     : build doc in doc/"
	@echo "make clean       : clean everything"
