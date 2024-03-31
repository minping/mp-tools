package dbfKit.pojo.dto;

import dbfKit.enums.DBFDataType;
import lombok.Data;

/**
 * dbf文件的字段信息
 * @Author zhangqx
 * @Date 2023/3/15
 **/
@Data
public class DbfField {
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private DBFDataType dbfType;
    /**
     * 字段长度
     */
    private int length;


}
