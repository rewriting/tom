L l = `conc(a(), b(), c(), a(), b());
L l2 = `conc();

%match(L l) {
  conc(
      x*,
      b(),
      y*,
      a(),
      z*
  ) -> {
    l2 = `conc(x*,
               b(),
               y*,
               a(),
               z*
         );
  }
}

private static L abc() {
return `conc(a(),b(),c());
}

public void test2() {
assertEquals(`conc(a(),b(),c()),`conc(abc()));
}
