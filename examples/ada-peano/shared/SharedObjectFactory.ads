with SharedObject; use SharedObject;

package SharedObjectFactory is 

	type SharedObjectEntry is 
	record
	Next : SharedObjectEntry := null;
	Element : access all SharedObject ;
	end record;

	type SharedObjectFactory is record
	LogSize : Natural = 10 ;
	tableSize : Natural = 2 ** LogSize ;
	table : array (1 .. tableSize) of SharedObjectEntry ;	
	end record;

	function build(this: SharedObjectFactory, prototype: SharedObjec'Class) return SharedObject'Class;

end SharedObjectFactory;

