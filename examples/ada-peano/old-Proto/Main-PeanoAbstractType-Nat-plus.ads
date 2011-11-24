with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;
with main.PeanoAbstractType.Nat.zero ; use main.PeanoAbstractType.Nat.zero ;
package main.PeanoAbstractType.Nat.plus is 

	type plus is new Nat with 
	record
	x1 : Nat'Class ; 
	x2 : Nat'Class ; 
end	record;
	
	overriding
	function toString(this: plus) return String;
	
	overriding
	function getChildCount(this: access plus) return Integer;
	
	function getChildAt(this: access plus; index: Integer) return VisitablePtr ;
	
	overriding		
	procedure setChildren(this: access plus; childs: ObjectPtrArrayPtr ) ; 
	
	overriding
	function getChildren(this: access plus) return ObjectPtrArrayPtr ; 
	
	overriding
	procedure setChildAt(this: access plus; index: Integer; child: VisitablePtr);

	function make(x1: Nat; x2: Nat) return plus;

	procedure init(this: plus; x1: Nat; x2: Nat; hashCode: Integer);

	procedure initHashCode(this: plus);
	
	function symbolName(this: plus) return String;

	function getArity(this: plus) return Integer;

	function duplicate(this: plus) return plus;

	function compareToLPO(this: plus; o: Object'Class) return Integer;

	function compareTo(this: plus; o: Object'Class) return Integer;

	function equivalent(this: plus; o: SharedObject'Class) return boolean;

	function isplus(this: plus) return boolean;

	function getx1(this: plus) return Nat'Class;

	function setx1(this: plus; set_arg: Nat) return Nat'Class;

	function getx2(this: plus) return Nat'Class;

	function setx2(this: plus; set_arg: Nat) return Nat'Class;
	
	function hashFunction(this: plus) return Integer ;
	
end main.PeanoAbstractType.Nat.plus;

