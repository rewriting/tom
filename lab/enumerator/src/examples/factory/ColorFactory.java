package examples.factory;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class ColorFactory {

	public static final Enumeration<Color> getEnumeration() {

		Enumeration<Color> enumRes = null;

		final Enumeration<Color> emptyEnum = Enumeration.singleton(null);

		final Enumeration<Color> redEnum = Enumeration.singleton(Color.RED);

		final Enumeration<Color> greeEnum = Enumeration.singleton(Color.GREEN);

		final Enumeration<Color> blueEnum = Enumeration.singleton(Color.BLUE);


//		enumRes = emptyEnum.plus(redEnum.pay()).plus(greeEnum.pay(2)).plus(blueEnum.pay(3));

		enumRes = blueEnum;
		enumRes = enumRes.pay().plus(greeEnum);
		enumRes = enumRes.pay().plus(redEnum);
//		enumRes = enumRes.pay().plus(emptyEnum);
		

		return enumRes;
	}

}
