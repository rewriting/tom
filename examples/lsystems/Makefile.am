#
#   Lsystems
#

SUBDIRS         = lsruntime

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(JTOM):$$CLASSPATH

javadir         = .
JAVAROOT        = ${top_builddir}/examples/Lsystems/

BOOTSOURCES     = Lsystems1.java Lsystems2.java Lsystems3.java Lsystems4.java Lsystems5.java Lsystems6.java Lsystems7.java RunExamples.java
java_JAVA       = $(BOOTSOURCES)


EXTRA_DIST      = $(BOOTSOURCES) Lsystems1.t Lsystems2.t Lsystems3.t Lsystems4.t Lsystems5.t Lsystems6.t Lsystems7.t
CLEANFILES      = $(BOOTSOURCES)
BUILT_SOURCES   = 


TOMFLAGS = --noWarning --import $(top_srcdir)/examples/Lsystems/lsruntime/adt --import $(top_srcdir)/examples/Lsystems/lsruntime
TOM = ${prefix}/bin/jtom.src

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
