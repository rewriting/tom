	with SharedObjectP; use SharedObjectP; 
package body SharedObjectFactoryP is 
	
	
	function projector(this: SharedObjectFactory; entree: Integer) return Integer is 
	begin
		return entree mod this.table'Length ;		
	end projector;

	procedure build(this:in out SharedObjectFactory; prototype: in SharedObject'Class; foundObj: out SharedObjectPtr) is

index: Integer := projector(this,prototype.hashCode);
relevantEntry: aliased SharedObjectEntry := this.table(index);
e : access SharedObjectEntry := relevantEntry'Access ;
prev : access SharedObjectEntry := null;
deepness : Integer := 0;


	begin
	
	loop
		exit when e = null;
		
		foundObj := e.element;

		if equivalent(prototype,foundObj) then 
			-- Successful search

			if deepness > 5 and prev /= null then
				-- Swapping the found entry to first place if needed
			prev.next := e.next;
			e.all.next := this.table(index); 
			this.table(index) := e;
			end if;
		
		return ;
		end if;

		-- Going deeper in collision list
		deepness := deepness + 1 ;
		prev := e;
		e := e.next; 
		
	end loop;

	-- Search failed: constructing and inserting a new entry 
	foundObj := prototype.duplicate'Access ;
	relevantEntry := (Next => relevantEntry'Access, Element => foundObj);
	this.Size := this.Size+1 ;
		
	end build;


end SharedObjectFactoryP;

	
