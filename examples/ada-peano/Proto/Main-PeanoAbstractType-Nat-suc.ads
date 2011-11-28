with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package Main.PeanoAbstractType.Nat.suc is

	type suc is new Nat with
	record
	pred: SharedObjectPtr;
	end record;

	function make(pred : SharedObjectPtr) return suc;

	procedure init(this: in out suc; pred: SharedObjectPtr; hashCode: Integer); 

	procedure initHashCode(this: in out suc; pred: SharedObjectPtr);
	
	function symbolName(this: suc) return String;

	function toString(this: suc) return String;

	function getArity(this: suc) return Integer;

	overriding
	function duplicate(this: suc) return SharedObject'Class;

	function equivalent(this: suc; o: SharedObject'Class) return boolean;

	function issuc(this: suc) return boolean;

	function hashFunction(this: suc) return Integer ;

	sucGomProto: access suc := new suc;

end Main.PeanoAbstractType.Nat.suc;

	 

