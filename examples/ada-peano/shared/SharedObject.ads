package SharedObject is 
	type SharedObject is interface;

	function duplicate(this: SharedObject) return SharedObject is abstract;
	function equivalent(this: SharedObject, o: SharedObject) is abstract;
	function hashCode(this: SharedObject) return Integer is abstract;

end SharedObject;


