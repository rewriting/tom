all: PeanoTest

PeanoTest: PeanoTest.tom.o Peano.o Peano_dict.o
	gcc -o PeanoTest PeanoTest.tom.o Peano.o Peano_dict.o -L$(ELANDIR)/lib -lATerm

PeanoTest.tom.o: PeanoTest.tom.c
	gcc -c -I$(ELANDIR)/include PeanoTest.tom.c

Peano.o: Peano.c
	gcc -c -I$(ELANDIR)/include Peano.c

Peano_dict.o: Peano_dict.c
	gcc -c -I$(ELANDIR)/include Peano_dict.c

PeanoTest.tom.c: PeanoTest.t
	jtom -c PeanoTest.t

Peano.t: Peano.adt
	adt-to-c -i Peano.adt -j -v

Peano.adt: Peano.sdf
	sdf2-to-adt -i Peano.sdf -I

clean: 
	rm -rf peano PeanoTest.java *.class
