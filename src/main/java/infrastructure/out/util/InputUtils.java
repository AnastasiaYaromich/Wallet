package infrastructure.out.util;

import org.apache.commons.lang.StringUtils;
import java.util.List;

public class InputUtils {
    /**
     * The method check if user enter a value for property name and return a result.
     * @param propertyName the name of property.
     * @param value the value of property.
     * @return
     * false - if user doesn't enter a value for property.
     * true - if user enter a value for property.
     */
    public static boolean isEmpty(String propertyName, String value) {
        boolean isEmpty = StringUtils.isBlank(value);
        if (isEmpty) {
            System.out.println(String.format("Invalid %s - cannot be empty", propertyName));
        }
        return isEmpty;
    }

    /**
     * The method check if user enter a value for property name and return a result.
     * @param propertyName the name of property.
     * @param value the value of property.
     * @return
     * false - if user doesn't enter a value for property.
     * true - if user enter a value for property.
     */
    public static boolean isEmpty(String propertyName, Object value) {
        boolean isEmpty = value == null;
        if (isEmpty) {
            System.out.println(String.format("Invalid %s - cannot be empty", propertyName));
        }
        return isEmpty;
    }

}
