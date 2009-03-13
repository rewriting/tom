all: gom aterm converter mept implode

implode:
	tom ImplodeMept.t SdfTool.t
	javac sdfgom/ImplodeMept.java sdfgom/SdfTool.java

mept:
	tom TestMept.t
	javac sdfgom/TestMept.java

testaterm: aterm

aterm:
	tom TestATerm.t
	javac TestATerm.java

converter: gom
	tom SDFConverter.t MEPTConverter.t
	javac sdfgom/MEPTConverter.java sdfgom/SDFConverter.java

javac: tom
	javac sdfgom/*.java

tom: gom
	tom *.t

gom:
	gom *.gom

clean: clean.aterm clean.sdfgom clean.mept clean.bool

clean.aterm: 
	rm -f TestATerm.class
	rm -f TestATerm.java
	rm -Rf testaterm

clean.sdfgom:
	rm -Rf sdfgom

clean.mept: 
	rm -Rf mept

clean.bool: 
	rm -Rf bool

clean.implode: 
	rm -f sdfgom/Implode*

clean.testmept: 
	rm -f sdfgom/TestMept*
