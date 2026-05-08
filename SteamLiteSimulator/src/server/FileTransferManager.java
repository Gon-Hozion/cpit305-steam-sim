package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileTransferManager {

    public static boolean sendFile(String filePath, OutputStream outputStream) {

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return false;
        }

        
        try (FileInputStream fileInput = new FileInputStream(file)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInput.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            System.out.println("File sent successfully!");
            return true;

        } catch (IOException e) {
            System.out.println("File transfer error: " + e.getMessage());
            return false;
        }
    }
}