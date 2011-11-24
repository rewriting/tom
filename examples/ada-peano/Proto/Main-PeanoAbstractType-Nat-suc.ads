with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package Main.PeanoAbstractType.Nat.suc is

	type suc is new Nat with
	record
	pred: NatPtr;
	end record;

	function make(pred : NatPtr) return suc;

	procedure init(this: in out suc; pred: NatPtr; hashCode: Integer); 

	procedure initHashCode(this: in out suc; pred: NatPtr);
	
	function symbolName(this: suc) return String;

	function toString(this: suc) return String;

	function getArity(this: suc) return Integer;

	function duplicate(this: suc) return suc; 

	function equivalent(this: suc; o: SharedObject'Class) return boolean;

	function issuc(this: suc) return boolean;

	function getpred(this: suc) return Nat'Class;

	function setpred(this: suc; set_arg: NatPtr) return Nat'Class;

	function hashFunction(this: suc) return Integer ;

	sucGomProto: access suc := new suc;

end Main.PeanoAbstractType.Nat.suc;

	 

