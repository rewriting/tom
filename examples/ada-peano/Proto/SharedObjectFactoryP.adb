	with SharedObjectP; use SharedObjectP;
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;
with Ada.Text_IO; use Ada.Text_IO;

package body SharedObjectFactoryP is 
	
	
	function projector(this: SharedObjectFactory; entree: Integer) return Integer is 
	begin
		return entree mod this.table'Length ;		
	end projector;



	function build(this:  SharedObjectFactory; prototype: SharedObject'Class) return SharedObject'Class is 
		

foundObj : SharedObjectPtr ;
foundObjPhy : aliased SharedObject'Class := duplicate(prototype) ;
index: Integer := projector(this,prototype.hashCode);
e : access SharedObjectEntry := this.table(index);
prev : access SharedObjectEntry := null;
deepness : Integer := 0;
status : Integer ;

	begin
	Put_Line("----------------");
	Put_Line("Injection started") ;

	status := -1 ; --Default value

	loop
		exit when e = null;
		foundObj := e.element;	

		Put_Line("Comparing to item n°"&Integer'Image(deepness)&" in collision list n°"&Integer'Image(index)&": "&toString(foundObj.all)) ;

		if equivalent(prototype,foundObj.all) then	
			-- Successful search

			if deepness > 5 and prev /= null then
			Put_Line("Reorganization");
				-- Swapping the found entry to first place if needed
			swapToFirst(this.table.all,prev.all, e.all, index) ; 
			end if;

		status := 0 ; -- Signalling already existant SharedObject
		foundObjPhy := foundObj.all ;
		Put_Line("Equivalent found");
		return foundObjPhy ;
		end if;
		-- Going deeper in collision list
		deepness := deepness + 1 ;
		prev := e;
		e := e.all.next; 
		
	end loop;

	status := +1 ; --Signalling construction of a new SharedObject
	
	Put_Line("New object constructed") ;

	foundObj := foundObjPhy'Unchecked_Access ;
	insertInTable(this.table.all,index,foundObj) ;

	return foundObjPhy ;	
	
	end build;

	procedure swapToFirst(table: in out chosenTable; prev: in out SharedObjectEntry; e: in out SharedObjectEntry; index: Integer) is
	begin
	
			prev.next := e.next;
			e.next := table(index); 
			table(index) := table(index).next;

	end; 


	procedure insertInTable(table: in out chosenTable; index: Integer; foundObj: SharedObjectPtr) is
	begin

		
	
	table(index) := new SharedObjectEntry'(table(index),null) ;	
	table(index).element := foundObj ;	


	end;

	procedure plusSize(table: in out chosenTable; level: Integer) is begin
	
	null;

	end;	

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
