package com.eservice.sinsimiot.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.UUID;

/**
 * @description: 提供图片的操作
 * @author: silent
 **/
public class ImageUtil {

    public static String getImageStr(String imgFilePath) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(data);
        // 返回Base64编码过的字节数组字符串

    }

    public static InputStream baseToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {

            byte[] bytes1 = Base64.getDecoder().decode(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }

    public static BufferedImage getBufferedImage(String base64string) {
        BufferedImage image = null;
        try {
            InputStream stream = baseToInputStream(base64string);
            image = ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * base64转byte
     *
     * @param base64Img
     * @return
     */
    public static byte[] getImageByte(String base64Img) {
        byte[] data = null;
        if (base64Img != null) {
            data = Base64.getDecoder().decode(base64Img);
            return data;
        }
        return null;
    }

    /**
     * 压缩 MultipartFile 类型图片
     *
     * @param avatarFile
     * @return
     */
    public static String compressedByMultipartFile(MultipartFile avatarFile) {
        try {
            File file = FileUtil.multipartFileToFile(avatarFile);
            return compressed(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 压缩指定路径图片
     *
     * @param path
     * @return
     */
    public static String compressedByPath(String path) {
        try {
            File file = new File(path);
            return compressed(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 压缩指定路径图片
     *
     * @param base64
     * @return
     */
    public static String compressedByBase64(String base64) {
        try {
            String fileName = UUID.randomUUID().toString();
            if (saveBase64ToFile(".", base64, fileName)) {
                File file = new File(String.format("./%s", fileName));
                return compressed(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片压缩
     *
     * @param file
     * @return
     */
    public static String compressed(File file) {
        try {
            Long fileSize = file.length();
            if (fileSize > 1024 * 1024 * 4) {
                Thumbnails.of(file).scale(1F).outputQuality(0.1F).toFile(file);
            } else if (fileSize > 1024 * 1024 * 3) {
                Thumbnails.of(file).scale(1F).outputQuality(0.2F).toFile(file);
            } else if (fileSize > 1024 * 1024 * 2) {
                Thumbnails.of(file).scale(1F).outputQuality(0.3F).toFile(file);
            } else if (fileSize > 1024 * 1024) {
                Thumbnails.of(file).scale(1F).outputQuality(0.5F).toFile(file);
            } else if (fileSize > 600 * 1024) {
                Thumbnails.of(file).scale(1F).outputQuality(0.7F).toFile(file);
            }
            return getFileBase64(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片的base64
     *
     * @param file
     * @return
     */
    public static String getFileBase64(File file) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte[] buffInput = new byte[fin.available()];
            fin.read(buffInput);
            String imageBase64 = Base64.getEncoder().encodeToString(buffInput);
            return imageBase64.replaceAll("[\\s*\t\n\r]", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @param path     保存文件的总路径
     * @param rowData  照片文件的base64
     * @param fileName 文件名称
     * @return 文件路径
     * eg: xh333.jpg
     */
    public static boolean saveBase64ToFile(String path, String rowData, String fileName) {
        boolean isSuccess = true;
        try {
            if (path != null) {
                if (FileUtil.existsFile(path)) {
                    String targetFileName = String.format("%s%s", path, fileName);
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(new File(targetFileName)));
                    byte[] b = Base64.getDecoder().decode(rowData);
                    for (int i = 0; i < b.length; ++i) {
                        if (b[i] < 0) {
                            b[i] += 256;
                        }
                    }
                    out.write(b);
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * @param path     保存文件的总路径
     * @param rowData  照片文件的二进制流
     * @param fileName 文件名称
     * @return 文件路径
     * eg: xh333.jpg
     */
    public static boolean saveByteToFile(String path, byte[] rowData, String fileName) {
        boolean isSuccess = true;
        try {
            if (path != null) {
                if (FileUtil.existsFile(path)) {
                    String targetFileName = String.format("%s%s", path, fileName);
                    BufferedOutputStream out = new BufferedOutputStream(
                            new FileOutputStream(new File(targetFileName)));
                    for (int i = 0; i < rowData.length; ++i) {
                        if (rowData[i] < 0) {
                            rowData[i] += 256;
                        }
                    }
                    out.write(rowData);
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }
}
