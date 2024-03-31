package fileKit.fileReader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import baseKit.StrKit;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.util.IOUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

public class DocKit {
    //----------------------------------- txt、csv ----------------------------------------

    /**
     * 读取txt、csv文件文本内容，使用 BufferReader 逐行读取，行之间 \n 分隔; 默认使用 UTF-8 编码
     *
     * @param filePath 待读取文件路径
     * @return 文本内容字符串
     * @throws IOException
     */
    public static String readTxtContent(String filePath) throws IOException {
        return readTxtContent(filePath, StandardCharsets.UTF_8);
    }

    /**
     * 读取txt、csv文件文本内容，使用 BufferReader 逐行读取，行之间 \n 分隔
     *
     * @param filePath 待读取文件路径
     * @return 文本内容字符串
     * @throws IOException
     */
    public static String readTxtContent(String filePath, Charset charset) throws IOException {
        if (StrKit.isBlank(filePath)) {
            return null;
        }
        return readTxtContent(new File(filePath), charset);
    }

    /**
     * 读取txt、csv文件文本内容，使用 BufferReader 逐行读取，行之间 \n 分隔; 默认使用 UTF-8 编码
     *
     * @param file 待读取内容的文件
     * @return 文件文本内容字符串
     * @throws IOException 文件找不到或读取不到内容时抛出异常
     */
    public static String readTxtContent(File file) throws IOException {
        return readTxtContent(file, StandardCharsets.UTF_8);
    }

    /**
     * 读取txt、csv文件文本内容，使用 BufferReader 逐行读取，行之间 \n 分隔
     *
     * @param file    待读取内容的文件
     * @param charset 字符编码
     * @return 文件文本内容字符串
     * @throws IOException 文件找不到或读取不到内容时抛出异常
     */
    public static String readTxtContent(File file, Charset charset) throws IOException {
        if (!file.exists()) {
            return null;
        }

//        String type = FileTypeUtil.getType(file);
//        if (!"txt".equalsIgnoreCase(type) && !"csv".equalsIgnoreCase(type)) {
//            throw new IOException("不支持该文件类型: " + type);
//        }

        return readTxtContent(Files.newInputStream(file.toPath()), charset);
    }

    /**
     * 读取txt、csv文件文本内容，使用 BufferReader 逐行读取，行之间 \n 分隔; 默认使用 UTF-8 编码
     *
     * @param inputStream 待读取内容的输入流
     * @return 文件文本内容字符串
     * @throws IOException 文件找不到或读取不到内容时抛出异常
     */
    public static String readTxtContent(InputStream inputStream) throws IOException {
        return readTxtContent(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * 读取txt、csv文件文本内容，使用 BufferReader 逐行读取，行之间 \n 分隔
     *
     * @param inputStream 待读取内容的输入流
     * @param charset     字符编码，若为空则使用 UTF-8
     * @return 文件文本内容字符串
     * @throws IOException 文件找不到或读取不到内容时抛出异常
     */
    public static String readTxtContent(InputStream inputStream, Charset charset) throws IOException {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, charset));
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }


    //----------------------------------- Word ----------------------------------------

    /**
     * <p>读取 Word 文件的文本内容，仅读取文本内容，不会读取其他格式内容</p>
     *
     * @param filePath 待读取文件路径
     * @return 文件文本内容字符串
     * @throws IOException
     */
    public static String readWordContent(String filePath) throws IOException {
        if (StrKit.isBlank(filePath)) {
            return null;
        }
        return readWordContent(new File(filePath));
    }

    /**
     * <p>读取 Word 文件的文本内容，仅读取文本内容，不会读取其他格式内容</p>
     *
     * @param file 待读取文件
     * @return 文件文本内容字符串
     * @throws IOException 文件异常或文件格式不是 Word 时抛出此异常
     */
    public static String readWordContent(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        return readWordContent(Files.newInputStream(file.toPath()));
    }

    /**
     * <p>读取 Word 文件的文本内容，仅读取文本内容，不会读取其他格式内容</p>
     *
     * @param inputStream 待读取文件输入流
     * @return 文件文本内容字符串
     * @throws IOException 文件异常或文件格式不是 Word 时抛出此异常
     */
    public static String readWordContent(InputStream inputStream) throws IOException {
        inputStream = FileMagic.prepareToCheckMagic(inputStream);
        FileMagic fileMagic = FileMagic.valueOf(inputStream);
        switch (fileMagic) {
            case OLE2:
                return readDocContent(inputStream);
            case OOXML:
                return readDocxContent(inputStream);
            default:
                throw new IOException("不支持该文件类型: " + fileMagic);
        }
    }

    /**
     * <p>读取 Word 文件的文本内容，仅读取文本内容，不会读取其他格式内容</p>
     * <p>此方法仅支持读取 doc 格式的 Word 文件，docx 格式内容读取: {@link DocKit#readDocxContent(InputStream)}</p>
     *
     * @param inputStream 待读取文件的输入流
     * @return 文本内容字符串
     * @throws IOException
     */
    private static String readDocContent(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder textBuilder = new StringBuilder();
        System.out.println("inputStream.available() = " + inputStream.available());
        IOUtils.setByteArrayMaxOverride((int) Math.max(inputStream.available() * 1.1, 100000));
        try (HWPFDocument document = new HWPFDocument(inputStream)) {
            Range range = document.getRange();
            int numParagraphs = range.numParagraphs();
            int blockSize = 100; // 每个块的段落数

            for (int blockStart = 0; blockStart < numParagraphs; blockStart += blockSize) {
                int blockEnd = Math.min(blockStart + blockSize, numParagraphs);
                StringBuilder blockTextBuilder = new StringBuilder();

                for (int i = blockStart; i < blockEnd; i++) {
                    Paragraph paragraph = range.getParagraph(i);
                    String text = paragraph.text();
                    blockTextBuilder.append(text);
                }

                textBuilder.append(blockTextBuilder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return textBuilder.toString();
    }

    /**
     * <p>读取 Word 文件的文本内容，仅读取文本内容，不会读取其他格式内容</p>
     * <p>此方法仅支持读取 docx 格式的 Word 文件，doc 格式内容读取: {@link DocKit#readWordContent(InputStream)}</p>
     *
     * @param inputStream 待读取文件的输入流
     * @return 文本内容字符串
     * @throws IOException
     */
//    private static String readDocxContent(InputStream inputStream) throws IOException {
//        if (inputStream == null) {
//            return null;
//        }
//        StringBuilder textBuilder = new StringBuilder();
//        int available = inputStream.available();
//        IOUtils.setByteArrayMaxOverride((int) Math.max(available * 1.1, 100000));
//        try (XWPFDocument document = new XWPFDocument(inputStream)) {
//            List<XWPFParagraph> paragraphs = document.getParagraphs();
//            int numParagraphs = paragraphs.size();
//            int blockSize = 100; // 每个块的段落数
//
//            for (int blockStart = 0; blockStart < numParagraphs; blockStart += blockSize) {
//                int blockEnd = Math.min(blockStart + blockSize, numParagraphs);
//                StringBuilder blockTextBuilder = new StringBuilder();
//
//                for (int i = blockStart; i < blockEnd; i++) {
//                    XWPFParagraph paragraph = paragraphs.get(i);
//                    String text = paragraph.getText();
//                    blockTextBuilder.append(text);
//                }
//                textBuilder.append(blockTextBuilder);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            try {
//                inputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return textBuilder.toString();
//    }
    private static String readDocxContent(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder textBuilder = new StringBuilder();
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
            MainDocumentPart part = wordMLPackage.getMainDocumentPart();
            for (Object o : part.getContent()) {
                textBuilder.append(o.toString()).append("\n");
            }
            return textBuilder.toString();
        } catch (Docx4JException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    //----------------------------------- Pdf ----------------------------------------

    /**
     * <p>读取 Pdf 文件的文本内容，仅读取文本内容，不会读取其他格式内容</p>
     *
     * @param filePath 待读取文件路径
     * @return 文件文本内容字符串
     * @throws IOException
     */
    public static String readPdfContent(String filePath) throws IOException {
        if (StrKit.isBlank(filePath)) {
            return null;
        }
        return readPdfContent(new File(filePath));
    }

    /**
     * <p>读取 Pdf 文件的文本内容，仅读取文本内容，不会读取其他格式内容</p>
     *
     * @param file 待读取文件
     * @return 文件文本内容字符串
     * @throws IOException
     */
    public static String readPdfContent(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        return readPdfContent(Files.newInputStream(file.toPath()));
    }

    /**
     * <p>读取 Pdf 文件的文本内容，仅读取文本内容，不会读取其他格式内容</p>
     *
     * @param is 待读取文件输入流
     * @return 文本内容字符串
     * @throws IOException
     */
    public static String readPdfContent(InputStream is) throws IOException {
        is = FileMagic.prepareToCheckMagic(is);
        FileMagic fileMagic = FileMagic.valueOf(is);
        if (fileMagic != FileMagic.PDF) {
            throw new IOException("不支持的文件类型: " + fileMagic);
        }

        StringBuilder resultBuilder = new StringBuilder();
        PDDocument pdfdoc = null;
        try {
            pdfdoc = PDDocument.load(is);
            PDFTextStripper stripper = new PDFTextStripper();
            int numPages = pdfdoc.getNumberOfPages();
            // 每个块的页面数量
            int blockSize = 10;
            for (int blockStart = 0; blockStart < numPages; blockStart += blockSize) {
                int blockEnd = Math.min(blockStart + blockSize, numPages);

                // PDFTextStripper的页面编号从1开始
                stripper.setStartPage(blockStart + 1);
                stripper.setEndPage(blockEnd);

                String blockText = stripper.getText(pdfdoc);
                resultBuilder.append(blockText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                is.close();
                if (pdfdoc != null) {
                    pdfdoc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultBuilder.toString();
    }
}
