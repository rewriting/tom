
CC=gcc
DICTTOC=dicttoc
LDFLAGS=-L/home/tonio/apps/lib -lATerm
CFLAGS=-Wall -I/home/tonio/apps/include -I.

Peano_dict.c: Peano.dict
	${DICTTOC} -no-date -dict Peano.dict

loulou: loulou.tom.c
	${CC} -o loulou loulou.c

bus: bus.tom.c
	${CC} -o bus bus.tom.c

cfib: cfib.tom.c
	${CC} -o cfib cfib.tom.c

fibATerm: fibATerm.tom.o
	${CC} -o $@ fibATerm.tom.o ${LDFLAGS}

odd: odd.tom.o
	${CC} -o $@ odd.tom.o ${LDFLAGS}
	
nqueens: nqueens.tom.o
	${CC} -o $@ nqueens.tom.o ${LDFLAGS}
	
nqueens2: nqueens2.tom.o
	${CC} -o $@ nqueens2.tom.o ${LDFLAGS}
	
Peano_dict.o: Peano_dict.c

PeanoTest: PeanoTest.tom.o Peano_dict.o
	${CC} -o $@ PeanoTest.tom.o Peano_dict.o ${LDFLAGS}
