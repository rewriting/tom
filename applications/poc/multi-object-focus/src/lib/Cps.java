package lib;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 09/11/12
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public abstract class Cps<B,A> extends Fun<Fun<A,B>,B> {
    public static <B,A> Cps<B,A> unit(final A a) {
        return new Cps<B, A>() {
            public B apply(Fun<A,B> k) throws MOFException {
                return k.apply(a);
            }
        };
    }

    public <C> Cps<B,C> bind(final Fun<A,Cps<B,C>> f) {
        final Cps<B,A> t = this;
        return new Cps<B, C>() {
            public B apply(final Fun<C,B> k) throws MOFException {
                return t.apply(new Fun<A,B>() { public B apply(A a) throws MOFException {
                                                   return f.apply(a).apply(k);
                                              }}
                              );
            }};
    }

    public static <A> A run(Cps<A,A> m) throws MOFException{
      return m.apply(new Fun<A, A>() {
          public A apply(A arg) throws MOFException {
              return arg;
          }
      });
    }
}
