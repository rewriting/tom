all: MOF.class

tom: MOF.t term/TSL.t
	tom -v -p term/TSL.t
	tom -v -p MOF.t

MOF.class: tom 
	javac MOF.java

.PHONY: clean run test tom

clean:
	find . -name "*.class" -delete
	rm -fr term/TSL.java MOF.java

run: MOF.class
	java MOF

test: clean all run
