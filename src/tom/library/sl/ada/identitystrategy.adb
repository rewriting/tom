with VisitablePackage, EnvironmentPackage;
use  VisitablePackage, EnvironmentPackage;
package body IdentityStrategy is

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	overriding
	function toString(o: Identity) return String is
	begin
		return "Identity()";
	end;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	overriding
	function visitLight(str:access Identity; any: ObjectPtr; i: access Introspector'Class) return Object'Class is
	begin
		return any.all;
	end;
	
	overriding
	function visit(str: access Identity; i: access Introspector'Class) return Integer is
	begin
		return EnvironmentPackage.SUCCESS;
	end;
	
	----------------------------------------------------------------------------

	procedure makeIdentity(i : in out Identity) is
	begin
		initSubterm(i);
	end;

	----------------------------------------------------------------------------
end IdentityStrategy;
