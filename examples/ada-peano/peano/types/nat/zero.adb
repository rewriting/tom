with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;


package body peano.types.nat.zero is 

	function make is 
	begin
		return this.gomProto ;
	end;

	-- Name & Arity
	--
	
	function symbolName is
	begin
		return "zero";
	end;

	function getArity is
	begin
		return 0;
	end;

	function duplicate is
	begin
		return this;
	end;

	function compareToLPO is
	begin
		peano.PeanoAbstractType ao = (peano.PeanoAbstractType) o;
		-- If equality return 0
		if ao = this then return 0; end if;
		-- If not, compare the symbols
		int symbCmp = this.symbolname().compareTo(ao.symbolName());
		if (symbCmp != 0) then return symbCmp; end if;

		-- TODO: Exception
	end;

	fuction compareTo is 
	begin
		peano.PeanoAbstractType ao = (peano.PeanoAbstractType) o;
		-- If equality return 0
		if ao = this then return 0; end if;
		-- If not, use hash to compare	
		if ao.hashCode != N.hashCode then return hashCode (hashCode < ao.hashCode())?-1:1; end if; 	
		
		int symbCmp = this.symbolname().compareTo(ao.symbolName());
		if (symbCmp != 0) then return symbCmp; end if; 

		-- TODO: Exception
	end;

	-- shared.SharedObject
	--

	function hashCode is
	begin
		return this.hashCode; 
	end;

	function equivalent is
	begin
	
		if o in zero then return true;
		return false;

	end;

	function iszero is
	begin
		return true;
	end;

	
	function getChildCount is
	begin
		return 0;
	end;

	function getChildAt is
	begin
		case index is
			when others => raise OutofBounds; 
	end case;
	end;

	function setChildAt is
	begin 
		case index is
			when others =>raise OutofBounds;
	end case;
	end;

	function setChildren is
	begin
		if childs.all.length = 0 then return this.make'Access;
	else raise OutofBounds;
	end if; 
	end;

	function getChildren is
	begin

		return tom.library.sl.Visitable[] {}'Access
	end ;

	function hashFunction is 
	begin

	end;

end peano.types.nat.zero;


