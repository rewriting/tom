all: RmDoubles.class
	java RmDoubles

RmDoubles.java: RmDoubles.t
	tom RmDoubles.t

RmDoubles.class: RmDoubles.java
	javac RmDoubles.java

clean:
	rm -rf rmdoubles RmDoubles.java RmDoubles*.class

