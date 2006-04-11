all: PeanoTest

PeanoTest: PeanoTest.class
	java PeanoTest

PeanoTest.class: peano/PeanoFactory.java PeanoTest.java
	rm -f peano/*.class *.class
	javac PeanoTest.java

PeanoTest.java: PeanoTest.t
	jtom --import peano PeanoTest.t

peano/PeanoFactory.java: peano Peano.sdf
	sdf2-to-java -p peano -o peano -j -i Peano.sdf -v -I

peano:
	mkdir peano

clean: 
	rm -rf peano PeanoTest.java *.class
