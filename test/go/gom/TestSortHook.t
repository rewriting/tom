package gom

// Port of test/gom/TestSortHook.t: sort block hooks.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testsorthook.m.types.*;`.
// port: a sort block hook inserts Java methods into the abstract class of
// the sort; a Go interface has no method bodies, so the Go block holds
// functions that take the term (Java's this) as their first parameter:
// my.hookSort1() is hookSort1(my); symbolName() is this.SymbolName();
// java.util.Map/HashMap is a Go map. The sort import hook has nothing to
// import (a Go import hook must be empty: imports belong to the importing
// Go file). The statements of a hook body end with `;` (Tom emits the
// host code of the generated mapping on a single line).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module m
    abstract syntax
    Sort1 = a() | b()
    Sort2 = c() | d()
    sort Sort1:block() {
      func hookSort1(this Sort1) string {
        return "1"+this.SymbolName();
      };

      /* Just to make sure the import did work */
      func getEmptyMap(this Sort1) map[any]any {
        return map[any]any{};
      }
    }

    sort Sort1:import() {
      // port: nothing to import (maps are builtin in Go)
    }

    sort Sort2:block() {
      func hookSort2(this Sort2) string {
        %match(this) {
          c() -> { return "2c"; }
          d() -> { return "2d"; }
        };
        return "plonk";
      }
    }
  }

func TestHook1a(t *testing.T) {
    var my Sort1 = `a()
    var res string = hookSort1(my)
    assertEquals(t, "1a",res)
}

func TestHook1b(t *testing.T) {
    var my Sort1 = `b()
    var res string = hookSort1(my)
    assertEquals(t, "1b",res)
}

func TestHook2c(t *testing.T) {
    var my Sort2 = `c()
    var res string = hookSort2(my)
    assertEquals(t, "2c",res)
}

func TestHook2d(t *testing.T) {
    var my Sort2 = `d()
    var res string = hookSort2(my)
    assertEquals(t, "2d",res)
}

// org.junit.Assert.assertEquals(expected, actual)
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
