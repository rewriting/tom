EXTRA_DIST = jtom.sh.src jtom.vim jtom.bat jtom.sh adt-to-java.bat adt-to-java.sh

bin_SCRIPTS = jtom jtom.stable jtom.src
#noinst_SCRIPTS = jtom.stable jtom.src

CLEANFILES =${bin_SCRIPTS}

top_abs_srcdir=$(shell cd ${top_srcdir}; pwd)
top_abs_builddir=$(shell cd ${top_builddir}; pwd)
tom_signature=${TOMSIGNATURE}


TSJAR=TomSignature-${VERSION}.jar
SETJAR=set-${VERSION}.jar
TNODEJAR=TNode-${VERSION}.jar


% : %.sh.src Makefile
	sed 's@__CLASSES__@${prefix}/classes:${prefix}/share/${TSJAR}:${prefix}/share/${SETJAR}:${prefix}/share/${TNODEJAR}:${top_abs_builddir}/stable:${top_abs_builddir}/stable/adt/${TSJAR}:${top_abs_builddir}/stable/adt/${SETJAR}:${top_abs_builddir}/stable/adt/${TNODEJAR}@g;\
	     s@__INCLUDE__@"-I ${prefix}/share/jtom -I ${top_abs_builddir}/stable/adt"@g;\
	     s@__JAVA_ATERM__@${JAVA_ATERM}@g' < $< > $@ && chmod +x $@

%.stable : %.sh.src Makefile
	sed 's@__CLASSES__@${top_abs_builddir}/stable:${top_abs_builddir}/stable/adt/${TSJAR}:${top_abs_builddir}/stable/adt/${SETJAR}:${top_abs_builddir}/stable/adt/${TNODEJAR}@g;\
	     s@__INCLUDE__@"-I ${top_abs_builddir}/stable/adt"@g;\
	     s@__JAVA_ATERM__@${JAVA_ATERM}@g' < $< > $@ && chmod +x $@

%.src : %.sh.src Makefile
	sed 's@__CLASSES__@${top_abs_builddir}/src:${top_abs_builddir}/src/adt:${top_abs_builddir}/stable:${top_abs_builddir}/stable/adt/${TSJAR}:${top_abs_builddir}/stable/adt/${SETJAR}:${top_abs_builddir}/stable/adt/${TNODEJAR}@g;\
	     s@__INCLUDE__@"-I ${top_abs_builddir}/src/adt -I ${top_abs_builddir}/stable/adt"@g;\
	     s@__JAVA_ATERM__@${JAVA_ATERM}@g' < $< > $@ && chmod +x $@
