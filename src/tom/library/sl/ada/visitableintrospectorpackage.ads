with IntrospectorPackage, VisitablePackage, ObjectPack;
use  IntrospectorPackage, VisitablePackage, ObjectPack;

package VisitableIntrospectorPackage is

	type VisitableIntrospector is new Introspector and Object with null record;
	
	procedure setChildren(intro: in out VisitableIntrospector; o: in out Visitable'Class; children: ObjectPtrArrayPtr);
	function  getChildren(intro: VisitableIntrospector; o: Object'Class) return ObjectPtrArrayPtr;
	procedure setChildAt(intro: in out VisitableIntrospector; o: in out Visitable'Class; i: Integer; child: Object'Class);
	function  getChildAt(intro: VisitableIntrospector; o: Object'Class; i: Integer) return Object'Class;
	function  getChildCount(intro: VisitableIntrospector; o: Object'Class) return Integer;
	
	function  getInstance return IntrospectorPtr;
	function  toString(vi : VisitableIntrospector) return String;
	
	mapping : IntrospectorPtr := new VisitableIntrospector;

end VisitableIntrospectorPackage;
