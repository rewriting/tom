package body VisitableIntrospectorPackage is
	function setChildren(intro: access VisitableIntrospector; o: ObjectPtr; children: ObjectPtrArrayPtr) return ObjectPtr is
	begin
		return ObjectPtr( VisitablePackage.setChildren(VisitablePtr(o), children) );
	end;
	
	function getChildren(intro: access VisitableIntrospector; o: ObjectPtr) return ObjectPtrArrayPtr is
	begin
		return VisitablePackage.getChildren(VisitablePtr(o));
	end;
	
	function setChildAt(intro: access VisitableIntrospector; o: ObjectPtr; i: Integer; child: ObjectPtr) return ObjectPtr is
	begin
		return ObjectPtr ( VisitablePackage.setChildAt(VisitablePtr(o), i, VisitablePtr(child)) );
	end;
	
	function getChildAt(intro: access VisitableIntrospector; o: ObjectPtr; i: Integer) return ObjectPtr is
	begin
		return ObjectPtr( VisitablePackage.getChildAt(VisitablePtr(o), i) );
	end;
	
	function getChildCount(intro: access VisitableIntrospector; o: ObjectPtr) return Integer is
	begin
		return VisitablePackage.getChildCount(VisitablePtr(o));
	end;
	
	function getInstance return IntrospectorPtr is
	begin
		return VisitableIntrospectorPackage.mapping;
	end;
	
	function toString(vi : VisitableIntrospector) return String is
	begin
		return "VisitableIntrospector()";
	end;

end VisitableIntrospectorPackage;
