#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/runtime/set/jgtreeset
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH


JAVASOURCES     = SetFactory.java SetConstructor.java\
		  JGTreeSet.java JGTreeSetImpl.java\
		  JGTreeSet_Branch.java JGTreeSet_BranchImpl.java\
		  JGTreeSet_EmptySet.java JGTreeSet_EmptySetImpl.java\
		  JGTreeSet_Pair.java JGTreeSet_PairImpl.java\
		  JGTreeSet_Singleton.java JGTreeSet_SingletonImpl.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES)

JAVAROOT        = ${top_builddir}/stable/

