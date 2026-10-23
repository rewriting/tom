// Port of test/Test.t.
// port: org.junit.Assert.fail is t.Fatal.
// port: no main — `go test` runs the tests (the original main only launched JUnit).
package test

import "testing"

%gom {
  module test
  imports
  abstract syntax
}

func Test1(t *testing.T) {
    if true {
      return 
    } 
    t.Fatal("should not be there")
  }
