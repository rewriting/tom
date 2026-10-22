with System, System.Storage_Elements;
use  System, System.Storage_Elements;

package body ObjectPack is

	function hash(obj: Item) return Integer is
		addr : Integer_Address := To_Integer(obj'Address);
	begin
		return Integer(addr mod 2**31);
	end;
	
end;
