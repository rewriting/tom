with ObjectPack, StrategyPackage, EnvironmentPackage, IntrospectorPackage, VisitablePackage, PositionPackage;
use  ObjectPack, StrategyPackage, EnvironmentPackage, IntrospectorPackage, VisitablePackage, PositionPackage;

package AbstractStrategyPackage is

	type AbstractStrategy is abstract new Strategy and Object with
	record
		env: EnvironmentPtr := null;
	end record;
	
	type AbstractStrategyPtr is access all AbstractStrategy'Class;
	
	----------------------------------------------------------------------------
	-- Strategy implementation
	----------------------------------------------------------------------------
	function  visit(str: access AbstractStrategy; any: access Environment) return VisitablePtr;
	function  visit(str: access AbstractStrategy; any: VisitablePtr) return VisitablePtr;
	function  visit(str: access AbstractStrategy; envt: access Environment; i: access Introspector'Class) return ObjectPtr;
	function  visit(str: access AbstractStrategy; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr;
	function  visitLight(str: access AbstractStrategy; any: VisitablePtr) return VisitablePtr;
	function  getEnvironment(str: AbstractStrategy) return access Environment;
	procedure setEnvironment(str: in out AbstractStrategy; env: access Environment);
	----------------------------------------------------------------------------
	
	function  getRoot(str: AbstractStrategy) return ObjectPtr;
	procedure setRoot(str: in out AbstractStrategy; any: ObjectPtr);
	function  getSubject(str: AbstractStrategy) return ObjectPtr;
	procedure setSubject(str: in out AbstractStrategy; any: ObjectPtr);
	function  getPosition(str: AbstractStrategy) return Position;
	function  getAncestor(str: AbstractStrategy) return ObjectPtr;
	procedure init(str: in out Strategy'Class; i: IntrospectorPtr);
	procedure init(str: in out Strategy'Class; env: access Environment);
	
	----------------------------------------------------------------------------
		
end AbstractStrategyPackage;

