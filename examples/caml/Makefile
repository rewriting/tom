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

tompeano: peanotype.cmo PeanoSimple1.tom.cmo
	ocamlc -o tompeano peanotype.cmo PeanoSimple1.tom.cmo

List1.cmo: List1.ml
	ocamlc -c List1.ml

List1.tom.cmo: List1.tom.ml
	ocamlc -c List1.tom.ml

list: tomExtension.cmo tomList.cmo List1.cmo
	ocamlc -o list tomExtension.cmo tomList.cmo List1.cmo

tomlist: List1.tom.cmo
	ocamlc -o tomlist List1.tom.cmo

erat: erat.t
	tom.src --camlCode erat.t
	ocamlopt -o erat erat.tom.ml

Eratopt: erat.t
	tom.src --camlCode -O erat.t
	ocamlopt -o eratopt erat.tom.ml

erat2: erat.tom.ml
	ocamlopt -o erat erat.tom.ml
