with VisitFailurePackage, VisitablePackage, EnvironmentPackage;
use  VisitFailurePackage, VisitablePackage, EnvironmentPackage;
package body SequenceStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(o: Sequence) return String is
	begin
		return "Sequence()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access Sequence; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
		op : ObjectPtr := visitLight( StrategyPtr(str.arguments(FIRST)), any, i);
	begin
		return visitLight(StrategyPtr(str.arguments(SECOND)), op, i);
	end;
	
	overriding
	function visit(str: access Sequence; i: access Introspector'Class) return Integer is
		status : Integer :=  visit(StrategyPtr(str.arguments(FIRST)), i);
	begin
		if status = EnvironmentPackage.SUCCESS then
			return visit(StrategyPtr(str.arguments(SECOND)), i);
		else
			return status;
		end if;
	end;
	
	----------------------------------------------------------------------------

	procedure makeSequence(s : in out Sequence; str1, str2 : StrategyPtr) is
	begin
		initSubterm(s, str1, str2);
	end;

	function make(str1, str2: StrategyPtr) return StrategyPtr is
		ns : StrategyPtr := null;
	begin
		if str2 = null then
			return str1;
		else
			ns := new Sequence;
			makeSequence(Sequence(ns.all), str1, str2);
			return ns;
		end if;
	end;
	
	function newSequence(str1, str2: StrategyPtr) return StrategyPtr is
	begin
		return SequenceStrategy.make(str1,str2);
	end;

	----------------------------------------------------------------------------
end SequenceStrategy;
