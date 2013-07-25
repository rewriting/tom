package tomchecker;

import tom.library.enumerator.*;

public class Product2<A,B>{
  public A proj1;
  public B proj2;

  public  Product2(A a, B b){
    this.proj1 = a;
    this.proj2 = b;
  }

  public static <A,B> Enumeration<Product2<A,B>> enumerate(Enumeration<A> ea, Enumeration<B> eb){
    F<A,F<B,Product2<A,B>>> fun = new F<A,F<B,Product2<A,B>>>(){
			public F<B,Product2<A,B>> apply(final A a) {
				return new F<B,Product2<A,B>>(){
          public Product2<A,B> apply(final B b) {
            return new Product2<A,B>(a,b);
          }
        };
			}
		};

// just to understand types
//     Enumeration<F<A,F<B,Product<A,B>>>> efun =  Enumeration.singleton(fun);
//     Enumeration<F<B,Product<A,B>>> ffun = Enumeration.apply(efun,ea);
//     Enumeration<Product<A,B>> res = Enumeration.apply(ffun,eb);
//     return res;

    return Enumeration.apply(Enumeration.apply(Enumeration.singleton(fun),ea),eb);


  } 

  public String toString(){
    return "{"+proj1+","+proj2+"}";
  }


  @Override
    public int hashCode() {
    int hash = 7;
    hash = 73 * hash + (this.proj1 != null ? this.proj1.hashCode() : 0);
    hash = 73 * hash + (this.proj2 != null ? this.proj2.hashCode() : 0);
    return hash;
  }

  @Override
    public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Product2<A, B> other = (Product2<A, B>) obj;
    if (this.proj1 != other.proj1 && (this.proj1 == null || !this.proj1.equals(other.proj1))) {
      return false;
    }
    if (this.proj2 != other.proj2 && (this.proj2 == null || !this.proj2.equals(other.proj2))) {
      return false;
    }
    return true;
  }

} 
