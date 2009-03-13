package sdfgom;

import java.io.*;
import java.lang.*;
import java.util.*;
import aterm.*;
import aterm.pure.*;

import sdf.*;
import sdf.types.*;

import tom.library.sl.*;
import sdfgom.SDFConverter;

public class SdfTool {
  %include { sl.tom }
  %include { sdf/sdf.tom }

  public static PureFactory factory = SingletonFactory.getInstance();

  public static Grammar convert(ATerm at) {
    SDFConverter sdfConverter = new SDFConverter();
    Grammar g = Grammar.fromTerm(at,sdfConverter);
    return g;
  }

} 
