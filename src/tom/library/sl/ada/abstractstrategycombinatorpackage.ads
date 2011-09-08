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
	
	procedure setChildren(v: in out AbstractStrategyCombinator ; children : ObjectPtrArrayPtr);
	function  getChildren(v: AbstractStrategyCombinator) return ObjectPtrArrayPtr;
	function  getChildAt(v: AbstractStrategyCombinator; i : Integer) return VisitablePtr;
	procedure setChildAt(v: in out AbstractStrategyCombinator; i: in Integer; child: in VisitablePtr);
	function  getChildCount(v: AbstractStrategyCombinator) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure initSubterm(sc : in out AbstractStrategyCombinator);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str : StrategyPtr);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str1,str2 : StrategyPtr);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str1,str2,str3 : StrategyPtr);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str : ObjectPtrArray);
	function  getVisitors(sc: AbstractStrategyCombinator) return ObjectPtrArray;
	
	----------------------------------------------------------------------------
	
end AbstractStrategyCombinatorPackage;

