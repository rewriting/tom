all: a.out peano

a.out: peanotype.cmo tomExtension.cmo PeanoSimple1.cmo
	ocamlc -o a.out peanotype.cmo tomExtension.cmo PeanoSimple1.cmo

peanotype.cmo: peanotype.ml
	ocamlc -c peanotype.ml

tomExtension.cmo: tomExtension.ml
	ocamlc -c tomExtension.ml

PeanoSimple1.cmo: PeanoSimple1.ml
	ocamlc -c PeanoSimple1.ml

PeanoSimple1.tom.cmo: PeanoSimple1.tom.ml
	ocamlc -c PeanoSimple1.tom.ml

peano: peanotype.cmo tomExtension.cmo PeanoSimple1.tom.cmo
	ocamlc -o peano peanotype.cmo tomExtension.cmo PeanoSimple1.tom.cmo
