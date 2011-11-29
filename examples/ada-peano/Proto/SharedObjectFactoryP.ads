with SharedObjectP; use SharedObjectP;


package SharedObjectFactoryP is 

	type SharedObjectPtr is access all SharedObject'Class;
	pragma Controlled (SharedObjectPtr) ;

	type SharedObjectEntry is  
	record
	Next : access SharedObjectEntry := null;
	Element : SharedObjectPtr := null ;
	end record;

	type SharedObjectEntryPtr is access all SharedObjectEntry;	
	pragma Controlled (SharedObjectEntryPtr) ;

	type HashTable is array (Integer range <>) of access SharedObjectEntry;
	type chosenTable is new HashTable (0 .. 99) ;
	type tablePtr is access all chosenTable;
	

	type SharedObjectFactory is record
	Size : Natural := 0 ;
	LogSize : Natural := 10 ;
	table : tablePtr := new chosenTable ;
	end record;

	function projector(this: SharedObjectFactory; entree: Integer) return Integer;

	function build(this: SharedObjectFactory; prototype: SharedObject'Class) return SharedObject'Class ;

	procedure plusSize(table: in out chosenTable; level: Integer); 

procedure swapToFirst(table: in out chosenTable; prev: in out SharedObjectEntry; e:in out SharedObjectEntry; index: Integer) ;

	procedure insertInTable(table: in out chosenTable; index: Integer; foundObj: SharedObjectPtr) ;

	procedure stats(this: SharedObjectFactory) ;

end SharedObjectFactoryP;

