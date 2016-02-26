package com.google.gwt.tomwebapp.ui   ;

import com.google.gwt.tomwebapp.shared.FieldVerifier;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.DOM;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Composite;
import com.github.gwtbootstrap.client.ui.*;
import java.util.*;
import sa.Main;
import com.google.gwt.tomwebapp.client.GreetingService;
import com.google.gwt.tomwebapp.client.GreetingServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppUiBinder extends Composite {

  interface AppUiBinderUiBinder extends UiBinder<Widget, AppUiBinder> {}

  private static AppUiBinderUiBinder uiBinder = GWT.create(AppUiBinderUiBinder.class);

  private GreetingServiceAsync runService = (GreetingServiceAsync) GWT.create(GreetingService.class);

  @UiField TextArea nsArea;
  @UiField ListBox models;
  //@UiField Button runButton;
  @UiField CheckBox verbose;
  @UiField CheckBox debug;
  @UiField CheckBox withAP;
  @UiField CheckBox withAT;
  @UiField CheckBox aprove;
  @UiField CheckBox timbuk;
  @UiField CheckBox metalevel;
  @UiField CheckBox approx;
  @UiField CheckBox withType;
  @UiField CheckBox minimize;
  @UiField CheckBox ordered;
  @UiField CheckBox pattern;
  @UiField CheckBox tom;

  @UiField VerticalPanel mainPanel;
  @UiField VerticalPanel checkboxesPanel;
  @UiField HorizontalPanel frontPanel;
  @UiField HorizontalPanel codingPanel;

  @UiField Label example;
  @UiField Label options;
  //@UiField Label code;


  public AppUiBinder() {
    initWidget(uiBinder.createAndBindUi(this));
    models.addItem("Select ns file");
    models.addItem("gxx.ns");  // lb.isItemSelected(1)
    models.addItem("demo.ns"); // lb.isItemSelected(2)
  }

  @UiHandler("runButton")
  public void onClick(ClickEvent event) {
    this.run();
  }

  @UiHandler("models")
  public void onChange(ChangeEvent event) {
      this.onChangeBody(models);
  }

  private void run() {
    Main m = new Main(nsArea.getText());
    m.options.setOptions(verbose.getValue(), debug.getValue(), withAP.getValue(), withAT.getValue(), aprove.getValue(), timbuk.getValue(), metalevel.getValue(), approx.getValue(), withType.getValue(), minimize.getValue(), ordered.getValue(), pattern.getValue(), tom.getValue(), "test");
    nsArea.setText(m.getOutput());
    
    /*
   runService.returnOutput(nsArea.getText(), verbose.getValue(), debug.getValue(), withAP.getValue(), withAT.getValue(), aprove.getValue(), timbuk.getValue(), metalevel.getValue(), approx.getValue(), withType.getValue(), minimize.getValue(), ordered.getValue(), pattern.getValue(), tom.getValue(),  "test", new AsyncCallback<String>() {
 
        public void onFailure(Throwable caught) {
        }

        public void onSuccess(String result) {
           nsArea.setText(result);
        }
    });  
    */
    
  }

  private void onChangeBody(ListBox lb) {
    if(lb.isItemSelected(1)) {
      nsArea.setText("abstract syntax\nT = a()\n\t| b()\n\t| g(T)\n\t| f(T,T)\n\nstrategies\n\nr1() =  [ g(x) -> x ]\n\nobu(s) = mu x.(one(x) <+ s)\nrepeat(s) = mu x.((s;x)<+identity)\ninnermost(s) = repeat(obu(s))\n\nmainStrat() = innermost(r1())\n");
    }
    else if(lb.isItemSelected(2)) {
      nsArea.setText("abstract syntax\nT = a()\n\t| g(T)\n\nstrategies\n\nr1() =  [ g(x) -> x ]\n\nobu(s) = mu x.(one(x) <+ s)\nrepeat(s) = mu x.((s;x)<+identity)\ninnermost(s) = repeat(obu(s))\n\nmainStrat() = innermost(r1())\n");
    }
    else {
      nsArea.setText("");
    }
  }
}
