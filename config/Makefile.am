bin_SCRIPTS = jtom-config

EXTRA_DIST = jtom-config.sh.src program-check.m4 README $(wildcard *.m4)

jtom-config: jtom-config.sh.src Makefile
	sed 's@__PREFIX__@${prefix}@g;\
	     s@__EXTERNAL_JARS__@${EXTERNAL_JARS}@g;\
             s@__VERSION__@${VERSION}@g' < $< > $@ && chmod +x $@

CLEANFILES = $(bin_SCRIPTS)
