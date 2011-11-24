with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package Main.PeanoAbstractType.Nat.suc is

	type suc is new Nat with
	record
	pred: Nat'Class;
	end record;

	overriding
	function duplicate(this: suc) return suc;

	overriding	
	function getChildCount(this: access suc) return Integer;

	overriding
	function getChildAt(this: access suc; index: Integer) return VisitablePtr ;

	overriding
	procedure setChildren(this: access suc; childs: ObjectPtrArrayPtr ) ; 

	overriding
	function getChildren(this: access suc) return ObjectPtrArrayPtr ; 

	overriding
	procedure setChildAt(this: access suc; index: Integer; child: VisitablePtr);

	function make(pred : Nat) return suc;

	procedure init(this: suc; pred: Nat; hashCode: Integer);

	procedure initHashCode(this: suc);
	
	function symbolName(this: suc) return String;

	function getArity(this: suc) return Integer;

	function compareToLPO(this: suc; o: Object'Class) return Integer;

	function compareTo(this: suc; o: Object'Class) return Integer;

	function equivalent(this: suc; o: SharedObject'Class) return boolean;

	function issuc(this: suc) return boolean;

	function getpred(this: suc) return Nat'Class;

	function setpred(this: suc; set_arg: Nat'Class) return Nat'Class;

	function hashFunction(this: suc) return Integer ;

	gomProto: access suc := new suc;
	

end Main.PeanoAbstractType.Nat.suc;

	 
