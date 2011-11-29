with Main.PeanoAbstractType ; use Main.PeanoAbstractType ;
--with VisitablePackage, ObjectPack; use ObjectPack;


package Main.PeanoAbstractType.Nat is 

	type Nat is abstract new PeanoAbstractType with null record ; 
	type NatPtr is access all Nat'Class ;

	function iszero(this: Nat) return boolean;

	function issuc(this: Nat) return boolean;

	function isplus(this: Nat) return boolean;

	function getpred(this: Nat) return Nat'Class ;

	procedure setpred(this: Nat; arg: Nat) ;

	function getx1(this: Nat) return Nat'Class ;

	procedure setx1(this: Nat; arg: Nat)  ;

	function getx2(this: Nat) return Nat'Class ;

	procedure setx2(this: Nat; arg: Nat)  ;

	overriding
	function toString(this: Nat) return String ;


end Main.PeanoAbstractType.Nat;




