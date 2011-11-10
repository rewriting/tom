with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;


package body peano.types.nat.zero is 

	function make(this: zero) return zero is 
	begin
		return this.gomProto ;
	end;

	-- Name & Arity
	--
	
	function toString(this: zero) return String is
	begin
		return "zero";
	end;

	function getArity(this: zero) return Integer is
	begin
		return 0;
	end;

	function duplicate(this: zero) return SharedObject'Class is
	begin
		return this;
	end;

	function compareToLPO(this: zero,o: Object'Class) return Integer is
	begin
		peano.PeanoAbstractType ao = (peano.PeanoAbstractType) o;
		-- If equality return 0
		if ao = this then return 0; end if;
		-- If not, compare the symbols
		int symbCmp = this.symbolname().compareTo(ao.symbolName());
		if (symbCmp != 0) then return symbCmp; end if;

		-- TODO: Exception
	end;

	fuction compareTo(this: zero, o: Object'Class) return Integer is 
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

	function hashCode(this: zero) return Integer is
	begin
		return this.hashCode; 
	end;

	function equivalent(this: zero, o: shared.SharedObject'Class) return boolean is
	begin
	
		if o in zero then return true;
		return false;

	end;

	function iszero(this: zero)  return boolean is
	begin
		return true;
	end;

	
	function getChildCount(this: access Visitable'Class) return Integer is
	begin
		return 0;
	end;

	function getChildAt(this: access Visitable'Class, index: Integer) return VisitablePtr is
	begin
		case index is
			when others => raise OutofBounds; 
	end case;
	end;

	function setChildAt(this: access Visitable'Class, index: Integer, v: access Visitable'Class) return VisitablePtr is
	begin 
		case index is
			when others =>raise OutofBounds;
	end case;
	end;

	function setChildren(this: access Visitable'Class, childs: ObjectPtrArrayPtr) is
	begin
		if childs.all.length = 0 then return this.make'Access;
	else raise OutofBounds;
	end if; 
	end;

	function getChildren(this: access Visitable'Class) return ObjectPtrArrayPtr  is
	begin

		return tom.library.sl.Visitable[] {}'Access
	end ;

	function hashFunction(this:zero) return Integer is 
	begin
--TODO
	end;

end peano.types.nat.zero;


