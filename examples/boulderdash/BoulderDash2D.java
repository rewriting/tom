/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package boulderdash;

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

import boulderdash.boulder.*;
import aterm.pure.*;

// BOULDERDASH 2D APPLI BUT ALSO APPLET
// <applet code=BoulderDash2D.class width=400 height=440>
// <param name=fps value="10">
// </applet>

public class BoulderDash2D extends JApplet implements ActionListener {
  private BoulderDashCore core = new BoulderDashCore();
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
        sourceImage = this.getToolkit().getImage("boulderdash/images.gif");
    } else {
      sourceImage = getImage(this.getClass().getResource("images.gif"));
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

