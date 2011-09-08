with VisitFailurePackage, EnvironmentPackage;
use  VisitFailurePackage, EnvironmentPackage;
package body AbstractStrategyBasicPackage is

	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	overriding
	function visit(str: access AbstractStrategyBasic; i: access Introspector'Class) return Integer is
		obj: ObjectPtr := null;
	begin
		obj :=  visitLight(StrategyPtr(str) , getSubject(str.env.all), i);
		EnvironmentPackage.setSubject( str.env.all, obj);
		return EnvironmentPackage.SUCCESS;
		
		exception
			when VisitFailure =>
				return EnvironmentPackage.FAILURE;
	end;
	
	----------------------------------------------------------------------------
	-- Visitable implementation
	----------------------------------------------------------------------------
	overriding
	function getChildCount(v : AbstractStrategyBasic) return Integer is
	begin
		return 1;
	end;
	
	overriding
	procedure setChildren(v: in out AbstractStrategyBasic ; children : ObjectPtrArrayPtr) is
	begin
		v.any := StrategyPtr(children(children'First));
	end;
	
	overriding
	function getChildren(v: AbstractStrategyBasic) return ObjectPtrArrayPtr is
	begin
		return new ObjectPtrArray'( 0 => ObjectPtr(v.any) );
	end;
	
	overriding
	function getChildAt(v: AbstractStrategyBasic; i : Integer) return VisitablePtr is
		IndexOutOfBoundsException : exception;
	begin
		if i = 0 then
			return VisitablePtr(v.any);
		else
			raise IndexOutOfBoundsException;
		end if;
	end;
	
	overriding
	procedure setChildAt(v: in out AbstractStrategyBasic; i: in Integer; child: in VisitablePtr) is
		IndexOutOfBoundsException : exception;
	begin
		if i = 0 then
			v.any := StrategyPtr(child);
		else
			raise IndexOutOfBoundsException;
		end if;
	end;
	
	----------------------------------------------------------------------------
	
	procedure makeAbstractStrategyBasic(asb : in out AbstractStrategyBasic'Class; s: StrategyPtr) is
	begin
		if s = null then
			asb.any := null;
		else
			asb.any := new Strategy'Class'(s.all);
		end if;
	end;
	
	----------------------------------------------------------------------------
		
	
		
end AbstractStrategyBasicPackage;

