with VisitablePackage, EnvironmentPackage, VisitFailurePackage;
use  VisitablePackage, EnvironmentPackage, VisitFailurePackage;
package body OneStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(o: One) return String is
	begin
		return "One()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function tryVisitLight(str : StrategyPtr; o: ObjectPtr; intro :access Introspector'Class; newChild: access ObjectPtr) return Boolean is
	begin
		newChild.all := visitLight(str, o, intro);
		return true;
		exception when VisitFailure => return false;
	end;
	
	overriding
	function visitLight(str:access One; any: ObjectPtr; intro: access Introspector'Class) return ObjectPtr is
		childCount : Integer := getChildCount(intro, any);
		newChild : aliased ObjectPtr := null;
		noException : Boolean;
	begin
		for i in 0..childCount-1 loop
			noException := tryVisitLight(StrategyPtr(str.arguments(ARG)),  getChildAt(intro, any, i), intro, newChild'Access);
			if noException then
				return setChildAt(intro, any, i, newChild);
			end if;
		end loop;
		
		raise VisitFailure;
	end;
	
	overriding
	function visit(str: access One; intro: access Introspector'Class) return Integer is
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
				upLocal(str.env.all);
			end if;
		end loop;
		return EnvironmentPackage.FAILURE;
	end;
	
	----------------------------------------------------------------------------

	procedure makeOne(o : in out One; v: StrategyPtr) is
	begin
		initSubterm(o,v);
	end;
	
	function newOne(v: StrategyPtr) return StrategyPtr is
		id : StrategyPtr := new One;
	begin
		makeOne(One(id.all), v);
		return id;
	end;

	----------------------------------------------------------------------------
end OneStrategy;
