with VisitablePackage, ObjectPack;
use  VisitablePackage, ObjectPack;
package VisitableBuiltinPackage is

	type VisitableBuiltin is new Visitable and Object with
	record
		builtin : ObjectPtr := null;
	end record;
	
	
	function  toString(vb: VisitableBuiltin) return String;
	
	function setChildren(v: access VisitableBuiltin ; children : ObjectPtrArrayPtr) return VisitablePtr;
	function  getChildren(v: access VisitableBuiltin) return ObjectPtrArrayPtr;
	
	function  getChildAt(v: access VisitableBuiltin; i : Integer) return VisitablePtr;
	function  setChildAt(v: access VisitableBuiltin; i: in Integer; child: in VisitablePtr) return VisitablePtr;
	
	function  getChildCount(v:access VisitableBuiltin) return Integer;
	
	function  makeVisitableBuiltinPackage(o: ObjectPtr) return VisitableBuiltin;
	function  getBuiltin(v: VisitableBuiltin) return ObjectPtr;
    

end VisitableBuiltinPackage;
