#
#    TOM - To One Matching Compiler
#

SUBDIRS = images styles

EXTRA_DIST = 

WWWDIR = /local/www-equipes/protheo/SOFTWARES/tom
PAGES = index.html releases.html

www:
	chmod 666 ${PAGES}
	scp ${PAGES} ${LORIAUSER}@loria.loria.fr:${WWWDIR}
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
        done

