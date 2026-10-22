// Frozen prototype copy — the maintained version is src/go/tom/gom/backend (module tom).

// Package backend turns a Gom AST (see package gom) into a Go package
// whose term constructors rely on the shared-objects runtime
// (see package library/sharedobjects) for maximal sharing / hash-consing.
package backend
