#
#   Lsystems
#

include LsystemsMakeRules.am

CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

javadir         = .
JAVAROOT        = ${top_builddir}/examples/Lsystems/

BOOTSOURCES     = ${LsystemsAPI}
java_JAVA       = $(BOOTSOURCES)
CLEANFILES      = ${LsystemsAPI} lsystems.signature

EXTRA_DIST      = $(BOOTSOURCES) LsystemsMakeRules.am lsystems.signature

