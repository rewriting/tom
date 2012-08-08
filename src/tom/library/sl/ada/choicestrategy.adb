with VisitFailurePackage, VisitablePackage, EnvironmentPackage;
use  VisitFailurePackage, VisitablePackage, EnvironmentPackage;

with Ada.Text_IO; use Ada.Text_IO;
package body ChoiceStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(c: Choice) return String is
	begin
		return "Choice()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access Choice; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
		optr : ObjectPtr := null;
	begin
		optr :=  visitLight(StrategyPtr(str.arguments(FIRST)), any, i);
		return optr;
		
		exception when VisitFailure =>
				return visitLight(StrategyPtr(str.arguments(SECOND)), any, i);
	end;
	
	overriding
	function visit(str: access Choice; i: access Introspector'Class) return Integer is
		subject : ObjectPtr := null;
		status : Integer := 0;
	begin
		subject := getSubject(str.env.all);
		status := visit(StrategyPtr(str.arguments(FIRST)), i);
		if status = EnvironmentPackage.SUCCESS then
			return status;
		else
			setSubject(str.env.all, subject);
			return visit(StrategyPtr(str.arguments(SECOND)), i);
		end if;
	end;
	
	----------------------------------------------------------------------------
	
	procedure makeChoice(c : in out Choice; first, second: StrategyPtr) is
	begin
		initSubterm(c, first, second);
	end;
	
	function make(first, second: StrategyPtr) return StrategyPtr is
	begin
		if second = null then
			return first;
		else
			return newChoice(first, second);
		end if;
	end;
	

	function newChoice(first, second: StrategyPtr) return StrategyPtr is
		ret : StrategyPtr := new Choice;
	begin
		makeChoice(Choice(ret.all), first, second);
		return ret;
	end;

	----------------------------------------------------------------------------
	
	

end ChoiceStrategy;
