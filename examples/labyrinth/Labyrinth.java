/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
package labyrinth;

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

import tom.library.sl.VisitFailure;

public class Labyrinth extends JApplet implements ActionListener {
    private LabyrinthCore core = new LabyrinthCore();

    private JButton btnAnimation = new JButton("Animation");

    private JButton btnGetNext = new JButton("Get next");

    private JButton btnStopAnimation = new JButton("Stop");

    private JLabyrinthPanel p = new JLabyrinthPanel();

    private Timer timer;

    private boolean appMode = false;

    public void init() {
        String str = null;
        int fps = 0;
        // How many milliseconds between frames?
        try {
            str = getParameter("fps");
            if (str != null) {
                fps = Integer.parseInt(str);
            }
        } catch (Exception e) {
        }
        int delay = (fps > 0) ? (1000 / fps) : 100;
        // Set up a timer that calls this object's action handler.
        timer = new Timer(delay, this);
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        // button 1
        btnAnimation.setEnabled(true);
        btnAnimation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAnimation.setEnabled(false);
                btnGetNext.setEnabled(false);
                btnStopAnimation.setEnabled(true);
                startAnimation();
            }
        });
        btnStopAnimation.setEnabled(false);
        btnStopAnimation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAnimation.setEnabled(true);
                btnGetNext.setEnabled(true);
                stopAnimation();
            }
        });
        // button 2
        btnGetNext.setEnabled(true);
        btnGetNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean result = false;
                try {
                    result = core.getStep();
                } catch (VisitFailure e1) {
                    System.out.println("Exception:");
                    e1.printStackTrace();
                }
                p.setBDSpace(core.getSpace());
                p.repaint();
                if (result) {
                    btnAnimation.setEnabled(false);
                    btnStopAnimation.setEnabled(false);
                    btnGetNext.setEnabled(false);
                }
            }
        });
        // JLabyrinthPanel
        p.setBDSpace(core.start());
        p.setImages(loadImages());
        p.setBackground(Color.blue);
        p.setPreferredSize(new Dimension(400, 400));
        // Construct everything
        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(btnAnimation);
        cp.add(btnGetNext);
        cp.add(btnStopAnimation);
        cp.add(p);
    }

    // Can be invoked from any thread.
    public synchronized void startAnimation() {
        // Start animating!
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    // Can be invoked from any thread.
    public synchronized void stopAnimation() {
        // Stop the animating thread.
        if (timer.isRunning()) {
            timer.stop();
        }
        btnAnimation.setEnabled(true);
        btnGetNext.setEnabled(true);
        btnStopAnimation.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        // Advance the animation frame.
        try {
            boolean result = core.getStep();
            p.setBDSpace(core.getSpace());
            p.repaint();
            if (result) {   
                stopAnimation();
                btnAnimation.setEnabled(false);
                btnGetNext.setEnabled(false);
                btnStopAnimation.setEnabled(false);
            }
        } catch (VisitFailure ex) {
            System.out.println("Exception:" + ex.getMessage());
        }
    }

    public void setAppMode(boolean appMode) {
        this.appMode = appMode;
    }

    public static void main(String[] args) {
        Labyrinth b = new Labyrinth();
        b.runApplicationMode();
    }

    public void runApplicationMode() {
        JFrame frame = new JFrame("Labyrinth Application");
        frame.setBackground(Color.green);
        frame.setSize(400, 450);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Labyrinth b2d = new Labyrinth();
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
        if (appMode) {
            sourceImage = this.getToolkit().getImage(
            "labyrinth/images/images.gif");
        } else {
            sourceImage = getImage(this.getClass().getResource(
            "images/images.gif"));
        }
        tracker.addImage(sourceImage, 0);
        Image[] img;
        // Now break it up into pieces
        img = new Image[LabyrinthImagesData.NUM_IMAGES];
        for (int i = 0; i < LabyrinthImagesData.NUM_IMAGES; i++) {
            img[i] = splitSourceImage(sourceImage,
                    LabyrinthImagesData.imgCoord[i]);
            tracker.addImage(img[i], i + 1);
        }

        // Wait for all of the image pieces to get into memory
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("Labyrinth.init() caught " + e);
            e.printStackTrace();
        }
        return img;
    }

    private Image splitSourceImage(Image sourceImage, int[] dimen) {
        ImageFilter filter;
        ImageProducer producer;

        filter = new CropImageFilter(dimen[0], dimen[1], dimen[2], dimen[3]);
        producer = new FilteredImageSource(sourceImage.getSource(), filter);
        return (createImage(producer));
    }

}
