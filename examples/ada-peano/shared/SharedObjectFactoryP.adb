	with SharedObjectP; use SharedObjectP; 
package body SharedObjectFactoryP is 
	
	
	function projector(this: SharedObjectFactory; entree: Integer) return Integer is 
	begin
		return entree mod this.table'Length ;		
	end projector;



	procedure build(this:in out SharedObjectFactory; prototype: in SharedObject'Class; foundObj: out SharedObjectPtr; status: out Integer) is

index: Integer := projector(this,prototype.hashCode);
e : access SharedObjectEntry := this.table(index);
prev : access SharedObjectEntry := null;
deepness : Integer := 0;
	begin
	
	status := 0 ; --Default value

	loop
		exit when e = null;
	
		foundObj := e.element'Unchecked_Access;	

		if equivalent(prototype,foundObj.all) then 
			-- Successful search

			if deepness > 5 and prev /= null then
				-- Swapping the found entry to first place if needed
			prev.next := e.next;
			e.next := this.table(index); 
			this.table(index) := this.table(index).next;
			end if;


		status := +1 ; -- Signalling already existant SharedObject
		return ;
		end if;

		-- Going deeper in collision list
		deepness := deepness + 1 ;
		prev := e;
		e := e.next; 
		
	end loop;

	this := addingEntryToTable(this,index,prototype);
	foundObj := this.table(index).element'Access;
	this.Size := this.Size+1 ;
	status := -1 ; --Signalling construction of a new SharedObject
	end build;


	function addingEntryToTable(this: in SharedObjectFactory; index: in Integer; prototype: SharedObject'Class) return SharedObjectFactory is
	
		newEntry : aliased SharedObjectEntry ;
		newSharedObject : SharedObject := prototype.duplicate ;
		tempFactory : SharedObjectFactory := this ;
			
	begin

	return tempFactory : SharedObjectFactory := this  do 

		newEntry.Next := this.table(index); 
		newEntry.Element := newSharedObject;

		tempFactory.table(index) := newEntry'Access ;
		
	end return ;

	end addingEntryToTable; 




	function toString(this: SharedObjectFactory) return String is
begin

		return "This SharedObjectFactory has "&Integer'Image(this.Size)&" elements"; 

end toString ;


end SharedObjectFactoryP;
