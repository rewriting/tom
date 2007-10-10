all: build dist/build

build:
	ant

clean:
	ant clean
	rm -rf dist/build
	rm -rf dist/libs

dist/build: build
	cp -r build dist
	cp -r libs dist
	cp ../../src/dist/lib/* dist/libs

	
