with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package SequenceStrategy is

	FIRST  : constant Integer := 0;
	SECOND : constant Integer := 1;

	type Sequence is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function  toString(o: Sequence) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visitLight(str:access Sequence; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access Sequence; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeSequence(s : in out Sequence; str1, str2 : StrategyPtr);
	function  make(str1, str2: StrategyPtr) return StrategyPtr;
	function newSequence(str1, str2: StrategyPtr) return StrategyPtr;
	
	----------------------------------------------------------------------------

end SequenceStrategy;
