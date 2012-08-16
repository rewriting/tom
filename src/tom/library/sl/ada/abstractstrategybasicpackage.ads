with ObjectPack, AbstractStrategyPackage, StrategyPackage, IntrospectorPackage, VisitablePackage;
use  ObjectPack, AbstractStrategyPackage, StrategyPackage, IntrospectorPackage, VisitablePackage;
package AbstractStrategyBasicPackage is

	type AbstractStrategyBasic is abstract new AbstractStrategy and Object with
	record
		any : StrategyPtr := null;
	end record;
	
	type AbstractStrategyBasicPtr is access all AbstractStrategyBasic'Class;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function visit(str: access AbstractStrategyBasic; i: access Introspector'Class) return Integer;

	----------------------------------------------------------------------------
	-- Visitable implementation
	----------------------------------------------------------------------------
	
	function setChildren(v: access AbstractStrategyBasic ; children : ObjectPtrArrayPtr) return VisitablePtr;
	function getChildren(v: access AbstractStrategyBasic) return ObjectPtrArrayPtr;
	function getChildAt(v: access AbstractStrategyBasic; i : Integer) return VisitablePtr;
	function setChildAt(v: access AbstractStrategyBasic; i: in Integer; child: in VisitablePtr) return VisitablePtr;
	function getChildCount(v: access AbstractStrategyBasic) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeAbstractStrategyBasic(asb : in out AbstractStrategyBasic'Class; s: StrategyPtr);
	
	----------------------------------------------------------------------------
	
end AbstractStrategyBasicPackage;

