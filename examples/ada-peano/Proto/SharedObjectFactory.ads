with SharedObject; use SharedObject;

package SharedObjectFactory is 

	type SharedObjectEntry is 
	record
	Next : access all SharedObjectEntry := null;
	Element : access SharedObject.SharedObject ;
	end record;

	type GenArray is array (Integer range <>) of SharedObjectEntry;

	type SharedObjectFactory is record
	LogSize : Natural := 10 ;
	table : GenArray (1 .. 2 ** 10) ;
	end record;

	function projector(this: SharedObjectFactory; entree: Integer) return Integer;

	function build(this: SharedObjectFactory; prototype: SharedObject.SharedObject'Class) return SharedObject.SharedObject'Class;

end SharedObjectFactory;

