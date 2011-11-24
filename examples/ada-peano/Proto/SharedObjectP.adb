package body SharedObjectP is 

NotOverriden : exception;

function toString(this: SharedObject) return String is
begin
	return "Unnamed"; 
end;

function duplicate(this: SharedObject) return SharedObject is
begin
	raise NotOverriden with "call to duplicate failed";
       return this;	
end;

function equivalent(this: SharedObject; o: SharedObject) return Boolean is
begin
	raise NotOverriden with "call to equivalent failed";
	return false; 
end;

function hashCode(this: SharedObject) return Integer is
begin
	raise NotOverriden with "hashCode retrieval failed";
	return 0;
end;



end SharedObjectP;

