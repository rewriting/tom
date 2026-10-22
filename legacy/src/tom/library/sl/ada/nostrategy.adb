with VisitablePackage, EnvironmentPackage, VisitFailurePackage;
use  VisitablePackage, EnvironmentPackage, VisitFailurePackage;
package body NoStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(n: No) return String is
	begin
		return "Not()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access No; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
		optr : ObjectPtr;
		exceptionRaised : Boolean := true;
	begin
		optr := visitLight(StrategyPtr(str.arguments(ARG)), any, i);
		exceptionRaised := false;
		raise VisitFailure;
		
		exception
			when VisitFailure =>
			
			if exceptionRaised then
				return any;
			else
				raise; -- exception propagated
			end if;
	end;
	
	overriding
	function visit(str: access No; i: access Introspector'Class) return Integer is
		subject : ObjectPtr := getSubject(str.env.all);
		status : Integer := visit( StrategyPtr( str.arguments(ARG)), i);
	begin
		setSubject(str.env.all, subject);
		if status /=  EnvironmentPackage.SUCCESS then
			return EnvironmentPackage.SUCCESS;
		else
			return EnvironmentPackage.FAILURE;
		end if;
	end;
	
	----------------------------------------------------------------------------

	procedure makeNo(n : in out No; v : StrategyPtr) is
	begin
		initSubterm(n, v);
	end;
	
	function newNo(v : StrategyPtr) return StrategyPtr is
		ret: StrategyPtr := new No;
	begin
		makeNo(No(ret.all), v);
		return ret;
	end;

	----------------------------------------------------------------------------
end NoStrategy;
