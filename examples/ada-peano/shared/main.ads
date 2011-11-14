with sharedObjectP; use SharedObjectP;
package Main is
		type testie is new SharedObject with
		record
			serie: Integer:=42;
		end record;

function duplicate(this: testie) return SharedObject'Class ;

function equivalent(this: testie; o: testie) return boolean ; 

function hashCode(this: testie) return Integer ;

function toString(this: testie) return String ;

end Main;
