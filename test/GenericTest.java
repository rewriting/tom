import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.lang.reflect.*;

public abstract class GenericTest extends TestCase {
  private int testNumber;
  private static final String[] primitiveWrappers = new String[]{
    "java.lang.Boolean",
    "java.lang.Character",
    "java.lang.Byte", 
    "java.lang.Short",
    "java.lang.Integer",
    "java.lang.Long",
    "java.lang.Float",
    "java.lang.Double"};

  private static final Class[] primitiveClasses = new Class[]{
    java.lang.Boolean.TYPE,
    java.lang.Character.TYPE,
    java.lang.Byte.TYPE, 
    java.lang.Short.TYPE,
    java.lang.Integer.TYPE,
    java.lang.Long.TYPE,
    java.lang.Float.TYPE,
    java.lang.Double.TYPE};

  private static final String[] primitiveTypes = new String[]{
    "boolean","char","byte","short","int","long","float","double"};


  private Object[][] testTab;

  public GenericTest(int testNumber,Object[][] testTab) {
    super("test");
    this.testNumber = testNumber;
    this.testTab = testTab;
  }


  public void test() {
    try{
      Object[] td = testTab[this.testNumber];
      int nbParam = ((Integer)td[1]).intValue();
      Class[] parameterTypes = new Class[nbParam];
      Object[] parameters = new Object[nbParam];
      boolean isPrimitive = false;
      for (int j=0 ;j<nbParam;j++){
        String parameterClass = (String)(td[2+j]);
        //Gestion des types primitifs pour les parametres
        isPrimitive = false;
        for (int i=0;i<primitiveClasses.length& !isPrimitive;i++){
          if (parameterClass.equals(primitiveTypes[i])){
            isPrimitive = true;
            parameterTypes[j]=primitiveClasses[i];
          }
        }
        if(! isPrimitive){   
          parameterTypes[j] = Class.forName(parameterClass);
        }
        parameters[j] = td[2+nbParam+j];
      }
      // La methode a tester doit etre une methode de la classe de test
      // Le receveur de la methode est this
      Method methode = this.getClass().getMethod((String)td[0],parameterTypes);
      Class returnType = methode.getReturnType();
      //Gestion des types primitifs pour le retour d'une methode
      isPrimitive = false;
      for (int i=0;i<primitiveClasses.length & !isPrimitive;i++){

        if (returnType.equals(primitiveClasses[i])){
          isPrimitive = true;
          Method value = Class.forName(primitiveWrappers[i]).getMethod(primitiveTypes[i]+"Value",null);
          assertEquals(
              td[0]+" : "+" expected "+td[2+2*nbParam]+" for term "+td[1+2*nbParam],
              value.invoke(methode.invoke(this,parameters),null), 
              value.invoke(td[2+2*nbParam],null));
        }
      }
      if(! isPrimitive){
        // si la méthode testee est de type void, il faut faire attention d'avoir redéfini correctement equals
        if(returnType.getName().equals("void")){
          methode.invoke(this,parameters);
          assertSame(
              td[0]+" : "+" expected "+td[2+2*nbParam]+" for term "+td[1+2*nbParam],
              this, 
              td[2+2*nbParam]);
        }
        else{
          assertSame(
              td[0]+" : "+" expected "+td[2+2*nbParam]+" for term "+td[1+2*nbParam],
              methode.invoke(this,parameters), 
              td[2+2*nbParam]);
        }
      }
    } catch(Exception e) {
      System.out.println(e);
    }
  }

}
