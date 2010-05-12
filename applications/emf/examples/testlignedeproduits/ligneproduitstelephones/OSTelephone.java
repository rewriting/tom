package ligneproduitstelephones;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.util.Enumerator;

/**
 * @model
 */
public enum OSTelephone implements Enumerator {

 /**
   * @model name="Android"
   */
  ANDROID(0, "Android", "Android"),

  /**
   * @model name="MacOS"
   */
  MACOS(1, "MACOS", "MACOS"),

  /**
   * @model name="WindowsCE"
   */
  WINDOWSCE(2, "WINDOWSCE", "WINDOWSCE"),
  
  /**
   * @model name="BlackBerry"
   */
  BLACKBERRY(3, "BLACKBERRY", "BLACKBERRY"),
  
  /**
   * @model name="Symbian"
   */
  SYMBIAN(4, "Symbian", "Symbian"),
  
  /**
   * @model name="Other"
   */
  OTHER(5, "Other", "Other");

		/**
	 * The '<em><b>Android</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Android</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ANDROID
	 * @model name="Android"
	 * @generated
	 * @ordered
	 */
	public static final int ANDROID_VALUE = 0;
		/**
	 * The '<em><b>MACOS</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MACOS</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MACOS
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MACOS_VALUE = 1;
		/**
	 * The '<em><b>WINDOWSCE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>WINDOWSCE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WINDOWSCE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int WINDOWSCE_VALUE = 2;
		/**
	 * The '<em><b>BLACKBERRY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BLACKBERRY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BLACKBERRY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int BLACKBERRY_VALUE = 3;
		/**
	 * The '<em><b>Symbian</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Symbian</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SYMBIAN
	 * @model name="Symbian"
	 * @generated
	 * @ordered
	 */
	public static final int SYMBIAN_VALUE = 4;
		/**
	 * The '<em><b>Other</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Other</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #OTHER
	 * @model name="Other"
	 * @generated
	 * @ordered
	 */
	public static final int OTHER_VALUE = 5;
		/**
	 * An array of all the '<em><b>OS Telephone</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final OSTelephone[] VALUES_ARRAY =
		new OSTelephone[] {
			ANDROID,
			MACOS,
			WINDOWSCE,
			BLACKBERRY,
			SYMBIAN,
			OTHER,
		};
		/**
	 * A public read-only list of all the '<em><b>OS Telephone</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<OSTelephone> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

		/**
	 * Returns the '<em><b>OS Telephone</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OSTelephone get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OSTelephone result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

		/**
	 * Returns the '<em><b>OS Telephone</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OSTelephone getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OSTelephone result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

		/**
	 * Returns the '<em><b>OS Telephone</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OSTelephone get(int value) {
		switch (value) {
			case ANDROID_VALUE: return ANDROID;
			case MACOS_VALUE: return MACOS;
			case WINDOWSCE_VALUE: return WINDOWSCE;
			case BLACKBERRY_VALUE: return BLACKBERRY;
			case SYMBIAN_VALUE: return SYMBIAN;
			case OTHER_VALUE: return OTHER;
		}
		return null;
	}

		/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;
		/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;
		/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

		/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private OSTelephone(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

		/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

		/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

		/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

		/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
}