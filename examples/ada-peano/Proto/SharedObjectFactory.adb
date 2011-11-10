package body SharedObjectFactory is 
	with SharedObject; use SharedObject; 



	function projector(this: SharedObjectFactory, entree: Integer) return Integer is 
	begin
		return entree mod SharedObjectFactory.table'Length ;		
	end projector;

	function build(this: SharedObjectFactory, prototype: SharedObject'Class) return SharedObject'Class is
	begin
	index: Integer := this.projecteur(prototype.hashCode);
	
	e : SharedObjectEntry := this.table(index);
	prev : SharedObjectEntry := null;
	deepness : Integer := 0;

	loop
		exit when e = null;
		
		foundObj: SharedObj := e.element.all;

		if prototype.equivalent(foundObj) then 
			-- Successful search

			if deepness > 5 and prev != null then
				-- Swapping the found entry to first place if needed
			prev.next := e.next;
			e.next := this.table(index); 
			this.table(index) := e;
			end if;
		
		return foundObj;
		end if;

		-- Going deeper in collision list
		deepness := deepness + 1 ;
		prev := e;
		e := e.next; 
		
	end loop;

	-- Search failed: constructing and inserting a new entry 
	foundObj = prototype.duplicate() ;
	this.table(index) := (Next => this.table(index), Element => foundObj'Access);
	return foundObj;
		
	end build;


end SharedObjectFactory;

	
