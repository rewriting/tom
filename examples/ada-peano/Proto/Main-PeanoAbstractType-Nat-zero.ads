with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package Main.PeanoAbstractType.Nat.zero is 

	type zero is new Nat with
		record
		hashCode: integer;
		gomProto: zero := build(factory,new zero);
		end record;

	function make(this: zero) return zero;
	
	function toString(this: zero) return String;
	
	function getArity(this: zero) return Integer;
	
	function duplicate(this: zero) return SharedObject'Class;
	
	function compareToLPO(this: zero;o: Object'Class) return Integer;
	
	function compareTo(this: zero; o: Object'Class) return Integer;
	
	function hashCode(this: zero) return Integer;
	
	function equivalent(this: zero; o: shared.SharedObject'Class) return boolean; 
	
	function iszero(this: zero)  return boolean;

	function getChildCount(this: access Visitable'Class) return Integer;

	function getChildAt(this: access Visitable'Class; index: Integer) return VisitablePtr ;

	function setChildAt(this: access Visitable'Class; index: Integer; v: access Visitable'Class) return VisitablePtr;

	function setChildren(this: access Visitable'Class; childs: ObjectPtrArrayPtr ) return ObjectPtrArrayPtr ;

	function getChildren(this: access Visitable'Class) return ObjectPtrArrayPtr ; 

	function hashFunction(this:zero) return Integer; 

end Main.PeanoAbstractType.Nat.zero;

