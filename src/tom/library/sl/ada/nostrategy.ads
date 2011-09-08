with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package NoStrategy is

	ARG : constant Integer := 0;

	type No is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function toString(n: No) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function visitLight(str:access No; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function visit(str: access No; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeNo(n : in out No; v : StrategyPtr);
	function newNo(v : StrategyPtr) return StrategyPtr;
	----------------------------------------------------------------------------

end NoStrategy;
