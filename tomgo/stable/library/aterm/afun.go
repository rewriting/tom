package aterm

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/sharedobjects"
)

// AFun is a function symbol — the head of an ATermAppl. Two AFuns
// are equivalent when they have the same name, arity, and quoted-flag.
type AFun struct {
	name    string
	arity   int
	quoted  bool
	hash    uint32
	factory *Factory
}

func (f *AFun) Name() string  { return f.name }
func (f *AFun) Arity() int    { return f.arity }
func (f *AFun) IsQuoted() bool { return f.quoted }

func (f *AFun) Type() int    { return AFUN }
func (f *AFun) Hash() uint32 { return f.hash }

func (f *AFun) Equivalent(other sharedobjects.Term) bool {
	o, ok := other.(*AFun)
	return ok && f.name == o.name && f.arity == o.arity && f.quoted == o.quoted
}

func (f *AFun) Duplicate() sharedobjects.Term {
	clone := *f
	return &clone
}

// String renders the AFun exactly like Java's AFunImpl#toString —
// quoted symbols get backslash-escaped quotes around them, unquoted
// ones go raw. The output is suitable for splicing into a textual
// term representation.
func (f *AFun) String() string {
	if !f.quoted {
		return f.name
	}
	var b strings.Builder
	b.WriteByte('"')
	for i := 0; i < len(f.name); i++ {
		c := f.name[i]
		switch c {
		case '\\', '"':
			b.WriteByte('\\')
			b.WriteByte(c)
		case '\n':
			b.WriteString("\\n")
		case '\t':
			b.WriteString("\\t")
		case '\r':
			b.WriteString("\\r")
		default:
			if c < 32 {
				fmt.Fprintf(&b, "\\%03o", c)
			} else {
				b.WriteByte(c)
			}
		}
	}
	b.WriteByte('"')
	return b.String()
}
