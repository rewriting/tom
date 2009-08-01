package Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Conf.ConfString;

/**
 * 
 * Class qui permet de modifier les valeurs envoyés lors de l'utilisation de l'applet
 * Il correspond au form qui est affiché à droite sur la page web
 */
public class FormRServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		// 1 extraction des paramètres
		String button2 = request.getParameter("boutonApplet");
		HttpSession s = request.getSession();
		
		// 2 action
		if (button2 == null){
			return ;
		}
		if (button2.equals(ConfString.butStop)) {
			s.removeAttribute("objetCacher");
			s.removeAttribute("le_nomClasse");
			s.removeAttribute("le_scriptBash");
		} // fin action butStop
		
		response.sendRedirect("index.jsp");
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/** 
	 * Handles the HTTP <code>GET</code> method.
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
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Action effectuer à cause de l'applet";
	}// </editor-fold>
}
