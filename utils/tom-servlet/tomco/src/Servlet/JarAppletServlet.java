package Servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class JarAppletServlet
 * Classe qui permet de faire le lien vers le jar pour l'applet
 */
public class JarAppletServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession s = request.getSession();
		File dir = (File) getServletContext().getAttribute(
				"javax.servlet.context.tempdir");
		String lienApplet = "file://"+ dir.getAbsolutePath() + "/" + s.getId()
				+ "/etu.jar";
		URLConnection con = (new URL(lienApplet)).openConnection();
		response.setContentType(con.getContentType());
		response.setContentLength(con.getContentLength());
		response.setDateHeader("Expires", con.getLastModified());
		response.addHeader("Content-Disposition", "attachment; filename=etu.jar");
		BufferedInputStream buf = new BufferedInputStream(con.getInputStream());
		int readBytes = 0;
		ServletOutputStream stream = response.getOutputStream();
		while ((readBytes = buf.read()) != -1) {
			stream.write(readBytes);
		}
		
		stream.close();
		buf.close();

	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Renvoi le lien de l'applet";
	}// </editor-fold>
}
