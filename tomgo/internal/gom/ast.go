package gom

// Module is the root of a parsed `.gom` file. It supports a dotted
// module name, a list of imported names, a sequence of sort
// declarations and an optional list of hook constructs.
//
// Hooks are captured as opaque records — their body is kept as raw
// text. Recognising the body and lowering it to Go is the backend's
// job (currently only `sort HookList:block()` from Objects.gom).
type Module struct {
	Name    []string // dotted module name parts, e.g. ["gom","b","u","i","l","t","i","n","Builtin"]
	Imports []string // names imported via `imports …`
	Sorts   []SortDecl
	Hooks   []GomHook
}

// QualifiedName returns the dotted module name as a single string.
func (m *Module) QualifiedName() string {
	return joinDots(m.Name)
}

// SortDecl declares a sort with one or more alternatives:
//
//	SortName = Op1(...) | Op2(...) | ...
type SortDecl struct {
	Name         string
	Alternatives []Alternative
	Line         int
}

// Alternative is one production for a sort: an operator name and its
// (possibly empty, possibly variadic) argument list.
type Alternative struct {
	Op       string
	Args     []Arg
	Variadic bool // true iff Args has exactly one element with no Name and Type.Variadic semantics
	Line     int
}

// GomHook captures a Gom hook construct of the form
//
//	[scope] PointCut ':' Kind '(' Args ')' '{' Body '}'
//
// where Scope is one of {"sort","module","operator",""} and Body is the
// raw text between the outer braces (string and `//`/`/* */` comments
// inside the body are skipped while looking for the matching `}`).
type GomHook struct {
	Scope    string
	PointCut string
	Kind     string
	Args     []string
	Body     string
	Line     int
}

// Arg is one argument of an operator. Two shapes:
//   - named slot:    Name != "", Type set, Variadic == false  (e.g. `b:Bool`)
//   - variadic type: Name == "", Type set, Variadic == true   (e.g. `Wrapper*` inside `Vary(Wrapper*)`)
type Arg struct {
	Name     string
	Type     string
	Variadic bool
}

func joinDots(parts []string) string {
	switch len(parts) {
	case 0:
		return ""
	case 1:
		return parts[0]
	}
	n := len(parts) - 1
	for _, p := range parts {
		n += len(p)
	}
	b := make([]byte, 0, n)
	for i, p := range parts {
		if i > 0 {
			b = append(b, '.')
		}
		b = append(b, p...)
	}
	return string(b)
}
