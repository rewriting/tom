with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package OneStrategy is
	ARG : constant Integer := 0;

	type One is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function toString(o: One) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function visitLight(str:access One; any: ObjectPtr; intro: access Introspector'Class) return ObjectPtr;
	function visit(str: access One; intro: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeOne(o : in out One; v: StrategyPtr);
	function newOne(v: StrategyPtr) return StrategyPtr;
	----------------------------------------------------------------------------

end OneStrategy;
