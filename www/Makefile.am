#
#    TOM - To One Matching Compiler
#

SUBDIRS = images styles

EXTRA_DIST = 

WWWDIR = /local/www-equipes/protheo/SOFTWARES/tom
PAGES = index.html releases.html

www:
	cp ${PAGES} ${WWWDIR}
	chmod 666 ${WWWDIR}/*.html
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
        done

