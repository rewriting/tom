// Command tomgo is the Go side of the Tom compiler (spec J4): the
// subcommand suffix reads on stdin the bundle the Java compiler hands over
// (package tom/engine/bridge) and runs the phases ported to Go. In J4.0
// no phase is ported: suffix refuses them, and suffix --echo only rewrites
// the bundle (the identity check of the bridge).
package main

import (
	"bufio"
	"errors"
	"fmt"
	"io"
	"os"

	"tom/engine/bridge"
)

const usage = "usage: tomgo suffix [--echo]   (bundle on stdin)\n"

// ported lists the phases the Go suffix can run (none in J4.0).
var ported = map[string]bool{}

func main() { os.Exit(run(os.Args[1:], os.Stdin, os.Stdout, os.Stderr)) }

func run(args []string, stdin io.Reader, stdout, stderr io.Writer) int {
	if len(args) == 0 || args[0] != "suffix" {
		fmt.Fprint(stderr, usage)
		return 2
	}
	echo := false
	for _, a := range args[1:] {
		if a != "--echo" {
			fmt.Fprint(stderr, usage)
			return 2
		}
		echo = true
	}
	b, err := bridge.Read(stdin)
	if err != nil {
		line, msg := 0, err.Error()
		var be *bridge.Error
		if errors.As(err, &be) {
			line, msg = be.Line, be.Msg
		}
		fmt.Fprintf(stderr, "ERROR tomgo - %d %s\n", line, msg)
		return 1
	}
	if echo {
		w := bufio.NewWriter(stdout)
		if err := b.Write(w); err == nil {
			err = w.Flush()
		}
		if err != nil {
			fmt.Fprintf(stderr, "ERROR tomgo %s 0 %v\n", b.Input, err)
			return 1
		}
		return 0
	}
	if !ported[b.From] {
		fmt.Fprintf(stderr, "ERROR tomgo %s 0 phase %s is not ported to Go yet\n", b.Input, b.From)
		return 2
	}
	return 0
}
