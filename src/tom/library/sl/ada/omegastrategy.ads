with ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
use  ObjectPack, AbstractStrategyCombinatorPackage, IntrospectorPackage, StrategyPackage;
package OmegaStrategy is

	ARG : constant Integer := 0;

	type Omega is new AbstractStrategyCombinator and Object with
	record
		indexPosition: Integer := 0;
	end record;

	----------------------------------------------------------------------------
	-- Object implementation
	----------------------------------------------------------------------------
	
	function  toString(o: Omega) return String;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	
	function  visitLight(str:access Omega; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access Omega; i: access Introspector'Class) return Integer;
	
	----------------------------------------------------------------------------
	
	procedure makeOmega(om : in out Omega; ip: Integer; v: StrategyPtr);
	function newOmega(ip: Integer; v: StrategyPtr) return StrategyPtr;
	function  getPos(om : Omega) return Integer;

end OmegaStrategy;
