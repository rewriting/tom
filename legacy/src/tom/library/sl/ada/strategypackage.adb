with System, System.Storage_Elements;
use  System, System.Storage_Elements;

package body StrategyPackage is
	function "<"(Left,Right: StrategyPtr) return Boolean is
		laddr : Integer_Address := 0;
		raddr : Integer_Address := 0;
	begin
		if left /= null then
			laddr := To_Integer(Left.all'Address);
		end if;
		
		if right /= null then
			raddr := To_Integer(Right.all'Address);
		end if;
		return laddr < raddr;
	end;
end StrategyPackage;

