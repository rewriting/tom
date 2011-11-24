with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package Main.PeanoAbstractType.Nat.zero is 

	type zero is new Nat with null record;

	overriding
	function duplicate(this: zero) return zero;

	overriding	
	function getChildCount(this: access zero) return Integer;

	overriding	
	function getChildAt(this: access zero; index: Integer) return VisitablePtr ;

	overriding
	procedure setChildren(this: access zero; childs: ObjectPtrArrayPtr ) ; 

	overriding
	function getChildren(this: access zero) return ObjectPtrArrayPtr ; 

	overriding
	procedure setChildAt(this: access zero; index: Integer; child: VisitablePtr);


	function make(this: zero) return zero;
	
	function toString(this: zero) return String;
	
	function getArity(this: zero) return Integer;
	
	function compareToLPO(this: zero;o: Object'Class) return Integer;
	
	function compareTo(this: zero; o: Object'Class) return Integer;
	
	function equivalent(this: zero; o: SharedObjectP.SharedObject'Class) return boolean; 
	
	function iszero(this: zero)  return boolean;

	function hashFunction(this:zero) return Integer; 

	zeroGomProto: access zero := new zero;
	

end Main.PeanoAbstractType.Nat.zero;
