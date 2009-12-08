package modele;

	import java.awt.Color;
	import java.util.Random;
	public class RandomColor
	{
	    
	    private Random rand;

	    /**
	     * Constructor for objects of class RandomColor initializes the
	     * random number generator
	     */
	    public RandomColor()
	    {
	        rand = new Random();
	    }

	    /**
	     * randomColor returns a pseudorandom Color
	     * 
	     * @return a pseudorandom Color
	     */
	    public Color randomColor()
	    {
	        return(new Color(rand.nextInt(256), 
	                         rand.nextInt(256),
	                         rand.nextInt(256)));
	    }
	    
	    /**
	     * randomGray returns a pseudorandom gray Color
	     * 
	     * @return a pseudorandom Color
	     */
	    public Color randomGray()
	    {
	        int intensity = rand.nextInt(256);
	        return(new Color(intensity,intensity,intensity));
	    }
	}
