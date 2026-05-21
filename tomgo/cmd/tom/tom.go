// Command tom runs the Tom compiler pipeline on a `.t` source file.
// It is the Go analogue of `java tom.engine.Tom` minus the
// XML-configurable pipeline: phases are wired in a fixed canonical
// order, matching Java's BootstrapPluginsList.
//
// Usage:
//
//	tom [flags] <input.t>
//
// Flags:
//
//	-o <file>     Write the AST dump to <file> (default: stdout).
//	-stop=<phase> Stop after the named phase (parser / transformer /
//	              syntaxchecker / desugarer / typer / typechecker /
//	              expander / compiler / optimizer / backend). Default:
//	              "backend" (run the whole chain).
//	-v            Verbose: print each phase as it runs.
package main

import (
	"flag"
	"fmt"
	"os"
	"path/filepath"
	"strings"

	"tom/tomgo/stable/tom"
	"tom/tomgo/stable/tom/backend"
	"tom/tomgo/stable/tom/compiler"
	"tom/tomgo/stable/tom/desugarer"
	"tom/tomgo/stable/tom/expander"
	"tom/tomgo/stable/tom/optimizer"
	"tom/tomgo/stable/tom/parser"
	"tom/tomgo/stable/tom/starter"
	"tom/tomgo/stable/tom/syntaxchecker"
	"tom/tomgo/stable/tom/transformer"
	"tom/tomgo/stable/tom/typechecker"
	"tom/tomgo/stable/tom/typer"
)

// phase ties a friendly name to a Run function. The order in this
// slice IS the canonical pipeline order — change it here and the
// compiler driver picks up the new flow.
type phase struct {
	name string
	run  func(tom.State) (tom.State, error)
}

var pipeline = []phase{
	{"starter", starter.Run},
	{"parser", parser.Run},
	{"transformer", transformer.Run},
	{"syntaxchecker", syntaxchecker.Run},
	{"desugarer", desugarer.Run},
	{"typer", typer.Run},
	{"typechecker", typechecker.Run},
	{"expander", expander.Run},
	{"compiler", compiler.Run},
	{"optimizer", optimizer.Run},
	{"backend", backend.Run},
}

func main() {
	out := flag.String("o", "", "write AST dump to file (default stdout)")
	stop := flag.String("stop", "backend", "stop after this phase (see -h)")
	verbose := flag.Bool("v", false, "print each phase as it runs")
	flag.Usage = func() {
		fmt.Fprintln(os.Stderr, "Usage: tom [flags] <input.t>")
		fmt.Fprintln(os.Stderr, "")
		flag.PrintDefaults()
		fmt.Fprintln(os.Stderr, "")
		fmt.Fprintln(os.Stderr, "Phases (in order):")
		for _, p := range pipeline {
			fmt.Fprintln(os.Stderr, "  "+p.name)
		}
	}
	flag.Parse()
	if flag.NArg() != 1 {
		flag.Usage()
		os.Exit(2)
	}

	input, err := filepath.Abs(flag.Arg(0))
	if err != nil {
		die("resolve input path: %v", err)
	}

	state := tom.State{Filename: input}
	for _, p := range pipeline {
		if *verbose {
			fmt.Fprintln(os.Stderr, "→ "+p.name)
		}
		state, err = p.run(state)
		if err != nil {
			die("%s: %v", p.name, err)
		}
		if p.name == *stop {
			break
		}
	}

	dump := fmt.Sprintf("%v\n", state.Code)
	// Normalise input path so the dump is reproducible across machines.
	dump = strings.ReplaceAll(dump, input, "__INPUT__")
	dump = strings.ReplaceAll(dump, filepath.Dir(input), "__DIR__")

	if *out == "" || *out == "-" {
		fmt.Print(dump)
	} else if err := os.WriteFile(*out, []byte(dump), 0o644); err != nil {
		die("write %s: %v", *out, err)
	}
}

func die(format string, args ...any) {
	fmt.Fprintf(os.Stderr, "tom: "+format+"\n", args...)
	os.Exit(1)
}
