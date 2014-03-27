package tom.library.theory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import org.junit.contrib.theories.Theories;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import tom.library.enumerator.Enumeration;

public final class TomCheck extends Theories {

	private static HashMap<Type, Enumeration<?>> enumerations;

	public TomCheck(Class<?> klass) throws InitializationError {
		super(klass);
	}

	public static Enumeration<?> get(Type type) {
		return enumerations.get(type);
	}

	@Override
	public void run(RunNotifier notifier) {
		TomCheck.initEnumerations(getTestClass().getJavaClass());
		super.run(notifier);
	}

	@Override
	protected void collectInitializationErrors(List<Throwable> errors) {
		validateEnumDecl(errors);
		super.collectInitializationErrors(errors);
	}

	private void validateEnumDecl(List<Throwable> errors) {
		Field[] fields = getTestClass().getJavaClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getAnnotation(Enum.class) == null) {
				continue;
			}
			if (!Modifier.isStatic(field.getModifiers())) {
				errors.add(new Error("Enum field " + field.getName() + " must be static"));
			}
			if (!Modifier.isPublic(field.getModifiers())) {
				errors.add(new Error("Enum field " + field.getName() + " must be public"));
			}
			if (! field.getType().equals(Enumeration.class)) {
				errors.add(new Error("Enum field " + field.getName() + " must be of type Enumeration<?>"));
			}
		}
	}



	public static void initEnumerations(Class<?> testclass) {
		enumerations = new HashMap<Type, Enumeration<?>>();
		Field[] fields = testclass.getFields();
		for(Field field: fields) {
			if (field.getAnnotation(Enum.class) != null) {
				try {
					ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
					Type dataType = fieldType.getActualTypeArguments()[0];
					enumerations.put(dataType, (Enumeration<?>) field.get(null));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}

	}


}
