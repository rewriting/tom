all: TestSystemF.class
	java TestSystemF

TestSystemF.java: TestSystemF.t
	tom TestSystemF.t

TestSystemF.class: TestSystemF.java
	javac TestSystemF.java

TestSystemF.t: systemf/SystemF.tom

systemf/SystemF.tom: systemf.gom
	gom systemf.gom

clean:
	rm -rf systemf *.class *.java

