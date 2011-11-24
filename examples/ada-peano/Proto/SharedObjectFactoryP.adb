	with SharedObjectP; use SharedObjectP;
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;
with Ada.Text_IO; use Ada.Text_IO;

package body SharedObjectFactoryP is 
	
	
	function projector(this: SharedObjectFactory; entree: Integer) return Integer is 
	begin
		return entree mod this.table'Length ;		
	end projector;



	procedure build(this:in out SharedObjectFactory; prototype: in SharedObject'Class; foundObj: out SharedObjectPtr) is

index: Integer := projector(this,prototype.hashCode);
e : access SharedObjectEntry := this.table(index);
prev : access SharedObjectEntry := null;
construct : aliased SharedObject'Class := prototype ;
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
		e := e.next; 
		
	end loop;

	this.Size := this.Size+1 ;
	status := +1 ; --Signalling construction of a new SharedObject
	
	this.table(index) := new SharedObjectEntry'(this.table(index),null) ;	
	construct := duplicate(prototype) ;	
	this.table(index).element := construct'Unchecked_Access ;	

	foundObj := this.table(index).element;
	
	
	end build;


	procedure stats(this: SharedObjectFactory) is
		i : Integer := 0;
		e : access SharedObjectEntry ;
begin

	Put_Line("This SharedObjectFactory has "&Integer'Image(this.Size)&" elements: ");

loop
 i := i + 1 ; 
 exit when i = this.table'Length ;

 Put_Line("\nCollision list "&Integer'Image(i)&": ") ;

 e := this.table(i) ; 

 loop
	exit when e = null ;

	Put_Line(e.element.all.toString&", ") ;
 
	e := e.next ;

 end loop;


end loop; 	

Put_Line("\n\n------------------") ;
end stats ;


end SharedObjectFactoryP;
