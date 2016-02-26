package com.google.gwt.tomwebapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
  void greetServer(String input, AsyncCallback<String> callback)
      throws IllegalArgumentException;

  public void returnOutput(String input, boolean verbose, boolean debug, boolean withAP, boolean withAT, boolean aprove, boolean timbuk, boolean metalevel, boolean approx, boolean withType, boolean minimize, boolean ordered, boolean pattern, boolean tom, String classname, AsyncCallback<String> callback);
}
