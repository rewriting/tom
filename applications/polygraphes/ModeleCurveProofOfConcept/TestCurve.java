
	package modele;

	import java.awt.BasicStroke;
	import java.awt.Dimension;
	import java.awt.Graphics;
	import java.awt.Graphics2D;
	import java.awt.RenderingHints;
	import java.awt.geom.*;
	import java.util.ArrayList;
	import java.util.List;
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.SwingUtilities;

	public class TestCurve extends JPanel
	{
	  private static final float STROKE_WIDTH = 8.0f;
	  
	  public TestCurve()
	  {
//
	  }
	  
	  @Override
	  protected void paintComponent(Graphics g)
	  {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D)g;
	    g2.setStroke(new BasicStroke(
	        STROKE_WIDTH, 
	        BasicStroke.CAP_ROUND, 
	        BasicStroke.JOIN_ROUND));
	    g2.setRenderingHint(
	        RenderingHints.KEY_ANTIALIASING, 
	        RenderingHints.VALUE_ANTIALIAS_ON);
//	    // create new CubicCurve2D.Double
//	    CubicCurve2D c = new CubicCurve2D.Double();
//	    // draw CubicCurve2D.Double with set coordinates
//	    c.setCurve(0, 0, 0, 100, 100, 0, 100, 100);
	    
	    QuadCurve2D d = new QuadCurve2D.Double();
	    // draw CubicCurve2D.Double with set coordinates
	    d.setCurve(0, 0, 0, 100,100, 100);
	    g2.draw(d);
	    QuadCurve2D e = new QuadCurve2D.Double();
	    // draw CubicCurve2D.Double with set coordinates
	    d.setCurve(100, 100, 200, 100,200, 200);
	    g2.draw(d);
  }
	  
	  public static void main(String[] args)
	  {
	    SwingUtilities.invokeLater(new Runnable()
	    {
	      public void run()
	      {
	        createAndShowGUI();
	      }
	    });
	  }

	  private static void createAndShowGUI()
	  {
	    TestCurve test = new TestCurve();
	    test.setPreferredSize(new Dimension(400,400));
	    
	    JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().add(test);
	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	  }
	}
