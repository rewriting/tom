// Package sharedobjects is the Go port of the historical shared-objects
// runtime used by Gom-generated code: every structurally equal term is
// represented by a single in-memory instance (hash-consing / maximal
// sharing). Two equal terms compare equal by pointer identity in O(1).
package sharedobjects
