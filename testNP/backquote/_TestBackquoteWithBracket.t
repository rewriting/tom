public class TestBackquoteWithBracket {

  public void testDefault1() {
    assertEquals( `f[arg1=_].getarg1(), `a() );
    assertEquals( `f[arg1= _].getarg1(), `a() );
    assertEquals( `f[arg1=_ ].getarg1(), `a() );
    assertEquals( `f[arg1= _ ].getarg1(), `a() );
    assertEquals( `f[].getarg1(), `a() );
    assertEquals( `f[].getarg1(), `a[] );
  }

  public void testDefault2() {
    assertEquals( `g[arg1=a()].getarg1(), `a() );
    assertEquals( `g[arg1=a()].getarg2(), `b() );
    assertEquals( `g[arg1=a[]].getarg2(), `b() );
    assertEquals( `g[arg1=a()].getarg2(), `b() );
    assertEquals( `g[arg1=a(),arg2=_].getarg2(), `b() );
  }

  public void testDefault3() {
    assertEquals( `h[].getarg1(), `a() );
    assertEquals( `h[].getarg2(), `g(_,_) );
    assertEquals( `h[].getarg2(), `g[arg1=a(),arg2=b()] );
    assertEquals( `h[].getarg3(), `f[arg1=_] );
    assertEquals( `h[].getarg3(), `f[arg1=a()] );
  }

  public void testDefault4() {
    assertEquals( `f[arg1=g[]].getarg1(), `g[] );
  }
}
