with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package Main.PeanoAbstractType.Nat.zero is 

	type zero is new Nat with null record;
	type ZeroPtr is access all Zero ;

	function make return zeroptr;

	overriding
	function duplicate(this: zero) return SharedObject'Class;

	function toString(this: zero) return String;
	
	function getArity(this: zero) return Integer;
	
	function equivalent(this: zero; o: SharedObjectP.SharedObject'Class) return boolean; 
	
	function iszero(this: zero)  return boolean;

	function hashFunction(this:zero) return Integer; 

	zeroGomProto: access zero := new zero;

end Main.PeanoAbstractType.Nat.zero;

