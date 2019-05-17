# Chapter 1 
## Modularity Matters

Goal of modularity: managing and reducing complexity.

Modularization is the act of decomposing a system into self-contained but interconnected modules. Modules are identifiable artifacts containing code, with metadata describing the module and its relation to other modules.

Modules must adhere to three core tenets:
Strong encapsulation. A module must be able to conceal part of its code from other modules.
Well-defined interfaces. Encapsulation is fine, but if modules are to work together, not everything can be encapsulated. Code that is not encapsulated is, by definition, part of the public API of a module.
Explicit dependencies. Modules often need other modules to fulfill their obligations. Such dependencies must be part of the module definition, in order for modules to be self-contained.
