with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package FailStrategy is

	type Fail is new AbstractStrategyCombinator and Object with
	record
		message: access String := null;
	end record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function  toString(f: Fail) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visitLight(str:access Fail; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access Fail; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	function newFail return StrategyPtr;
	function newFail(m: String) return StrategyPtr;
	function newFail(m: access String) return StrategyPtr;

end FailStrategy;
