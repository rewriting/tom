package main

// Grouped declarations and struct bodies in an action. Each outer variable
// (a..e) is the subject of a %match whose patterns do not inspect it, and
// is not read anywhere else, so it needs "_ = a" unless the action
// surely reads it. Here the action only declares a name equal to it, in a
// var ( ), const ( ) or type ( ) group, or names a struct or interface
// field/method: the scan of the host text must not take those for reads.

import "fmt"

%include { int.tom }

func main() {
  var a, b, c, d, e int64 = 1, 2, 3, 4, 5
  %match(int a, int b, int c, int d, int e) {
    _, _, _, _, _ -> {
      var (
        a, y int
      )
      const (
        b = 2
      )
      type (
        c int
      )
      p := struct{ d, y int }{4, 5}
      var q interface{ e() }
      a, y = 1, 2
      fmt.Println(a+y+b+p.d, q == nil)
    }
  }
}
