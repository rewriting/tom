package main

// Port of test/gom/BenchConsGom.t (a benchmark run as a main program).
// port: `import gom.list.types.*;` is the dot import of the Go package
// that gomgo generates from List.gom (sub-directory list).
// port: the BenchConsGom instance methods are functions (the backquoted
// `elim(…)` is an unqualified call of the host function); Java's int is
// int64, the Go type of Gom's int.
// port: System.currentTimeMillis() is time.Now().UnixMilli();
// shared.SingletonSharedObjectFactory.getInstance() is the factory of
// the generated package, whose Stats() it prints.
// port: like the original, the program prints its usage and stops when
// it is not given <max> (the oracle runs it without arguments, so only
// the deterministic usage line is compared; the timings are not).

import (
  "fmt"
  "os"
  "strconv"
  "time"

  . "fixture/list"
)

  %include { list/List.tom }

  func genere(n int64) List {
    if(n>2) {
      var l List = genere(n-1)
      return `conc(n,l*)
    } else {
      return `conc(2)
    }
  }

  func elim(l List) List {
    %match(List l) {
      conc(x*,e1,y*,e2,z*) -> {
        if(`e2%`e1 == 0) {
          return `elim(conc(x*,e1,y*,z*))
        }
      }
    }
    return l
  }

  func reverse(l List) List {
    var res List = `conc()
    for {
      %match(List l) {
        conc() -> { return res }
        conc(h,t*) -> {
          res = `conc(h,res*)
          l = `t
        }
      }
    }
  }

  func run(max int64) {
    //System.out.println(" l = " + genere(100));

    var startChrono int64 = time.Now().UnixMilli()
    
    elim(reverse(genere(max)))
    
    fmt.Println(fmt.Sprint(max) + " " + fmt.Sprint(time.Now().UnixMilli()-startChrono))
    fmt.Println(Factory().Stats())
  }

  func main() {
    var max int64 = 100
    // port: Integer.parseInt(args[0]) throws when args is empty or not a
    // number; both cases print the usage.
    if len(os.Args) < 2 {
      fmt.Println("Usage: java list.BenchConsGom <max>")
      return
    }
    m, err := strconv.ParseInt(os.Args[1], 10, 32)
    if err != nil {
      fmt.Println("Usage: java list.BenchConsGom <max>")
      return
    }
    max = m
    run(max)
  }
