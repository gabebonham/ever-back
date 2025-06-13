package hedge.ever.common.mappers;

import hedge.ever.common.interfaces.NamedEnum;

import java.lang.reflect.Field;
import java.util.List;

public class GeneralMapper {

    public static <S, T> T map(S source, Class<T> targetClass) {

        try {
            T target = targetClass
                    .getDeclaredConstructor()
                    .newInstance();
            Field[] targetFields = getAllFields(targetClass);
            Field[] sourceFields = getAllFields(source.getClass());

            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true);
                Object value = sourceField.get(source);

                for (Field targetField : targetFields) {
                    if (sourceField
                            .getName()
                            .equals(targetField.getName())) {
                        targetField.setAccessible(true);
                        if (value instanceof NamedEnum && targetField
                                .getType()
                                .equals(String.class)) {
                            value = ((NamedEnum) value).getName();
                        }

                        if (value instanceof String && targetField
                                .getType()
                                .isEnum()) {
                            value = getEnumFromName(targetField.getType(),
                                    (String) value);
                        }

                        if (value == null || targetField
                                .getType()
                                .isAssignableFrom(value.getClass())) {
                            targetField.set(target,
                                    value);
                        }
                        break;
                    }
                }
            }

            return target;
        } catch (Exception e) {
            throw new RuntimeException("Mapping error: " + e.getMessage(),
                    e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum<E> & NamedEnum> E getEnumFromName(Class<?> enumClass, String name) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("Class " + enumClass.getName() + " is not an enum");
        }

        for (E enumConstant : (E[]) enumClass.getEnumConstants()) {
            if (enumConstant
                    .getName()
                    .equalsIgnoreCase(name)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("No enum constant found for name: " + name);
    }

    private static Field[] getAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (clazz.getSuperclass() != null) {
            Field[] parentFields = getAllFields(clazz.getSuperclass());
            Field[] allFields = new Field[fields.length + parentFields.length];
            System.arraycopy(fields,
                    0,
                    allFields,
                    0,
                    fields.length);
            System.arraycopy(parentFields,
                    0,
                    allFields,
                    fields.length,
                    parentFields.length);
            return allFields;
        }
        return fields;
    }

    public static <S, T> List<T> map(List<S> list, Class<T> targetClass) {
        return list
                .stream()
                .map(i -> GeneralMapper.map(i,
                        targetClass))
                .toList();
    }
}