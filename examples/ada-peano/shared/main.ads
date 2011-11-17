with sharedObjectP; use SharedObjectP;
package Main is
		type testie is new SharedObject with
		record
			serie: Integer:=42;
		end record;

overriding
function duplicate(this: testie) return testie ;

function equivalent(this: testie; o: testie) return boolean ; 

overriding
function hashCode(this: testie) return Integer ;

function toString(this: testie) return String ;

end Main;
