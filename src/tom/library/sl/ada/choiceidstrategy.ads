with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package ChoiceIdStrategy is

	FIRST 	: constant Integer := 0;
	SECOND 	: constant Integer := 1;

	type ChoiceId is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function  toString(c: ChoiceId) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visitLight(str:access ChoiceId; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access ChoiceId; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	function make(first, second : StrategyPtr) return StrategyPtr;
	function newChoiceId(first, second: StrategyPtr) return StrategyPtr;

end ChoiceIdStrategy;
