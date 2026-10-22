with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package IdentityStrategy is

	type Identity is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function toString(o: Identity) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function visitLight(str:access Identity; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function visit(str: access Identity; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeIdentity(i : in out Identity);
	function newIdentity return StrategyPtr;
	----------------------------------------------------------------------------

end IdentityStrategy;
