#!/bin/sh
    
#default is to generate all

usage () {
        echo "Usage: $name [ -html (HTML version only) | -dvips (DVI and PS only) | -all ] " 1>&2
	exit
}

if [ $# -ge 1 ]; then
    for x in "$@"; do
    case "$x" in
    -html)
	hevea -fix -o manual1.html manual.hva manual.tex
	hevea -o manual1.html manual.hva manual.tex
	hacha -o manual.html manual1.html
	shift
	;;
    -dvips) 
	shift
	latex manual.tex
	dvips manual.dvi >! manual.ps
	;;
    -all)
	hevea -fix -o manual1.html manual.hva manual.tex
	hevea -o manual1.html manual.hva manual.tex
	hacha -o manual.html manual1.html
	latex manual.tex
	dvips manual.dvi -o manual.ps
        ;;
    -*)
	usage
        ;;    
    esac
    done
else
    hevea -fix -o manual1.html manual.hva manual.tex
    hevea -o manual1.html manual.hva manual.tex
    hacha -o manual.html manual1.html
    latex manual.tex
    dvips manual.dvi -o manual.ps
fi
