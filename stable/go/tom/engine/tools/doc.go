// Package tools is the Go port of the subset of tom.engine.tools and of
// tom.engine.TomBase the backend uses (spec J4 §4.3; plan J4.1a):
// OutputCode (outputcode.go), ASTFactory (astfactory.t), SymbolTable
// (symboltable.t) and TomBase (tombase.t). A .t file is Tom+Go, compiled by
// mage regenTomGo into X.t.go (gitignored in src/, kept in the amorce
// stable/go/tom). Only the Go target is ported (D5).
package tools
