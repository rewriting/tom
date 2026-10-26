










package testarray2



import (
  "context"
  "fmt"
  "log/slog"
  "os"
  "testing"
)



var logger *slog.Logger
var level slog.Level = slog.LevelDebug

  func tom_TestArray2_equal_term_jtList(l1  []any , l2  []any ) bool { return listEquals(l1, l2);  }; func tom_TestArray2_is_sort_jtList(t any) bool { return t != nil;  }; func tom_TestArray2_is_fun_sym_conc(t  []any ) bool { return t != nil;  }; func tom_TestArray2_empty_array_conc(n int64)  []any  { return (make([]any, 0, n)) }; func tom_TestArray2_cons_array_conc(e  *Element , l  []any )  []any  { return (myAdd(e,l)) }; func tom_TestArray2_get_element_conc_jtList(l  []any , n int64)  *Element  { return l[n].(*Element);  }; func tom_TestArray2_get_size_conc_jtList(l  []any ) int64 { return int64(len(l));  }; func tom_TestArray2_get_slice_conc(subject  []any , begin int64, end int64)  []any  { var result  []any  = tom_TestArray2_empty_array_conc(end-begin); for begin != end { result = tom_TestArray2_cons_array_conc(tom_TestArray2_get_element_conc_jtList(subject, begin), result); begin++ }; return result }; func tom_TestArray2_append_array_conc(l2  []any , l1  []any )  []any  { var size1 int64 = tom_TestArray2_get_size_conc_jtList(l1); var size2 int64 = tom_TestArray2_get_size_conc_jtList(l2); var index int64; var result  []any  = tom_TestArray2_empty_array_conc(size1+size2); index = size1; for index > 0 { result = tom_TestArray2_cons_array_conc(tom_TestArray2_get_element_conc_jtList(l1, size1-index), result); index-- }; index = size2; for index > 0 { result = tom_TestArray2_cons_array_conc(tom_TestArray2_get_element_conc_jtList(l2, size2-index), result); index-- }; return result }; 













func myAdd(e any, l []any) []any {
    l = append(l, e)
    return l
  }

  func tom_TestArray2_equal_term_jtElement(t1  *Element , t2  *Element ) bool { return t1 == t2;  }; func tom_TestArray2_is_sort_jtElement(t any) bool { return (t != nil);  }; func tom_TestArray2_is_fun_sym_ListElement(t  *Element ) bool { return (t != nil) && isList(t.GetObject());  }; func tom_TestArray2_make_ListElement(v  []any )  *Element  { return (NewElement(v)) }; func tom_TestArray2_get_slot_ListElement_v(e  *Element )  []any  { return e.GetObject().([]any);  }; func tom_TestArray2_make_a()  *Element  { return (NewElement("a")) }; func tom_TestArray2_make_b()  *Element  { return (NewElement("b")) }; 





















func TestMain(m *testing.M) {
  setUp()
  code := m.Run()
  tearDown()
  os.Exit(code)
}



func setUp() {
    logger = slog.Default()
  }



func tearDown() {
    logger = nil
  }



func TestVariableStar1(t *testing.T) {
    nbSol := 0
    var l []any =  tom_TestArray2_cons_array_conc(tom_TestArray2_make_b(),tom_TestArray2_cons_array_conc(tom_TestArray2_make_a(),tom_TestArray2_cons_array_conc(tom_TestArray2_make_ListElement(tom_TestArray2_cons_array_conc(tom_TestArray2_make_b(),tom_TestArray2_cons_array_conc(tom_TestArray2_make_a(),tom_TestArray2_empty_array_conc(2)))),tom_TestArray2_empty_array_conc(3)))); { { if tom_TestArray2_is_sort_jtList(l) { if tom_TestArray2_is_fun_sym_conc(any(l).( []any )) { var tomMatch1_2 int64 = 0; if !(tomMatch1_2 >= tom_TestArray2_get_size_conc_jtList(any(l).( []any ))) { var tomMatch1_10  *Element  = tom_TestArray2_get_element_conc_jtList(any(l).( []any ),tomMatch1_2); if tom_TestArray2_is_fun_sym_ListElement(any(tomMatch1_10).( *Element )) { var tomMatch1_9  []any  = tom_TestArray2_get_slot_ListElement_v(tomMatch1_10); if tom_TestArray2_is_fun_sym_conc(any(tomMatch1_9).( []any )) { var tomMatch1_13 int64 = 0; var tomMatch1_end_16 int64 = tomMatch1_13; for { { var tomMatch1_3 int64 = tomMatch1_2 + 1; var tomMatch1_end_6 int64 = tomMatch1_3; for { { if !(tomMatch1_end_6 >= tom_TestArray2_get_size_conc_jtList(any(l).( []any ))) { 


        nbSol++
        logger.Log(context.Background(), level,
            "R = " + fmt.Sprint(tom_TestArray2_get_slice_conc(tomMatch1_9,tomMatch1_13,tomMatch1_end_16)) + " T = " + fmt.Sprint(tom_TestArray2_get_slice_conc(tomMatch1_9,tomMatch1_end_16,tom_TestArray2_get_size_conc_jtList(tomMatch1_9)))+" X1 = " + fmt.Sprint(tom_TestArray2_get_slice_conc(any(l).( []any ),tomMatch1_3,tomMatch1_end_6)) + " u = " + fmt.Sprint(tom_TestArray2_get_element_conc_jtList(any(l).( []any ),tomMatch1_end_6)) + " X2 = " + fmt.Sprint(tom_TestArray2_get_slice_conc(any(l).( []any ),tomMatch1_end_6 + 1,tom_TestArray2_get_size_conc_jtList(any(l).( []any )))))
      ;  }; tomMatch1_end_6 = tomMatch1_end_6 + 1;  };  if !(!(tomMatch1_end_6 > tom_TestArray2_get_size_conc_jtList(any(l).( []any )))) { break; }; }; tomMatch1_end_16 = tomMatch1_end_16 + 1;  };  if !(!(tomMatch1_end_16 > tom_TestArray2_get_size_conc_jtList(tomMatch1_9))) { break; }; };  };  };  };  };  };  };  }; 

    assertTrue(t, "TestVariableStar1",nbSol==6)
  }



type Element struct {
    object any
  }



func NewElement(o any) *Element {
      return &Element{object: o}
    }



func (e *Element) GetObject() any {
      return e.object
    }



func (e *Element) String() string {
      if isList(e.GetObject()) {
        return "ListElement(" + fmt.Sprint(e.GetObject()) + ")"
      } else {
        return fmt.Sprint(e.GetObject())
      }
    }




func isList(o any) bool { _, ok := o.([]any); return ok }




func listEquals(l1, l2 []any) bool {
  if len(l1) != len(l2) {
    return false
  }
  for i := range l1 {
    if l1[i] != l2[i] {
      return false
    }
  }
  return true
}




func assertTrue(t *testing.T, msg string, cond bool) {
  t.Helper()
  if !cond {
    t.Fatal(msg)
  }
}
