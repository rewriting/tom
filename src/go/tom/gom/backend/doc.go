// Package backend turns a Gom AST (see package gom) into Go code whose
// term constructors rely on the shared-objects runtime (see package
// library/sharedobjects) for maximal sharing / hash-consing, and into
// the Tom mapping of the module (see backend.go).
package backend
