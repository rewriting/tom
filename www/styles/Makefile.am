#
#    TOM - To One Matching Compiler
#

EXTRA_DIST = 

WWWDIR = /local/www-equipes/protheo/SOFTWARES/tom
STYLESDIR = ${WWWDIR}/styles
STYLES = mainstyle.css

www:
	cp ${STYLES} ${STYLESDIR}
	chmod 666 ${STYLESDIR}/*.css
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
        done

