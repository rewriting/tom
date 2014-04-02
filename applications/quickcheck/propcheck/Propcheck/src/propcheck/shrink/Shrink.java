package propcheck.shrink;

public interface Shrink<A> {
	/**
	 * Shrinks the term to its possible subterm. This implementation is 
	 * based on {@link <a href="http://hackage.haskell.org/package/QuickCheck-2.7.3/docs/src/Test-QuickCheck-Arbitrary.html#genericShrink"> Haskell's genericShrink </a>}
	 * @return the shrinking result
	 */
	public A getNextshrinkedTerm();
	
	/**
	 * returns current term
	 * @return current term
	 */
	public A getCurrentTerm();
	
	/**
	 * Sets current term
	 */
	public void setCurrentTerm(A term);
	
	/**
	 * 
	 * @return true if the term can be shrunk
	 */
	public boolean hasNextSubterm();
}
