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


}
