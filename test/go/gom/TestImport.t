package gom

/*
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

// Port of test/gom/TestImport.t.
// port: `import gom.importing.types.*;` and `import
// gom.imported.types.*;` are the dot import of the Go package that gomgo
// generates from Importing.gom and the modules it imports (Imported,
// Leaf), all in the sub-directory importing.
// port: java.util.Collection is the Go interface Collection below and
// ArrayList its slice-backed implementation; the bodies of the
// %typeterm Collection hooks are Go.
// port: test.toString() is fmt.Sprint(test); Java's visitLight throws
// VisitFailure, the Go VisitLight returns it (the try/catch tests it).
// port: the strategy parameter col (a field of the Java strategy class)
// is read as tom__strat.col, the field of the Go strategy.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "testing"

  . "fixture/importing"
  "tom/library/sl"
)

// Collection is java.util.Collection (the methods the test uses).
type Collection interface {
  Add(e any) bool
  Size() int
}

// ArrayList is java.util.ArrayList.
type ArrayList struct{ elems []any }

func (l *ArrayList) Add(e any) bool { l.elems = append(l.elems, e); return true }
func (l *ArrayList) Size() int      { return len(l.elems) }

  %include { sl.tom }
  %include { importing/Importing.tom }
  %typeterm Collection {
    implement { Collection }
    is_sort(t) { func() bool { _, ok := any($t).(Collection); return ok }() } 
  }

func TestMake(t *testing.T) {
    var test Out = `Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))
    assertEquals(t, fmt.Sprint(test),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))")
}

func TestStrat(t *testing.T) {
    var test Out = `Loop(Loop(Pack(Atom()),LeafSlot(Leaf())),Element(Element(Atom())))
    var set Collection = &ArrayList{} /* Make sure we count all inserts */
    if _, e := `BottomUp(Count(set)).VisitLight(test, sl.VisitableIntrospector{}); e != nil {
      fail(t, fmt.Sprint(e) + " catched")
    }
    assertEquals(t, 4,set.Size())
}

  %strategy Count(col:Collection) extends `Identity() {
    visit Inner {
      a@Atom() -> { tom__strat.col.Add(`a) }
    }
    visit Out {
      l@Loop[] -> { tom__strat.col.Add(`l) }
    }
  }

// org.junit.Assert.assertEquals(expected, actual)
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

// org.junit.Assert.fail(message)
func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
