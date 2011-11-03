with peano.PeanoAbstractType ; use peano.PeanoAbstractType ;
with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;

package peano.types.nat is new peano.PeanoAbstractType with 

	type Nat is tagged new peano.PeanoAbstractType.PeanoAbstractType with Visitable;
	record
	hashCode: integer;
	_pred: Nat
	end record


	function iszero(this: Nat'Class) return boolean;

	function issuc(this: Nat'Class) return boolean;

	function isplus(this: Nat'Class) return boolean;

	function getpred(this: Nat'Class) return Nat'Class ;

	function setpred(this: Nat'Class, _arg: Nat'Class) return Nat'Class ;

	function getx1(this: Nat'Class) return Nat'Class ;

	function setx1(this: Nat'Class, _arg: Nat'Class) return Nat'Class ;

	function getx2(this: Nat'Class) return Nat'Class ;

	function setx2(this: Nat'Class, _arg: Nat'Class) return Nat'Class ;

	function length(this: Nat'Class) return Integer ;

	function reverse(this: Nat'Class) return Nat'Class ;

end peano.types.nat;


