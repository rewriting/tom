with ObjectPack; use ObjectPack;

package SharedObjectP is 
	type SharedObject is new Object with null record;
	
	overriding
	function toString(this:SharedObject) return String;
	function duplicate(this: SharedObject) return SharedObject ;
	function equivalent(this: SharedObject; o: SharedObject) return Boolean;
	function hashCode(this: SharedObject) return Integer;


end SharedObjectP;
