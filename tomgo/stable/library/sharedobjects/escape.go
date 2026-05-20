package sharedobjects

import (
	"fmt"
	"strings"
)

// JavaEscape returns s wrapped in double-quotes with AT-format escapes —
// the same convention used by aterm.AFunImpl.toString() in the Java
// reference (stable/lib/runtime/aterm.jar). Tom's AST `String()` methods,
// emitted by the generated code for string slots, call this so that AST
// dumps are byte-identical to the Java reference parser output.
//
// Escape table (verified empirically with a small Java program against
// the stable Tom build — see reports/escape-survey.md for the dump):
//
//	'\\' → \\          '\b' → \b
//	'"'  → \"          '\f' → \f
//	'\n' → \n          '\'' → \'
//	'\t' → \t          '`'  → \140     (TOM-specific, backquote delimiter)
//	'\r' → \r
//	char < 32 or >= 127 → \NNN          (3-digit octal)
//	other printable ASCII → as-is
func JavaEscape(s string) string {
	var b strings.Builder
	b.Grow(len(s) + 2)
	b.WriteByte('"')
	for _, c := range s {
		switch c {
		case '\\':
			b.WriteString(`\\`)
		case '"':
			b.WriteString(`\"`)
		case '\n':
			b.WriteString(`\n`)
		case '\t':
			b.WriteString(`\t`)
		case '\r':
			b.WriteString(`\r`)
		case '\b':
			b.WriteString(`\b`)
		case '\f':
			b.WriteString(`\f`)
		case '\'':
			b.WriteString(`\'`)
		case '`':
			b.WriteString(`\140`)
		default:
			if c < 32 || c >= 127 {
				fmt.Fprintf(&b, `\%03o`, c)
			} else {
				b.WriteRune(c)
			}
		}
	}
	b.WriteByte('"')
	return b.String()
}
