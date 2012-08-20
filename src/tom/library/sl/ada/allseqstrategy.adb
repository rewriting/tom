with VisitablePackage, EnvironmentPackage, VisitFailurePackage;
use  VisitablePackage, EnvironmentPackage, VisitFailurePackage;
package body AllSeqStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(o: AllSeq) return String is
	begin
		return "AllSeq()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access AllSeq; any: ObjectPtr; intro: access Introspector'Class) return ObjectPtr is
		childCount : Integer := getChildCount(intro, any);
		newChild : ObjectPtr := null;
		result : ObjectPtr := any;
	begin
		for i in 0..childCount-1 loop
			newChild := visitLight(StrategyPtr(str.arguments(ARG)),  getChildAt(intro, result, i), intro);
			result := setChildAt(intro, result, i, newChild);
		end loop;
		
		return result;
	end;
	
	overriding
	function visit(str: access AllSeq; intro: access Introspector'Class) return Integer is
		childCount : Integer := getChildCount(intro,  getSubject(str.env.all));
		status : Integer;
	begin
		for i in 0..childCount-1 loop
			down(str.env.all, i+1);
			status := visit(StrategyPtr(str.arguments(ARG)), intro);
			if status = EnvironmentPackage.SUCCESS then
				up(str.env.all);
				return status;
			else
				up(str.env.all);
			end if;
		end loop;
		return EnvironmentPackage.SUCCESS;
	end;
	
	----------------------------------------------------------------------------

	procedure makeAllSeq(o : in out AllSeq; v: StrategyPtr) is
	begin
		initSubterm(o,v);
	end;
	
	function newAllSeq(v: StrategyPtr) return StrategyPtr is
		id : StrategyPtr := new AllSeq;
	begin
		makeAllSeq(AllSeq(id.all), v);
		return id;
	end;

	----------------------------------------------------------------------------
end AllSeqStrategy;
