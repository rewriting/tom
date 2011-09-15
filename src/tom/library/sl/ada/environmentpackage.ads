with PathPackage, IntrospectorPackage, ObjectPack, PositionPackage;
use  PathPackage, IntrospectorPackage, ObjectPack, PositionPackage;

package EnvironmentPackage is

	DEFAULT_LENGTH 	: constant Integer := 8;
	SUCCESS 		: constant Integer := 0;
	FAILURE 		: constant Integer := 1;
	IDENTITY 		: constant Integer := 2;

	type Environment is new Object with
	record
		current 		: Integer;
		omega 			: IntArrayPtr := null;
		introspector 	: IntrospectorPtr;
		status 			: Integer := EnvironmentPackage.SUCCESS;
		subterm 		: ObjectPtrArrayPtr := null; --subterms must inherit from Object
	end record;
	
	type EnvironmentPtr is access all Environment;

	procedure makeEnvironment(env: in out Environment);	
	procedure makeEnvironment(env: in out Environment; intro: IntrospectorPtr);
	
	function newEnvironment return EnvironmentPtr;	
	function newEnvironment(intro: IntrospectorPtr) return EnvironmentPtr;
	
	function  clone(env: Environment) return Environment;
	function  equals(env1, env2 : Environment) return Boolean;
	function  hashCode(env: Environment) return Integer;
	function  getStatus(env: Environment) return Integer;
	procedure setStatus(env: in out Environment; s: Integer);
	function  getRoot(env: Environment) return ObjectPtr;
	procedure setRoot(env: in out Environment; r: ObjectPtr);
	function  getCurrentStack(env: Environment) return ObjectPtrArray;
	function  getAncestor(env: Environment) return ObjectPtr;
	function  getSubject(env: Environment) return ObjectPtr;
	procedure setSubject(env: in out Environment; root: ObjectPtr);
	function  getIntrospector(env: Environment) return IntrospectorPtr;
	procedure setIntrospector(env: in out Environment; i: IntrospectorPtr);
	function  getSubOmega(env: Environment) return Integer;
	function  depth(env: Environment) return Integer;
	function  getPosition(env: Environment) return Position;
	procedure up(env: in out Environment);
	procedure upLocal(env: in out Environment);
	procedure down(env: in out Environment; n: Integer);
	procedure followPath(env: in out Environment; p: Path'Class);
	procedure followPathLocal(env: in out Environment; p: Path'Class);
	procedure goToPosition(env: in out Environment; p: Position);

	function  toString(env: Environment) return String;
	
	private
	procedure makeEnvironment(env: in out Environment; len: Integer ; intro: IntrospectorPtr);
	procedure ensureLength(env: in out Environment; minLength: Integer);
	procedure genericFollowPath(env: in out Environment; p: Path'Class; local: Boolean);


end EnvironmentPackage;


