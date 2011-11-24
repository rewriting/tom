package body Main.PeanoAbstractType.Nat is

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
	raise pseudo_Abstract_Called;
	return this;
end;

procedure setpred(this: Nat; arg: Nat) is
begin
	raise pseudo_Abstract_Called;

end;

function duplicate(this: Nat) return Nat is
begin
	raise pseudo_Abstract_Called;
	return this;
end;

function getx1(this: Nat) return Nat'Class is
begin
	raise pseudo_Abstract_Called;
	return this;
end;

procedure setx1(this: Nat; arg: Nat)  is
begin
	raise pseudo_Abstract_Called;
end;


function getx2(this: Nat) return Nat'Class is
begin
	raise pseudo_Abstract_Called;
	return this;
end;

procedure setx2(this: Nat; arg: Nat)  is
begin
	raise pseudo_Abstract_Called;
end;

end  Main.PeanoAbstractType.Nat ; 



