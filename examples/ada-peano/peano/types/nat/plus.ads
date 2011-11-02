with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;
use tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;

package peano.types.nat.plus is new peano.types.nat
	
	type plus is new Nat with Visitable with 
	record
	gomProto: plus := new plus;
	hashCode: integer;
	_x1 : Nat;
	_x2 : Nat;
	end record
	
	gomProto: plus := new plus();

	function make(_x1: Nat, _x2: Nat) return plus;

	procedure init(this: plus, _x1: Nat, _x2: Nat, hashCode: Integer);

	procedure initHashCode(this: plus);
	
	function symbolName(this: plus) return String;

	function getArity(this: plus) return Integer;

	function duplicate(this: plus) return SharedObject;

	function compareToLPO(this: plus, o: Object'Class) return Integer;

	function compareTo(this: plus, o: Object'Class) return Integer;

	function hashcode(this: plus) return Integer;

	function equivalent(this: plus, o: SharedObject'Class) return boolean;

	function isplus(this: plus) return boolean;

	function getx1(this: plus) return Nat;

	function setx1(this: plus, set_arg: Nat) return Nat;

	function getx2(this: plus) return Nat;

	function setx2(this: plus, set_arg: Nat) return Nat;
	
	function getChildCount(this: access Visitable'Class) return Integer;

	function getChildAt(this: access Visitable'Class, index: Integer) return VisitablePtr ;

	function setChildAt(this: access Visitable'Class, index: Integer, v: access Visitable'Class) return VisitablePtr;

	function setChildren(this: access Visitable'Class, childs: ObjectPtrArrayPtr ) return ObjectPtrArrayPtr ;

	function getChildren(this: access Visitable'Class) return ObjectPtrArrayPtr ; 
	
end peano.types.nat.plus;

