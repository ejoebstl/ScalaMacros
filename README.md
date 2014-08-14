ScalaMacros
===========

This macro finds all free variables which are bound from an outside scopein a given function parameter.

*Usage* 

sbt/sbt compile 
to compile

sbt/sbt mkapp
to create executable bin/application.sh

Main method is located in 
ScalaMacros/core/src/main/scala/application/Main.scala 

Samples can be found in Main.scala

Macro is located in 
ScalaMacros/macros/src/main/scala/FindFreeVars.scala 

Link to discussion: https://groups.google.com/d/msg/scala-user/VSAFechtnwk/NDNeNzf9Fi4J

*How this macro works*

1) Use a compile time macro to fetch information about the AST which can be used at runtime: https://stackoverflow.com/questions/24480926/how-can-i-use-scalas-runtime-reflection-to-inspect-a-passed-anonymous-function/25316506#25316506
2) Internals of the macro: 
    a) Find all Indents in the passed function.
    b) Filter out classes, packages, types, function calls.
	c) Filter out variables which are declared inside the passed function.
	d) Create an expression which, when evaluated, returns a list of (Name, Value) tuples for each variable.  
