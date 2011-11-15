	with SharedObjectP; use SharedObjectP; 
package body SharedObjectFactoryP is 
	
	
	function projector(this: SharedObjectFactory; entree: Integer) return Integer is 
	begin
		return entree mod this.table'Length ;		
	end projector;

	procedure build(this:in out SharedObjectFactory; prototype: in SharedObject'Class; foundObjPhy: out SharedObject'Class) is

index: Integer := projector(this,prototype.hashCode);
relevantEntry: aliased SharedObjectEntry := this.table(index).all;
e : access SharedObjectEntry := this.table(index);
prev : access SharedObjectEntry := null;
deepness : Integer := 0;

	begin
	
	loop
		exit when e = null;
		
		foundObjPhy := e.element.all;

		if equivalent(prototype,foundObjPhy) then 
			-- Successful search

			if deepness > 5 and prev /= null then
				-- Swapping the found entry to first place if needed
			prev.next := e.next;
			e.all.next := this.table(index); 
			this.table(index) := this.table(index).next;
			end if;
		return ;
		end if;

		-- Going deeper in collision list
		deepness := deepness + 1 ;
		prev := e;
		e := e.next; 
		
	end loop;

	-- Search failed: constructing and inserting a new entry
	foundObjPhy := prototype.duplicate ; 
	this.table(index).all := (Next => this.table(index), Element => foundObjPhy'Access);
	this.Size := this.Size+1 ;
	return ;

	end build;

	function toString(this: SharedObjectFactory) return String is
begin

		return "SharedObjectFactory has "&Integer'Image(this.Size)&" elements"; 

end toString ;


end SharedObjectFactoryP;
