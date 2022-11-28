package com.java.ymjr.core.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.java.ymjr.core.converter.LocalDateConverter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EasyExcelUtil {
    /**
     * 导入数据
     *
     * @param stream
     * @param tClass
     * @param sheetNo
     * @param <T>
     * @return
     */
    public static <T> List<T> readExcel(InputStream stream, Class<T> tClass, int sheetNo) {
        List<T> list = new ArrayList<>();
        EasyExcel.read(stream)
                //反射获取类型
                .head(tClass)
                //读取的excel的sheet索引
                .sheet(sheetNo)
                //注册监听器
                .registerReadListener(new AnalysisEventListener<T>() {
                    @Override
                    public void invoke(T t, AnalysisContext analysisContext) {
                        list.add(t);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        System.out.println("读取数据完毕");
                    }
                }).registerConverter(new LocalDateConverter()).doRead();
        return list;
    }

    /**
     * 导出Excel方法
     *
     * @param response
     * @param clazz
     * @param sheetName  Sheet名称
     * @param list
     */

    public static void exportData(HttpServletResponse response, Class clazz, String sheetName, List list) {
        ServletOutputStream outputStream = null;
        try {
            //设置响应的内容类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            // response.setCharacterEncoding();
            //设置响应头，弹出一个下载对话框
            String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName + ".xlsx");
            // response.setHeader("Content-Disposition","attachment;fileName="+System.currentTimeMillis()+".xlsx");
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, clazz).registerConverter(new LocalDateConverter())
                    .sheet(sheetName).doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
