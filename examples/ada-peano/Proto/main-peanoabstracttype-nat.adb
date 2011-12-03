with Main.peanoabstracttype.Nat.zero ;
use Main.peanoabstracttype.Nat.zero ;


package body Main.PeanoAbstractType.Nat is

function toString(this: Nat) return String is
begin
	return "unextended Nat" ; 
end;


	pseudo_Abstract_Called : Exception ;

function iszero(this: Nat) return boolean is 
begin
	return false;
end;

function issuc(this: Nat) return boolean is 
begin
	return false;
end;

function isplus(this: Nat) return boolean is 
begin
	return false;
end;

function getpred(this: Nat) return Nat'Class is
begin
	raise pseudo_Abstract_Called with "This Nat has no pred";
	return zeroGomProto.all; 
end;

procedure setpred(this: Nat; arg: Nat) is
begin
	raise pseudo_Abstract_Called with "This Nat has no pred";

end;

function getx1(this: Nat) return Nat'Class is
begin
	raise pseudo_Abstract_Called with "This Nat has no x1";
	return zeroGomProto.all;
end;

procedure setx1(this: Nat; arg: Nat)  is
begin
	raise pseudo_Abstract_Called with "This Nat has no x1";
end;


function getx2(this: Nat) return Nat'Class is
begin
	raise pseudo_Abstract_Called with "This Nat has no x2";
	return zeroGomProto.all;
end;

procedure setx2(this: Nat; arg: Nat)  is
begin
	raise pseudo_Abstract_Called with "This Nat has no x2";
end;


end  Main.PeanoAbstractType.Nat ; 

