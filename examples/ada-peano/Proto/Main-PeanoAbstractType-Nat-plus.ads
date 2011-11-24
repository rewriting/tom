with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;
with main.PeanoAbstractType.Nat.zero ; use main.PeanoAbstractType.Nat.zero ;
package main.PeanoAbstractType.Nat.plus is 

	type plus is new Nat with 
	record
	x1 : NatPtr; 
	x2 : NatPtr; 
end	record;

	function make(x1: NatPtr; x2: NatPtr) return plus;

	procedure initHashCode(this: in out plus; x1: NatPtr; x2: NatPtr);

	procedure init(this: in out plus; x1: NatPtr; x2: NatPtr; hash: Integer) ; 

	function symbolName(this: plus) return String;

	function toString(this: plus) return String;

	function getArity(this: plus) return Integer;

	function duplicate(this: plus) return plus;

	function equivalent(this: plus; o: SharedObject'Class) return boolean;

	function isplus(this: plus) return boolean;

	function getx1(this: plus) return Nat'Class;

	function setx1(this: plus; set_arg: NatPtr) return Nat'Class;

	function getx2(this: plus) return Nat'Class;

	function setx2(this: plus; set_arg: NatPtr) return Nat'Class;
	
	function hashFunction(this: plus) return Integer ;

	plusGomProto : access  plus := new plus ;
	
end main.PeanoAbstractType.Nat.plus;


