import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import adt.*;
// BOULDERDASH 2D APPLI BUT ALSO APPLET
// <applet code=BoulderDash2D.class width=400 height=440>
// </applet>

public class BoulderDash2D extends JApplet {
  private BoulderDashCore core = new BoulderDashCore(new TermFactory(16));
  private JButton b1 = new JButton("Animation"), 
    b2 = new JButton("Get next");
  private BoulderDashPanel p = new BoulderDashPanel();
  
  public void init() {
    p.setBDSpace(core.start());
    
    Container cp = getContentPane();
    cp.setLayout(new FlowLayout());
    b1.setEnabled(true);
    b1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          b1.setEnabled(false);
          b2.setEnabled(false);
          HashMap map = core.getStep();
          while(map != null) {
            p.setBDSpace(map);
            p.repaint();
            /*try{
              System.out.println("...");
              Thread.sleep(1000);
              } catch(InterruptedException ex) {
              System.out.println("InterruptedException catched during sleep"+ex.getStackTrace());
              }*/
            map = core.getStep();
          }
        }
      });
    b2.setEnabled(true);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          HashMap map = core.getStep();
          if (map != null) {
            p.setBDSpace(map);
            p.repaint();
          } else {
            b1.setEnabled(false);
            b2.setEnabled(false);
          }
        }
      });
    p.setBackground(Color.blue);
    p.setPreferredSize(new Dimension(400, 400));
    cp.add(b1);
    cp.add(b2);
    cp.add(p);
  }
  
  public static void main(String[] args) {
    BoulderDash2D b = new BoulderDash2D();
    b.run();
  }
  
  public void run() {
    JFrame frame = new JFrame("BoulderDash Application");
    frame.setBackground(Color.green);
    p.setBackground(Color.blue);
    frame.setSize(400, 420);
    frame.setContentPane(p);
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
    p.setBDSpace(core.start());
    frame.setVisible(true);
    HashMap map =new HashMap();
    while(map != null) {
      frame.repaint();
      /*try 
        {
        System.out.println("OKKKKKKKKKKKKKKKKKKKKK");
          // ask currently executing Thread to sleep for 1000ms
          Thread.sleep(1000); 
          }
      catch(InterruptedException e)   
      {      
      System.out.println("Sleep interrupted:"+e);      
      }
      */
        //test.updateImg();
      map = core.getStep();
      p.setBDSpace(map);
    }
  }
}

class BoulderDashPanel extends JPanel {
  private HashMap space;
  private static final int NUM_IMAGES = 15;
  private BufferedImage bufferedImage;
  private Image[] img;
  private static int[][] imgCoord =
  {{  0, 0,20,20},   //0: Space
   { 20, 0,20,20},   //1: Dirt
   { 40, 0,20,20},   //2: Titanium Wall
   {  0,20,20,20},   //3: Brick Wall
   { 20,20,20,20},   //4: Magic Wall
   { 40,20,20,20},   //5: Player
   {  0,40,20,20},   //6: Rock
   { 20,40,20,20},   //7: Diamond
   { 40,40,20,20},   //8: Explosion
   {  0,60,20,20},   //9: Butterfly
   { 20,60,20,20},   //10: Firefly
   { 40,60,20,20},   //11; Amoeba
   {  0,80,20,20},   //12: Closed Exit
   { 20,80,20,20},   //13: Open Exit
   { 40,80,20,20}};  //14: Robot (not programmed)
  
  BoulderDashPanel() {
    space = new  HashMap();
    loadImages();
  }

  public void setBDSpace(HashMap space) {
    this.space = space;
  }
  
  private void loadImages() {
      // Break up source image into individual images
      // First, load the large image
    Image sourceImage;
    MediaTracker tracker = new MediaTracker(this);
    sourceImage = getToolkit().getImage("images.gif");
    tracker.addImage(sourceImage,0);
    
      // Now break it up into pieces
    img = new Image[NUM_IMAGES];
    for (int i=0; i<NUM_IMAGES; i++) {
      img[i] = splitSourceImage(sourceImage,imgCoord[i]);
      tracker.addImage(img[i], i+1);
    }
    
      // Wait for all of the image pieces to get into memory
    try {
      tracker.waitForAll();
    } catch (InterruptedException e) {
      System.out.println("Boulderdash.init() caught "+e);
      e.printStackTrace();
    }
  }
  
  private Image splitSourceImage(Image sourceImage, int[] dimen) {
    ImageFilter filter;
    ImageProducer producer;
    
    filter = new CropImageFilter(dimen[0],dimen[1],dimen[2],dimen[3]);
    producer = new FilteredImageSource(sourceImage.getSource(), filter);
    return (createImage(producer));
  }
  
  public void update(Graphics g) {
    System.out.println("update called");
    Image im = img[0];
    for(int i=0;i<NUM_IMAGES-1;i++) {
      img[i] = img[i+1];
    }
    img[NUM_IMAGES-1] = im;
    paint(g);
  }
  
  public void updateImg() {
      //System.out.println("updateImg called");
    Image im = img[0];
    for(int i=0;i<NUM_IMAGES-1;i++) {
      img[i] = img[i+1];
    }
    img[NUM_IMAGES-1] = im;
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
      //System.out.println("paintComponent called");
    g.fillRect(0,0,400,400);
    Graphics2D g2d = (Graphics2D)g;
    if(space == null || space.isEmpty()) {
        // Default: just an animation
      for (int x=0;x<400;x+=20) {
        for (int y=0;y<400;y+=20) {
          g2d.drawImage(img[(x/20+y/20)%15], x, y, null);
        }
      }
    } else {
      Iterator it = space.values().iterator();
        // Warning between North and south,, east and west
      while(it.hasNext()) {
        Bead b = (Bead) it.next();
        int x = b.getPos().getX().intValue();
        int y = b.getPos().getY().intValue();
        g2d.drawImage(img[b.getValue().intValue()], x*20, y*20, null);
      }
    }
  }
}
  
