
# to be executed, you have to set the property aterm.home to the path where
# aterm is installed
CC=gcc
DICTTOC=${ATERM_HOME}/bin/dicttoc
#LDFLAGS=-L${ATERM_HOME}/lib -lATerm
#CFLAGS=-Wall -I${ATERM_HOME}/include -I.

all: loulou bus cfib fibATerm odd PeanoTest

loulou: loulou.tom.c
	${CC} -o loulou loulou.tom.c

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

PeanoTest: Peano_dict.c PeanoTest.tom.o Peano_dict.o Peano.o 
	${CC} -o $@ PeanoTest.tom.o Peano_dict.o Peano.o ${LDFLAGS}

Peano_dict.c: Peano.dict
	${DICTTOC} -no-date -dict Peano.dict

