with VisitablePackage, IntrospectorPackage, ObjectPack;
use  VisitablePackage, IntrospectorPackage, ObjectPack;

limited with EnvironmentPackage;

package StrategyPackage is

	type Strategy is interface and Visitable and Object;

	procedure setEnvironment(str: in out Strategy; p: EnvironmentPackage.Environment) is abstract;
	function  getEnvironment(str: Strategy) return access EnvironmentPackage.Environment is abstract;
	
	function  visit(str: access Strategy; any: Visitable'Class) return VisitablePtr is abstract;
	function  visitLight(str: access Strategy; any: Visitable'Class) return VisitablePtr is abstract;
	function  visit(str: access Strategy; any: EnvironmentPackage.Environment) return VisitablePtr is abstract;
	function  visit(str: access Strategy; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is abstract;
	function  visitLight(str:access  Strategy; any: ObjectPtr; i: access Introspector'Class) return ObjectPtr is abstract;
	function  visit(str: access Strategy; envt: EnvironmentPackage.Environment; i: access Introspector'Class) return ObjectPtr is abstract;
	function  visit(str: access Strategy; i: access Introspector'Class) return Integer is abstract;
	
	type StrategyPtr is access all Strategy'Class;
		
end StrategyPackage;

