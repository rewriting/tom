#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/exception
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH

stabledir 	= $(top_srcdir)/stable/jtom/exception
BOOTSOURCES	= TomException.java TomRuntimeException.java CheckWarningException.java CheckErrorException.java TomIncludeException.java
java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST      = $(BOOTSOURCES)
JAVAROOT        = ${top_builddir}/src/

bootinstall :
	$(mkinstalldirs) $(stabledir)
	cp -f $(BOOTSOURCES) $(stabledir)
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
	done
