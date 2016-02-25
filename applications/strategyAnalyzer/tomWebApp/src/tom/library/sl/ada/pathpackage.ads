with ObjectPack;
use  ObjectPack;

package PathPackage is

	type Path is interface and Object;

	type IntArray is array (Natural range <> ) of Integer;
	type IntArrayPtr is access all IntArray;
	

	function add(p1, p2: Path) return Path is abstract;
	function sub(p1, p2: Path) return Path is abstract;
	function inverse(p:Path) return Path is abstract;
	function length(p: Path) return Natural is abstract;
	function getHead(p: Path) return Integer is abstract;
	function getTail(p: Path) return Path is abstract;
	function conc(p: Path; i: Integer) return Path is abstract;
	function getCanonicalPath(p: Path) return Path is abstract;
	function toIntArray(p: Path) return IntArrayPtr is abstract;


end PathPackage;
