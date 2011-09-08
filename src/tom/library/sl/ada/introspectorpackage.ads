with VisitablePackage, ObjectPack;
use  VisitablePackage, ObjectPack;

package IntrospectorPackage is

	type Introspector is interface and Object;

	procedure setChildren(intro: access Introspector; o: ObjectPtr; children: ObjectPtrArrayPtr) is abstract;
	function  getChildren(intro: access Introspector; o: ObjectPtr) return ObjectPtrArrayPtr is abstract;
	procedure setChildAt(intro: access Introspector; o: ObjectPtr; i: Integer; child: ObjectPtr) is abstract;
	function  getChildAt(intro: access Introspector; o: ObjectPtr; i: Integer) return ObjectPtr is abstract;
	function  getChildCount(intro: access Introspector; o: ObjectPtr) return Integer is abstract;
	
	type IntrospectorPtr is access all Introspector'Class;

end IntrospectorPackage;

