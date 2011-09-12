with VisitFailurePackage, VisitablePackage, EnvironmentPackage;
use  VisitFailurePackage, VisitablePackage, EnvironmentPackage;

with Ada.Text_IO; use Ada.Text_IO;
package body ChoiceIdStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(c: ChoiceId) return String is
	begin
		return "ChoiceId()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access ChoiceId; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
		v : ObjectPtr := visitLight(StrategyPtr(str.arguments(FIRST)), any, i);
	begin
		if v = any then
			return visitLight(StrategyPtr(str.arguments(SECOND)), v, i);
		else
			return v;
		end if;
	end;
	
	overriding
	function visit(str: access ChoiceId; i: access Introspector'Class) return Integer is
		subject : ObjectPtr;
		status : Integer;
	begin
		subject := getSubject(str.env.all);
		status := visit(StrategyPtr(str.arguments(FIRST)), i);
		if status = EnvironmentPackage.SUCCESS and then getSubject(str.env.all) /= subject then
			return status;
		else
			setSubject(str.env.all, subject);
			if status = EnvironmentPackage.SUCCESS then
				return visit(StrategyPtr(str.arguments(SECOND)), i);
			else
				return status;
			end if;
		end if;
	end;
	
	----------------------------------------------------------------------------
	
	procedure makeChoiceId(c : in out ChoiceId; first, second: StrategyPtr) is
	begin
		initSubterm(c, first, second);
	end;
	
	function make(first, second: StrategyPtr) return StrategyPtr is
	begin
		if second = null then
			return first;
		else
			return newChoiceId(first, second);
		end if;
	end;
	

	function newChoiceId(first, second: StrategyPtr) return StrategyPtr is
		ret : StrategyPtr := new ChoiceId;
	begin
		makeChoiceId(ChoiceId(ret.all), first, second);
		return ret;
	end;

	----------------------------------------------------------------------------
	
	

end ChoiceIdStrategy;
