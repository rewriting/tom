with VisitablePackage; use VisitablePackage;
with SharedObjectFactory; use SharedObjectFactory;
with SharedObject; use SharedObject;

package Main.PeanoAbstractType is

	type PeanoAbstractType is abstract new Visitable and SharedObject.SharedObject with
		record
			factory : access SharedObjectFactory.SharedObjectFactory := new SharedObjectFactory.SharedObjectFactory; 
		end record;

	function symbolName return String is abstract;


end Main.PeanoAbstractType;
	
