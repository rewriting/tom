with ObjectPack;
use  ObjectPack;
package VisitablePackage is

	type Visitable is interface and Object;
	
	function  getChildCount(v: Visitable) return Integer is abstract;
	
	procedure setChildren(v: in out Visitable ; children : ObjectPtrArrayPtr) is abstract;
	function  getChildren(v: Visitable) return ObjectPtrArrayPtr is abstract;
	
	function  getChildAt(v: Visitable; i : Integer) return Visitable'Class is abstract;
	procedure setChildAt(v: in out Visitable; i: in Integer; child: in Visitable'Class) is abstract;
	

	
	type VisitablePtr is access all Visitable'Class;
    

end VisitablePackage;
