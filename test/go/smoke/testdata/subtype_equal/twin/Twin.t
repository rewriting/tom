package smoke;

// Tom+Java twin of ../subtype_equal.t: compiled by legacy/, its output is
// ../expected.txt (TestJavaTwins).

public class Twin {
  static class tInt {}
  static class tNat extends tInt {}
  static class zero extends tNat {
    public boolean equals(Object o) { return o instanceof zero; }
    public int hashCode() { return 0; }
  }
  static class neg extends tInt {
    tNat m;
    neg(tNat m) { this.m = m; }
    public boolean equals(Object o) { return o instanceof neg && ((neg)o).m.equals(m); }
    public int hashCode() { return 1; }
  }
  static class div {
    tInt n;
    div(tInt n) { this.n = n; }
  }

  %typeterm tFloat {
    implement { div }
    is_sort(t) { $t != null }
    equals(t1,t2) { $t1 == $t2 }
  }

  %typeterm tInt {
    implement { tInt }
    is_sort(t) { ($t instanceof tInt) }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %typeterm tNat extends tInt {
    implement { tNat }
    is_sort(t) { ($t instanceof tNat) }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op tNat zero() {
    is_fsym(t) { ($t instanceof zero) }
    make() { new zero() }
  }

  %op tInt neg(m:tNat) {
    is_fsym(t) { ($t instanceof neg) }
    get_slot(m, t) { ((neg)$t).m }
    make(t0) { new neg($t0) }
  }

  %op tFloat div(n:tInt) {
    is_fsym(t) { $t != null }
    get_slot(n, t) { $t.n }
    make(t0) { new div($t0) }
  }

  static int count(div d) {
    int n = 0;
    %match {
      (zero() == x) && div[n=x] << d -> { n++; }
      (zero() != x) && div[n=x] << d -> { n += 10; }
    }
    return n;
  }

  public static void main(String[] args) {
    System.out.println("div(zero()) " + count(`div(zero())));
    System.out.println("div(neg(zero())) " + count(`div(neg(zero()))));
  }
}
