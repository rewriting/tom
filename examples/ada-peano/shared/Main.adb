with Ada.Text_IO; use Ada.Text_IO;
with SharedObjectP; use SharedObjectP;

package body Main is
	
function duplicate(this: testie) return SharedObject'Class is
begin
	return this;
end duplicate;

function equivalent(this: testie; o: testie) return boolean is
begin 
	if this.serie = o.serie then return true; end if;
	
	return false;
end equivalent;

function hashCode(this: testie) return Integer is
begin
	return this.serie;
end hashCode;

function toString(this: testie) return String is
begin

return "testie n°"&Natural'Image(this.serie);

end toString;



end Main;

