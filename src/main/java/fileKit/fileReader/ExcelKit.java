package fileKit.fileReader;

import baseKit.StrKit;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelKit {
    /**
     * 解析excel
     *
     * @param is              是
     * @param rowSeparator    行隔离膜
     * @param columnSeparator 列分离器
     * @return {@link List}<{@link String}>
     * @throws IOException ioexception
     */
    public static List<String> readContent(InputStream is, String rowSeparator, String columnSeparator) throws Exception {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (true) {
            try {
                StringBuilder sb = iterInSheet(is, i, null, rowSeparator, columnSeparator, "UTF-8");
                if (sb.length() == 0) {
                    break;
                }
                if (sb.length() > 0) {
                    res.add(sb.toString());
                }
            } catch (EmptyFileException e) {
                break;
            }

        }
        return res;
    }

    /**
     * 解析excel
     *
     * @param is 是
     * @return {@link List}<{@link String}>
     * @throws IOException ioexception
     */
    public static List<String> readContent(InputStream is) throws Exception {
        return readContent(is, "\n", "\t");
    }

    /**
     * 解析excel
     *
     * @param file            文件
     * @param rowSeparator    行隔离膜
     * @param columnSeparator 列分离器
     * @return {@link List}<{@link String}>
     * @throws IOException ioexception
     */
    public static List<String> readContent(File file, String rowSeparator, String columnSeparator) throws Exception {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (true) {
            try {
                String content = readContent(file, i++, rowSeparator, columnSeparator);
                if (StrKit.isBlank(content)) {
                    break;
                }
                res.add(content);
            } catch (EmptyFileException e) {
                break;
            }
        }
        return res;
    }

    /**
     * 解析excel
     *
     * @param file 文件
     * @return {@link List}<{@link String}>
     * @throws IOException ioexception
     */
    public static List<String> readContent(File file) throws Exception {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (true) {
            try {
                String content = readContent(file, i++);
                if (StrKit.isBlank(content)) {
                    break;
                }
                res.add(content);
            } catch (EmptyFileException e) {
                break;
            }
        }
        return res;
    }

    /**
     * 解析excel
     *
     * @param file            文件
     * @param sheetName       表名字
     * @param rowSeparator    行隔离膜
     * @param columnSeparator 列分离器
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readContent(File file, String sheetName, String rowSeparator, String columnSeparator) throws Exception {
        return readContent(file, sheetName, 0, rowSeparator, columnSeparator);
    }

    /**
     * 解析excel
     *
     * @param file            文件
     * @param sheetIndex      表索引
     * @param rowSeparator    行隔离膜
     * @param columnSeparator 列分离器
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readContent(File file, int sheetIndex, String rowSeparator, String columnSeparator) throws Exception {
        return readContent(file, null, sheetIndex, rowSeparator, columnSeparator);
    }

    /**
     * 解析excel
     *
     * @param file       文件
     * @param sheetIndex 表索引
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readContent(File file, int sheetIndex) throws Exception {
        return readContent(file, null, sheetIndex);
    }

    /**
     * 解析excel
     *
     * @param file      文件
     * @param sheetName 表名字
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readContent(File file, String sheetName) throws Exception {
        return readContent(file, sheetName, 0);
    }

    /**
     * 解析excel
     *
     * @param is         是
     * @param sheetIndex 表索引
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readContent(InputStream is, int sheetIndex) throws Exception {
        return readContent(is, null, sheetIndex);
    }


    /**
     * 解析excel
     *
     * @param is              是
     * @param sheetIndex      表索引
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readContent(InputStream is, int sheetIndex, String rowSeparator, String columnSeparator) throws Exception {
        return readContent(is, null, sheetIndex, rowSeparator, columnSeparator);
    }

    /**
     * 解析excel
     *
     * @param is        是
     * @param sheetName 表名字
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readContent(InputStream is, String sheetName) throws Exception {
        return readContent(is, sheetName, 0);
    }

    /**
     * 解析excel
     *
     * @param is              是
     * @param sheetName       表名字
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @return {@link String}
     * @throws IOException ioexception
     */
    public static String readContent(InputStream is, String sheetName, String rowSeparator, String columnSeparator) throws Exception {
        return readContent(is, sheetName, 0, rowSeparator, columnSeparator);
    }

    /**
     * 解析excel
     *
     * @param file            文件
     * @param sheetName       表名字
     * @param sheetIndex      表索引
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @return {@link String}
     * @throws IOException ioexception
     */
    private static String readContent(File file, @Nullable String sheetName, int sheetIndex, String rowSeparator, String columnSeparator) throws Exception {
        return readContent(Files.newInputStream(file.toPath()), sheetName, sheetIndex, rowSeparator, columnSeparator);
    }

    /**
     * 解析excel
     *
     * @param file       文件
     * @param sheetName  表名字
     * @param sheetIndex 表索引
     * @return {@link String}
     * @throws IOException ioexception
     */
    private static String readContent(File file, @Nullable String sheetName, int sheetIndex) throws Exception {
        return readContent(Files.newInputStream(file.toPath()), sheetName, sheetIndex, "\n", "\t");
    }

    /**
     * 解析excel
     *
     * @param is         是
     * @param sheetName  表名字
     * @param sheetIndex 表索引
     * @return {@link String}
     * @throws IOException ioexception
     */
    private static String readContent(InputStream is, @Nullable String sheetName, int sheetIndex) throws Exception {
        return readContent(is, sheetName, sheetIndex, "\n", "\t");
    }

    /**
     * 解析excel文件
     *
     * @param is              是
     * @param sheetName       表名字
     * @param sheetIndex      表索引
     * @param rowSeparator    行分隔符
     * @param columnSeparator 列分隔符
     * @return {@link String}
     * @throws IOException ioexception
     */
    private static String readContent(InputStream is, @Nullable String sheetName, int sheetIndex, String rowSeparator, String columnSeparator) throws Exception {
        if (rowSeparator == null) {
            rowSeparator = "\n";
        }
        if (columnSeparator == null) {
            columnSeparator = "\t";
        }
        return iterInSheet(is, sheetIndex, sheetName, rowSeparator, columnSeparator, "UTF-8").toString();
    }

    public static StringBuilder iterInSheet(InputStream is, int sheetIndex, @Nullable String sheetName, String rowSeparator, String columnSeparator, String encoding) throws Exception {
        StringBuilder res = new StringBuilder();
        IOUtils.setByteArrayMaxOverride(200000000);
        OPCPackage opcPackage = OPCPackage.open(is);
        XSSFReader reader = new XSSFReader(opcPackage);
        SharedStringsTable sharedStringsTable = (SharedStringsTable) reader.getSharedStringsTable();
        StylesTable stylesTable = reader.getStylesTable();
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        SheetHandler sheetHandler = new SheetHandler(sharedStringsTable, stylesTable, res, rowSeparator, columnSeparator);
        xmlReader.setContentHandler(sheetHandler);
        if (StrKit.isNotBlank(sheetName)) {
            XSSFReader.SheetIterator sheetIterator = (XSSFReader.SheetIterator) reader.getSheetsData();
            while (sheetIterator.hasNext()) {
                InputStream sheetStream = sheetIterator.next();
                String currentSheetName = sheetIterator.getSheetName();
                if (currentSheetName.equalsIgnoreCase(sheetName)) {
                    InputSource sheetSource = new InputSource(sheetStream);
                    sheetSource.setEncoding(encoding);
                    xmlReader.parse(sheetSource);
                    sheetStream.close();
                    break;
                }
            }
        } else {
            Iterator<InputStream> sheetIterator = reader.getSheetsData();
            int currentSheetIndex = 0;
            while (sheetIterator.hasNext()) {
                InputStream sheetStream = sheetIterator.next();
                if (currentSheetIndex == sheetIndex) {
                    InputSource sheetSource = new InputSource(sheetStream);
                    sheetSource.setEncoding(encoding);
                    xmlReader.parse(sheetSource);
                    sheetStream.close();
                    break;
                }
                currentSheetIndex++;
            }
        }

        return res;
    }

    private static class SheetHandler extends DefaultHandler {
        private final SharedStringsTable sharedStringsTable;
        private final StylesTable stylesTable;
        private final StringBuilder contentBuilder;
        private final String rowSeparator;
        private final String columnSeparator;
        private StringBuilder cellContent;
        private boolean isCell;
        private CellDataType nextDataType;
        private CellStyle cellStyle;

        public SheetHandler(SharedStringsTable sharedStringsTable, StylesTable stylesTable, StringBuilder contentBuilder, String rowSeparator, String columnSeparator) {
            this.sharedStringsTable = sharedStringsTable;
            this.stylesTable = stylesTable;
            this.contentBuilder = contentBuilder;
            this.rowSeparator = rowSeparator;
            this.columnSeparator = columnSeparator;
            this.cellContent = new StringBuilder();
            this.isCell = false;
            this.nextDataType = CellDataType.NUMBER; // 默认数据类型为数字
            this.cellStyle = null;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (StrKit.equals(qName, "c")) {
                String cellType = attributes.getValue("t");
                String cellStyleId = attributes.getValue("s");
                if (cellType != null) {
                    this.nextDataType = getCellDataType(cellType);
                }
                if (cellStyleId != null) {
                    this.cellStyle = stylesTable.getStyleAt(Integer.parseInt(cellStyleId));
                }
                this.isCell = true;
                this.cellContent.setLength(0);
            }
        }


        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (isCell && StrKit.equals(qName, "v")) {
                String value = getCellValue();
                contentBuilder.append(value).append(columnSeparator);
            } else if (StrKit.equals(qName, "row")) {
                contentBuilder.append(rowSeparator);
            }
            isCell = false;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (isCell) {
                cellContent.append(ch, start, length);
            }
        }

        private CellDataType getCellDataType(String cellType) {
            if (cellType != null && StrKit.equals(cellType, ("s"))) {
                return CellDataType.SHARED_STRING;
            } else {
                return CellDataType.NUMBER;
            }
        }

        private String getCellValue() {
            String value = cellContent.toString().trim();
            switch (nextDataType) {
                case SHARED_STRING:
                    int sharedStringIndex = Integer.parseInt(value);
                    value = sharedStringsTable.getItemAt(sharedStringIndex).getString();
                    break;
                default:
                    break;
            }
            return value;
        }

        private enum CellDataType {
            NUMBER, SHARED_STRING
        }
    }
}
