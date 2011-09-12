with VisitablePackage, ObjectPack;
use  VisitablePackage, ObjectPack;

package IntrospectorPackage is

	type Introspector is interface and Object;

	function setChildren(intro: access Introspector; o: ObjectPtr; children: ObjectPtrArrayPtr) return ObjectPtr is abstract;
	function  getChildren(intro: access Introspector; o: ObjectPtr) return ObjectPtrArrayPtr is abstract;
	function setChildAt(intro: access Introspector; o: ObjectPtr; i: Integer; child: ObjectPtr) return ObjectPtr is abstract;
	function  getChildAt(intro: access Introspector; o: ObjectPtr; i: Integer) return ObjectPtr is abstract;
	function  getChildCount(intro: access Introspector; o: ObjectPtr) return Integer is abstract;
	
	type IntrospectorPtr is access all Introspector'Class;

end IntrospectorPackage;

