with VisitablePackage, ObjectPack;
use  VisitablePackage, ObjectPack;

package IntrospectorPackage is

	type Introspector is interface and Object;

	procedure setChildren(intro: in out Introspector; o: in out Visitable'Class; children: ObjectPtrArrayPtr) is abstract;
	function  getChildren(intro: Introspector; o: Object'Class) return ObjectPtrArrayPtr is abstract;
	procedure setChildAt(intro: in out Introspector; o: in out Visitable'Class; i: Integer; child: Object'Class) is abstract;
	function  getChildAt(intro: Introspector; o: Object'Class; i: Integer) return Object'Class is abstract;
	function  getChildCount(intro: Introspector; o: Object'Class) return Integer is abstract;
	
	type IntrospectorPtr is access all Introspector'Class;

end IntrospectorPackage;

