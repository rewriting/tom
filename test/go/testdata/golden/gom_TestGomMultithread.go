package gom














import (
  "sync"
  "sync/atomic"
  "testing"
  "time"
)

  func tom_TestGomMultithread_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_TestGomMultithread_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_TestGomMultithread_make_a()  T  { return (MakeA()) }; func tom_TestGomMultithread_make_b()  T  { return (MakeB()) }; func tom_TestGomMultithread_make_c()  T  { return (MakeC()) }; func tom_TestGomMultithread_make_f(One  T , Two  T , Three  T , Four  T )  T  { return (MakeF(One,Two,Three,Four)) }; 








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
            var res T = tom_TestGomMultithread_make_a()
            for i := 0; i < DEPTH; i++ {
              res = tom_TestGomMultithread_make_f(res,tom_TestGomMultithread_make_b(),tom_TestGomMultithread_make_c(),res)
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




func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
