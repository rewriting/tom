all: peano tompeano list

peano: peanotype.cmo tomExtension.cmo PeanoSimple1.cmo
	ocamlc -o peano peanotype.cmo tomExtension.cmo PeanoSimple1.cmo

peanotype.cmo: peanotype.ml
	ocamlc -c peanotype.ml

tomExtension.cmo: tomExtension.ml
	ocamlc -c tomExtension.ml

tomList.cmo: tomList.ml
	ocamlc -c tomList.ml

PeanoSimple1.cmo: PeanoSimple1.ml
	ocamlc -c PeanoSimple1.ml

PeanoSimple1.tom.cmo: PeanoSimple1.tom.ml
	ocamlc -c PeanoSimple1.tom.ml

tompeano: peanotype.cmo tomExtension.cmo PeanoSimple1.tom.cmo
	ocamlc -o tompeano peanotype.cmo tomExtension.cmo PeanoSimple1.tom.cmo

List1.cmo: List1.ml
	ocamlc -c List1.ml

list: tomExtension.cmo tomList.cmo List1.cmo
	ocamlc -o list tomExtension.cmo tomList.cmo List1.cmo

tomlist: tomExtension.cmo List1.tom.cmo
	ocamlc -o list tomExtension.cmo List1.tom.cmo
