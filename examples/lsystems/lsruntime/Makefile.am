#
#   Lsystems
#

SUBDIRS         = adt graphic

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(JTOM):$$CLASSPATH

javadir         = .
JAVAROOT        = ${top_builddir}/examples/Lsystems/

BOOTSOURCES     = Ignore.java LsystemsInterface.java MatchLsystems.java LsystemsRuntime.java
java_JAVA       = $(BOOTSOURCES)

EXTRA_DIST      = $(BOOTSOURCES) lsystems.tom extras.tom
CLEANFILES      = LsystemsRuntime.java api.stamp
BUILT_SOURCES   = api.stamp

api: api.stamp
api.stamp: lsystems.adt
	$(ADTTOJAVA) -v --input $< --jtom --package lsruntime.adt --output adt
	mv adt/LsystemsMakeRules adt/LsystemsMakeRules.am
	echo timestamp > api.stamp

TOMFLAGS = --noWarning --import $(top_srcdir)/examples/Lsystems/lsruntime/adt
TOM = ${prefix}/bin/jtom.src
ADTTOJAVA = ${prefix}/bin/adt-to-java

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
