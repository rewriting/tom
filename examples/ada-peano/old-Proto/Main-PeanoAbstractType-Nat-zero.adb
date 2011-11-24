with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;


package body peano.types.nat.zero is 

	function make(this: zero) return zero is 
	begin
		return this.gomProto ;
	end;

	
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

	function equivalent(this: zero, o: shared.SharedObject'Class) return boolean is
	begin
	
		if o in zero then return true;
		return false;

	end;

	function iszero(this: zero)  return boolean is
	begin
		return true;
	end;

	
	function hashFunction(this:zero) return Integer is 
	begin
--TODO
	end;

end peano.types.nat.zero;


