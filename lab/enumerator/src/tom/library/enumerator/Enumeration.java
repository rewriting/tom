package tom.library.enumerator;

import java.math.BigInteger;
import java.util.List;

public class Enumeration<A> {

    private LazyList<Finite<A>> cacheParts;
    public P1<LazyList<Finite<A>>> p1;

    public Enumeration(final LazyList<Finite<A>> p) {
        this.cacheParts = p;
        this.p1 = new P1<LazyList<Finite<A>>>() {
    		public LazyList<Finite<A>> _1() {
    			return p;
    		}
    	};
    }

    public Enumeration(P1<LazyList<Finite<A>>> p1) {
        this.p1 = p1;
    }

    public LazyList<Finite<A>> parts() {
        if (cacheParts == null) {
            cacheParts = p1._1();
        }
        return cacheParts;
    }

    public static <A> Enumeration<A> empty() {
        return new Enumeration<A>(LazyList.<Finite<A>>nil());
    }

    public static <A> Enumeration<A> singleton(A x) {
        return new Enumeration<A>(LazyList.singleton(Finite.singleton(x)));
    }

    public A get(BigInteger i) {
        return index(parts(), i);
    }

    public Enumeration<A> plus(final Enumeration<A> other) {
        return new Enumeration<A>(
                new P1<LazyList<Finite<A>>>() {
                    public LazyList<Finite<A>> _1() {
                        return zipPlus(Enumeration.this.parts(), other.parts());
                    }
                });
    }

    /**
     * tools for LazyList
     */
    private static <A> A index(LazyList<Finite<A>> ps, BigInteger i) {
        while (!ps.isEmpty()) {
            Finite<A> head = ps.head();
            BigInteger card = head.getCard();
            if (i.compareTo(card) < 0) {
                return head.get(i);
            } else {
            	ps = ps.tail();
            	i = i.subtract(card);
            }
        }
        throw new RuntimeException("index " + i + " out of range");
    }
    
    /*
     * zipPlus([f1,f2,f3], [g1,g2]) = [f1+g1, f2+g2] 
     */
    private static <A> LazyList<Finite<A>> zipPlus(final LazyList<Finite<A>> xs, final LazyList<Finite<A>> ys) {
    	if (xs.isEmpty() || ys.isEmpty()) {
    		return xs.append(ys);
    	}
    	P1<LazyList<Finite<A>>> p = new P1<LazyList<Finite<A>>>() {
    		public LazyList<Finite<A>> _1() {
    			return zipPlus(xs.tail(), ys.tail());
    		}
    	};
    	return LazyList.cons(xs.head().plus(ys.head()),p);
    }

    public <B> Enumeration<B> map(final F<A, B> f) {
        final F<Finite<A>, Finite<B>> g = new F<Finite<A>, Finite<B>>() {
            public Finite<B> apply(Finite<A> x) {
                return x.map(f);
            }
        };
        return new Enumeration<B>(
                new P1<LazyList<Finite<B>>>() {
                    public LazyList<Finite<B>> _1() {
                        return Enumeration.this.parts().map(g);
                    }
                });
    }

    public Enumeration<A> pay() {
        return new Enumeration<A>(
                new P1<LazyList<Finite<A>>>() {
                    public LazyList<Finite<A>> _1() {
                    	return LazyList.cons(Finite.<A>empty(), p1);
                    }
                });
    }

    public Enumeration<A> pay(final int n) {
    	return new Enumeration<A>(
    			new P1<LazyList<Finite<A>>>() {
    				public LazyList<Finite<A>> _1() {
    					LazyList<Finite<A>> res = LazyList.cons(Finite.<A>empty(), p1);
    					for(int i=1 ; i<n ; i++) {
    						final LazyList<Finite<A>> tail = res;
    						res = LazyList.cons(Finite.<A>empty(), new P1<LazyList<Finite<A>>>() {
    							public LazyList<Finite<A>> _1() { return tail; };
    						});
    					}
    					return res;
    				}
    			});
    }
    
    public <B> Enumeration<P2<A, B>> times(final Enumeration<B> other) {
        return new Enumeration<P2<A, B>>(
                new P1<LazyList<Finite<P2<A, B>>>>() {
                    public LazyList<Finite<P2<A, B>>> _1() {
                        return prod(Enumeration.this.parts(), other.parts().reversals());
                    }
                });
    }

    /**
     * tools for LazyList
     */
    
    
    
    private static <A, B> LazyList<Finite<P2<A, B>>> prod(LazyList<Finite<A>> xs, final LazyList<LazyList<Finite<B>>> rys) {
        if (xs.isEmpty() || rys.isEmpty()) {
            return LazyList.nil();
        }
        return goY(xs, rys);
    }

    private static <A, B> LazyList<Finite<P2<A, B>>> goY(final LazyList<Finite<A>> xs, final LazyList<LazyList<Finite<B>>> rys) {
    	P1<LazyList<Finite<P2<A, B>>>> p = new P1<LazyList<Finite<P2<A, B>>>>() {
    		public LazyList<Finite<P2<A, B>>> _1() {
    			return (rys.tail().isEmpty()) ? goX(xs, rys.head()) : goY(xs, rys.tail());
    		}
    	};
    	return LazyList.cons(conv(xs, rys.head()), p);
    }

    private static <A, B> LazyList<Finite<P2<A, B>>> goX(final LazyList<Finite<A>> xs, final LazyList<Finite<B>> ry) {
        F<LazyList<Finite<A>>, Finite<P2<A, B>>> fs = new F<LazyList<Finite<A>>, Finite<P2<A, B>>>() {
            public Finite<P2<A, B>> apply(LazyList<Finite<A>> x) {
                return conv(x, ry);
            }
        };
        return xs.tail().tails().map(fs);
    }

    private static <A, B> Finite<P2<A, B>> conv(LazyList<Finite<A>> xs, LazyList<Finite<B>> ys) {
    	Finite<P2<A,B>> result = Finite.empty();
		if(ys.isEmpty()) { return result; }
    	while(true) {
    		if(xs.isEmpty()) { return result; }
    		result = result.plus(xs.head().times(ys.head()));
    		ys = ys.tail();
    		if(ys.isEmpty()) { return result; }
    		xs = xs.tail();
    	}
    }

    public static <A, B> Enumeration<B> apply(final Enumeration<F<A, B>> subject, final Enumeration<A> other) {
        F<P2<F<A, B>, A>, B> pair = new F<P2<F<A, B>, A>, B>() {
            public B apply(P2<F<A, B>, A> p) {
                return p._1().apply(p._2());
            }
        };
        return subject.times(other).map(pair);
    }

    public static <E> Enumeration<E> fix(final F<Enumeration<E>, Enumeration<E>> f) {
        Enumeration<E> e = new Enumeration<E>((LazyList<Finite<E>>) null);
        final Enumeration<E> res = f.apply(e);
        e.p1 = new P1<LazyList<Finite<E>>>() {

            public LazyList<Finite<E>> _1() {
                return res.cacheParts;
            }
        };
        //e.cacheParts = res.cacheParts;

        return res;
    }

    public static <A, B> Enumeration<A> fixMultiple(
            final F<Enumeration<A>, Enumeration<B>> f1AB, 
            final F<Enumeration<B>, Enumeration<B>> f1_B,
            final F<Enumeration<B>, Enumeration<A>> f1BA, 
            final F<Enumeration<A>, Enumeration<A>> f1AA, 
            final F<Enumeration<A>, Enumeration<A>> f1_A) {
        
        Enumeration<A> ea = new Enumeration<A>((LazyList<Finite<A>>) null);
        Enumeration<B> eb = new Enumeration<B>((LazyList<Finite<B>>) null);

        final Enumeration<B> resfAB = f1AB.apply(ea);
        final Enumeration<B> resf_B = f1_B.apply(eb);
        final Enumeration<A> resfBA = f1BA.apply(eb);
        final Enumeration<A> resfAA = f1AA.apply(ea);
        final Enumeration<A> resf_A = f1_A.apply(ea);

        final Enumeration<A> resA = resfBA.plus(resfAA).plus(resf_A);
        final Enumeration<B> resB = resfAB.plus(resf_B);

        ea.p1 = new P1<LazyList<Finite<A>>>() {

            public LazyList<Finite<A>> _1() {
                return resA.parts();
            }
        };

        eb.p1 = new P1<LazyList<Finite<B>>>() {

            public LazyList<Finite<B>> _1() {
                return resB.parts();
            }
        };

        return resA;
    }
    
    public List<List<A>> toList() {
    	F<Finite<A>,List<A>> f = new F<Finite<A>,List<A>>() {
    		public List<A> apply(Finite<A> arg) {
    			return arg.toList();
    		}
    	};
    	return parts().map(f).toList();
    }
    
    public static <A> Enumeration<A> fromList(List<List<A>> l) {
    	LazyList<List<A>> res = LazyList.fromList(l);
    	F<List<A>,Finite<A>> f = new F<List<A>,Finite<A>>() {
    		public Finite<A> apply(List<A> arg) {
    			return Finite.fromList(arg);
    		}
    	};
    	return new Enumeration<A>(res.map(f));
    }
    
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Enumeration) {
			return this.toList().equals(((Enumeration<?>) obj).toList());
		}
		return super.equals(obj);
	}

    
    public String toString() {
		return toList().toString();
    }
    
}
