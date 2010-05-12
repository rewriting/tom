import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;

import ligneproduitstelephones.*;

import tom.library.sl.*;

class ClassUsingLPTMapping {

  %include{ ligneproduitstelephonesmapping.tom }

  %include { sl.tom }

  public static void main(String[] args) {
    Telephone t1=`Telephone("Milestone", true, Android(), null);
    Telephone t2=`Telephone("Dext", true, Android(), null);
    Telephone t3=`Telephone("iPhone", true, MACOS(), null);
    Telephone t4=`Telephone("8800c", false, BLACKBERRY(), null);
    Telephone t5=`Telephone("Dream", true, Android(), null);
    Telephone t6=`Telephone("iPhone 3G", true, MACOS(), null);
    Telephone t7=`Telephone("iPhone 3GS", true, MACOS(), null);
    Telephone t8=`Telephone("6210 navigator", false, Symbian(), null);
    Marque m1=`Marque("Motorola", TelephoneEList(t1,t2));
    Marque m2=`Marque("Apple", TelephoneEList(t3));
    Marque m3=`Marque("BlackBerry", TelephoneEList(t4));
    Marque m4=`Marque("HTC", TelephoneEList(t5));
    Marque m5=`Marque("Nokia", TelephoneEList(t8));
    t1.setMarque(m1);
    t2.setMarque(m1);
    t3.setMarque(m2);
    t4.setMarque(m3);
    t5.setMarque(m4);
    t6.setMarque(m2);
    t7.setMarque(m2);
    t8.setMarque(m5);
    LigneProduitsTelephones lpt = `LigneProduitsTelephones("My product line name", 
        MarqueEList(m1,m2,m3,m4,m5), TelephoneEList(t1,t2,t3,t4,t5,t6,t7,t8));

    System.out.println("Test1 :");
    /* List phones */
    try {
      listPhones(lpt);
    } catch (VisitFailure e) {
      System.out.println("Erreur dans l'application de la stratégie stratPrintPhone " + e);
    }
    System.out.println("\n#################################################");
    System.out.println("Test2 :");
      whichIsTactile(lpt);
    System.out.println("\n#################################################");
    System.out.println("Test3 :");
    brandsHavingAndroidPhones(lpt);

    System.out.println("\n#################################################");
    System.out.println("Test4 :");
    //phonesByOS(lpt);
  }
  
  
  public static void listPhones(LigneProduitsTelephones lpt) throws VisitFailure {
    // print all phones names from product line lpt
    System.out.println("\nListe des téléphones avec '%match' :");
    %match (lpt) {
      LigneProduitsTelephones(_,_,TelephoneEList(_*,Telephone(name,_,_,_),_*)) -> {
        System.out.println(`name);
      }
    }

    // same thing using strategies and a containment instrospector
    System.out.println("\nListe des téléphones avec '%strategy' :");
    `TopDown(Try(stratPrintPhone())).visitLight(lpt, new EcoreContainmentIntrospector());
  }

  %strategy stratPrintPhone() extends Fail() {
    visit Telephone {
      Telephone(name,_,_,_) -> { System.out.println(`name); }
    }
  }


 public static void whichIsTactile(LigneProduitsTelephones lpt) {
    // print all tactile phones names from product line lpt
    System.out.println("\nListe des téléphones tactiles :");
    %match (lpt) {
      LigneProduitsTelephones(_,_,TelephoneEList(_*,Telephone(name,tactile,_,_),_*)) && tactile == true -> {
        System.out.println(`name);
      } 
    }
  }


  public static void brandsHavingAndroidPhones(LigneProduitsTelephones lpt) {
    java.util.HashSet<String> brandList = new java.util.HashSet<String>();
    // print all brands having at least an Android phone
    %match (lpt) {
      LigneProduitsTelephones(_,_,TelephoneEList(_*,Telephone(_,_,os,m),_*)) &&
        os == Android() -> {
        brandList.add(`m.getName());
      }
    }
    System.out.println("\nListe des marques ayant au moins un téléphone Android : " + brandList);
  }

  public static void phonesByOS(LigneProduitsTelephones lpt) {
    java.util.List<String> androidList = new java.util.ArrayList<String>();
    java.util.List<String> macosList = new java.util.ArrayList<String>();
    java.util.List<String> windowsList = new java.util.ArrayList<String>();
    java.util.List<String> symbianList = new java.util.ArrayList<String>();
    java.util.List<String> bbList = new java.util.ArrayList<String>();
    java.util.List<String> otherosList = new java.util.ArrayList<String>();

    %match {
      LigneProduitsTelephones(_,_,TelephoneEList(_*,Telephone(name,_,os,_),_*)) << lpt -> {
        %match(os) {
          Android() -> { androidList.add(`name); }
          MACOS() -> { macosList.add(`name); }
          WINDOWSCE() -> { windowsList.add(`name); }
          BLACKBERRY() -> { bbList.add(`name); }
          Symbian() -> { symbianList.add(`name); }
          Other() -> { otherosList.add(`name); }
        }
      }
    }

    System.out.println("\nTri des téléphones par OS\n");
    System.out.println("Téléphones sous Android : " + androidList);
    System.out.println("Téléphones sous MacOS : " + macosList);
    System.out.println("Téléphones sous Windows : " + windowsList);
    System.out.println("Téléphones sous BlackBerry : " + bbList);
    System.out.println("Téléphones sous Symbian : " + symbianList);
    System.out.println("Téléphones sous d'autres systèmes : " + otherosList);
  }

}
