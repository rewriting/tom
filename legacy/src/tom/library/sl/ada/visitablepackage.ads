with ObjectPack;
use  ObjectPack;
package VisitablePackage is

	type Visitable is interface and Object;
	type VisitablePtr is access all Visitable'Class;
	
	function getChildCount(v: access Visitable) return Integer is abstract;
	
	function setChildren(v: access Visitable ; children : ObjectPtrArrayPtr) return VisitablePtr is abstract;
	function getChildren(v: access Visitable) return ObjectPtrArrayPtr is abstract;
	
	function getChildAt(v: access Visitable; i : Integer) return VisitablePtr is abstract;
	function setChildAt(v: access Visitable; i: in Integer; child: in VisitablePtr) return VisitablePtr is abstract;

    

end VisitablePackage;
