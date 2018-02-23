---
title: Documentation:EMF Mapping Generator
permalink: /Documentation:EMF_Mapping_Generator/
---

EMF mappings generator is an automatic mappings generator for [Eclipse Modeling Framework](http://www.eclipse.org/modeling/emf/).

Using EMF mapping generator
===========================

The EMF mappings generator has been integrated in the current stable stable version (2.9). To use it, just install Tom, Tom-EMF will also be installed. Two scripts compose Tom-EMF:

-   *emf-generate-mappings* is equivalent to the command *java TomMappingFromEcore*. It takes one (or several) EPackage name(s) as argument(s) which are used to name the generated mappings (**you have to use the full qualified EPackage name)**). The mappings are generated in the current directory,
-   *emf-generate-xmi* is equivalent to the command *java EcoreMappingToXMI*. It takes an argument (the package name) and generates an XMI file which is printed on the standard output

Use case: Let's say you want to generate the mapping for the petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage used in the [SimplePDLToPetrinet](/Documentation:EMFUseCaseSimplePDL2Petrinet "wikilink") example. Just type the following command (you obviously have to download the petrinet jar file):

`emf-generate-mappings -cp petrinetsemantics.jar petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage`

The '-nt' option allows to generate constructs with subtyping:

`emf-generate-mappings -cp petrinetsemantics.1.0.0.jar -nt petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage`

After enabling the '-nt' option, '%typeterm' constructs will integrate subtyping as following:

``` tom
…
%typeterm Place extends Node {
…
```

Then, if you want to have a serialization, just use the following command:

`emf-generate-xmi petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage > DDMMPetriNetPackage.xmi`

Tom-EMF reference page
======================

A description of how the generators work is available in the Section [EMF](/Documentation:EMF "wikilink").

How to use the generators and the generated mappings
====================================================

To see an use of the EMF mappings generator with an example, please look at the [library example](/Documentation:Playing_with_EMF "wikilink") and [SimplePDLToPetrinet example](/Documentation:EMFUseCaseSimplePDL2Petrinet "wikilink"). As we are trying to write an usable documentation, this page is still under construction. If you think that some information are missing, juste create an account and modify the page by adding a question.