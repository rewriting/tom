package aterm

import (
	"strings"

	"tom/tomgo/stable/library/sharedobjects"
)

// ATermList is a head/tail style cons list. The empty list is the
// canonical sentinel `factory.emptyList` (head == nil, tail == nil).
type ATermList struct {
	head    ATerm
	tail    *ATermList
	length  int
	hash    uint32
	factory *Factory
}

func (l *ATermList) Type() int             { return LIST }
func (l *ATermList) Hash() uint32          { return l.hash }
func (l *ATermList) Factory() *Factory     { return l.factory }
func (l *ATermList) GetLength() int        { return l.length }
func (l *ATermList) IsEmpty() bool         { return l.head == nil }
func (l *ATermList) GetFirst() ATerm       { return l.head }
func (l *ATermList) GetNext() *ATermList   { return l.tail }
func (l *ATermList) IsEqual(o ATerm) bool  { return ATerm(l) == o }
func (l *ATermList) GetAnnotations() *ATermList       { return l.factory.emptyList }
func (l *ATermList) SetAnnotations(_ *ATermList) ATerm { return l }
func (l *ATermList) HasAnnotations() bool             { return false }
func (l *ATermList) Match(pattern string) []any       { return matchPattern(l, pattern) }
func (l *ATermList) Make(args []any) ATerm            { return makePattern(l, args) }

func (l *ATermList) String() string {
	var b strings.Builder
	b.WriteByte('[')
	for first, cur := true, l; !cur.IsEmpty(); cur = cur.tail {
		if !first {
			b.WriteByte(',')
		}
		first = false
		b.WriteString(cur.head.String())
	}
	b.WriteByte(']')
	return b.String()
}

// IndexOf returns the first index ≥ start at which element is found,
// or -1 if absent.
func (l *ATermList) IndexOf(element ATerm, start int) int {
	idx := 0
	for cur := l; !cur.IsEmpty(); cur = cur.tail {
		if idx >= start && cur.head == element {
			return idx
		}
		idx++
	}
	return -1
}

// LastIndexOf scans backwards from `start`; passing -1 means "from
// the end". Returns -1 when absent.
func (l *ATermList) LastIndexOf(element ATerm, start int) int {
	if start < 0 {
		start = l.length - 1
	}
	last := -1
	idx := 0
	for cur := l; !cur.IsEmpty(); cur = cur.tail {
		if idx <= start && cur.head == element {
			last = idx
		}
		idx++
	}
	return last
}

// Concat returns this list followed by other (a fresh list).
func (l *ATermList) Concat(other *ATermList) *ATermList {
	if l.IsEmpty() {
		return other
	}
	return l.factory.MakeList(l.head, l.tail.Concat(other))
}

// Append returns a list with `element` added at the end.
func (l *ATermList) Append(element ATerm) *ATermList {
	return l.Concat(l.factory.MakeListSingle(element))
}

// Insert returns a list with `element` cons-prepended at the front.
func (l *ATermList) Insert(element ATerm) *ATermList {
	return l.factory.MakeList(element, l)
}

// InsertAt returns this list with `element` injected at position i.
func (l *ATermList) InsertAt(element ATerm, i int) *ATermList {
	if i == 0 {
		return l.Insert(element)
	}
	return l.factory.MakeList(l.head, l.tail.InsertAt(element, i-1))
}

// GetPrefix returns a list containing every element except the last.
func (l *ATermList) GetPrefix() *ATermList {
	if l.IsEmpty() || l.tail.IsEmpty() {
		return l.factory.emptyList
	}
	return l.factory.MakeList(l.head, l.tail.GetPrefix())
}

// GetLast returns the last element of the list.
func (l *ATermList) GetLast() ATerm {
	for cur := l; ; cur = cur.tail {
		if cur.tail.IsEmpty() {
			return cur.head
		}
	}
}

// ElementAt returns the element at position i (0-based).
func (l *ATermList) ElementAt(i int) ATerm {
	cur := l
	for k := 0; k < i; k++ {
		cur = cur.tail
	}
	return cur.head
}

// RemoveElementAt returns a new list with the element at position i
// dropped. Out-of-range positions return the list unchanged (matching
// Java's lenient behaviour in pure.ATermListImpl).
func (l *ATermList) RemoveElementAt(i int) *ATermList {
	if l.IsEmpty() {
		return l
	}
	if i == 0 {
		return l.tail
	}
	return l.factory.MakeList(l.head, l.tail.RemoveElementAt(i-1))
}

// Reverse returns the list with elements in reverse order — useful
// for algorithms that prepend repeatedly and then flip for the final
// answer (one-pass O(n)).
func (l *ATermList) Reverse() *ATermList {
	out := l.factory.emptyList
	for cur := l; !cur.IsEmpty(); cur = cur.tail {
		out = l.factory.MakeList(cur.head, out)
	}
	return out
}

// Remove returns the list with the first occurrence of `element`
// dropped. Returns the list unchanged if the element is absent.
func (l *ATermList) Remove(element ATerm) *ATermList {
	if l.IsEmpty() {
		return l
	}
	if l.head == element {
		return l.tail
	}
	return l.factory.MakeList(l.head, l.tail.Remove(element))
}

// Replace returns a copy of the list with position i set to
// `element`. Out-of-range positions are an error (panics).
func (l *ATermList) Replace(element ATerm, i int) *ATermList {
	if l.IsEmpty() {
		panic("aterm: Replace: index out of range")
	}
	if i == 0 {
		return l.factory.MakeList(element, l.tail)
	}
	return l.factory.MakeList(l.head, l.tail.Replace(element, i-1))
}

// DictPut treats the list as a list of `[key, value]` pairs and
// returns a list with the pair for `key` set to `value` — replacing
// any prior pair for the same key (key equality is by hash-cons
// pointer identity).
func (l *ATermList) DictPut(key, value ATerm) *ATermList { return dictPut(l, key, value) }

// DictGet looks up `key` in a `[key, value]` pair list, returning
// the value or nil if the key isn't present.
func (l *ATermList) DictGet(key ATerm) ATerm { return dictGet(l, key) }

// DictRemove drops the pair for `key`, returning the list unchanged
// when no such pair exists.
func (l *ATermList) DictRemove(key ATerm) *ATermList { return dictRemove(l, key) }

// ---------------------------------------------------------------------------
// dict helpers — operate on a list of [key, value] pairs.
// ---------------------------------------------------------------------------

func dictPut(l *ATermList, key, value ATerm) *ATermList {
	f := l.factory
	pair := f.MakeList(key, f.MakeList(value, f.emptyList))
	if l.IsEmpty() {
		return f.MakeList(pair, f.emptyList)
	}
	cur := l.head.(*ATermList)
	if cur.head == key {
		return f.MakeList(pair, l.tail)
	}
	return f.MakeList(l.head, dictPut(l.tail, key, value))
}

func dictGet(l *ATermList, key ATerm) ATerm {
	for cur := l; !cur.IsEmpty(); cur = cur.tail {
		pair := cur.head.(*ATermList)
		if pair.head == key {
			return pair.tail.head
		}
	}
	return nil
}

func dictRemove(l *ATermList, key ATerm) *ATermList {
	if l.IsEmpty() {
		return l
	}
	cur := l.head.(*ATermList)
	if cur.head == key {
		return l.tail
	}
	return l.factory.MakeList(l.head, dictRemove(l.tail, key))
}

func (l *ATermList) Equivalent(other sharedobjects.Term) bool {
	o, ok := other.(*ATermList)
	if !ok {
		return false
	}
	if l.length != o.length {
		return false
	}
	if l.length == 0 {
		return true
	}
	return l.head == o.head && l.tail == o.tail
}

func (l *ATermList) Duplicate() sharedobjects.Term {
	clone := *l
	return &clone
}
