package rocks.novateam.axis.sow.poc.backend.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import rocks.novateam.axis.sow.poc.backend.Configuration;

/**
 * This servlet handles media importation.
 * 
 * Uploaded files will be stored in the folder given in the `configuration.yml` file.
 * 
 * The HTTP request @b must
 *     + Have a <code>multipart/form-data</code> MIME type ;
 *     + Contain a field named <code>file</code> containing the file to import.
 * 
 * The HTTP response will have a <code>application/json</code> MIME type, and may contain:
 *     + <code>{'status': 'ok', 'filename': <em>filename</em>}</code> if the request succeeded ;
 *     + <code>{'status': 'ko', 'message': <em>message</em>}</code> if the request failed.
 * 
 * @see Configuration
 * 
 * @author Richard Degenne
 */
@MultipartConfig
public class importServlet extends HttpServlet {
    
    /**
     * File extension for uploaded files.
     * 
     * AXIS-SOW-POC will only work with MP4 files for the time being.
     */
    public static String FILE_EXTENSION = ".mp4";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JsonObjectBuilder json = Json.createObjectBuilder();
        try {
            // Get data from the request object
            Part filePart = request.getPart("file");
            if(filePart == null) {
                throw new FileNotFoundException("No 'file' part given in the request.");
            }
            String fileName = getNextFileName();
            InputStream fileContent = filePart.getInputStream();

            // Copy media to disk
            File file = new File(Configuration.getInstance().getUploadFolder()+fileName);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, false); // TODO: Have an incremental id for file names.
            IOUtils.copy(fileContent, fileOutputStream);
            
            json.add("status", "ok")
                    .add("filename", fileName);
        }
        catch(FileNotFoundException e) {
            json.add("status", "ko")
                    .add("message", e.getMessage());
        }
        
        // Send response
        response.setContentType("application/json");
        try(PrintWriter out = response.getWriter()) {
            out.println(json.build().toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet handles media importation.";
    }// </editor-fold>

    /**
     * Gets the best filename for an uploaded file.
     * 
     * Iterates over the upload folder to find the first integer
     * number not already present in the folder.
     * 
     * @return The best filename to give to a newly uplaoded file.
     * 
     * @see Configuration#uploadFolder()
     */
    private String getNextFileName() {
        System.out.println("Upload folder: "+Configuration.getInstance().getUploadFolder());
        File folder = new File(Configuration.getInstance().getUploadFolder());
        File[] files = folder.listFiles();
        int fileNumber = 1; // File names start from 1.
        
        for(int i=0 ; i<files.length ; ++i, ++fileNumber) {
            System.out.println("Comparing "+files[i]+" to "+fileNumber+FILE_EXTENSION);
            if(files[i].compareTo(new File(Configuration.getInstance().getUploadFolder()+fileNumber+FILE_EXTENSION)) != 0) {
                break;
            }
        }
        return fileNumber+FILE_EXTENSION; // WARNING: Hardcoded file extension here.
    }
}