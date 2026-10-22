// Package aterm is the small subset of the Java aterm library (aterm.*,
// aterm.pure.*) used by the test/*.t programs ported to Tom+Go: function
// symbols, applications and lists, built by a factory that maximally
// shares them (two equal terms are the same pointer, so == is equality,
// as in Java), plus a parser for the textual syntax ("f(a,[b,c])").
package aterm

import (
	"fmt"
	"strings"
	"sync"
)

// ATerm is a maximally shared term: an *ATermAppl or an *ATermList.
type ATerm interface {
	fmt.Stringer
	id() int
}

// AFun is a function symbol (name, arity, quoted).
type AFun struct {
	name   string
	arity  int
	quoted bool
	uid    int
}

func (f *AFun) GetName() string { return f.name }
func (f *AFun) GetArity() int   { return f.arity }
func (f *AFun) IsQuoted() bool  { return f.quoted }

// ATermAppl is the application of an AFun to arguments.
type ATermAppl struct {
	fun  *AFun
	args []ATerm
	uid  int
}

func (a *ATermAppl) id() int                  { return a.uid }
func (a *ATermAppl) GetAFun() *AFun           { return a.fun }
func (a *ATermAppl) GetName() string          { return a.fun.name }
func (a *ATermAppl) GetArity() int            { return len(a.args) }
func (a *ATermAppl) GetArgument(i int) ATerm  { return a.args[i] }
func (a *ATermAppl) GetArgumentList() []ATerm { return append([]ATerm(nil), a.args...) }

func (a *ATermAppl) String() string {
	name := a.fun.name
	if a.fun.quoted {
		name = fmt.Sprintf("%q", name)
	}
	if len(a.args) == 0 {
		return name
	}
	parts := make([]string, len(a.args))
	for i, x := range a.args {
		parts[i] = x.String()
	}
	return name + "(" + strings.Join(parts, ",") + ")"
}

// ATermList is a shared cons list; the empty list has no first element.
type ATermList struct {
	first   ATerm
	next    *ATermList
	factory *PureFactory
	uid     int
}

func (l *ATermList) id() int                   { return l.uid }
func (l *ATermList) IsEmpty() bool             { return l.next == nil }
func (l *ATermList) GetFirst() ATerm           { return l.first }
func (l *ATermList) GetNext() *ATermList       { return l.next }
func (l *ATermList) Insert(e ATerm) *ATermList { return l.factory.MakeListCons(e, l) }

func (l *ATermList) GetLength() int {
	n := 0
	for ; !l.IsEmpty(); l = l.next {
		n++
	}
	return n
}

func (l *ATermList) String() string {
	var parts []string
	for x := l; !x.IsEmpty(); x = x.next {
		parts = append(parts, x.first.String())
	}
	return "[" + strings.Join(parts, ",") + "]"
}

type afunKey struct {
	name   string
	arity  int
	quoted bool
}

type consKey struct{ first, next int }

// PureFactory builds maximally shared terms.
type PureFactory struct {
	mu    sync.Mutex
	next  int
	afuns map[afunKey]*AFun
	appls map[string]*ATermAppl
	lists map[consKey]*ATermList
	empty *ATermList
}

// NewPureFactory returns a fresh factory (the size hint is ignored).
func NewPureFactory(size int) *PureFactory {
	f := &PureFactory{
		afuns: map[afunKey]*AFun{},
		appls: map[string]*ATermAppl{},
		lists: map[consKey]*ATermList{},
	}
	f.next++
	f.empty = &ATermList{factory: f, uid: f.next}
	return f
}

var (
	singletonOnce sync.Once
	singleton     *PureFactory
)

// GetInstance is SingletonFactory.getInstance().
func GetInstance() *PureFactory {
	singletonOnce.Do(func() { singleton = NewPureFactory(16) })
	return singleton
}

func (f *PureFactory) MakeAFun(name string, arity int, quoted bool) *AFun {
	f.mu.Lock()
	defer f.mu.Unlock()
	k := afunKey{name, arity, quoted}
	if a, ok := f.afuns[k]; ok {
		return a
	}
	f.next++
	a := &AFun{name: name, arity: arity, quoted: quoted, uid: f.next}
	f.afuns[k] = a
	return a
}

func (f *PureFactory) MakeAppl(fun *AFun, args ...ATerm) *ATermAppl {
	if len(args) != fun.arity {
		panic(fmt.Sprintf("aterm: %s has arity %d, got %d arguments", fun.name, fun.arity, len(args)))
	}
	var b strings.Builder
	fmt.Fprint(&b, fun.uid)
	for _, x := range args {
		fmt.Fprint(&b, ",", x.id())
	}
	k := b.String()
	f.mu.Lock()
	defer f.mu.Unlock()
	if a, ok := f.appls[k]; ok {
		return a
	}
	f.next++
	a := &ATermAppl{fun: fun, args: append([]ATerm(nil), args...), uid: f.next}
	f.appls[k] = a
	return a
}

// MakeList returns the empty list.
func (f *PureFactory) MakeList() *ATermList { return f.empty }

// MakeListCons returns [first | next].
func (f *PureFactory) MakeListCons(first ATerm, next *ATermList) *ATermList {
	k := consKey{first.id(), next.uid}
	f.mu.Lock()
	defer f.mu.Unlock()
	if l, ok := f.lists[k]; ok {
		return l
	}
	f.next++
	l := &ATermList{first: first, next: next, factory: f, uid: f.next}
	f.lists[k] = l
	return l
}

// Parse reads the textual form of a term: f, f(t1,…,tn), [t1,…,tn].
func (f *PureFactory) Parse(s string) ATerm {
	p := &parser{f: f, s: s}
	t := p.term()
	p.skip()
	if p.i != len(p.s) {
		panic(fmt.Sprintf("aterm: trailing text in %q at %d", s, p.i))
	}
	return t
}

type parser struct {
	f *PureFactory
	s string
	i int
}

func (p *parser) skip() {
	for p.i < len(p.s) && strings.ContainsRune(" \t\r\n", rune(p.s[p.i])) {
		p.i++
	}
}

func (p *parser) peek() byte {
	p.skip()
	if p.i < len(p.s) {
		return p.s[p.i]
	}
	return 0
}

func (p *parser) expect(c byte) {
	if p.peek() != c {
		panic(fmt.Sprintf("aterm: expected %q in %q at %d", c, p.s, p.i))
	}
	p.i++
}

func (p *parser) terms(end byte) []ATerm {
	var ts []ATerm
	if p.peek() == end {
		p.i++
		return ts
	}
	for {
		ts = append(ts, p.term())
		if p.peek() == ',' {
			p.i++
			continue
		}
		p.expect(end)
		return ts
	}
}

func (p *parser) term() ATerm {
	if p.peek() == '[' {
		p.i++
		ts := p.terms(']')
		l := p.f.MakeList()
		for i := len(ts) - 1; i >= 0; i-- {
			l = p.f.MakeListCons(ts[i], l)
		}
		return l
	}
	start := p.i
	for p.i < len(p.s) {
		c := p.s[p.i]
		if c == '_' || c == '-' || c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' {
			p.i++
			continue
		}
		break
	}
	if p.i == start {
		panic(fmt.Sprintf("aterm: expected a term in %q at %d", p.s, p.i))
	}
	name := p.s[start:p.i]
	var args []ATerm
	if p.i < len(p.s) && p.s[p.i] == '(' {
		p.i++
		args = p.terms(')')
	}
	return p.f.MakeAppl(p.f.MakeAFun(name, len(args), false), args...)
}
