#
#    TOM - To One Matching Compiler
#

SUBDIRS = 

EXTRA_DIST = 

WWWDIR = /local/www-equipes/protheo/SOFTWARES/tom
IMAGESDIR = ${WWWDIR}/images
IMAGES = tom_logo.jpg tom_logo.png

www:
	cp ${IMAGES} ${IMAGESDIR}
	chmod 666 ${IMAGESDIR}/*.*
	list='$(SUBDIRS)'; for subdir in $$list; do \
           (cd $$subdir ; $(MAKE) $@ ;) \
        done

