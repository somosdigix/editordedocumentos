package utils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectionUtils {

	public static Map<String, Object> getFieldsAndValues(Object object) {
		Map<String, Object> mapFieldNameAndValueObject = new LinkedHashMap<>();
		try {
			Class<? extends Object> objectClass = object.getClass();
			mapearCamposDoObjeto(object, mapFieldNameAndValueObject, objectClass);
			mapearSuperClass(object, mapFieldNameAndValueObject, object.getClass().getSuperclass());
		} catch (Exception excecao) {
			new RuntimeException(excecao);
		}
		return mapFieldNameAndValueObject;
	}

	private static void mapearSuperClass(Object object, Map<String, Object> mapFieldNameAndValueObject, Class<?> superclass) throws IllegalAccessException {
		if (superclass != null) {
			mapearCamposDoObjeto(object, mapFieldNameAndValueObject, superclass);
			mapearSuperClass(object, mapFieldNameAndValueObject, superclass.getSuperclass());
		}
	}

	private static void mapearCamposDoObjeto(Object object, Map<String, Object> mapFieldNameAndValueObject, Class<? extends Object> objectClass) throws IllegalAccessException {
		for (Field field : objectClass.getDeclaredFields()) {
			field.setAccessible(true);
			mapFieldNameAndValueObject.put(field.getName(), field.get(object));
		}
	}
}
