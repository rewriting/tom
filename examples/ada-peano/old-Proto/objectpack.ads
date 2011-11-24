package ObjectPack is

	type Object is interface;
		function toString(obj: Object) return String is abstract;
	type ObjectPtr is access all Object'Class;
	type ObjectPtrArray is array (Natural range <>) of ObjectPtr;
	type ObjectPtrArrayPtr is access all ObjectPtrArray;
	
	generic
		type Item is private;
	function hash(obj: Item) return Integer;
	
end;
