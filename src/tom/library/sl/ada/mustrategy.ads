with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;

with Ada.Containers.Ordered_Sets;
with Ada.Containers.Doubly_Linked_Lists;

package MuStrategy is

	VAR : constant Integer := 0;
	V : constant Integer := 1;

	type Mu is new AbstractStrategyCombinator and Object with
	record
		expanded : Boolean := false;
	end record;
	
	type MuPtr is access all Mu;
	
	package StrategyStr_Sets is new Ada.Containers.Ordered_Sets(StrategyPtr, "<");
	package StrategyStr_LL is new Ada.Containers.Doubly_Linked_Lists(MuPtr);
	
	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function  toString(c: Mu) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visitLight(str:access Mu; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access Mu; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	function newMu(var, v: StrategyPtr) return StrategyPtr;
	procedure expand(s: StrategyPtr);

end MuStrategy;
