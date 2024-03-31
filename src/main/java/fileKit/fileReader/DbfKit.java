package fileKit.fileReader;

import baseKit.StrKit;
import dbfKit.exception.DBFException;
import dbfKit.pojo.dto.DbfInfo;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class DbfKit {
    /**
     * 解析dbf
     *
     * @param is     是
     * @param encode 编码
     * @return {@link String}
     * @throws DBFException dbfexception
     */
    public static String readDbfContent(InputStream is, String encode) throws DBFException {
        return readDbfContent(is, "\n", "\t", encode);
    }

    /**
     * 解析dbf
     *
     * @param file   文件
     * @param encode 编码
     * @return {@link String}
     * @throws DBFException dbfexception
     */
    public static String readDbfContent(File file, String encode) throws DBFException {
        return readDbfContent(file, "\n", "\t", encode);
    }

    /**
     * 解析dbf
     *
     * @param is              是
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @param encode          编码
     * @return {@link String}
     * @throws DBFException dbfexception
     */
    public static String readDbfContent(InputStream is, String rowSeparator, String columnSeparator, String encode) throws DBFException {
        StringBuilder res = new StringBuilder();
        DbfInfo info = dbfKit.DbfKit.readDbfFile(is, encode);
        readContent(rowSeparator, columnSeparator, res, info);
        return res.toString();
    }

    /**
     * 解析dbf
     *
     * @param file            文件
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @param encode          编码
     * @return {@link String}
     * @throws DBFException dbfexception
     */
    public static String readDbfContent(File file, String rowSeparator, String columnSeparator, String encode) throws DBFException {
        StringBuilder res = new StringBuilder();
        DbfInfo info = dbfKit.DbfKit.readDbfFile(file, encode);
        readContent(rowSeparator, columnSeparator, res, info);
        return res.toString();
    }


    /**
     * 解析dbf
     *
     * @param file            文件
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @return {@link String}
     * @throws DBFException dbfexception
     */
    public static String readDbfContent(File file, String rowSeparator, String columnSeparator) throws DBFException {
        StringBuilder res = new StringBuilder();
        DbfInfo info = dbfKit.DbfKit.readDbfFile(file, "GBK");
        readContent(rowSeparator, columnSeparator, res, info);
        return res.toString();
    }

    /**
     * 阅读内容
     *
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @param res             res
     * @param info            信息
     */
    private static void readContent(String rowSeparator, String columnSeparator, StringBuilder res, DbfInfo info) {
        if (info == null) {
            return;
        }
        List<List<Object>> dataList = info.getDataList();
        for (List<Object> objects : dataList) {
            StringBuilder sb = new StringBuilder();
            for (Object object : objects) {
                if (object != null && StrKit.isNotBlank(object.toString())) {
                    sb.append(object).append(columnSeparator);
                }
            }
            if (sb.length() > 0) {
                res.append(sb).append(rowSeparator);
            }
        }
    }

    /**
     * 解析dbf
     *
     * @param file 文件
     * @return {@link String}
     * @throws DBFException dbfexception
     */
    public static String readDbfContent(File file) throws DBFException {
        return readDbfContent(file, "\n", "\t");
    }

    /**
     * 解析dbf
     *
     * @param is              是
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @return {@link String}
     * @throws DBFException dbfexception
     */
    public static String readDbfContent(InputStream is, String rowSeparator, String columnSeparator) throws DBFException {
        StringBuilder res = new StringBuilder();
        DbfInfo info = dbfKit.DbfKit.readDbfFile(is, "GBK");
        readContent(rowSeparator, columnSeparator, res, info);
        return res.toString();
    }

    /**
     * 解析dbf
     *
     * @param is 是
     * @return {@link String}
     * @throws DBFException dbfexception
     */
    public static String readDbfContent(InputStream is) throws DBFException {
        StringBuilder res = new StringBuilder();
        DbfInfo info = dbfKit.DbfKit.readDbfFile(is, "GBK");
        readContent("\n", "\t", res, info);
        return res.toString();
    }
}
