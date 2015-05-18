package examples.factory;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class TreeFactory {

    public static final <T> Enumeration<Tree<T>> getEnumeration(
        final Enumeration<T> enumeration) {

        Enumeration<Tree<T>> enumRes = null;

        final Enumeration<Tree<T>> emptyEnum = Enumeration.singleton(null);

        final F<T, F<Tree<T>, F<Tree<T>, Tree<T>>>> _tree = new F<T, F<Tree<T>, F<Tree<T>, Tree<T>>>>() {
            public F<Tree<T>, F<Tree<T>, Tree<T>>> apply(final T e) {
                return new F<Tree<T>, F<Tree<T>, Tree<T>>>() {
                    public F<Tree<T>, Tree<T>> apply(final Tree<T> l) {
                        return new F<Tree<T>, Tree<T>>() {
                            public Tree<T> apply(final Tree<T> r) {
                                return new Tree<T>(e, l, r);
                            }
                        };
                    }
                };
            }
        };

        F<Enumeration<Tree<T>>, Enumeration<Tree<T>>> treeEnum = new F<Enumeration<Tree<T>>, Enumeration<Tree<T>>>() {
            public Enumeration<Tree<T>> apply(final Enumeration<Tree<T>> t) {
                return emptyEnum.plus(
                    Enumeration.apply(Enumeration.apply(Enumeration.apply(
                        Enumeration.singleton(_tree), enumeration), t), t))
                    .pay();
            }
        };
        enumRes = Enumeration.fix(treeEnum);

        return enumRes;
    }

}
