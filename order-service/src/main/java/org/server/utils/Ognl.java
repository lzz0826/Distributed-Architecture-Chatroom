package org.server.utils;

import java.util.Collection;

public final class Ognl {
	
	/**
	 * 判斷字串,集合,陣列不為空且長度大於0
   *
   * 可以在MyBatis
   *     <if test="@org.server.utils.Ognl@check(amount)">
   * 使用
	 *
	 * @param object
	 * @return
	 */
    public static boolean check(Object object) {
        if (object == null) {
            return false;
        } else if (object instanceof String) {
            return ((String) object).trim().length() != 0;
        } else if (object instanceof Collection) {
            return !((Collection) object).isEmpty();
        } else if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            return array.length != 0;
        } else {
            return true;
        }
    }

    /**
     * 判斷不是null
     * 
     * @param object
     * @return
     */
    public static boolean checkNotNull(Object object) {
        if (object == null) {
            return false;
        } else {
            return true;
        }
    }

    private Ognl() {
    }
}
