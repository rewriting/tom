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
	
	procedure setChildren(v: in out AbstractStrategyCombinator ; children : ObjectPtrArray);
	function  getChildren(v: AbstractStrategyCombinator) return ObjectPtrArray;
	function  getChildAt(v: AbstractStrategyCombinator; i : Integer) return Visitable'Class;
	procedure setChildAt(v: in out AbstractStrategyCombinator; i: in Integer; child: in Visitable'Class);
	function  getChildCount(v: AbstractStrategyCombinator) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure initSubterm(sc : in out AbstractStrategyCombinator);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str : Strategy'Class);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str1,str2 : Strategy'Class);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str1,str2,str3 : Strategy'Class);
	procedure initSubterm(sc : in out AbstractStrategyCombinator; str : ObjectPtrArray);
	function  getVisitors(sc: AbstractStrategyCombinator) return ObjectPtrArray;
	
	----------------------------------------------------------------------------
	
end AbstractStrategyCombinatorPackage;

