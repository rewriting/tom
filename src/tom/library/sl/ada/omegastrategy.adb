with VisitFailurePackage, VisitablePackage, EnvironmentPackage;
use  VisitFailurePackage, VisitablePackage, EnvironmentPackage;
package body OmegaStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(o: Omega) return String is
	begin
		return "Omega()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access Omega; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
		sptr : StrategyPtr := StrategyPtr(str.arguments(ARG));
	begin
		if str.indexPosition = 0 then
			return StrategyPackage.visitLight(sptr, any, i);
		elsif str.indexPosition > 0 and then str.indexPosition <= IntrospectorPackage.getChildCount(i.all, any.all) then
			declare
			childNumber : Integer := str.indexPosition - 1;
			optr : ObjectPtr := new Object'Class'( IntrospectorPackage.getChildAt(i.all, any.all, childNumber) );
			newChild : ObjectPtr := StrategyPackage.visitLight( StrategyPtr(str.arguments(ARG)) , optr , i);
			begin
				IntrospectorPackage.setChildAt(i.all, Visitable'Class(any.all), childNumber, newChild.all);
				return ObjectPtr(i);
			end;
		else
			raise VisitFailure;
		end if;
	end;
	
	overriding
	function visit(str: access Omega; i: access Introspector'Class) return Integer is
		sptr : StrategyPtr := StrategyPtr(str.arguments(ARG));
		status : Integer;
	begin
		if str.indexPosition = 0 then
			return StrategyPackage.visit(sptr, i);
		elsif str.indexPosition > 0 and then str.indexPosition <= IntrospectorPackage.getChildCount(i.all, getSubject(getEnvironment(str.all).all).all) then
			down(str.env.all, str.indexPosition);
			status := visit( StrategyPtr(str.arguments(ARG)), i);
			up ( str.env.all );
			return status;
		else
			return EnvironmentPackage.FAILURE;
		end if;
	end;
	
	----------------------------------------------------------------------------
	
	procedure makeOmega(om : in out Omega; ip: Integer; v: Strategy'Class) is
	begin
		initSubterm(om, v);
		om.indexPosition := ip;
	end;
	
	function newOmega(ip: Integer; v: Strategy'Class) return StrategyPtr is
		ret : StrategyPtr := new Omega;
	begin
		makeOmega(Omega(ret.all), ip, v);
		return ret;
	end;
	
	function getPos(om : Omega) return Integer is
	begin
		return om.indexPosition;
	end;
	
	----------------------------------------------------------------------------
	
	

end OmegaStrategy;
