package BoulderDash;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.image.ImageFilter;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.Timer;

import java.net.URL;

import java.util.HashMap;

import BoulderDash.boulder.*;
import aterm.pure.*;

// BOULDERDASH 2D APPLI BUT ALSO APPLET
// <applet code=BoulderDash2D.class width=400 height=440>
// <param name=fps value="10">
// </applet>

public class BoulderDash2D extends JApplet implements ActionListener {
  private BoulderDashCore core = new BoulderDashCore(new Factory(new PureFactory()));
  private JButton b1 = new JButton("Animation"), 
    b2 = new JButton("Get next");
  private JBoulderDashPanel p = new JBoulderDashPanel();
  private Timer timer;
  private boolean appMode = false;
  
  public void init() {
    String str = null;
    int fps = 0;
      //How many milliseconds between frames?  
    try { 
    str = getParameter("fps");
    if (str != null) {
        fps = Integer.parseInt(str);
      }
    } catch (Exception e) {}
    int delay = (fps > 0) ? (1000 / fps) : 300;
      //Set up a timer that calls this object's action handler.
    timer = new Timer(delay, this);
    timer.setInitialDelay(0);
    timer.setCoalesce(true);
      // button 1
    b1.setEnabled(true);
    b1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          b1.setEnabled(false);
          b2.setEnabled(false);
          startAnimation();
        }
      });
      // button 2
    b2.setEnabled(true);
    b2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          HashMap map = core.getStep();
          if (!map.isEmpty()) {
            p.setBDSpace(map);
            p.repaint();
          } else {
            b1.setEnabled(false);
            b2.setEnabled(false);
          }
        }
      });
      // JBoulderDashPanel 
    p.setBDSpace(core.start());
    p.setImages(loadImages());
    p.setBackground(Color.blue);
    p.setPreferredSize(new Dimension(400, 400));
      // Construct everything
    Container cp = getContentPane();
    cp.setLayout(new FlowLayout());
    cp.add(b1);
    cp.add(b2);
    cp.add(p);
  }

    //Can be invoked from any thread.
  public synchronized void startAnimation() {
      //Start animating!
    if (!timer.isRunning()) {
      timer.start();
    }
  }
  
    //Can be invoked from any thread.
  public synchronized void stopAnimation() {
      //Stop the animating thread.
    if (timer.isRunning()) {
      timer.stop();
    }
  }
  
  public void actionPerformed(ActionEvent e) {
      //Advance the animation frame.
    HashMap map = core.getStep();
    if(!map.isEmpty()) {
      p.setBDSpace(map);
      p.repaint();
    } else {
      stopAnimation();
    }
  }

  public void setAppMode(boolean appMode) {
    this.appMode = appMode;
  }
  
  public static void main(String[] args) {
    BoulderDash2D b = new BoulderDash2D();
    b.runApplicationMode();
  }
  
  public void runApplicationMode() {
    JFrame frame = new JFrame("BoulderDash Application");
    frame.setBackground(Color.green);
    frame.setSize(400, 450);
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {System.exit(0);}
      });
    BoulderDash2D b2d = new BoulderDash2D();
    frame.setContentPane(b2d);
    b2d.setAppMode(true);
    b2d.init();
    b2d.start();
    frame.setVisible(true);
  }
  
  private Image[] loadImages() {
      // Break up source image into individual images
      // First, load the large image
    Image sourceImage;
    MediaTracker tracker = new MediaTracker(this);
    URL url = null;
    if (appMode) {
        sourceImage = this.getToolkit().getImage("BoulderDash/images.gif");
    } else {
      sourceImage = getImage(getCodeBase(),"BoulderDash/images.gif");
    }
    tracker.addImage(sourceImage,0);
    Image[] img;
      // Now break it up into pieces
    img = new Image[BoulderDashImagesData.NUM_IMAGES];
    for (int i=0; i<BoulderDashImagesData.NUM_IMAGES; i++) {
      img[i] = splitSourceImage(sourceImage,BoulderDashImagesData.imgCoord[i]);
      tracker.addImage(img[i], i+1);
    }
    
      // Wait for all of the image pieces to get into memory
    try {
      tracker.waitForAll();
    } catch (InterruptedException e) {
      System.out.println("Boulderdash.init() caught "+e);
      e.printStackTrace();
    }
    return img;
  }
  
  private Image splitSourceImage(Image sourceImage, int[] dimen) {
    ImageFilter filter;
    ImageProducer producer;
    
    filter = new CropImageFilter(dimen[0],dimen[1],dimen[2],dimen[3]);
    producer = new FilteredImageSource(sourceImage.getSource(), filter);
    return (createImage(producer));
  }
  
} // Class BoulderDash2D

