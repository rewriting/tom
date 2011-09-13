with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package MuVarStrategy is

	type MuVar is new AbstractStrategyCombinator and Object with
	record
		instance : StrategyPtr := null;
		name : access String := null;
	end record;
	
	type MuVarPtr is access all MuVar;
	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function  toString(c: MuVar) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visitLight(str:access MuVar; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access MuVar; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	function newMuVar(s : access String) return StrategyPtr;
	function newMuVar(s : String) return StrategyPtr;
	
	function equals(m : access MuVar; o : ObjectPtr) return Boolean;
	function hashCode(m : access MuVar) return Integer;
	function getInstance(m: access MuVar) return StrategyPtr;
	procedure setInstance(m: access MuVar; s: StrategyPtr);
	procedure setName(m: access MuVar; n: access String);
	function isExpanded(m: access Muvar) return Boolean;
	function getName(m: access Muvar) return access String;

end MuVarStrategy;
