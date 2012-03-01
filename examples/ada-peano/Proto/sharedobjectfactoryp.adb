with SharedObjectP; use SharedObjectP;
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;
with Ada.Text_IO; use Ada.Text_IO;

package body SharedObjectFactoryP is 
	
	function projector(this: SharedObjectFactory; entree: Integer) return Integer is 
	begin
		return entree mod this.table'Length ;		
	end projector;
	function build(this:  SharedObjectFactory; prototype: SharedObject'Class; adr: SharedObjectPtr) return SharedObject'Class is 
		
foundObj : SharedObjectPtr ;
foundObjPhy : aliased SharedObject'Class := duplicate(prototype) ;
index: Integer := projector(this,prototype.hashCode);
e : access SharedObjectEntry := this.table(index);
prev : access SharedObjectEntry := null;
deepness : Integer := 0;
status : Integer ;

	begin
	status := -1 ; --Default value

	loop
		exit when e = null;
		foundObj := e.element;	
		if equivalent(prototype,foundObj.all) then	
			-- Successful search
			if deepness > 5 and prev /= null then
				-- Swapping the found entry to first place if needed
				prev.next := e.next ;
				e.next := this.table.all(index);
				this.table.all(index) := this.table.all(index).next ;
				end if;

		status := 0 ; -- Signalling already existant SharedObject
		foundObjPhy := foundObj.all ;
		return foundObjPhy ;
		end if;
		-- Going deeper in collision list
		deepness := deepness + 1 ;
		prev := e;
		e := e.all.next; 
		
	end loop;

	status := +1 ; --Signalling construction of a new SharedObject
	
	this.table.all(index) := new SharedObjectEntry'(this.table.all(index),null) ;
	this.table.all(index).element := adr ; 

	return foundObjPhy ;	
	
	end build;



	procedure stats(this: SharedObjectFactory) is
		i : Integer := -1;
		e : access SharedObjectEntry ;
		currentEntry : SharedObjectEntry ;
		currentElement : SharedObjectPtr ;
begin
Put_Line("") ;
Put_Line("------------------") ;
	

	Put_Line("This SharedObjectFactory has"&Integer'Image(this.Size)&" elements: ");

loop
 i := i + 1 ; 
 exit when i = this.table'Length ;
 Put_Line("Collision list "&Integer'Image(i)&": ") ;
 e := this.table(i) ; 
 loop
	exit when e = null ;
	currentEntry := e.all ;
	currentElement := currentEntry.element ;
	Put_Line(currentElement.toString&", ") ;
	e := e.next ;

 end loop;
end loop; 	
Put_Line("------------------") ;
end stats ;


end SharedObjectFactoryP;
