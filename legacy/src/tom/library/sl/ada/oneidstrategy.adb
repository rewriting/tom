with VisitablePackage, EnvironmentPackage, VisitFailurePackage;
use  VisitablePackage, EnvironmentPackage, VisitFailurePackage;
package body OneIdStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(o: OneId) return String is
	begin
		return "OneId()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access OneId; any: ObjectPtr; intro: access Introspector'Class) return ObjectPtr is
		childCount : Integer := getChildCount(intro, any);
		newSubterm : ObjectPtr := null;
	begin
		for i in 0..childCount-1 loop
			newSubterm := visitLight(StrategyPtr(str.arguments(ARG)),  getChildAt(intro, any, i), intro);
			if newSubterm /= getChildAt(intro, any, i) then
				return setChildAt(intro, any, i, newSubterm);
			end if;
		end loop;
		
		return any;
	end;
	
	overriding
	function visit(str: access OneId; intro: access Introspector'Class) return Integer is
		childCount : Integer := getChildCount(intro,  getSubject(str.env.all));
		oldSubject : ObjectPtr := null;
		newSubject : ObjectPtr := null;
		status : Integer;
	begin
		for i in 0..childCount-1 loop
			down(str.env.all, i+1);
			oldSubject := getSubject(str.env.all);
			status := visit(StrategyPtr(str.arguments(ARG)), intro);
			newSubject := getSubject(str.env.all);
			if status = EnvironmentPackage.SUCCESS and then oldSubject /= newSubject then
				up(str.env.all);
				return status;
			else
				upLocal(str.env.all);
			end if;
		end loop;
		return EnvironmentPackage.SUCCESS;
	end;
	
	----------------------------------------------------------------------------

	procedure makeOneId(o : in out OneId; v: StrategyPtr) is
	begin
		initSubterm(o,v);
	end;
	
	function newOneId(v: StrategyPtr) return StrategyPtr is
		id : StrategyPtr := new OneId;
	begin
		makeOneId(OneId(id.all), v);
		return id;
	end;

	----------------------------------------------------------------------------
end OneIdStrategy;
