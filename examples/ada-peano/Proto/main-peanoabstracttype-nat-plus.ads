with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;
with main.PeanoAbstractType.Nat.zero ; use main.PeanoAbstractType.Nat.zero ;
package main.PeanoAbstractType.Nat.plus is 

	type plus is new Nat with 
	record
	x1 : NatPtr; 
	x2 : NatPtr; 
end	record;

	type PlusPtr is access all Plus; 	

	function make(x1: NatPtr; x2: NatPtr) return PlusPtr;

	procedure initHashCode(this: in out plus; x1: NatPtr; x2: NatPtr);

	procedure init(this: in out plus; x1: NatPtr; x2: NatPtr; hash: Integer) ; 

	function symbolName(this: plus) return String;

	overriding
	function toString(this: plus) return String;

	function getArity(this: plus) return Integer;

	overriding
	function duplicate(this: plus) return SharedObject'Class;

	function equivalent(this: plus; o: SharedObject'Class) return boolean;

	function isplus(this: plus) return boolean;

	function hashFunction(this: plus) return Integer ;

	plusGomProto : access  plus := new plus ;
	
end main.PeanoAbstractType.Nat.plus;


