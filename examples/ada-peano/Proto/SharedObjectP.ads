with ObjectPack; use ObjectPack;

package SharedObjectP is 
	type SharedObject is abstract new Object with record
	hashCode : Integer ; 
end record ;	
	
	function duplicate(this: SharedObject) return SharedObject'Class is abstract ;
	function equivalent(this: SharedObject; o: SharedObject'Class) return Boolean ;


end SharedObjectP;
