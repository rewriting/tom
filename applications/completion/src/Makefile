OCAMLC=ocamlc.opt -g
OCAMLOPT=ocamlopt.opt
OCAMLLEX=ocamllex.opt
OCAMLYACC=ocamlyacc -v
TOM=tom

SOURCES=globals.cmo datatypes.cmo comp.cmo interp.cmo parser.cmo lexer.cmo main.cmo
OPTSRC= $(SOURCES:.cmo=.cmx)

BEST=opt

CLEAN= *.cmo *.cmi *.cmx *.o parser.ml lexer.ml parser.mli parser.output \
	comp comp.opt comp.byte comp.ml

all: comp.$(BEST)

comp.byte: $(SOURCES)
	$(OCAMLC) $(SOURCES) -o comp.byte

comp.opt: $(OPTSRC)
	$(OCAMLOPT) $(OPTSRC) -o comp.opt

.SUFFIXES: .ml .cmo .t .mly .mll .mli .cmi .cmx

.ml.cmo: 
	$(OCAMLC) -c $<

.t.ml:
	$(TOM) --camlCode -o $*.ml $<

.mll.ml:
	$(OCAMLLEX) $<

.mly.mli:
	$(OCAMLYACC) $<

.mly.ml:
	$(OCAMLYACC) $<

.mli.cmi:
	$(OCAMLC) -c $<

.ml.cmx:
	$(OCAMLOPT) -c $<

parser.ml: parser.cmi

parser.cmo: parser.cmi

comp.ml: datatypes_tom.t comp.cmi

fix: comp.ml
	sed "s/\([&][&] \)\(tom.match[0-9]*.tom.anti.constraints.status\)/\1!\2/g" \
		<$< > /tmp/comp.ml
	mv /tmp/comp.ml comp.ml

clean:
	-rm $(CLEAN)

dep: *.ml *.mli
	ocamldep *.ml *.mli > .depends

include .depends