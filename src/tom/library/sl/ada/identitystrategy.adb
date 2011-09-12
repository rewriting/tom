with VisitablePackage, EnvironmentPackage;
use  VisitablePackage, EnvironmentPackage;
with Ada.Text_IO; use Ada.Text_IO;
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
	function visitLight(str:access Identity; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is
	begin
		return any;
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
	
	function newIdentity return StrategyPtr is
		id : StrategyPtr := new Identity;
	begin
		makeIdentity(Identity(id.all));
		return id;
	end;

	----------------------------------------------------------------------------
end IdentityStrategy;
