

package body Main.PeanoAbstractType.Nat.zero is 

	function make return Zero is 
	begin
		return zero(build(Main.PeanoAbstractType.factory.all,zeroGomProto.all)) ;
	end;

	function duplicate(this: zero) return SharedObject'Class is
	begin
	return this;
end ;

	function toString(this: zero) return String is
	begin
		return "zero";
	end;

	function getArity(this: zero) return Integer is
	begin
		return 0;
	end;

	function duplicate(this: zero) return zero is
	begin
		return this;
	end;

	function equivalent(this: zero; o: SharedObject'Class) return boolean is
	begin
	
		if o in zero then return true; end if ;
		return false;

	end;

	function iszero(this: zero)  return boolean is
	begin
		return true;
	end;

	
	function hashFunction(this:zero) return Integer is 
	begin
	
		return 0 ; 
	
	end;

end Main.PeanoAbstractType.Nat.zero;



