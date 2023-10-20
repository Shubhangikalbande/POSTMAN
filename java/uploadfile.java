import static spark.Spark.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUploadApi {
    public static void main(String[] args) {
        // Specify the directory where uploaded files will be saved
        String uploadDir = "uploads";
        File uploadDirectory = new File(uploadDir);
        uploadDirectory.mkdir();

        // Set up Spark web server
        port(8080);

        // Define a route to handle file uploads
        post("/upload", (request, response) -> {
            try (InputStream is = request.raw().getPart("file").getInputStream()) {
                // Generate a unique file name (you may want a more robust approach)
                String fileName = System.currentTimeMillis() + "_" + request.raw().getPart("file").getSubmittedFileName();

                // Save the uploaded file to the specified directory
                try (OutputStream os = new FileOutputStream(uploadDir + "/" + fileName)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }

                return "File uploaded successfully: " + fileName;
            } catch (Exception e) {
                response.status(500);
                return "Error uploading file: " + e.getMessage();
            }
        });
    }
}
