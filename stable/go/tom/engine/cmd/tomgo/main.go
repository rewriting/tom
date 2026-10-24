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
	"strings"

	"tom/engine/bridge"
	"tom/engine/platform"
)

const usage = "usage: tomgo suffix [--echo]   (bundle on stdin)\n"

// ported lists the phases the Go suffix can run (none in J4.0), keyed by
// the lower-cased Tom.config plugin name without spaces: the bundle's
// "from" is compared case-insensitively.
var ported = map[string]bool{}

// bundleFile is the "file" of the diagnostics about the bundle itself.
const bundleFile = "-bundle-"

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
		platform.WriteDiag(stderr, "ERROR", bundleFile, line, msg)
		return 1
	}
	if echo {
		w := bufio.NewWriter(stdout)
		if err := b.Write(w); err == nil {
			err = w.Flush()
		}
		if err != nil {
			platform.WriteDiag(stderr, "ERROR", b.InputName, 0, err.Error())
			return 1
		}
		return 0
	}
	if !ported[strings.ToLower(b.From)] {
		platform.WriteDiag(stderr, "ERROR", b.InputName, 0, fmt.Sprintf("phase %s is not ported to Go yet", b.From))
		return 2
	}
	return 0
}
