OCAMLC?=ocamlc

all: tompeano tomlist peanomapped erat searchdup

peanotype.cmo: peanotype.ml
	${OCAMLC} -c peanotype.ml

tomExtension.cmo: tomExtension.ml
	${OCAMLC} -c tomExtension.ml

tomList.cmo: tomList.ml
	${OCAMLC} -c tomList.ml

PeanoSimple1.tom.cmo: PeanoSimple1.tom.ml
	${OCAMLC} -c PeanoSimple1.tom.ml

tompeano: peanotype.cmo PeanoSimple1.tom.cmo
	${OCAMLC} -o tompeano peanotype.cmo PeanoSimple1.tom.cmo

List1.cmo: List1.ml
	${OCAMLC} -c List1.ml

List1.tom.cmo: List1.tom.ml
	${OCAMLC} -c List1.tom.ml

list: tomExtension.cmo tomList.cmo List1.cmo
	${OCAMLC} -o list tomExtension.cmo tomList.cmo List1.cmo

tomlist: List1.tom.cmo
	${OCAMLC} -o tomlist List1.tom.cmo

erat: erat.tom.ml
	${OCAMLC} -o erat erat.tom.ml

searchdup: searchdup.tom.ml
	${OCAMLC} -o searchdup searchdup.tom.ml

eratopt: erat.tom.ml
	tom.src --camlCode -O erat.t
	ocamlopt -o eratopt erat.tom.ml

peanomapped: PeanoMapped.tom.ml
	${OCAMLC} -o peanomapped PeanoMapped.tom.ml
