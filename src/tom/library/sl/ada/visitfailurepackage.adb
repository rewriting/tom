with Ada.Text_IO;
use  Ada.Text_IO;

package body VisitFailurePackage is

	procedure RaiseVisitFailure(msg: String) is
	begin
		put_line(msg);
		raise VisitFailure;
	end RaiseVisitFailure;

end VisitFailurePackage;
