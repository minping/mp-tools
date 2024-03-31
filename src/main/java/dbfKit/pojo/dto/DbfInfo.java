package dbfKit.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 读取dbf的实体类
 * @Date 2023/3/15
 **/
@Data
public class DbfInfo {
    /**
     * 数据
     */
    private List<List<Object>> dataList;
    /**
     * 字段（列信息）
     */
    private List<DbfField> fields;
    /**
     * 数据行数
     */
    private int recordCount;
    /**
     * 字段数量
     */
    private int fieldCount;

}
