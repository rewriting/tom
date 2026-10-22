with VisitableIntrospectorPackage, VisitFailurePackage, Ada.Text_IO;
use  VisitableIntrospectorPackage, VisitFailurePackage, Ada.Text_IO;

with AbstractStrategyCombinatorPackage;
use  AbstractStrategyCombinatorPackage;

with Ada.Text_IO; use Ada.Text_IO;
package body AbstractStrategyPackage is

	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	overriding
	function visit(str: access AbstractStrategy; any: access Environment) return VisitablePtr is
	begin
		return VisitablePtr( visit(str, any, VisitableIntrospectorPackage.getInstance) );
	end;
	
	overriding
	function visit(str: access AbstractStrategy; any: VisitablePtr) return VisitablePtr is
	begin
		return VisitablePtr(visit(str, ObjectPtr(any), VisitableIntrospectorPackage.getInstance));
	end;
	
	overriding
	function visit(str: access AbstractStrategy; envt: access Environment; i: access Introspector'Class) return ObjectPtr is
		status : Integer;
	begin
		init(str.all, envt);
		status := visit(StrategyPtr(str), i);
		
		if status = EnvironmentPackage.SUCCESS then
			return AbstractStrategyPackage.getSubject(str.all);
		else
			raise VisitFailure;
		end if;
	end;
	
	overriding
	function visitLight(str: access AbstractStrategy; any: VisitablePtr) return VisitablePtr is
	begin
		return VisitablePtr( visitLight(StrategyPtr(str), ObjectPtr(any), VisitableIntrospectorPackage.getInstance) );
	end;
	
	overriding
	function visit(str: access AbstractStrategy; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
		status: Integer;
	begin
		
		init(str.all, IntrospectorPtr(i));
		AbstractStrategyPackage.setRoot(str.all, any);
		status := visit(StrategyPtr(str), i);
		if status = EnvironmentPackage.SUCCESS then
			return AbstractStrategyPackage.getRoot(str.all);
		else
			raise VisitFailure;
		end if;
	end;
	
	overriding
	function getEnvironment(str: AbstractStrategy) return access Environment is
		RuntimeException : exception;
	begin
		if str.env /= null then
			return str.env;
		else
			put_line("environment not initialized");
			raise RuntimeException; -- raise an error
		end if;
	end;
	
	overriding
	procedure setEnvironment(str: in out AbstractStrategy; env: access Environment) is
	begin
		str.env := EnvironmentPtr(env);
	end;
	
	----------------------------------------------------------------------------







	----------------------------------------------------------------------------
	
	function getRoot(str: AbstractStrategy) return ObjectPtr is
	begin
		return EnvironmentPackage.getRoot(getEnvironment(str).all);
	end;
	
	procedure setRoot(str: in out AbstractStrategy; any: ObjectPtr) is
	begin
		EnvironmentPackage.setRoot(str.env.all, any);
	end;
	
	function getSubject(str: AbstractStrategy) return ObjectPtr is
	begin
		return EnvironmentPackage.getSubject(getEnvironment(str).all);
	end;
	
	procedure setSubject(str: in out AbstractStrategy; any: ObjectPtr) is
	begin
		EnvironmentPackage.setSubject(str.env.all, any);
	end;
	
	function getPosition(str: AbstractStrategy) return Position is
	begin
		return EnvironmentPackage.getPosition(getEnvironment(str).all);
	end;
	
	function getAncestor(str: AbstractStrategy) return ObjectPtr is
	begin
		return EnvironmentPackage.getAncestor(getEnvironment(str).all);
	end;
	
	procedure init(str: in out Strategy'Class; i: IntrospectorPtr) is
		env : EnvironmentPtr := new Environment;
	begin
		makeEnvironment(env.all, i);
		AbstractStrategyPackage.init(str, env);
	end;
	
	procedure init(str: in out Strategy'Class; env: access Environment) is
		child : VisitablePtr := null;
		tmp : VisitablePtr := new Visitable'Class'(Visitable'Class(str));
	begin
		if str in AbstractStrategy'Class then
			declare
				as : AbstractStrategy'Class := AbstractStrategy'Class(str);
			begin
				if as.env = env then
					return;
				end if;
			end;
		end if;

		setEnvironment(str, env);

		for i in 0..getChildCount(tmp)-1 loop
			child := getChildAt(tmp, i);
			if child.all in Strategy'Class then
				init(AbstractStrategy(child.all), env);
			end if;
		end loop;

	end;
	
	
		
end AbstractStrategyPackage;
