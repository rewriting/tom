#
#    TOM - To One Matching Compiler
#

SUBDIRS         = runtime tools exception xml parser checker compiler optimizer backend debug verifier

javadir         = ${prefix}/classes/jtom
stabledir 	= $(top_srcdir)/stable/jtom

TOMSOURCES  = TomBase.t
JAVASOURCES = Tom.java TomEnvironment.java
BOOTSOURCES	= $(TOMSOURCES:.t=.java) $(JAVASOURCES)
java_JAVA       = $(BOOTSOURCES)
EXTRA_DIST 	= $(BOOTSOURCES) $(TOMSOURCES) 
CLEANFILES 	= $(TOMSOURCES:.t=.java) tomjava.stamp
BUILT_SOURCES   = tomjava.stamp $(BOOTSOURCES)

LOCALFLAGS = --noWarning 
include $(top_srcdir)/src/make_rules

bootstep:
	@echo "cleaning phase"
	@$(MAKE) clean  >> $(BOOTSTRAPLOG)
	@echo "building phase"
	@$(MAKE) >> $(BOOTSTRAPLOG)
	@echo "installing phase"
	@$(MAKE) install >> $(BOOTSTRAPLOG)

BOOTSTRAPDIR     = ../jtom
BOOTSTRAPDIRCOPY = ../tmp/jtom.bootstrap
BOOTSTRAPLOG     = ../tmp/bootstrap.log

bootstrap:
	@rm -f $(BOOTSTRAPLOG)
	@rm -rf $(BOOTSTRAPDIRCOPY)  > $(BOOTSTRAPLOG)

	@echo
	@echo "BOOTSTRAPPING PHASE 1"
	@echo
	@$(MAKE) bootstep

	@echo
	@echo "BOOTSTRAPPING PHASE 2"
	@echo
	@$(MAKE) bootstep 
	@echo "copying phase"
	@$(INSTALL) -d $(BOOTSTRAPDIR) $(BOOTSTRAPDIRCOPY) >> $(BOOTSTRAPLOG)
	@cp -rp $(BOOTSTRAPDIR)/* $(BOOTSTRAPDIRCOPY)/  >> $(BOOTSTRAPLOG)

	@echo
	@echo "BOOTSTRAPPING PHASE 3"
	@echo
	@$(MAKE) bootstep

	@echo
	@echo "BOOTSTRAPPING CHECK"
	@echo "-------------------"
	@diff -r $(BOOTSTRAPDIR) $(BOOTSTRAPDIRCOPY)
	@rm -rf $(BOOTSTRAPDIRCOPY)

