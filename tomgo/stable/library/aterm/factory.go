package aterm

import (
	"math"
	"strings"

	"tom/tomgo/stable/library/sharedobjects"
)

// Factory is the entry point for every aterm construction. It wraps
// a sharedobjects.Factory for the hash-cons table and caches the
// canonical empty list so all empty-list references end up the same
// Go pointer.
type Factory struct {
	store     *sharedobjects.Factory
	emptyList *ATermList
}

// NewFactory returns a fresh factory with a freshly minted empty list.
func NewFactory() *Factory {
	f := &Factory{store: sharedobjects.NewFactory()}
	// The empty list is a self-referential singleton: a list whose
	// head is nil and whose tail is itself. We can't quite express
	// "self" cleanly so we settle for a list whose tail is nil too —
	// IsEmpty just looks at head.
	proto := &ATermList{factory: f, length: 0}
	proto.hash = hashList(nil, nil)
	canon := f.store.Build(proto).(*ATermList)
	f.emptyList = canon
	return f
}

// MakeInt builds an ATermInt for the given value.
func (f *Factory) MakeInt(value int) *ATermInt {
	proto := &ATermInt{value: int32(value), factory: f, hash: hashInt(int32(value))}
	return f.store.Build(proto).(*ATermInt)
}

// MakeLong builds an ATermLong for the given value.
func (f *Factory) MakeLong(value int64) *ATermLong {
	proto := &ATermLong{value: value, factory: f, hash: hashLong(value)}
	return f.store.Build(proto).(*ATermLong)
}

// MakeReal builds an ATermReal for the given value.
func (f *Factory) MakeReal(value float64) *ATermReal {
	proto := &ATermReal{value: value, factory: f, hash: hashReal(value)}
	return f.store.Build(proto).(*ATermReal)
}

// MakeAFun builds an AFun (function symbol).
func (f *Factory) MakeAFun(name string, arity int, isQuoted bool) *AFun {
	proto := &AFun{name: name, arity: arity, quoted: isQuoted, factory: f}
	proto.hash = hashAFun(name, arity, isQuoted)
	return f.store.Build(proto).(*AFun)
}

// MakeAppl builds an ATermAppl with `fun` applied to args. The args
// length must match fun.arity, except for AFuns of arity 0 (where
// args must be empty).
func (f *Factory) MakeAppl(fun *AFun, args ...ATerm) *ATermAppl {
	if len(args) != fun.arity {
		panic("aterm: makeAppl arity mismatch")
	}
	proto := &ATermAppl{fun: fun, args: args, factory: f}
	proto.hash = hashAppl(fun, args)
	return f.store.Build(proto).(*ATermAppl)
}

// MakeApplList builds an ATermAppl whose arguments come from a list.
func (f *Factory) MakeApplList(fun *AFun, args *ATermList) *ATermAppl {
	out := make([]ATerm, 0, args.GetLength())
	for cur := args; !cur.IsEmpty(); cur = cur.tail {
		out = append(out, cur.head)
	}
	return f.MakeAppl(fun, out...)
}

// MakeList returns the empty list.
func (f *Factory) MakeListEmpty() *ATermList { return f.emptyList }

// MakeListSingle returns `[single]`.
func (f *Factory) MakeListSingle(single ATerm) *ATermList {
	return f.MakeList(single, f.emptyList)
}

// MakeList builds a cons cell (head : tail).
func (f *Factory) MakeList(head ATerm, tail *ATermList) *ATermList {
	proto := &ATermList{
		head:    head,
		tail:    tail,
		length:  tail.length + 1,
		factory: f,
	}
	proto.hash = hashList(head, tail)
	return f.store.Build(proto).(*ATermList)
}

// MakePlaceholder builds an ATermPlaceholder with the given
// placeholder type — typically an Appl such as `<int>`.
func (f *Factory) MakePlaceholder(typ ATerm) *ATermPlaceholder {
	proto := &ATermPlaceholder{placeholderType: typ, factory: f}
	proto.hash = hashPlaceholder(typ)
	return f.store.Build(proto).(*ATermPlaceholder)
}

// Parse parses a textual ATerm and returns it.
func (f *Factory) Parse(src string) ATerm {
	return parseTerm(f, src)
}

// Make parses `pattern`, then walks it filling placeholders with the
// values from args (consumed front-to-back). Returns the rebuilt term.
func (f *Factory) Make(pattern string, args []any) ATerm {
	p := parseTerm(f, pattern)
	r := &makeRunner{factory: f, args: args}
	return r.makeFrom(p)
}

// ImportTerm walks a term that may belong to a different factory
// (different hash table) and rebuilds it under this factory.
func (f *Factory) ImportTerm(t ATerm) ATerm {
	if t == nil {
		return nil
	}
	switch x := t.(type) {
	case *ATermInt:
		return f.MakeInt(int(x.value))
	case *ATermLong:
		return f.MakeLong(x.value)
	case *ATermReal:
		return f.MakeReal(x.value)
	case *ATermAppl:
		newArgs := make([]ATerm, len(x.args))
		for i, a := range x.args {
			newArgs[i] = f.ImportTerm(a)
		}
		newFun := f.MakeAFun(x.fun.name, x.fun.arity, x.fun.quoted)
		return f.MakeAppl(newFun, newArgs...)
	case *ATermList:
		if x.IsEmpty() {
			return f.emptyList
		}
		return f.MakeList(f.ImportTerm(x.head), f.ImportTerm(x.tail).(*ATermList))
	case *ATermPlaceholder:
		return f.MakePlaceholder(f.ImportTerm(x.placeholderType))
	}
	panic("aterm: ImportTerm: unsupported term type")
}

// Stats forwards to the underlying hash-cons factory.
func (f *Factory) Stats() sharedobjects.Stats { return f.store.Stats() }

// String returns a debug summary, mirroring Java's
// `PureFactory.toString` (`Factory{terms=...}`-style).
func (f *Factory) String() string {
	var b strings.Builder
	b.WriteString(f.store.Stats().String())
	return b.String()
}

// ---------------------------------------------------------------------------
// hashing
// ---------------------------------------------------------------------------

func hashInt(v int32) uint32 {
	h := sharedobjects.StringHash("int")
	return sharedobjects.MixSymbol(h, []uint32{uint32(v)})
}

func hashLong(v int64) uint32 {
	h := sharedobjects.StringHash("long")
	return sharedobjects.MixSymbol(h, []uint32{uint32(v), uint32(v >> 32)})
}

func hashReal(v float64) uint32 {
	bits := math.Float64bits(v)
	h := sharedobjects.StringHash("real")
	return sharedobjects.MixSymbol(h, []uint32{uint32(bits), uint32(bits >> 32)})
}

func hashAFun(name string, arity int, quoted bool) uint32 {
	q := uint32(0)
	if quoted {
		q = 1
	}
	h := sharedobjects.StringHash(name)
	return sharedobjects.MixSymbol(h, []uint32{uint32(arity), q})
}

func hashAppl(fun *AFun, args []ATerm) uint32 {
	hashes := make([]uint32, 0, len(args)+1)
	hashes = append(hashes, fun.Hash())
	for _, a := range args {
		hashes = append(hashes, a.Hash())
	}
	return sharedobjects.MixSymbol(sharedobjects.StringHash("appl"), hashes)
}

func hashList(head ATerm, tail *ATermList) uint32 {
	if head == nil {
		return sharedobjects.StringHash("list-empty")
	}
	return sharedobjects.MixSymbol(sharedobjects.StringHash("list"),
		[]uint32{head.Hash(), tail.Hash()})
}

func hashPlaceholder(t ATerm) uint32 {
	return sharedobjects.MixSymbol(sharedobjects.StringHash("placeholder"),
		[]uint32{t.Hash()})
}
