package examples;

import java.math.BigInteger;
import tom.library.enumerator.*;

public class DemoTree {

    public static void main(String args[]) {
        // examples for trees
        // TTree = Leat(A) | Node(A,TTree,TTree)
        // A = a | b

        System.out.println("START");

        Enumeration<A> aEnum = Enumeration.singleton((A) new aa());
        Enumeration<A> bEnum = Enumeration.singleton((A) new bb());
//        final Enumeration<A> AEnum = aEnum.plus(bEnum).pay();
        final Enumeration<A> AEnum = aEnum.plus(bEnum);

        int n = 2;
        System.out.println("Enumerator for " + "A");
        for (int i = 0; i < n; i++) {
            System.out.println("Get " + i + "th term: "
                    + AEnum.get(BigInteger.valueOf(i)));
        }

        Enumeration<Boolean> trueEnum = Enumeration.singleton(true);
        Enumeration<Boolean> falseEnum = Enumeration.singleton(false);
        final Enumeration<Boolean> boolEnum = trueEnum.plus(falseEnum).pay();
        //System.out.println(boolEnum.get(0));


        final F<A, TTree> leaf =
                new F<A, TTree>() {
                    public TTree apply(final A elem) {
                        return new Leaf(elem);
                    }
                };

        final F<A, F<TTree, F<TTree, TTree>>> node =
                new F<A, F<TTree, F<TTree, TTree>>>() {
                    public F<TTree, F<TTree, TTree>> apply(final A head) {
                        return new F<TTree, F<TTree, TTree>>() {
                            public F<TTree, TTree> apply(final TTree t1) {
                                return new F<TTree, TTree>() {
                                    public TTree apply(final TTree t2) {
                                        return new Node(head, t1, t2);
                                    }
                                };
                            }
                        };
                    }
                };

        F<Enumeration<TTree>, Enumeration<TTree>> f = new F<Enumeration<TTree>, Enumeration<TTree>>() {

            public Enumeration<TTree> apply(final Enumeration<TTree> e) {
                return leafEnum(leaf, AEnum).plus(nodeEnum(node, AEnum, e).pay());
            }
        };

        Enumeration<TTree> listEnum = Enumeration.fix(f);

        //listEnum.pay();
        System.out.println("Enumerator for " + "TTree");
        n = 20;
        for (int i = 0; i < n; i++) {
            System.out.println("Get " + i + "th term: "
                    + listEnum.get(BigInteger.valueOf(i)));
        }

        LazyList<Finite<TTree>> parts = listEnum.parts();
        for (int i = 0; i < 3 && !parts.isEmpty(); i++) {
            System.out.println(i + " --> " + parts.head());
            parts = parts.tail();
        }

//
        for (int p = 0; p < 10000; p = p + 100) {
            BigInteger i = java.math.BigInteger.TEN.pow(p);
            System.out.println("10^" + p + " --> " + listEnum.get(i).size());
            //listEnum.get(i);
        }

    }

    private static Enumeration<TTree> leafEnum(F<A, TTree> leaf, Enumeration<A> AEnum) {
        Enumeration<F<A, TTree>> singletonLeaf = Enumeration.singleton(leaf);
        Enumeration<TTree> res = Enumeration.apply(singletonLeaf, AEnum);
        return res;
    }

    private static Enumeration<TTree> nodeEnum(F<A, F<TTree, F<TTree, TTree>>> node, Enumeration<A> AEnum, Enumeration<TTree> e) {
        Enumeration<F<A, F<TTree, F<TTree, TTree>>>> singletonNode = Enumeration.singleton(node);
        Enumeration<F<TTree, F<TTree, TTree>>> singletonNodeHeadEnum = Enumeration.apply(singletonNode, AEnum);
        Enumeration<F<TTree, TTree>> singletonNodeArg1 = Enumeration.apply(singletonNodeHeadEnum, e);
        Enumeration<TTree> res = Enumeration.apply(singletonNodeArg1, e);
        return res;
    }

    private static abstract class TTree {

        public abstract int size();
    }

    private static class Leaf extends TTree {

        private A elem;

        public Leaf(A e) {
            this.elem = e;
        }

        public String toString() {
            return "[" + elem + "]";
        }

        public int size() {
            return 1 + elem.size();
        }
    }

    private static class Node extends TTree {

        private A head;
        private TTree t1, t2;

        public Node(A h, TTree t1, TTree t2) {
            this.head = h;
            this.t1 = t1;
            this.t2 = t2;
        }

        public String toString() {
            return head + "-(" + t1 + "," + t2 + ")";
        }

        public int size() {
            return head.size() + t1.size() + t2.size();
        }
    }

    private static abstract class A {

        public abstract int size();
    }

    private static class aa extends A {

        public String toString() {
            return "a";
        }

        public int size() {
            return 0;
        }
    }

    private static class bb extends A {

        public String toString() {
            return "b";
        }

        public int size() {
            return 0;
        }
    }
}
