package main

import (
	"fmt"
	"os"

	"tom/tomgo/stable/tom"
	"tom/tomgo/stable/tom/backend_go"
	"tom/tomgo/stable/tom/compiler"
	"tom/tomgo/stable/tom/desugarer"
	"tom/tomgo/stable/tom/parser"
	"tom/tomgo/stable/tom/starter"
	"tom/tomgo/stable/tom/syntaxchecker"
	"tom/tomgo/stable/tom/transformer"
	"tom/tomgo/stable/tom/typer"
)

func main() {
	if len(os.Args) < 3 {
		fmt.Fprintln(os.Stderr, "usage: tomgo_to_go <input.t> <output.go>")
		os.Exit(2)
	}
	s := tom.State{Filename: os.Args[1], Target: "go"}
	s, err := tom.Run(s,
		starter.Run, parser.Run, transformer.Run, syntaxchecker.Run,
		desugarer.Run, typer.Run, compiler.Run, backend_go.Run,
	)
	if err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(1)
	}
	if err := os.WriteFile(os.Args[2], s.Source, 0o644); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(1)
	}
}
