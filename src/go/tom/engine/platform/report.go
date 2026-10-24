package platform

import (
	"fmt"
	"io"
	"strings"
)

// WriteDiag writes one diagnostic line, the protocol GoBridgePlugin reads
// on the standard error of tomgo: LEVEL TAB file TAB line TAB message; the
// tabs and line breaks of the file and of the message become spaces.
func WriteDiag(w io.Writer, level, file string, line int, msg string) {
	clean := strings.NewReplacer("\t", " ", "\r", " ", "\n", " ")
	fmt.Fprintf(w, "%s\t%s\t%d\t%s\n", level, clean.Replace(file), line, clean.Replace(msg))
}

// Reporter writes the diagnostics of the Go suffix (Java: TomMessage.error,
// warning and info, through the logger and the status handler of the
// platform) and counts them.
type Reporter struct {
	w        io.Writer
	verbose  bool
	errors   int
	warnings int
}

// NewReporter writes on w (tomgo: its standard error); Info writes only
// when verbose (the option verbose).
func NewReporter(w io.Writer, verbose bool) *Reporter {
	return &Reporter{w: w, verbose: verbose}
}

// Error is TomMessage.error.
func (r *Reporter) Error(file string, line int, m Message, args ...any) {
	r.errors++
	WriteDiag(r.w, "ERROR", file, line, m.Text(args...))
}

// Warning is TomMessage.warning.
func (r *Reporter) Warning(file string, line int, m Message, args ...any) {
	r.warnings++
	WriteDiag(r.w, "WARNING", file, line, m.Text(args...))
}

// Info is TomMessage.info, shown with --verbose only. port: GoBridgePlugin
// does not relay INFO lines yet (J4.1b).
func (r *Reporter) Info(file string, line int, m Message, args ...any) {
	if r.verbose {
		WriteDiag(r.w, "INFO", file, line, m.Text(args...))
	}
}

// HasError is the status handler's hasError.
func (r *Reporter) HasError() bool { return r.errors > 0 }

// Counts returns the numbers of errors and warnings written.
func (r *Reporter) Counts() (errors, warnings int) { return r.errors, r.warnings }
