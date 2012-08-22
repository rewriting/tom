package enumerator;

public class Finite<A> {
  private int card;
  private Function1<A,Integer> indexer;

  public Finite(int c, Function1<A,Integer> f) {
    this.card=c;
    this.indexer=f;
  }

  public Finite<A> empty() {
    return new Finite(0, 
        new Function1<A,Integer>() { public A apply(Integer i) { throw new RuntimeException("index out of range"); }});
  }

  public Finite<A> singleton(final A x) {
    return new Finite(1, 
        new Function1<A,Integer>() { public A apply(Integer i) { 
          if(i==0) { return x; } else { throw new RuntimeException("index out of range"); }}});
  }

  public Finite<A> setCard(int newCard) {
    return new Finite<A>(newCard,indexer);
  }

  public Finite<A> setIndexer(Function1<A,Integer> newIndexer) {
    return new Finite<A>(card,newIndexer);
  }

  public A get(int i) {
    return indexer.apply(i);
  }

  public Finite<A> plus(final Finite<A> other) {
    return new Finite<A>(
        this.card+other.card,
        new Function1<A,Integer>() { public A apply(Integer i) { return (i<card)?get(i):other.get(i-card); }}
        );
  }

  public Finite<Pair<A,A>> times(final Finite<A> other) {
    return new Finite<Pair<A,A>>(
        this.card*other.card,
        new Function1<Pair<A,A>,Integer>() { 
          public Pair<A,A> apply(Integer i) {
            int q = i / other.card;
            int r = i % other.card;
            return new Pair<A,A>(get(q),get(r));
          }}
        );
  }

  private static <A,B,C> Function1<C,A> compose(final Function1<C,B> f, final Function1<B,A> g) {
    return new Function1<C,A>() { public C apply(A x) { return f.apply(g.apply(x)); }};
  }

  public Finite<A> map(final Function1<A,A> f) {
    return setIndexer(compose(f,indexer));
  }

  /*
  public Finite<A> apply(final Finite<A> other) {
    this.times(other).map(
        new Function1<Finite<A>,Pair<A,A>>() { 
        public A apply(Pair<A,A> p) { 
        return ;
        }}
        );
  }
*/

  private static class Pair<A,B> {
    protected A first;
    protected B second;
    public Pair(A a, B b) { this.first=a; this.second=b; }
  }
}


