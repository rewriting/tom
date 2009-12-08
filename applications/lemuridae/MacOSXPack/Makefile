all : build libtom

build : 
	cp -r ../build Lemuridae.app/Contents/Resources/

libtom :
	mkdir Lemuridae.app/Contents/Resources/libtom
	cp ../../../src/dist/lib/* Lemuridae.app/Contents/Resources/libtom/

clean :
	rm -rf Lemuridae.app/Contents/Resources/libtom Lemuridae.app/Contents/Resources/build/
