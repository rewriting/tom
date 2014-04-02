package propcheck.shrink;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PropcheckShink<A> implements Shrink<A> {

	private List<A> subterms = null;
	private Queue<Integer> indexes = null;
	
	private A term;
	private SubtermTraverser<A> traverser;
	
	private int index = 0;
	private boolean isNewInstance = true;

	public PropcheckShink(A term) {
		init(term);
	}

	void init(A term) {
		this.term = term;
		if (subterms == null) {
			subterms = new ArrayList<A>();
		} else {
			subterms.clear();
		}
		if (indexes == null) {			
			indexes = new LinkedList<Integer>();
		} else {
			indexes.clear();
		}
		traverser = SubtermTraverser.make(term);
		// add constants
		subterms.addAll(traverser.getConstants());
		
		// if is not newly instantiated, init() called from setTerm
		// then index should be 1 if there is constant
		if (!isNewInstance && !subterms.isEmpty()) {
			index = 1;
		} else {
			index = 0;
		}
		
		// add immediate subterms
		addSubtermsToList(retrieveSubterms(term));
		
		
	}
	
	public void addSubtermsToList(List<A> retrievedSubterms) {
		int lastIndex = subterms.size() - 1;
		for (int i = 0; i < retrievedSubterms.size(); i++) {
			lastIndex++;
			indexes.add(lastIndex);
			subterms.add(retrievedSubterms.get(i));
		}
	}
	
	@Override
	public boolean hasNextSubterm() {
		if (index < subterms.size()) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<A> retrieveSubterms(A term) {
		return traverser.getSubterms(term);
	}
	
	public void retrieveSubtermsRec() {
		if (!indexes.isEmpty()) {
			boolean retrieved = false;
			// try getting new subterms until one is retrieved, if not then
			// it will try to retrieve the next subterm in the list
			while (!retrieved && !indexes.isEmpty()) {
				// retrieve and remove the first element
				int idx = indexes.poll();
				A term = subterms.get(idx);
				List<A> terms = retrieveSubterms(term);
				if (!terms.isEmpty()) {
					addSubtermsToList(retrieveSubterms(term));
					retrieved = true;
				}
			}
		}
	}
	
	/**
	 * return constants first, then return subterms, then return subterms of each subterm recursively
	 */
	@Override
	public A getNextshrinkedTerm() {
		A subterm = subterms.get(index++);
		
		// fetch new subterms if possible
		if (index == subterms.size() - 1) {
			retrieveSubtermsRec();
		}
		return subterm;
	}

	@Override
	public A getCurrentTerm() {
		return term;
	}

	@Override
	public void setCurrentTerm(A term) {
		isNewInstance = false;
		init(term);
	}
}