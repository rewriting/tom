#
#    TOM - To One Matching Compiler
#

EXTRA_DIST = 

WWWDIR = /local/www-equipes/protheo/SOFTWARES/tom
IMAGESDIR = ${WWWDIR}/images
IMAGES = tom_logo.jpg tom_logo.png

www:
	chmod 666 ${IMAGES}
	scp ${IMAGES} ${LORIAUSER}@loria.loria.fr:${IMAGESDIR}
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
        done

