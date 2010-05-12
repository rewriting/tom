import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;

import ligneproduitstelephones.*;

import tom.library.sl.*;

class DemoStrategy {

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

    try {
      Telephone result = `TopDown(Try(upgradeVersion())).visitLight(nexus, new EcoreContainmentIntrospector());
      System.out.println(result.getOS().getVersion());
    } catch (VisitFailure e) {
      System.out.println("failure");
    }
  }

  %strategy upgradeVersion() extends Fail() {
    visit OSTelephone {
      OSTelephone(version) -> { System.out.println("***"); return `OSTelephone(version+1); }
    }
  }

}
