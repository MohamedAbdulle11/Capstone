package com.app.gamelibrarymanagement.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.apache.catalina.startup.ExpandWar.deleteDir;

public class SaveUpload {

    public static void saveFile(String img, String imgName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(img);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(imgName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + imgName, ioe);
        }
    }

    /**
     * This function is getting used, when we are deleting a specific game then we are deleting its pictures too.
     */
    public static boolean deleteIMG(String img) {
        File file = new File(img);
        if (file.exists()) {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteDir(f);
                }
            }
            return file.delete();
        } else {
            return false;
        }

    }
}