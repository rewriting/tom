.SUFFIXES: .t .java

.t.java:
	cd ../..;tom aterm2antlrgrammar/exceptions/$<

all: AntlrWrongActionException.java AntlrWrongActionsException.java AntlrWrongArgsException.java AntlrWrongCommentException.java AntlrWrongElementException.java AntlrWrongExceptionException.java AntlrWrongGrammarException.java AntlrWrongIdException.java AntlrWrongIntException.java AntlrWrongOptionException.java AntlrWrongOptionsException.java AntlrWrongRetException.java AntlrWrongRuleException.java AntlrWrongTokensException.java RecognitionException.java

clean:
	rm -f $(foreach v,$(wildcard *.t),$(shell echo $(basename $(v)).java))
	rm -f *.class
	rm -f *~
