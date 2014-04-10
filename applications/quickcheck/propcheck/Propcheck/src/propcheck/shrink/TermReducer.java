package propcheck.shrink;

import java.util.ArrayList;
import java.util.List;

import tom.library.sl.Visitable;

/**
 * alg:
 * 
 * get children
 * for each children
 * 	while child has next subterm
 * 		substitute term's child with its subterms
 * @author nauval
 *
 * @param <A>
 */
public class TermReducer<A> {
	private A rootTerm;
	private List<Pair<?>> subterms = null; 
	private List<?> rootSubterms = null;
	private SubtermTraverser<A> traverser;

	private int index;
	private boolean newInit = true;
	
	public TermReducer(A term) {
		init(term);
	}

	/**
	 * get immediate subterms 
	 * @param term
	 */
	void init(A term) {
		rootTerm = term;
		if (subterms == null) {
			subterms = new ArrayList<Pair<?>>();
		}
		//		if (rootSubterms == null) {
		//			rootSubterms = new ArrayList();
		//		}

		index = 0;
		buildSubterms();
		newInit = false;
	}

	public void buildSubterms() {
		traverser = SubtermTraverser.make(rootTerm);
		rootSubterms = traverser.getImmediateSubterms(rootTerm);

		// build constants as the first substituted subterms
		if (newInit) {
			for (int i = 0; i < rootSubterms.size(); i++) {
				subterms.addAll(getConstantsSubterms(rootSubterms.get(i), i));
			}
		}

		// build first level pair of index and subterms
		for (int i = 0; i < rootSubterms.size(); i++) {
			subterms.addAll(getSubterms(rootSubterms.get(i), i));
		}
	}

	public <E> List<Pair<E>> getConstantsSubterms(E term, int childIndex) {
		List<Pair<E>> pairs = new ArrayList<Pair<E>>();
		SubtermTraverser<E> t = SubtermTraverser.make(term);
		if (t.getConstants().contains(term)) {
			return pairs;
		}
		for (E constant : t.getConstants()) {
			pairs.add(new Pair<E>(childIndex, constant));
		}
		return pairs;
	}

	public <E> List<Pair<E>> getSubterms(E term, int childIndex) {
		List<Pair<E>> pairs = new ArrayList<Pair<E>>();
		SubtermTraverser<E> t = SubtermTraverser.make(term);
		List<E> sts = t.getSubterms(term);
		// add constants
		//		if (!t.getConstants().contains(term)) {
		//			List<E> csts = t.getConstants();
		//			for (E e : csts) {
		//				pairs.add(new Pair<E>(childIndex, e));
		//			}
		//		}
		//		System.out.println("TermReducer.getSubterms()");
		//		System.out.println("immediate st: " + term);
		for (E e : sts) {
			//			System.out.println("term: " + term + "\npair[" + childIndex + ", " + e + "]");
			pairs.add(new Pair<E>(childIndex, e));
		}
		return pairs;
	}

	/**
	 * 
	 *  
	 * @return reduced term
	 */
	public A reduce() {
		A term = replaceSubterm(rootTerm);
		rootTerm = term;
		if (index == subterms.size()) {
			buildSubterms();
		}
		return term;
	}

	public A replaceSubterm(A term) {
		Pair<?> pair = subterms.get(index++);
		A reducedTerm = null;
		try {
			reducedTerm = (A) ShrinkTools.invokeMethod(term, "setChildAt", new Class<?>[] {int.class, Visitable.class}, new Object[] {pair.getIndex(), pair.getTerm()});
		} catch (ShrinkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reducedTerm;
	}

	public boolean isReducable() {
		if (index < subterms.size()) {
			return true;
		} else {
			return false;
		}
	}

	public A getRootTerm() {
		return rootTerm;
	}

	public void setRootTerm(A rootTerm) {
		this.rootTerm = rootTerm;
	}
}

class Pair<A> {
	private int index;
	private A term;

	public Pair(int index, A term) {
		this.index = index;
		this.term = term;
	}

	public A getTerm() {
		return term;
	}

	public void setTerm(A term) {
		this.term = term;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}