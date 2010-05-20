import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;

import ligneproduitstelephones.*;

import tom.library.sl.*;

class ClassUsingLPTMapping {

  %include{ ligneproduitstelephonesmapping.tom }

  %include { sl.tom }

  public static void main(String[] args) {
    Telephone iphone  =`Telephone("iPhone 3GS", null, IphoneOS(1));
    Telephone iphone2  =`Telephone("iPhone 2", null, IphoneOS(1));
    Telephone nexus   =`Telephone("Nexus", null, Android(1));
    Marque google = `Marque("Google Phone", TelephoneEList(nexus));
    Marque apple = `Marque("Apple", TelephoneEList(iphone));
    iphone.setMarque(apple);
    iphone2.setMarque(apple);
    nexus.setMarque(google);

    LigneProduitsTelephones lpt = `LigneProduitsTelephones("Mes produits", 
        TelephoneEList(iphone,iphone2,nexus),
        MarqueEList(apple,google) );

    System.out.println(apple.getName());
    
    System.out.println(iphone.getName() + " version " + iphone.getOS().getVersion());

    %match(iphone) {
      Telephone(name,_,OSTelephone(numero)) -> {
        System.out.println(`name + " version " + `numero);
      }
    }

    %match(lpt) {
      LigneProduitsTelephones[telephones=TelephoneEList(_*, Telephone(name,_,OSTelephone(1)),_*)] -> {
        System.out.println(`name);
      }

      LigneProduitsTelephones[telephones=TelephoneEList(_*,Telephone(name1,m,_),_*, Telephone(name2,m,_),_*)] -> {
        System.out.println("meme marque: " + `name1 + " et " + `name2);
      }
    }



  }

}
