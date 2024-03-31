package fileKit;


import com.aspose.words.FontSettings;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * @author jh
 * @version 1.0
 * @className FileSupport
 * @date 2021/01/30 下午5:10
 * @description //文件支撑
 * @program dsfa-platform2.0
 */

public class FileSupport {

    static {
        //判断是否windows系统，Linux要读取字体，否则pdf字体为方格  //这是重点
        if (!FileKit.isWinOS ()) {
            FontSettings.getDefaultInstance ().setFontsFolder (File.separator + "usr"
                    + File.separator + "share" + File.separator + "fonts" + File.separator + "winFonts", true);
            FontSettings.getDefaultInstance ().setFontsFolder (File.separator + "usr"
                    + File.separator + "share" + File.separator + "fonts", true);
        }
    }

    /**
     * 删除路径下的文件
     * @param filePath
     */
    public static void deleteVirtualPathFile(String filePath) {
        boolean exists = FileKit.exists (filePath);
        if(exists) {
            File file = new File(filePath);
            file.delete ();
        }
    }


    /**
     * 以byte[]方式读取文件
     *
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static byte[] readFileByBytes(String fileName) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(fileName));
        ByteArrayOutputStream out = new ByteArrayOutputStream ();
        try {
            byte[] tempBytes = new byte[in.available ()];
            for (int i; (i = in.read (tempBytes)) != -1; ) {
                out.write (tempBytes, 0, i);
            }
        }catch(Exception e){
            throw e;
        }finally {
            out.close();
            in.close();
        }
        return out.toByteArray();
    }


    /**
     * 传入byte，生成文件，返回路径
     *
     * @param fileBytes
     * @param prefixPtah
     * @return
     */
    public static  String createFile(byte[] fileBytes, String fileName, String prefixPtah, String fileSuffix) {
        String allPath;
        FileOutputStream fos = null;
        byte[] bytes = null;
        // 处理该二进制文件，此处处理为输出到桌面
        String path = prefixPtah.concat ("/upload/files/temp/");
        allPath = path.concat (fileName.concat (fileSuffix));
        File resultFile = new File (allPath);
        if (!resultFile.getParentFile ().exists ()) {
            resultFile.getParentFile ().mkdirs ();
        }
        try {
            fos = new FileOutputStream (resultFile);
            fos.write (fileBytes);
        } catch (Exception e) {
            e.printStackTrace ();
            return null;
        }finally {
            if(fos != null) {
                try {
                    fos.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
        return allPath;
    }

    public static byte[] getDefaultImg()  {

        ClassPathResource classPathResource = new ClassPathResource("image/damaged.png");
        InputStream inputStream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream ();
        try {
            inputStream = classPathResource.getInputStream();
            byte[] tempbytes = new byte[inputStream.available ()];
            for (int i = 0; (i = inputStream.read (tempbytes)) != -1; ) {
                out.write (tempbytes, 0, i);
            }
            return out.toByteArray ();
        } catch (IOException e) {
            e.printStackTrace ();
        }finally {
            if(inputStream != null) {
                try {
                    inputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
            try {
                out.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
        return null;
    }


}
