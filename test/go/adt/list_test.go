package adt_test

import (
	"runtime"
	"testing"

	adt "tom/engine/adt"
)

// The engine ADT (tom/engine/adt) is generated as any Gom module: the lists are
// maximally-shared cons cells (Java Gom's Cons<op>/Empty<op>), so
// building a list by repeated cons — through the make_insert hook of
// concInstruction (TomInstruction.gom) — or by Make<Op>(args…), and
// walking it with GetTail, cost O(n) in total.

const listLen = 10000

// instructions returns m instructions (two shapes, alternately).
func instructions(m int) []adt.Instruction {
	out := make([]adt.Instruction, m)
	for i := range out {
		out[i] = adt.MakeNop()
		if i%2 == 0 {
			out[i] = adt.MakeReturn(adt.MakeBQDefault())
		}
	}
	return out
}

func consInstructions(elems []adt.Instruction) adt.InstructionList {
	var l adt.InstructionList = adt.EmptyConcInstruction()
	for _, e := range elems {
		l = adt.ConsConcInstruction(e, l)
	}
	return l
}

// BenchmarkConsConcInstruction10000 builds a 10 000-element
// concInstruction by repeated ConsConcInstruction (Java's
// ConsconcInstruction.make, with its make_insert hook).
func BenchmarkConsConcInstruction10000(b *testing.B) {
	elems := instructions(listLen)
	b.ReportAllocs()
	for i := 0; i < b.N; i++ {
		consInstructions(elems)
	}
}

// BenchmarkMakeConcInstruction10000 is Tom's `concInstruction(args…)`
// (Java's fromArray) on 10 000 elements.
func BenchmarkMakeConcInstruction10000(b *testing.B) {
	elems := instructions(listLen)
	b.ReportAllocs()
	for i := 0; i < b.N; i++ {
		adt.MakeConcInstruction(elems...)
	}
}

// TestConcInstructionIsLinear checks that building a concInstruction
// costs O(n) (the bytes allocated per element do not grow with the
// length of the list) and that walking it allocates nothing.
func TestConcInstructionIsLinear(t *testing.T) {
	// bytes allocated per element by building a list of m elements
	// (average of 3 builds, after a first one)
	perElem := func(m int, build func([]adt.Instruction) adt.InstructionList) float64 {
		elems := instructions(m)
		build(elems)
		var before, after runtime.MemStats
		runtime.ReadMemStats(&before)
		for i := 0; i < 3; i++ {
			build(elems)
		}
		runtime.ReadMemStats(&after)
		return float64(after.TotalAlloc-before.TotalAlloc) / 3 / float64(m)
	}
	builders := map[string]func([]adt.Instruction) adt.InstructionList{
		"ConsConcInstruction": consInstructions,
		"MakeConcInstruction": func(e []adt.Instruction) adt.InstructionList { return adt.MakeConcInstruction(e...) },
	}
	for name, build := range builders {
		small, large := perElem(1000, build), perElem(listLen, build)
		t.Logf("%s: %.0f bytes per element for %d elements, %.0f for 1000", name, large, listLen, small)
		if large > 2*small+64 {
			t.Errorf("%s allocates %.0f bytes per element for %d elements, %.0f for 1000: not linear", name, large, listLen, small)
		}
	}
	l := adt.MakeConcInstruction(instructions(listLen)...)
	if n := l.Length(); n != listLen {
		t.Fatalf("Length() = %d, want %d", n, listLen)
	}
	if allocs := testing.AllocsPerRun(10, func() {
		for cur := l; !cur.IsEmptyconcInstruction(); cur = cur.GetTailconcInstruction() {
		}
	}); allocs != 0 {
		t.Errorf("walking the list with GetTailconcInstruction allocates %v times", allocs)
	}
}
