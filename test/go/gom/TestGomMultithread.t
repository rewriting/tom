package gom

// Port of test/gom/TestGomMultithread.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testgommultithread.term.types.*;`.
// port: the ExecutorService of TASKS threads running the Runnable is
// TASKS goroutines; AtomicInteger is sync/atomic's Int32;
// executor.awaitTermination(20, SECONDS) waits for the goroutines with a
// 20 s timeout (an expired timeout is not an error in Java either:
// awaitTermination returns false and the assertion decides).
// port: the Runnable's task() is a closure.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "sync"
  "sync/atomic"
  "testing"
  "time"
)

  %gom(--multithread) {
    module term
    abstract syntax
    T = a()
      | b()
      | c()
      | f(One:T,Two:T,Three:T,Four:T)
  }

func TestMulti(t *testing.T) {
    const DEPTH = 4
    const TASKS = 10
    const COUNT = 100
    var count atomic.Int32
    var executor sync.WaitGroup
    for i := 0; i < TASKS; i++ {
      executor.Add(1)
      go func() {
          defer executor.Done()
          task := func() T {
            var res T = `a()
            for i := 0; i < DEPTH; i++ {
              res = `f(res, b(), c(), res)
            }
            return res
          }
          for i := 0; i < COUNT; i++ {
            var res T = task()
            var res2 T = task()
            if (res == res2) {
              count.Add(1)
            }
          }
      }()
    }
    done := make(chan struct{})
    go func() { executor.Wait(); close(done) }()
    select {
    case <-done:
    case <-time.After(20 * time.Second):
    }
    assertEquals(t, "Successful tasks", int32(TASKS*COUNT), count.Load())
}

// org.junit.Assert.assertEquals(message, expected, actual)
func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
