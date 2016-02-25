with ObjectPack, AbstractStrategyPackage, StrategyPackage, VisitablePackage;
use  ObjectPack, AbstractStrategyPackage, StrategyPackage, VisitablePackage;
package AbstractStrategyCombinatorPackage is

	type AbstractStrategyCombinator is abstract new AbstractStrategy and Object with
	record
		arguments : ObjectPtrArrayPtr := null; --contains only StrategyPtr
	end record;
	
	----------------------------------------------------------------------------
	-- Visitable implementation
	----------------------------------------------------------------------------
	
	function  setChildren(v: access AbstractStrategyCombinator ; children : ObjectPtrArrayPtr) return VisitablePtr;
	function  getChildren(v: access AbstractStrategyCombinator) return ObjectPtrArrayPtr;
	function  getChildAt(v: access AbstractStrategyCombinator; i : Integer) return VisitablePtr;
	function  setChildAt(v: access AbstractStrategyCombinator; i: in Integer; child: in VisitablePtr) return VisitablePtr;
	function  getChildCount(v: access AbstractStrategyCombinator) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure initSubterm(sc : in out AbstractStrategyCombinator);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str : StrategyPtr);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str1,str2 : StrategyPtr);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str1,str2,str3 : StrategyPtr);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str : ObjectPtrArray);
	function  getVisitors(sc: AbstractStrategyCombinator) return ObjectPtrArray;
	
	----------------------------------------------------------------------------
	
end AbstractStrategyCombinatorPackage;

