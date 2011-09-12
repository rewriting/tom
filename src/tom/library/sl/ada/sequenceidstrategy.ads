with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package SequenceIdStrategy is

	FIRST  : constant Integer := 0;
	SECOND : constant Integer := 1;

	type SequenceId is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function  toString(o: SequenceId) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visitLight(str:access SequenceId; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access SequenceId; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeSequenceId(s : in out SequenceId; str1, str2 : StrategyPtr);
	function  make(str1, str2: StrategyPtr) return StrategyPtr;
	
	----------------------------------------------------------------------------

end SequenceIdStrategy;
