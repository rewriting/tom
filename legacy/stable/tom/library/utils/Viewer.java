



























package tom.library.utils;



import java.io.*;
import tom.library.sl.*;
import java.util.Stack;
import java.util.List;
import aterm.pure.PureFactory;
import att.grappa.*;
import javax.swing.*;
import java.awt.event.*;






public class Viewer {

     private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}   private static   aterm.ATermList  tom_append_list_concATerm( aterm.ATermList l1,  aterm.ATermList  l2) {     if( l1.isEmpty() ) {       return l2;     } else if( l2.isEmpty() ) {       return l1;     } else if(  l1.getNext() .isEmpty() ) {       return  l2.insert( l1.getFirst() ) ;     } else {       return  tom_append_list_concATerm( l1.getNext() ,l2).insert( l1.getFirst() ) ;     }   }   private static   aterm.ATermList  tom_get_slice_concATerm( aterm.ATermList  begin,  aterm.ATermList  end, aterm.ATermList  tail) {     if( begin == end ) {       return tail;     } else if( end == tail  && ( end.isEmpty()  ||  end ==  aterm.pure.SingletonFactory.getInstance().makeList()  )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  ( aterm.ATermList )tom_get_slice_concATerm( begin.getNext() ,end,tail).insert( begin.getFirst() ) ;   }   



  
  
  public static void toDot(tom.library.sl.Visitable v, Writer w)
    throws java.io.IOException {
      if ( v instanceof tom.library.sl.Strategy ) {
        Strategy subj = (tom.library.sl.Strategy) v;
        Mu.expand(subj);
        try{
          subj = (Strategy) tom_make_TopDownCollect( new RemoveMu() ).visit(subj);
          w.write("digraph strategy {\nordering=out;");
          Strategy print = new PrintStrategy(w);
          tom_make_TopDownCollect(print).visit(subj);
        } catch (VisitFailure e) {throw new RuntimeException("unexcepted visit failure");}
        w.write("\n}");
        w.flush();
      } else {
        w.write("digraph visitable {\nordering=out;");
        try{
          Strategy print = new Print(w);
          tom_make_TopDown(print).visit(v);
        } catch (VisitFailure e) {throw new RuntimeException("unexcepted visit failure");}
        w.write("\n}");
        w.flush();
      }
    }

 
 public static void toDot(tom.library.sl.Visitable v) {
    try {
      Writer w = new BufferedWriter(new OutputStreamWriter(System.out));
      toDot(v,w);
      w.write('\n');
      w.flush();
    } catch(java.io.IOException e) {}
  }

  



  
  private static String getNodeFromPos(Position p) {
    int[] omega = p.toIntArray();
    StringBuilder r = new StringBuilder("p");
    for(int i=0 ; i<p.length() ; i++) {
      r.append(omega[i]);
      if(i<p.length()-1) {
        r.append("_");
      }
    }
    return r.toString();
  }

  
  static private class Print extends AbstractStrategyBasic {

    protected Writer w;

    public Print(Writer w) {
      super(( new tom.library.sl.Identity() ));
      this.w=w;
    }

    
    public Object visitLight(Object any, Introspector i) throws VisitFailure {
      throw new VisitFailure();
    }

    
    public int visit(Introspector introspector) {
      Visitable v = (Visitable) getEnvironment().getSubject();
      try {
        if (v instanceof Path) {
          Position current = getEnvironment().getPosition();
          Position father = current.up();
          w.write("\n              "+getNodeFromPos(current)+" [label=\"\"];\n              "+getNodeFromPos(father)+" -> "+getNodeFromPos(current)+"; "

);
          Position dest = (Position) current.add((Path)v).getCanonicalPath();
          w.write("\n              "+getNodeFromPos(current)+" -> "+getNodeFromPos(dest)+"; "
);
        } else {
          Position current = getEnvironment().getPosition();
          String term = v.toString();
          
          
          int end = term.indexOf("(");
          String name = term.substring(0,(end==-1)?term.length():end);
          w.write("\n              "+getNodeFromPos(current)+" [label=\""+name+"\"]; "
);
          if(!current.equals(Position.make())) {
            Position father = current.up();
            w.write("\n                "+getNodeFromPos(father)+" -> "+getNodeFromPos(current)+"; "
);
          }
        }
      } catch(IOException e) {}
      return Environment.SUCCESS;
    }
  }

  
  public static void display(Visitable vv) {
    final Visitable v = vv;
    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("Viewer");
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
        synchronized(v){  v.notify(); }}});
    try {
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec("dot");
      Writer out = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
      Viewer.toDot(v, out);
      out.close();
      Parser parser = new Parser(pr.getInputStream());
      parser.parse();
      Graph graph = parser.getGraph();
      GrappaPanel panel = new GrappaPanel(graph);
      panel.setScaleToFit(true);
      frame.getContentPane().add(panel, java.awt.BorderLayout.CENTER);
      frame.pack();
      frame.setVisible(true);
      synchronized(v){
        v.wait();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
  
  public static void toTree(tom.library.sl.Visitable v) {
    try {
      Writer w = new BufferedWriter(new OutputStreamWriter(System.out));
      toTree(v,w);
      w.write('\n');
      w.flush();
    } catch(java.io.IOException e) {}
  }

  
  public static void toTree(tom.library.sl.Visitable v, Writer w)
    throws java.io.IOException {
      aterm.ATermFactory atermFactory = new PureFactory();
      aterm.ATerm at = atermFactory.parse(v.toString());
      ATermToTree(at, w, new Stack<Integer>(), 0);
    }

  private static void writeContext(Writer w, Stack<Integer> context, int deep)
    throws java.io.IOException {
      for(int i=0; i<deep; i++) {
        if (context.contains(i)) {
          w.write("│");
        } else {
          w.write(' ');
        }
      }
    }

  
  private static void ATermToTree(aterm.ATerm term, Writer w, Stack<Integer> context, int deep)
    throws java.io.IOException {
      { /* unamed block */{ /* unamed block */if ( term instanceof aterm.ATerm ) {if ( (( aterm.ATerm )term) instanceof aterm.ATermInt ) {

          w.write("─"+ ((aterm.ATermInt)(( aterm.ATerm )term)).getInt() );
          return;
        }}}{ /* unamed block */if ( term instanceof aterm.ATerm ) {if ( (( aterm.ATerm )term) instanceof aterm.ATermAppl ) { aterm.AFun  tomMatch832_5= ((aterm.ATermAppl)(( aterm.ATerm )term)).getAFun() ;if ( (( aterm.AFun )tomMatch832_5) instanceof aterm.AFun ) { String  tom___afunname= tomMatch832_5.getName() ; aterm.ATermList  tom___list= ((aterm.ATermAppl)(( aterm.ATerm )term)).getArguments() ;

          String name = ( tomMatch832_5.isQuoted() )?"\""+tom___afunname+"\"":tom___afunname;
          aterm.ATermAppl a = (aterm.ATermAppl) term;
          if (a.getArity() == 0) { /* unamed block */  
            w.write("─"+name);
            return;
          } else if (a.getArity() == 1) { /* unamed block */  
            w.write("─" + name + "──");
            deep = deep + name.length() + 3;
            ATermToTree(tom___list.getFirst(),w,context,deep);
            return;
          } else { /* unamed block */
            int ndeep = deep + name.length() + 3;
            { /* unamed block */{ /* unamed block */if ( tom___list instanceof aterm.ATermList ) {if ( (( aterm.ATermList )tom___list) instanceof aterm.ATermList ) {if (!( (( aterm.ATermList )tom___list).isEmpty() )) { aterm.ATermList  tomMatch833_2= (( aterm.ATermList )tom___list).getNext() ; aterm.ATermList  tomMatch833_end_5=tomMatch833_2;do {{ /* unamed block */ aterm.ATermList  tom___l=tom_get_slice_concATerm(tomMatch833_2,tomMatch833_end_5, aterm.pure.SingletonFactory.getInstance().makeList() );if (!( tomMatch833_end_5.isEmpty() )) {if (  tomMatch833_end_5.getNext() .isEmpty() ) {

                
                w.write("─" + name + "─┬");
                context.push(ndeep-1);
                ATermToTree( (( aterm.ATermList )tom___list).getFirst() ,w,context,ndeep);
                context.pop();
                w.write('\n');

                
                { /* unamed block */{ /* unamed block */if ( tom___l instanceof aterm.ATermList ) {if ( (( aterm.ATermList )tom___l) instanceof aterm.ATermList ) { aterm.ATermList  tomMatch834_end_4=(( aterm.ATermList )tom___l);do {{ /* unamed block */if (!( tomMatch834_end_4.isEmpty() )) {

                    writeContext(w,context,ndeep-1);
                    w.write("├");
                    context.push(ndeep-1);
                    ATermToTree( tomMatch834_end_4.getFirst() ,w,context,ndeep);
                    context.pop();
                    w.write('\n');
                  }if ( tomMatch834_end_4.isEmpty() ) {tomMatch834_end_4=(( aterm.ATermList )tom___l);} else {tomMatch834_end_4= tomMatch834_end_4.getNext() ;}}} while(!( tomMatch834_end_4 == (( aterm.ATermList )tom___l) ));}}}}

                
                writeContext(w,context,ndeep-1);
                w.write("└");
                ATermToTree( tomMatch833_end_5.getFirst() ,w,context,ndeep);
              }}if ( tomMatch833_end_5.isEmpty() ) {tomMatch833_end_5=tomMatch833_2;} else {tomMatch833_end_5= tomMatch833_end_5.getNext() ;}}} while(!( tomMatch833_end_5 == tomMatch833_2 ));}}}}}}}}}}}




    }


  
  private static int counter = 0;

  
  static private String clean(String s) {
    s = s.replace('.','_');
    s = s.replace('$','_');
    s = s.replace('@','_');
    return s;
  }

  public static class RemoveMu extends tom.library.sl.AbstractStrategyBasic {public RemoveMu() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.library.sl.Strategy) ) {return ((T)visit_Strategy((( tom.library.sl.Strategy )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.library.sl.Strategy  _visit_Strategy( tom.library.sl.Strategy  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.library.sl.Strategy )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.library.sl.Strategy  visit_Strategy( tom.library.sl.Strategy  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.library.sl.Strategy) ) {if (( (( tom.library.sl.Strategy )tom__arg) instanceof tom.library.sl.Mu )) {


        return ( (tom.library.sl.Strategy)(( tom.library.sl.Strategy )tom__arg).getChildAt(tom.library.sl.Mu.V) );
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.sl.Strategy) ) {

        if (getEnvironment().getCurrentStack().contains(getEnvironment().getSubject())) { /* unamed block */
          
          throw new VisitFailure();
        }}}}return _visit_Strategy(tom__arg,introspector);}}




  static private class PrintStrategy extends AbstractStrategyBasic {

    protected Writer w;

    
    public PrintStrategy(Writer w) {
      super(( new tom.library.sl.Identity() ));
      this.w=w;
    }

    
    public Object visitLight(Object any, Introspector i) throws VisitFailure {
      throw new VisitFailure();
    }

    
    public int visit(Introspector introspector) {
      Visitable v = (Visitable) getEnvironment().getSubject();
      Position current = getEnvironment().getPosition();
      List<Object> stack = getEnvironment().getCurrentStack();
      try {
        
        if (stack.contains(v)) {
          int index = stack.indexOf(v);
          Position dest = (Position) current.clone();
          for(int i=current.length();i>index;i--) {
            dest = dest.up();
          }
          Position father = current.up();
          w.write("\n              "+getNodeFromPos(father)+" -> "+getNodeFromPos(dest)+"; "
);
          
          return Environment.FAILURE;
        }
        else {
          String[] tab = v.getClass().getName().split("\\.");
          String name = tab[tab.length-1];
          tab = name.split("\\$");
          name = tab[tab.length-1];
          w.write("\n              "+getNodeFromPos(current)+" [label=\""+name+"\"]; "
);
          if(stack.size()!=0) {
            Position father = current.up();
            w.write("\n                "+getNodeFromPos(father)+" -> "+getNodeFromPos(current)+"; "
);
          }
        }
      } catch(java.io.IOException e) {
        return Environment.FAILURE;
      }
      return Environment.SUCCESS;
    }

  }



}
