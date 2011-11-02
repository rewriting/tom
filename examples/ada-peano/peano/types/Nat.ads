with peano.PeanoAbstractType ; use peano.PeanoAbstractType ;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;

package peano.types.nat is new peano.PeanoAbstractType with 

	type Nat is tagged new peano.types.Nat with Visitable;
	record
	hashCode: integer;
	_pred: Nat
	end record


	function iszero(this: Nat) return boolean;

	function issuc(this: Nat) return boolean;

	function isplus(this: Nat) return boolean;

	function getpred(this: Nat) return Nat ;

	function setpred(this: Nat, _arg: Nat) return Nat ;

	function getx1(this: Nat) return Nat ;

	function setx1(this: Nat, _arg: Nat) return Nat ;

	function getx2(this: Nat) return Nat ;

	function setx2(this: Nat, _arg: Nat) return Nat ;

	function length(this: Nat) return Integer ;

	function reverse(this: Nat) return Nat ;

end peano.types.nat;


