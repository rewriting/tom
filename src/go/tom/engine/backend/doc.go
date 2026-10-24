// Package backend is the Go backend of the compiler, written in Tom+Go
// (spec J4 §4.4, plan J4.1b): the driver (backend.go, BackendPlugin.run),
// the Collector pass (collector.t) and the generator, AbstractGenerator →
// GenericGenerator → GoGenerator flattened into one type goGenerator
// (generator.t, generate.t, declarations.t, filtrage.t, listes.t,
// strategies.t). A .t file is compiled by mage regenTomGo into X.t.go
// (gitignored in src/, kept in the amorce stable/go/tom). Only the Go
// target is ported (D5).
package backend
