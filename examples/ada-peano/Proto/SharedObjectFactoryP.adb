	with SharedObjectP; use SharedObjectP;
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;
with Ada.Text_IO; use Ada.Text_IO;

package body SharedObjectFactoryP is 
	
	
	function projector(this: SharedObjectFactory; entree: Integer) return Integer is 
	begin
		return entree mod this.table'Length ;		
	end projector;



	procedure build(this:in out SharedObjectFactory; prototype: in SharedObject'Class; foundObj: out SharedObjectPtr; foundObjPhy : out SharedObject'Class) is

index: Integer := projector(this,prototype.hashCode);
e : access SharedObjectEntry := this.table(index);
prev : access SharedObjectEntry := null;
construct : aliased SharedObject'Class := prototype ;
deepness : Integer := 0;
status : Integer ;

	begin
	Put_Line("----------------");
	Put_Line("Injection started") ;

	status := -1 ; --Default value

	loop
		exit when e = null;
		foundObj := e.element;	
		foundObjPhy := foundObj.all ;

		Put_Line("Testing against: "&foundObjPhy.toString) ;

		if equivalent(prototype,foundObj.all) then	
			-- Successful search

			if deepness > 5 and prev /= null then
			Put_Line("Reorganization");
				-- Swapping the found entry to first place if needed
			prev.next := e.next;
			e.next := this.table(index); 
			this.table(index) := this.table(index).next;
			end if;

		status := 0 ; -- Signalling already existant SharedObject
		return ;
		end if;
		-- Going deeper in collision list
		deepness := deepness + 1 ;
		prev := e;
		e := e.all.next; 
		
	end loop;

	this.Size := this.Size+1 ;
	status := +1 ; --Signalling construction of a new SharedObject
	
	Put_Line("New object constructed") ;
	
	this.table(index) := new SharedObjectEntry'(this.table(index),null) ;	
	foundObjPhy := duplicate(prototype) ;	
	this.table(index).element := foundObjPhy'Unchecked_Access ;	

	foundObj := this.table(index).element;
	
	
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
	
	Put_Line("Something"&", ") ;
	e := e.next ;

 end loop;


end loop; 	

Put_Line("------------------") ;
end stats ;


end SharedObjectFactoryP;
