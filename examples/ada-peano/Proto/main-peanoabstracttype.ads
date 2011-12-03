-- with VisitablePackage; use VisitablePackage;
with SharedObjectFactoryP; use SharedObjectFactoryP;
with SharedObjectP; use SharedObjectP;

package Main.PeanoAbstractType is
--	type PeanoAbstractType is abstract new SharedObjectP.SharedObject and Visitable with
 
	type PeanoAbstractType is abstract new SharedObjectP.SharedObject with null record ;

				factory : access SharedObjectFactoryP.SharedObjectFactory := new SharedObjectFactoryP.SharedObjectFactory; 

	function symbolName return String is abstract;

end Main.PeanoAbstractType;
