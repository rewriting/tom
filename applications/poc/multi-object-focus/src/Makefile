all: MOF.class

run: MOF.class
	java MOF

MOF.class: MOF.java 
	javac MOF.java

MOF.java: MOF.t
	tom -v -p MOF.t

.PHONY: clean

clean:
	rm -fr *.class MOF.java
