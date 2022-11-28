package com.java.ymjr.qnoss.utils;

import java.util.UUID;

public class StringUtil {
    /**
     * 根据文件旧名称获取新文件名
     *
     * @param oldName
     * @return
     */
    public static String getNewName(String oldName) {
        //获取扩展名，比如.jpg
        String ext = oldName.substring(oldName.lastIndexOf("."));
        //通过UUID生成新文件名
        String name = UUID.randomUUID().toString();
        name = name.replaceAll("-", "");
        return name + ext;

    }
}
