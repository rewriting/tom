package tomchecker;

import tom.library.enumerator.*;

public class Product1<A> {
  public A proj1;

  public  Product1(A a) {
    this.proj1 = a;
  }
  
  public static <A> Enumeration<Product1<A>> enumerate(Enumeration<A> ea) {
    F<A,Product1<A>> fun = new F<A,Product1<A>>() {
			public Product1<A> apply(final A a) {
        return new Product1<A>(a);
      }
    };
  
    return Enumeration.apply(Enumeration.singleton(fun),ea);
  } 
 
  public String toString() {
    return "{" + proj1 + "}";
  }


  @Override
    public int hashCode() {
    int hash = 7;
    hash = 61 * hash + (this.proj1 != null ? this.proj1.hashCode() : 0);
    return hash;
  }

  @Override
    public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }
    if(obj instanceof Product1) {
      final Product1<A> other = (Product1<A>) obj;
      if(this.proj1 == other.proj1) {
        return true;
      } else if(this.proj1 != null && this.proj1.equals(other.proj1)) {
        return true;
      }
    }
    return false;

    /*
    if(getClass() != obj.getClass()) {
      return false;
    }
    final Product1<A> other = (Product1<A>) obj;
    if (this.proj1 != other.proj1 && (this.proj1 == null || !this.proj1.equals(other.proj1))) {
      return false;
    }
    return true;
    */
  }
} 
 
