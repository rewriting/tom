with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package AllsStrategy is
	ARG : constant Integer := 0;

	type Alls is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function toString(o: Alls) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function visitLight(str:access Alls; any: ObjectPtr; intro: access Introspector'Class) return ObjectPtr;
	function visit(str: access Alls; intro: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeAlls(i : in out Alls; v: StrategyPtr);
	function newAlls(v: StrategyPtr) return StrategyPtr;
	----------------------------------------------------------------------------

end AllsStrategy;
