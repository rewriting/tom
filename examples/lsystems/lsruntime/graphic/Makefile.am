#
#   Lsystems
#

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$(JTOM):$$CLASSPATH

javadir         = .
JAVAROOT        = ${top_builddir}/examples/Lsystems/

BOOTSOURCES     = LsystemsView.java Point3D.java LsystemsDraw.java
java_JAVA       = $(BOOTSOURCES)

EXTRA_DIST      = $(BOOTSOURCES) LsystemsDraw.t
CLEANFILES      = LsystemsDraw.java
BUILT_SOURCES   = 

TOMFLAGS = --noWarning --import $(top_srcdir)/examples/Lsystems/lsruntime/adt --import $(top_srcdir)/examples/Lsystems/lsruntime
TOM = ${prefix}/bin/jtom.src

%.java : %.t
	$(TOM) $(TOMFLAGS) $*
