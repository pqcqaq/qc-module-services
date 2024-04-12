package online.zust.qcqcqc.services.module.attachment.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import online.zust.qcqcqc.services.exception.ServiceException;
import online.zust.qcqcqc.services.module.attachment.exception.AttachmentServiceException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * @author qcqcqc
 * Date: 2024/3/18
 * Time: 23:45
 */
@Slf4j
public class FileUtils {
    /**
     * 压缩文件
     *
     * @param file 文件
     * @return 压缩后的文件
     */
    public static MultipartFile compress(MultipartFile file) {
        String suffix = getSuffix(Objects.requireNonNull(file.getOriginalFilename()));
        MultipartFile compressedFile;
        if (isImg(suffix)) {
            try {
                // Compress the image using Thumbnails library
                long size = file.getSize();
                InputStream inputStream = file.getInputStream();
                BufferedImage originalImage = ImageIO.read(inputStream);
                BufferedImage bufferedImage = resizeImage(size, originalImage);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                InputStream input;
                ImageIO.write(bufferedImage, suffix, os);
                input = new ByteArrayInputStream(os.toByteArray());

                compressedFile = new MockMultipartFile(
                        file.getName(),
                        file.getOriginalFilename(),
                        file.getContentType(),
                        input);
            } catch (Exception e) {
                log.error("压缩文件失败: " + e.getMessage());
                compressedFile = file;
            }
        } else {
            compressedFile = file;
        }
        return compressedFile;
    }

    /**
     * 压缩图片
     *
     * @param size          图片大小
     * @param originalImage 原始图片
     * @return 压缩后的图片
     * @throws IOException IO异常
     */
    private static BufferedImage resizeImage(long size, BufferedImage originalImage) throws IOException {
        //  100k - 1m 的
        BufferedImage bufferedImage;
        if ((1024 * 1024 * 0.1) <= size && size <= (1024 * 1024)) {
            log.info("图片大小: " + size + "压缩比例: 0.8");
            bufferedImage = Thumbnails.of(originalImage).scale(0.8f).outputQuality(0.75f).asBufferedImage();
        }
        // 1 - 2M 的
        else if ((1024 * 1024) < size && size <= (1024 * 1024 * 2)) {
            log.info("图片大小: " + size + "压缩比例: 0.6");
            bufferedImage = Thumbnails.of(originalImage).scale(0.6f).outputQuality(0.5f).asBufferedImage();
        }
        // 2M 以上的
        else if ((1024 * 1024 * 2) < size) {
            log.info("图片大小: " + size + "压缩比例: 0.5");
            bufferedImage = Thumbnails.of(originalImage).scale(0.5f).outputQuality(0.3f).asBufferedImage();
        } else {
            // 100k以下
            log.info("图片大小: " + size + "压缩比例: 1.0");
            bufferedImage = Thumbnails.of(originalImage).scale(1.0f).outputQuality(0.8f).asBufferedImage();
        }
        return bufferedImage;
    }

    /**
     * 判断是否是图片
     *
     * @param suffix 后缀
     * @return 是否是图片
     */
    private static boolean isImg(String suffix) {
        return "jpg".equals(suffix) || "jpeg".equals(suffix) || "png".equals(suffix) || "gif".equals(suffix);
    }

    /**
     * 获取文件名
     *
     * @param fileName 文件名
     * @return 文件名
     */
    public static String getNameWithoutSuffix(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 后缀
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 检查是不是图片文件
     *
     * @param filename 文件名
     * @return 是否是图片文件
     */
    public static boolean isImgFile(String filename) {
        return isImg(getSuffix(filename.substring(1)));
    }

    /**
     * base64转文件
     *
     * @param base64 base64
     * @param path   路径
     * @return 文件
     */
    public static File base64ToFile(String base64, Path path) {
        // 去掉前面的头
        base64 = base64.substring(base64.indexOf(",") + 1);
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            return compressFile(writeBytesToTempFile(bytes, getSuffix(path.getFileName().toString())), path);
        } catch (Exception e) {
            log.error("base64转文件失败", e);
            throw new AttachmentServiceException("base64转文件失败");
        }
    }

    /**
     * 压缩文件
     *
     * @param file 文件
     * @param path 路径
     * @return 文件
     */
    private static File compressFile(File file, Path path) {
        try {
            if (isImgFile(path.getFileName().toString())) {
                BufferedImage originalImage = ImageIO.read(file);
                long size = getFileSize(file);
                BufferedImage bufferedImage = resizeImage(size, originalImage);
                ImageIO.write(bufferedImage, getSuffix(path.getFileName().toString()), path.toFile());
                return path.toFile();
            }
            // 将文件直接写入
            log.info("非图片文件，不压缩");
            Path write = Files.write(path, Files.readAllBytes(file.toPath()));
            return write.toFile();
        } catch (IOException e) {
            log.error("压缩文件失败", e);
            throw new AttachmentServiceException("压缩文件失败");
        }
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小
     */
    private static long getFileSize(File file) {
        return file.length();
    }

    /**
     * 写入临时文件
     *
     * @param bytes  字节数组
     * @param suffix 后缀
     * @return 文件
     */
    public static File writeBytesToTempFile(byte[] bytes, String suffix) {
        // 创建临时文件
        try {
            Path tempFile = Files.createTempFile("temp", "." + suffix);
            Files.write(tempFile, bytes);
            return tempFile.toFile();
        } catch (IOException e) {
            log.error("创建临时文件失败", e);
            throw new AttachmentServiceException("创建临时文件失败");
        }
    }

    /**
     * 文件转MultipartFile
     *
     * @param file1 文件
     * @return MultipartFile
     */
    public static MultipartFile fileToMultipartFile(File file1) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file1);
            return new MockMultipartFile(file1.getName(), fileInputStream);
        } catch (IOException e) {
            log.error("文件转MultipartFile失败", e);
            throw new AttachmentServiceException("文件转MultipartFile失败");
        }
    }

    /**
     * 计算md5
     *
     * @param base64 base64
     * @return md5
     */
    public static String calMd5(String base64) {
        return MD52(base64);
    }

    /**
     * 计算md5
     *
     * @param in 输入流
     * @return md5
     */
    public static String calMd5(InputStream in) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = in.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return MD52(byteArrayOutputStream.toString());
        } catch (IOException e) {
            log.error("计算md5失败", e);
            throw new ServiceException("计算md5失败");
        }
    }


    /**
     * 计算md5
     *
     * @param input 输入
     * @return md5
     */
    private static String MD52(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            byte[] byteArray = md5.digest();

            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            // 一个字节对应两个16进制数，所以长度为字节数组乘2
            char[] charArray = new char[byteArray.length * 2];
            int index = 0;
            for (byte b : byteArray) {
                charArray[index++] = hexDigits[b >>> 4 & 0xf];
                charArray[index++] = hexDigits[b & 0xf];
            }
            return new String(charArray);
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5加密失败", e);
            throw new ServiceException("MD5加密失败");
        }
    }

    public static String getFileType(String filename) {
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return switch (suffix) {
            // image
            case "jpg", "jpeg", "png", "gif" -> "IMAGE";
            // video
            case "mp4", "avi", "mov", "rmvb", "flv", "3gp" -> "VIDEO";
            // audio
            case "mp3", "wav", "flac", "ape", "aac" -> "AUDIO";
            // document
            case "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "txt" -> "DOCUMENT";
            // csv
            case "csv" -> "CSV";
            // codes
            case "java", "c", "cpp", "py", "js", "html", "css", "json", "xml" -> "CODE";
            // script
            case "sh", "bat", "cmd" -> "SCRIPT";
            // config
            case "properties", "yml", "yaml", "ini", "conf" -> "CONFIG";
            // backup
            case "bak", "backup", "sql" -> "BACKUP";
            // other
            default -> "FILE";
        };
    }
}
