import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;

import java.util.HashMap;
import java.util.Iterator;

import adt.boulder.*;

public class JBoulderDashPanel extends JPanel {
  private HashMap space, lastDrawnSpace;
  private Image[] img;
  
  JBoulderDashPanel() {
    space = new  HashMap();
    lastDrawnSpace = null;
  }

  public void setBDSpace(HashMap space) {
    this.space = space;
  }
  
  public void setImages(Image[] images) {
    this.img = images;
  }
  
  public void update(Graphics g) {
    System.out.println("update called");
    /*Image im = img[0];
      for(int i=0;i<BoulderDashImagesData.NUM_IMAGES-1;i++) {
      img[i] = img[i+1];
      }
      img[BoulderDashImagesData.NUM_IMAGES-1] = im;
      paint(g);
    */
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.fillRect(0,0,400,400);
      //Graphics2D g2d = (Graphics2D)g;
    if(lastDrawnSpace != null && space.isEmpty()) {
      space = lastDrawnSpace;
    }
    
    Iterator it = space.values().iterator();
      // Warning between North and south,, east and west
    while(it.hasNext()) {
      Bead b = (Bead) it.next();
      int x = b.getPos().getX();
      int y = b.getPos().getY();
      g.drawImage(img[b.getValue()], x*BoulderDashImagesData.IMAGE_SIZE, y*BoulderDashImagesData.IMAGE_SIZE, null);
    }
    lastDrawnSpace = space;
  }

} // Class JBoulderDashPanel
