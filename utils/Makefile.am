EXTRA_DIST = jtom.sh.src jtom.vim jtom.bat jtom.sh adt-to-java.bat adt-to-java.sh

bin_SCRIPTS = jtom jtom.stable jtom.src
#noinst_SCRIPTS = jtom.stable jtom.src

CLEANFILES =${bin_SCRIPTS}

top_abs_srcdir=$(shell cd ${top_srcdir}; pwd)
top_abs_builddir=$(shell cd ${top_builddir}; pwd)

% : %.sh.src Makefile
	sed 's@__CLASSES__@${prefix}/classes:${top_abs_builddir}/stable@g;\
	     s@__INCLUDE__@"-I ${prefix}/share/jtom -I ${top_abs_builddir}/stable/jtom/runtime/xml/adt"@g;\
	     s@__JAVA_ATERM__@${JAVA_ATERM}@g' < $< > $@ && chmod +x $@

%.stable : %.sh.src Makefile
	sed 's@__CLASSES__@${top_abs_builddir}/stable@g;\
	     s@__INCLUDE__@"-I ${top_abs_builddir}/stable/jtom/runtime/xml/adt"@g;\
	     s@__JAVA_ATERM__@${JAVA_ATERM}@g' < $< > $@ && chmod +x $@

%.src : %.sh.src Makefile
	sed 's@__CLASSES__@${top_abs_builddir}/src:${top_abs_builddir}/stable@g;\
	     s@__INCLUDE__@"-I ${top_abs_builddir}/src/jtom/runtime/xml/adt -I ${top_abs_builddir}/stable/jtom/runtime/xml/adt"@g;\
	     s@__JAVA_ATERM__@${JAVA_ATERM}@g' < $< > $@ && chmod +x $@
