with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;
use tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;

package peano.types.nat.zero is new peano.types.nat with

	type zero is tagged new Nat with Visitable;
		record
		hashCode: integer;
		gomProto: zero := (zero) factory.build(new zero())
		end record;



	function make return zero;
	
	function symbolName(this: zero) return String;
	
	function getArity(this: zero) return Integer;
	
	function duplicate(this: zero) return SharedObject;
	
-- String Builder	
	
	function compareToLPO(this: zero,o: Object'Class) return Integer;
	
	function compareTo(this: zero, o: Object'Class) return Integer;
	
	function hashCode(this: zero) return Integer;
	
	function equivalent(this: zero, o: shared.SharedObject'Class) return boolean; 
	
	function iszero(this: zero)  return boolean;

	function getChildCount(this: zero) return Integer;

	function getChildCount(this: access Visitable'Class) return Integer;

	function getChildAt(this: access Visitable'Class, index: Integer) return VisitablePtr ;

	function setChildAt(this: access Visitable'Class, index: Integer, v: access Visitable'Class) return VisitablePtr;

	function setChildren(this: access Visitable'Class, childs: ObjectPtrArrayPtr ) return ObjectPtrArrayPtr ;

	function getChildren(this: access Visitable'Class) return ObjectPtrArrayPtr ; 

	-- function hashFunction(this:zero) return int is protected; 

end peano.types.nat.zero;

