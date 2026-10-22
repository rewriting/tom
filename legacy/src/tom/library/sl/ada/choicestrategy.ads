with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package ChoiceStrategy is

	FIRST 	: constant Integer := 0;
	SECOND 	: constant Integer := 1;

	type Choice is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function  toString(c: Choice) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visitLight(str:access Choice; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access Choice; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	function make(first, second : StrategyPtr) return StrategyPtr;
	function newChoice(first, second: StrategyPtr) return StrategyPtr;

end ChoiceStrategy;
