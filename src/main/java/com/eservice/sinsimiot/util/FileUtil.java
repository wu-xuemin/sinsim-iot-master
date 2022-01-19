package com.eservice.sinsimiot.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


import java.io.*;

/**
 * @author : silent
 * @description :   常用文件处理
 */
@Slf4j
public class FileUtil {

    private static int BUFFER_SIZE = 2 * 1024;
    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        byte[] values = FileUtils.readFileToByteArray(file);
        return EncryptUtil.getByteMD5Value(values);
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.isEmpty() || file.getSize() <= 0) {
        } else {
            InputStream ins;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }


    /**
     * 输入流转文件
     *
     * @param ins
     * @param file
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param files
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 判断路径是否存在
     *
     * @param path
     * @return
     */
    public static boolean existsFile(String path) {
        //存放的地址
        File dir = new File(path);
        //获取Linux文件权限,
        dir.setWritable(true, false);
        //判断目录是否存在
        if (!dir.exists()) {
            //不存在则新建,如创建失败则返回
            if (!dir.mkdir()) {
                System.out.println("文件夹创建失败");
                return false;
            }
        }
        return true;
    }

    /**
     * 判断在指定路径下是否存在指定名称的文件
     *
     * @param path
     * @param fileName
     * @return
     */
    public static boolean findFileExists(String path, String fileName) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                if (file.getName().equals(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 实现文件的拷贝
     *
     * @param srcPathStr 源文件的地址信息
     * @param desPathStr 目标文件的地址信息
     */
    public static boolean copyFile(String srcPathStr, String desPathStr, String fileName) {
        try {
            if (existsFile(desPathStr)) {
                desPathStr += fileName;
            } else {
                return false;
            }
            // 创建输入输出流对象
            FileInputStream fis = new FileInputStream(srcPathStr);
            FileOutputStream fos = new FileOutputStream(desPathStr);
            // 创建搬运工具
            byte datas[] = new byte[1024 * 8];
            // 创建长度
            int len = 0;
            // 循环读取数据
            while ((len = fis.read(datas)) != -1) {
                fos.write(datas, 0, len);
            }
            // 3.释放资源
            fis.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建ZIP文件
     *
     * @param sourcePath 文件或文件夹路径
     * @param zipPath    生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(String sourcePath, String zipPath, String fileName) {
        try {
            if (existsFile(zipPath)) {
                zipPath += fileName;
            } else {
                return;
            }
            FileOutputStream fos = new FileOutputStream(zipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            //此处修改字节码方式。
            zos.setEncoding("gbk");
            writeZip(new File(sourcePath), "", zos);
            zos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure) {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
            long end = System.currentTimeMillis();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            int result = (len = in.read(buf));
            while (result != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (keepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }
        }
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {
                //处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else {       //空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(parentPath + file.getName()));
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
