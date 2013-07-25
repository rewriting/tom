package tomchecker;

import tom.library.enumerator.*;

public class Product3<A,B,C>{
  public A proj1;
  public B proj2;
  public C proj3;

  public  Product3(A a, B b,C c){
    this.proj1 = a;
    this.proj2 = b;
    this.proj3 = c;
  }

  public static <A,B,C> Enumeration<Product3<A,B,C>> enumerate(Enumeration<A> ea, Enumeration<B> eb, Enumeration<C> ec){
    F<A,F<B,F<C,Product3<A,B,C>>>> fun = new F<A,F<B,F<C,Product3<A,B,C>>>>(){
			public F<B,F<C,Product3<A,B,C>>> apply(final A a) {
				return new F<B,F<C,Product3<A,B,C>>>(){
          public F<C,Product3<A,B,C>> apply(final B b) {
            return new F<C,Product3<A,B,C>>(){
              public Product3<A,B,C> apply(final C c) {
                return new Product3<A,B,C>(a,b,c);
              }
            };
          }
        };
			}
		};
    
    return Enumeration.apply(Enumeration.apply(Enumeration.apply(Enumeration.singleton(fun),ea),eb),ec);
  } 

  public String toString(){
    return "{"+proj1+","+proj2+","+proj3+"}";
  }


  @Override
    public int hashCode() {
    int hash = 7;
    hash = 17 * hash + (this.proj1 != null ? this.proj1.hashCode() : 0);
    hash = 17 * hash + (this.proj2 != null ? this.proj2.hashCode() : 0);
    hash = 17 * hash + (this.proj3 != null ? this.proj3.hashCode() : 0);
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
    final Product3<A, B, C> other = (Product3<A, B, C>) obj;
    if (this.proj1 != other.proj1 && (this.proj1 == null || !this.proj1.equals(other.proj1))) {
      return false;
    }
    if (this.proj2 != other.proj2 && (this.proj2 == null || !this.proj2.equals(other.proj2))) {
      return false;
    }
    if (this.proj3 != other.proj3 && (this.proj3 == null || !this.proj3.equals(other.proj3))) {
      return false;
    }
    return true;
  }

} 
