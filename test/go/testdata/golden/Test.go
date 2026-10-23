


package test



import "testing"









func Test1(t *testing.T) {
    if true {
      return 
    } 
    t.Fatal("should not be there")
  }
