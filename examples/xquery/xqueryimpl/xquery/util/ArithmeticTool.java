package xquery.util; 


import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import aterm.*;

import java.util.*;

public class ArithmeticTool {
  public static float min(Sequence seq, TNodeEvaluator eval)
	throws XQueryGeneralException 
  {
	Enumeration enum = seq.elements(); 
	if (!enum.hasMoreElements()) {
	  return 0;
	}


	float min = Float.MAX_VALUE; 
	while (enum.hasMoreElements()) {
	  TNode node=(TNode)(enum.nextElement());
	  float f = eval.getFloatValue(node);
	  if (f < min) {
		min = f; 
	  }
	}

	return min; 
  }


  public static float max(Sequence seq, TNodeEvaluator eval)
	throws XQueryGeneralException
  {
	Enumeration enum = seq.elements(); 
	if (!enum.hasMoreElements()) {
	  return 0;  // empty sequence return 0 as Max and min
	}


	float max = Float.MIN_VALUE; 
	while (enum.hasMoreElements()) {
	  TNode node=(TNode)(enum.nextElement());
	  float f = eval.getFloatValue(node);
	  if (f > max) {
		max = f; 
	  }
	}

	return max;
  }

  
  public static float abs(TNode node, TNodeEvaluator eval)
	throws XQueryGeneralException
  {
	return (float)java.lang.Math.abs(eval.getFloatValue(node));
  }


  public static float floor(TNode node, TNodeEvaluator eval)
	throws XQueryGeneralException
  {
	return (float)java.lang.Math.floor(eval.getFloatValue(node));
  }


  public static float ceiling(TNode node, TNodeEvaluator eval)
	throws XQueryGeneralException
  {
	return (float)java.lang.Math.ceil(eval.getFloatValue(node));
  }


  public static float avg(Sequence seq, TNodeEvaluator eval)
	throws XQueryGeneralException
  {
	if (seq.size()==0)
	  return 0;

	return ArithmeticTool.sum(seq,eval)/seq.size();
  }


  public static float sum(Sequence seq, TNodeEvaluator eval)
	throws XQueryGeneralException
  {
	Enumeration enum = seq.elements(); 

	float avg = 0; 
	while (enum.hasMoreElements()) {
	  TNode node=(TNode)(enum.nextElement());
	  avg+= eval.getFloatValue(node);
	}
		
	return avg;
  }


  public static int round(TNode node, TNodeEvaluator eval)
	throws XQueryGeneralException
  {
	return java.lang.Math.round(eval.getFloatValue(node));
  }

  public static float roundHalfToEven(TNode node, TNodeEvaluator eval)
	throws XQueryGeneralException
  {
	return Float.NaN;
  }
  
}