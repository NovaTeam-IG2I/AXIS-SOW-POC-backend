/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.novateam.axis.sow.poc.backend.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import rocks.novateam.axis.sow.poc.backend.ontology.TDBManager;

/**
 * Exports the ontology and its contents.
 *
 * The HTTP response of this servlet will:
 * <ul>
 * <li>Have an <code>application/owl+xml</code> MIME type;</li>
 * <li>Contain all the information currently stored in the TDB.</li>
 * </ul>
 *
 * <strong>Note</strong>: This solution is inelegant at best and dangerous at
 * worst: <strong>all</strong> data is going to be exported, including rights,
 * as well as wrapping and structuring objects, which may be a problem as far as
 * confidentiality and privacy are concerned.
 *
 * @author Richard Degenne
 */
public class ExportServlet extends HttpServlet {

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
        TDBManager tdbm = TDBManager.getInstance();

        // May be "rdf+xml", since these are not official MIME types
        response.setContentType("application/owl+xml");

        tdbm.exportOwl(response.getOutputStream());
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
        return "Exports the ontology and its contents.";
    }// </editor-fold>

}
