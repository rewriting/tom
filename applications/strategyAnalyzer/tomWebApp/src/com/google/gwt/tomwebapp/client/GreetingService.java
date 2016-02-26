package com.google.gwt.tomwebapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
  String greetServer(String name) throws IllegalArgumentException;
  
  String returnOutput(String input, boolean verbose, boolean debug, boolean withAP, boolean withAT, boolean aprove, boolean timbuk, boolean metalevel, boolean approx, boolean withType, boolean minimize, boolean ordered, boolean pattern, boolean tom, String classname);
}
