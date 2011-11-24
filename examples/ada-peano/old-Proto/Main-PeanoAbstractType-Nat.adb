with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;
with peano.types; use peano.types;

package Main.PeanoAbstractType.Nat is

function iszero(this: Nat'Class) return boolean is 
begin
	return false;
end;

function issuc(this: Nat'Class) return boolean is 
begin
	return false;
end;

function isplus(this: Nat'Class) return boolean is 
begin
	return false;
end;

function getpred(this: Nat'Class) return Nat'Class is
begin
	raise WrongType;
end;

function setpred(this: Nat'Class, _arg: Nat'Class) is
begin
	raise WrongType;
end;


function getx1(this: Nat'Class) return Nat'Class is
begin
	raise WrongType;
end;

function setx1(this: Nat'Class, _arg: Nat'Class) is
begin
	raise WrongType;
end;


function getx2(this: Nat'Class) return Nat'Class is
begin
	raise WrongType;
end;

function setx2(this: Nat'Class, _arg: Nat'Class) is
begin
	raise WrongType;
end;

function length(this: Nat'Class) return Integer is
begin 
	raise NotAList;
end;

function revers(this: Nat'Class) return Nat'Class is
begin 
raise NotAList;
end;


end  Main.PeanoAbstractType.Nat ; 


