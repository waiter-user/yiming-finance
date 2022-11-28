package com.java.ymjr.core.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * EasyExcel支持LocalDate类型的转换器
 */
public class LocalDateConverter implements Converter<LocalDate> {
    //格式化字符串
    private final String   PATTERN="yyyy-MM-dd";
    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 把Excel单元格数据转换为LocalDate类型
     * @param cellData  单元格数据
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        LocalDate localDate = LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(PATTERN));
        return localDate;
    }

    /**
     * 把LocalDate类型转换为Excel单元格数据
     * @param value
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public WriteCellData<?> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        WriteCellData<?>  writeCellData=new WriteCellData<>( value.format(DateTimeFormatter.ofPattern(PATTERN)));
        return writeCellData;
    }
}
