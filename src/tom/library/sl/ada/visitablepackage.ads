with ObjectPack;
use  ObjectPack;
package VisitablePackage is

	type Visitable is interface and Object;
	type VisitablePtr is access all Visitable'Class;
	
	function  getChildCount(v: Visitable) return Integer is abstract;
	
	procedure setChildren(v: in out Visitable ; children : ObjectPtrArrayPtr) is abstract;
	function  getChildren(v: Visitable) return ObjectPtrArrayPtr is abstract;
	
	function  getChildAt(v: Visitable; i : Integer) return VisitablePtr is abstract;
	procedure setChildAt(v: in out Visitable; i: in Integer; child: in VisitablePtr) is abstract;

    

end VisitablePackage;
