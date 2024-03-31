package dbfKit;


import baseKit.StrKit;
import dbfKit.pojo.bo.DBFField;
import dbfKit.pojo.dto.DbfField;
import dbfKit.pojo.dto.DbfInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * dbf工具类
 *
 * @Author zhangqx
 * @Date 2023/3/15
 **/
@Slf4j
public class DbfKit {

    public static final String UTF8 = "UTF-8";

    /**
     * description: 解析dbf文件（指定字符集）
     * create time: 2023/3/15 14:15
     * author: zhangqx
     * @return com.dsfa.platform.sdk.common.kit.dbf.pojo.dto.DbfInfo dbf信息对象
     * @Param: file dbf文件
     * @Param: charsetName 字符集（默认 UTF-8）
     **/
    public static DbfInfo readDbfFile(File file, String charsetName){

        DbfInfo dbfInfo = null;
        if (!file.exists()) {
            log.error("文件路径：{}，该文件不存在！", file.getAbsolutePath());
            return null;
        }
        try (
                FileInputStream inputStream = new FileInputStream(file);
                DBFReader dbfReader = new DBFReader(inputStream, Charset.forName(StrKit.isBlank(charsetName) ? UTF8 : charsetName), false)
        ) {
            log.debug("开始读取DBF文件，文件路径为: {}", file.getAbsolutePath());

            dbfInfo = buildDbfInfo(dbfReader);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbfInfo;
    }
    public static DbfInfo readDbfFile(InputStream is, String charsetName){
        DbfInfo dbfInfo = null;
        try (
                DBFReader dbfReader = new DBFReader(is, Charset.forName(StrKit.isBlank(charsetName) ? UTF8 : charsetName), false)
        ) {
            dbfInfo = buildDbfInfo(dbfReader);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbfInfo;
    }



    /**
     *  解析dbf文件（依照dbf文件内字符集解析）
     * @param file: dbf文件
     * @return DbfInfo dbf信息对象
     * @author zhangqx
     * @date 2023/3/29 18:02
     */
    public static DbfInfo readDbfFile(File file){

        DbfInfo dbfInfo = null;
        if (!file.exists()) {
            log.error("文件路径：{}，该文件不存在！", file.getAbsolutePath());
            return null;
        }
        try (
                FileInputStream inputStream = new FileInputStream(file);
                DBFReader dbfReader = new DBFReader(inputStream, null, false)
        ) {
            log.debug("开始读取DBF文件，文件路径为: {}", file.getAbsolutePath());

            dbfInfo = buildDbfInfo(dbfReader);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbfInfo;
    }
    /**
     * 根据dbf文件信息构建dbf对象
     *
     * @param dbfReader
     * @return
     */
    private static DbfInfo buildDbfInfo(DBFReader dbfReader) {
        DbfInfo dbfInfo = new DbfInfo();

        int recordCount = dbfReader.getRecordCount();
        List<List<Object>> dataList = new ArrayList<>(recordCount + 1);
        Object[] rowVales;
        while ((rowVales = dbfReader.nextRecord()) != null) {
            List<Object> rowList = new ArrayList<>(rowVales.length);
            for (int i = 0; i < rowVales.length; i++) {
                Object rowVale = rowVales[i];
                rowList.add(rowVale);
            }
            dataList.add(rowList);
        }
        //记录dbf文件字段信息
        buildDbfFields(dbfInfo, dbfReader);
        dbfInfo.setRecordCount(recordCount);
        dbfInfo.setDataList(dataList);

        return dbfInfo;
    }

    private static void buildDbfFields(DbfInfo dbfInfo, DBFReader dbfReader) {
        int fieldCount = dbfReader.getFieldCount();
        List<DbfField> dbfFields = new ArrayList<>(fieldCount);
        for (int i = 0; i < fieldCount; i++) {
            DBFField field = dbfReader.getField(i);
            DbfField dbfField = new DbfField();
            dbfField.setDbfType(field.getType());
            dbfField.setLength(field.getLength());
            dbfField.setName(field.getName());
            dbfFields.add(dbfField);
        }
        dbfInfo.setFields(dbfFields);
        dbfInfo.setFieldCount(fieldCount);
    }
}
