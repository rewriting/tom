package com.google.gwt.tomwebapp.server;

import com.google.gwt.tomwebapp.client.GreetingService;
import com.google.gwt.tomwebapp.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sa.Main;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

  public String greetServer(String input) throws IllegalArgumentException {
    // Verify that the input is valid.
    if (!FieldVerifier.isValidName(input)) {
      // If the input is not valid, throw an IllegalArgumentException back to
      // the client.
      throw new IllegalArgumentException(
          "Name must be at least 4 characters long");
    }

    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");

    // Escape data from the client to avoid cross-site script vulnerabilities.
    input = escapeHtml(input);
    userAgent = escapeHtml(userAgent);

    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent;
  }

  public String returnOutput(String input, boolean verbose, boolean debug, boolean withAP, boolean withAT, boolean aprove, boolean timbuk, boolean metalevel, boolean approx, boolean withType, boolean minimize, boolean ordered, boolean pattern, String classname) {
	  Main m = new Main(input);
          m.options.setOptions(verbose, debug, withAP, withAT, aprove, timbuk, metalevel, approx, withType, minimize, ordered, pattern, classname);
          return m.getOutput();
  }
  /**
   * Escape an html string. Escaping data received from the client helps to
   * prevent cross-site script vulnerabilities.
   *
   * @param html the html string to escape
   * @return the escaped string
   */
  private String escapeHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
        ">", "&gt;");
  }
}
