#
#    TOM - To One Matching Compiler
#

TSADT_STABLE=$(top_srcdir)/stable/adt/TomSignature-${VERSION}.jar
SETADT_STABLE=$(top_srcdir)/stable/adt/Set-${VERSION}.jar
TNODEADT_STABLE=$(top_srcdir)/stable/adt/TNode-${VERSION}.jar

javadir         = ${prefix}/test
JAVAROOT        = ${top_builddir}/test
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir):$(top_builddir)/src:$(top_builddir)/src/adt:$(top_builddir)/stable:$(TSADT_STABLE):$(SETADT_STABLE):$(TNODEADT_STABLE):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

source_java     = Peano.t \
		  List.t \
		  Array.t \
		  TestMatch.t \
		  TestList.t \
		  TestBuiltin.t \
		  Xml.t \
		  Record.t

source_c        = cfib1.t \
                  loulou.t

check_JAVA      = $(source_java:.t=.java)

PROGS           = cfib1 loulou

TESTS           = $(foreach t, ${check_JAVA}, $t.test) \
                  $(PROGS)

EXTRA_DIST      = $(source_java) $(source_c) $(check_JAVA)

%.java.test : %.java Makefile
	@(echo '#! /bin/sh';\
	 echo 'set -e';\
	 echo 'CLASSPATH=${top_builddir}:${top_builddir}/src:${top_builddir}/src/adt:${top_builddir}/stable:..:$(TSADT_STABLE):$(SETADT_STABLE):$(TNODEADT_STABLE):$(JAVA_ATERM):${SHARED_OBJECTS}:${CLASSPATH}';\
	 echo 'export CLASSPATH';\
	 echo "java $* ${srcdir}";\
	 echo "rm $*.java.test";\
	) > $@ && chmod +x $@

CLEANFILES = *.test $(BUILT_SOURCES) cfib1 loulou

EXEEXT =
noinst_PROGRAMS         = $(PROGS)
cfib1_SOURCES           = cfib1.tom.c
loulou_SOURCES          = loulou.tom.c

BUILT_SOURCES   = $(check_JAVA) \
		  cfib1.tom.c loulou.tom.c

# we use the last compiled compiler to compile the testsuite
TOMFLAGS = 
TOMSTABLE = ${top_builddir}/utils/jtom.stable
TOMSRC = ${top_builddir}/utils/jtom.src
TOMTEST = $(TOMSRC)

Record.java: Record.t
	$(TOMTEST) $(TOMFLAGS) --lazyType  Record.t

%.java : %.t
	$(TOMTEST) $(TOMFLAGS) $*

%.tom.c : %.t
	$(TOMTEST) --cCode $(TOMFLAGS) $*
