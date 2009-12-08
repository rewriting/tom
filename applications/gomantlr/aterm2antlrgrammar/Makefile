.SUFFIXES: .t .java

.t.java:
	cd ..;tom aterm2antlrgrammar/$<

all: ATerm2AntlrAction.java ATerm2AntlrAndElement.java ATerm2AntlrArgs.java ATerm2AntlrBlock.java ATerm2AntlrCharRange.java ATerm2AntlrClosure.java ATerm2AntlrComment.java ATerm2AntlrElement.java ATerm2AntlrException.java ATerm2AntlrGrammar.java ATerm2AntlrId.java ATerm2AntlrInt.java ATerm2AntlrNotElement.java ATerm2AntlrOptional.java ATerm2AntlrOption.java ATerm2AntlrOptions.java ATerm2AntlrOrElement.java ATerm2AntlrPositiveClosure.java ATerm2AntlrRet.java ATerm2AntlrRule.java ATerm2AntlrScope.java ATerm2AntlrTokens.java ATerm2AntlrUnrecognized.java ATerm2AntlrWrong.java String2AntlrString.java
	cd exceptions;make

classclean:
	cd exceptions;make classclean
	rm -f *.class

clean:
	make classclean
	cd exceptions;make clean
	rm -f $(foreach v,$(wildcard *.t),$(shell echo $(basename $(v)).java))
	rm -f *~
