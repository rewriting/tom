with peano.types.nat; use peano.types.nat;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;
use tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;

package peano.types.nat.suc is new peano.types.Nat
	type suc is tagged new Nat with Visitable;

	record
	gomProto: suc := new suc;
	hashCode: integer;
	_pred: Nat
	end record
	
	gomProto: suc := new suc();

	function make(_pred : Nat) return suc;

	function setpred(this: suc) return Nat;

	procedure init(this: suc, _pred: Nat, hashCode: Integer);

	procedure initHashCode(this: suc);
	
	function symbolName(this: suc) return String;

	function getArity(this: suc) return Integer;

	function duplicate(this: suc) return SharedObject;

	function compareToLPO(this: suc, o: Object'Class) return Integer;

	function compareTo(this: suc, o: Object'Class) return Integer;

	function hashcode(this: suc) return Integer;

	function equivalent(this: suc, o: SharedObject'Class) return boolean;

	function issuc(this: suc) return boolean;

	function getpred(this: suc) return Nat;

	function setpred(this: suc, set_arg: Nat) return Nat;

	function getChildCount(this: access Visitable'Class) return Integer;

	function getChildAt(this: access Visitable'Class, index: Integer) return VisitablePtr ;

	function setChildAt(this: access Visitable'Class, index: Integer, v:access Visitable'Class) return VisitablePtr;

	function setChildren(this: access Visitable'Class, childs: ObjectPtrArrayPtr ) return ObjectPtrArrayPtr ;

	function getChildren(this: access Visitable'Class) return ObjectPtrArrayPtr ; 
end peano.types.nat.suc ;

	 
