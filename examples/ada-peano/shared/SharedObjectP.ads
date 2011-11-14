with ObjectPack; use ObjectPack;

package SharedObjectP is 
	type SharedObject is interface and Object;

	function duplicate(this: SharedObject'Class) return SharedObject'Class is abstract;
	function equivalent(this: SharedObject'Class; o: SharedObject'Class) return Boolean is abstract;
	function hashCode(this: SharedObject'Class) return Integer is abstract;

end SharedObjectP;


