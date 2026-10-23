package adt

// hooks.t installs the hooks of the engine ADT in package adt: the
// construction hooks and the rules of the .gom files of this directory
// are Tom+Go code that gomgo writes into the mapping adt.tom (as for any
// Gom module); compiling this file, which includes the mapping, with
// tom -go gives hooks.go, whose init functions set the Hook<Make>
// variables that the constructors consult. Regenerate the package with
// `mage -d magefiles -w . regenAdt` (internal/adtgen).

%include { adt.tom }
