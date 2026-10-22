with PathPackage;
use  PathPackage;

with StrategyPackage; use StrategyPackage;

package PositionPackage is
	type Position is new Path with
		record
			omega : IntArrayPtr := null;
		end record;
	
	--type PositionPtr is access all Position;

	ROOT : access Position  := null;
	
	----------------------------------------------------------------------------
	-- Path implementation
	----------------------------------------------------------------------------
	
	function add(p1, path: Position) return Position;
	function sub(p1, p2: Position) return Position;
	function inverse(p:Position) return Position;
	function length(p: Position) return Natural;
	function getHead(p: Position) return Integer;
	function getTail(p: Position) return Position;
	function conc(p: Position; i: Integer) return Position;
	function getCanonicalPath(p: Position) return Position;
	function toIntArray(p: Position) return IntArrayPtr;
	
	----------------------------------------------------------------------------

	function make return Position;
	
	function makeFromSubarray(src : not null IntArrayPtr ; srcIndex: Integer ; length: Natural) return Position;
	function makeFromArray(arr: IntArrayPtr) return Position;

	function makeFromPath(p : Position) return Position;

	function clone(pos: Position) return Position;
	function hashCode(p: Position) return Integer;
	function equals(p1, p2: Position) return Boolean;

	function compare(p1: Position; path: Position) return Integer;
	function toString(p: Position) return String;
	

	function up(pos: Position) return Position;
	function down(pos: Position; i: Integer) return Position;
	function hasPrefix(pos, pref: Position) return Boolean;
	function changePrefix(pos, oldprefix, prefix: Position) return Position;
	
	function getOmega(p: Position; v: Strategy'Class) return Strategy'Class;
	function getOmegaPath(pos: Position; v: Strategy'Class) return StrategyPtr;

	--function getReplace(pos: Position; t: ObjectPtr) return StrategyPtr;
	--function getSubterm return StrategyPtr;

	----------------------------------------------------------------------------

end PositionPackage;
