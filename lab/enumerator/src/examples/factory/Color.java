package examples.factory;

import examples.factory.generation.EnumerateStatic;

@EnumerateStatic
public class Color {
	public static final Color RED = new Color(1);
	public static final Color GREEN = new Color(2);
	public static final Color BLUE = new Color(3);

	// possible values; 1,2 3
	private int color;

	public Color(int color) {
		this.color = color;
	}

	public Color() {
		this.color = 1;
	}

	@Override
	public String toString() {
		String res = "";
		switch (color) {
		case 1:
			res = "R";
			break;
		case 2:
			res = "G";
			break;
		case 3:
			res = "B";
			break;
		}
		return res;
	}

}
