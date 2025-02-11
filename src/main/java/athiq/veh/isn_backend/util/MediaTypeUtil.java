package athiq.veh.isn_backend.util;


import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MediaTypeUtil {

    private static final Map<String, MediaType> mediaTypeMap = new HashMap<>();

    static {
        // Image types
        mediaTypeMap.put("png", MediaType.IMAGE_PNG);
        mediaTypeMap.put("jpg", MediaType.IMAGE_JPEG);
        mediaTypeMap.put("jpeg", MediaType.IMAGE_JPEG);
        mediaTypeMap.put("gif", MediaType.IMAGE_GIF);
        mediaTypeMap.put("bmp", MediaType.valueOf("image/bmp"));
        mediaTypeMap.put("webp", MediaType.valueOf("image/webp"));

        // Document types
        mediaTypeMap.put("pdf", MediaType.APPLICATION_PDF);
        mediaTypeMap.put("doc", MediaType.valueOf("application/msword"));
        mediaTypeMap.put("docx", MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        mediaTypeMap.put("xls", MediaType.valueOf("application/vnd.ms-excel"));
        mediaTypeMap.put("xlsx", MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        mediaTypeMap.put("ppt", MediaType.valueOf("application/vnd.ms-powerpoint"));
        mediaTypeMap.put("pptx", MediaType.valueOf("application/vnd.openxmlformats-officedocument.presentationml.presentation"));

        // Text types
        mediaTypeMap.put("txt", MediaType.TEXT_PLAIN);
        mediaTypeMap.put("csv", MediaType.valueOf("text/csv"));
        mediaTypeMap.put("xml", MediaType.APPLICATION_XML);
        mediaTypeMap.put("json", MediaType.APPLICATION_JSON);
        mediaTypeMap.put("html", MediaType.TEXT_HTML);

        // Video types
        mediaTypeMap.put("mp4", MediaType.valueOf("video/mp4"));
        mediaTypeMap.put("avi", MediaType.valueOf("video/x-msvideo"));
        mediaTypeMap.put("mov", MediaType.valueOf("video/quicktime"));
        mediaTypeMap.put("wmv", MediaType.valueOf("video/x-ms-wmv"));

        // Audio types
        mediaTypeMap.put("mp3", MediaType.valueOf("audio/mpeg"));
        mediaTypeMap.put("wav", MediaType.valueOf("audio/wav"));
        mediaTypeMap.put("ogg", MediaType.valueOf("audio/ogg"));

        // Compressed file types
        mediaTypeMap.put("zip", MediaType.valueOf("application/zip"));
        mediaTypeMap.put("rar", MediaType.valueOf("application/x-rar-compressed"));
        mediaTypeMap.put("gz", MediaType.valueOf("application/gzip"));
    }

    public static MediaType getMediaType(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return MediaType.APPLICATION_OCTET_STREAM; // Default binary type if unknown
        }

        // Extract the file extension
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // Return the MediaType based on the file extension
        return mediaTypeMap.getOrDefault(fileExtension, MediaType.APPLICATION_OCTET_STREAM);
    }
}
