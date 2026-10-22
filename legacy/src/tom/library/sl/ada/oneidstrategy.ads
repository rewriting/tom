with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package OneIdStrategy is
	ARG : constant Integer := 0;

	type OneId is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function toString(o: OneId) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function visitLight(str:access OneId; any: ObjectPtr; intro: access Introspector'Class) return ObjectPtr;
	function visit(str: access OneId; intro: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeOneId(o : in out OneId; v: StrategyPtr);
	function newOneId(v: StrategyPtr) return StrategyPtr;
	----------------------------------------------------------------------------

end OneIdStrategy;
