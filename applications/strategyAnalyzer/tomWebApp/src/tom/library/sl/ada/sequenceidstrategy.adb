with VisitFailurePackage, VisitablePackage, EnvironmentPackage;
use  VisitFailurePackage, VisitablePackage, EnvironmentPackage;
package body SequenceIdStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(o: SequenceId) return String is
	begin
		return "SequenceId()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access SequenceId; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
		op : ObjectPtr := visitLight( StrategyPtr(str.arguments(FIRST)), any, i);
	begin
		if op /= any then
			return visitLight(StrategyPtr(str.arguments(SECOND)), op, i);
		else
			return op;
		end if;
	end;
	
	overriding
	function visit(str: access SequenceId; i: access Introspector'Class) return Integer is
		subject : ObjectPtr := getSubject(str.env.all);
		status : Integer := visit(StrategyPtr(str.arguments(FIRST)), i);
	begin
		if status = EnvironmentPackage.SUCCESS and then subject /= getSubject(str.env.all) then
			return visit(StrategyPtr(str.arguments(SECOND)), i);
		else
			return status;
		end if;
	end;
	
	----------------------------------------------------------------------------

	procedure makeSequenceId(s : in out SequenceId; str1, str2 : StrategyPtr) is
	begin
		initSubterm(s, str1, str2);
	end;

	function make(str1, str2: StrategyPtr) return StrategyPtr is
		ns : StrategyPtr := null;
	begin
		if str2 = null then
			return str1;
		else
			ns := new SequenceId;
			makeSequenceId(SequenceId(ns.all), str1, str2);
			return ns;
		end if;
	end;
	
	function newSequenceId(str1, str2: StrategyPtr) return StrategyPtr is
	begin
		return SequenceIdStrategy.make(str1,str2);
	end;

	----------------------------------------------------------------------------
end SequenceIdStrategy;
