with SharedObject; use SharedObject;
with Ada.Containers;  use Ada.Containers;

package SharedObjectFactory is 

 package Bucket is new Doubly_Linked_List(
      Element_Type => SharedObj,
    );
 	

	type SharedObjectFactory is record
	LogSize : Natural = 10 ;
	tableSize : Natural = 2 ** LogSize ;
	table : array (1 .. tableSize) of Bucket ;	
	end record;
	
	--function hashSize(n: Integer) return Integer;
	--function hashMask(this:SharedObjectFactory) return Integer;
	function hashKey(this:SharedObjectFactory, key: Integer) return Integer;



	function build(this: SharedObjectFactory, prototype: SharedObject) return SharedObject;



end SharedObjectFactory;

