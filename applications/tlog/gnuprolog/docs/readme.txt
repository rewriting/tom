GNU Prolog for Java 0.1.0
-------------------------

This is an almost conforming implementation of ISO Prolog
standard in Java. It implements almost all required predicates,
but there are some minor problems which do not allow me to
call it conforming. To check current status of conformance you
can try to run tests yourself. 

This package were never indended to be used as standalone
system, it is a library which is designed to embeddable into Java
applications which need Prolog to solve some tasks. The
interpreter intended for applications where Prolog performs
combinatory search and Java do rest. The library allows easy
communication between Java and Prolog. The library is released
under LGPL terms. 

It was build with JDK 1.2 from start and I do not plan to support
1.1.x platform, some features of JDK 1.2 required i.e: 

  * Weak references (planned) 
  * Improved serialization 
  * Collection Framework 

It does not have an IDE and I don't plan to create one. If you
wish you can undertake it as separate project. But some sort of
box GUI debugger is planned. 

How can you contribute: 

 1.Write more code. If you wish to do it, write me and I'll give
    you a list of current tasks from which you can select as big
    and challenging one as as you want. 
 2.Send me a copy of ISO Standard of Prolog. Now I have
    incomlple copy of some chapters which can be downloaded
    from http://www.als.com/nalp/prolog_std.html. 

I plan to introduce major changes to core as result of several
related optimizations. So if you are writing some predicates in
Java, be prepared to change your code. 

But this code will remain here for a while, and I'll fix minor
bugs. 

