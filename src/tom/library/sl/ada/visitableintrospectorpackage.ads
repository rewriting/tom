with IntrospectorPackage, VisitablePackage, ObjectPack;
use  IntrospectorPackage, VisitablePackage, ObjectPack;

package VisitableIntrospectorPackage is

	type VisitableIntrospector is new Introspector and Object with null record;
	
	procedure setChildren(intro: access VisitableIntrospector; o: ObjectPtr; children: ObjectPtrArrayPtr);
	function  getChildren(intro: access VisitableIntrospector; o: ObjectPtr) return ObjectPtrArrayPtr;
	procedure setChildAt(intro: access VisitableIntrospector; o: ObjectPtr; i: Integer; child: ObjectPtr);
	function  getChildAt(intro: access VisitableIntrospector; o: ObjectPtr; i: Integer) return ObjectPtr;
	function  getChildCount(intro: access VisitableIntrospector; o: ObjectPtr) return Integer;
	
	function  getInstance return IntrospectorPtr;
	function  toString(vi : VisitableIntrospector) return String;
	
	mapping : IntrospectorPtr := new VisitableIntrospector;

end VisitableIntrospectorPackage;
