.SUFFIXES: .t .java

.t.java:
	cd ..;tom utils/$<

all: PrintGomModule.java AST2ATerm.java

clean:
	rm -f $(foreach v,$(wildcard *.t),$(shell echo $(basename $(v)).java))
	rm -f *.class
	rm -f *~
