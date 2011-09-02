with ObjectPack, AbstractStrategyPackage, StrategyPackage, IntrospectorPackage, VisitablePackage;
use  ObjectPack, AbstractStrategyPackage, StrategyPackage, IntrospectorPackage, VisitablePackage;
package AbstractStrategyBasicPackage is

	type AbstractStrategyBasic is abstract new AbstractStrategy and Object with
	record
		any : StrategyPtr := null;
	end record;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visit(str: access AbstractStrategyBasic; i: access Introspector'Class) return Integer;

	----------------------------------------------------------------------------
	-- Visitable implementation
	----------------------------------------------------------------------------
	
	procedure setChildren(v: in out AbstractStrategyBasic ; children : ObjectPtrArray);
	function  getChildren(v: AbstractStrategyBasic) return ObjectPtrArray;
	function  getChildAt(v: AbstractStrategyBasic; i : Integer) return Visitable'Class;
	procedure setChildAt(v: in out AbstractStrategyBasic; i: in Integer; child: in Visitable'Class);
	function  getChildCount(v: AbstractStrategyBasic) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeAbstractStrategyBasic(asb : in out AbstractStrategyBasic'Class; s: Strategy'Class);
	
	----------------------------------------------------------------------------
	
end AbstractStrategyBasicPackage;

