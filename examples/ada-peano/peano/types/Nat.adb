with tom.library.sl.VisitablePackage, tom.library.sl.ObjectPack;
with peano.types; use peano.types;

package peano.types.nat is

function iszero is 
begin
	return false;
end;

function issuc is 
begin
	return false;
end;

function isplus is 
begin
	return false;
end;

function getpred is
begin
	raise WrongType;
end;

function setpred is
begin
	raise WrongType;
end;


function getx1 is
begin
	raise WrongType;
end;

function setx1 is
begin
	raise WrongType;
end;


function getx2 is
begin
	raise WrongType;
end;

function setx2 is
begin
	raise WrongType;
end;

function length is
begin 
	raise NotAList;
end;

function reverse is
begin 
raise NotAList;
end;


end peano.types.nat;


