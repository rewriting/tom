with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package AllSeqStrategy is
	ARG : constant Integer := 0;

	type AllSeq is new AbstractStrategyCombinator and Object with null record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function toString(o: AllSeq) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function visitLight(str:access AllSeq; any: ObjectPtr; intro: access Introspector'Class) return ObjectPtr;
	function visit(str: access AllSeq; intro: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeAllSeq(o : in out AllSeq; v: StrategyPtr);
	function newAllSeq(v: StrategyPtr) return StrategyPtr;
	----------------------------------------------------------------------------

end AllSeqStrategy;
