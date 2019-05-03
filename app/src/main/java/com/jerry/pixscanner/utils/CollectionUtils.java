package com.jerry.pixscanner.utils;

import java.util.Collection;
import java.util.List;

/**
 * Created by wzl on 30/11/2016.类说明：集合工具类
 */

public class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 获取list的最后一个元素
     * <strong>ATTENTION</strong>: 调用前请确认list非空
     */
    public static <T> T getLastOne(List<T> list) {
        return list.get(list.size() - 1);
    }

    /**
     * 位置相对应的元素是不是在集合内
     *
     * @param position 元素位置
     */
    public static boolean isItemInCollection(int position, Collection collection) {
        return !isEmpty(collection) && position > -1 && position < collection.size();
    }
}
