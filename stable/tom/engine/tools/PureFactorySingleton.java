package jtom.tools;

import aterm.pure.PureFactory;

public class PureFactorySingleton {

	private PureFactorySingleton() {}

	private static PureFactory instance = null;

	public static PureFactory getInstance() {
		if (instance == null) {
			instance = new PureFactory();
		}
		return instance;
	}

}