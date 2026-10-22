with VisitablePackage, IntrospectorPackage, ObjectPack;
use  VisitablePackage, IntrospectorPackage, ObjectPack;

limited with EnvironmentPackage;

package StrategyPackage is

	type Strategy is interface and Visitable and Object;

	procedure setEnvironment(str: in out Strategy; p: access EnvironmentPackage.Environment) is abstract;
	function  getEnvironment(str: Strategy) return access EnvironmentPackage.Environment is abstract;
	
	function  visit(str: access Strategy; any: VisitablePtr) return VisitablePtr is abstract;
	function  visitLight(str: access Strategy; any: VisitablePtr) return VisitablePtr is abstract;
	function  visit(str: access Strategy; any: access EnvironmentPackage.Environment) return VisitablePtr is abstract;
	function  visit(str: access Strategy; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is abstract;
	function  visitLight(str:access  Strategy; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is abstract;
	function  visit(str: access Strategy; envt: access EnvironmentPackage.Environment; i: access Introspector'Class) return ObjectPtr is abstract;
	function  visit(str: access Strategy; i: access Introspector'Class) return Integer is abstract;
	
	type StrategyPtr is access all Strategy'Class;
	function "<"(Left,Right: StrategyPtr) return Boolean; --used in MuVarStrategy
		
end StrategyPackage;

