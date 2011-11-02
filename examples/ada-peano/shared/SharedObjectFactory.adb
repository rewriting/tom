package body SharedObjectFactory is 
	with SharedObject; use SharedObject; 
 	with Ada.Containers.Hash_Tables; use Ada.Containers;	
	
	--function hashSize is
	--begin
	--	return ( 2 ** n); 
	--end;

	--function hashMask is
	--begin 
	--	return (hashSize(this.logsize) - 1) ;
	--end;

	function hashKey is
	begin
		return -- ???
	end;

	function hash is
	begin
		return(hashCode(a.all));
	end;	

	function build is
	begin
	
		foundObj : SharedObject;
	-- HashTable
	tab : array (1 .. this.tableSize) of Bucket ;	
	tab = this.table;
	-- Using hash to find appropriate bucket
        hash : integer = prototype.hashCode ;
	index : integer = hashKey(hash);
	buck : Bucket = tab(index);
	-- Introducing a cursor to go through it
	pos : Cursor = First(buck);


	-- Looking for the appropriate object if it exists
	loop
	exit when pos = No_Element ;
	foundObj = buck.Element(pos);
	if foundObj = prototype then
		return foundObj ;
	end if;
	end loop	

	-- If nothing old found, build anew
	foundObj = prototype.duplicate();
	-- ..and insert it in appropriate bucket
	Doubly_Linked_List.append(buck,foundObj,1);
        return foundObj;  	


end SharedObjectFactory;

	
