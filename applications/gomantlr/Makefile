all:
	gom gomantlr.gom
	tom Gomantlr.t
	javac *.java
	java Gomantlr Java.g

clean:
	cd utils;make clean
	rm -f *.java
	rm -f *.class
	rm -rf gomantlr
