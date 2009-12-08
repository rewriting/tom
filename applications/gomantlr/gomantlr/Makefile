.SUFFIXES: .t .java

.t.java:
	cd ..;tom gomantlr/$<

all: GenerateGom.java

classclean:
	rm -f *.class

clean:
	make classclean
	rm -f $(foreach v,$(wildcard *.t),$(shell echo $(basename $(v)).java))
	rm -f *~
