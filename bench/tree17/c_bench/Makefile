all: cbench

cbench.tom.c: cbench.t
	tom -c cbench.t

cbench: cbench.tom.c
	gcc cbench.tom.c -g -lATerm -o cbench

