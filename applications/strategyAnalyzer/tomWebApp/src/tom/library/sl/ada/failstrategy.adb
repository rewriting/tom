with VisitFailurePackage, VisitablePackage, EnvironmentPackage;
use  VisitFailurePackage, VisitablePackage, EnvironmentPackage;
package body FailStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(f: Fail) return String is
	begin
		if f.message = null then
			return "Fail("""")";
		else
			return "Fail(""" & f.message.all & """)";
		end if;
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access Fail; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
	begin
		if str.message = null then
			raise VisitFailure;
		else
			RaiseVisitFailure(str.message.all);
			return null; --never executed
		end if;
	end;
	
	overriding
	function visit(str: access Fail; i: access Introspector'Class) return Integer is
	begin
		return EnvironmentPackage.FAILURE;
	end;
	
	----------------------------------------------------------------------------
	
	procedure makeFail(f : in out Fail; m: access String) is
	begin
		initSubterm(f);
		f.message := m;
	end;
	
	function newFail return StrategyPtr is
		ret : StrategyPtr := new Fail;
	begin
		makeFail(Fail(ret.all), null);
		return ret;
	end;
	
	function newFail(m: String) return StrategyPtr is
		ret : StrategyPtr := new Fail;
	begin
		makeFail(Fail(ret.all), new String'(m));
		return ret;
	end;
	
	function newFail(m: access String) return StrategyPtr is
		ret : StrategyPtr := new Fail;
	begin
		makeFail(Fail(ret.all), m);
		return ret;
	end;

	----------------------------------------------------------------------------
	
	

end FailStrategy;
