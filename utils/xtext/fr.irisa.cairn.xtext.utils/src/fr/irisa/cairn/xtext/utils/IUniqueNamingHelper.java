package fr.irisa.cairn.xtext.utils;

/**
 * Identify naming groups of objects. See
 * {@link NamesAreUniqueInGroupValidationHelper}.
 * 
 * @author Antoine Floc'h - Initial contribution and API
 * 
 */
public interface IUniqueNamingHelper {

	/**
	 * Test if two objects are considered to be in the same naming group.
	 * 
	 * @param o1
	 * @param o2
	 * @return true if o1 and o2 mustn't have the same name.
	 * 
	 */
	public boolean areInSameNamingGroup(Object o1, Object o2);
}
