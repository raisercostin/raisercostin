package org.raisercostin.modules.register.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Module implements EntryPoint {
	private static final ModuleConstants constants = (ModuleConstants) GWT.create(ModuleConstants.class);
	// Service class to call server
	private ModuleServiceAsync regService;
	String verifiedCode;
	private String countryName = "";
	// public PNGImage pngImage;
	public Image pngImage;
	public TextBox codeCheck;
	public Image verifyImage;
	public HorizontalPanel hPanel, tPanel;

	public void onModuleLoad() {
		// DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		// regService = mysqlFactory.getZesusersDAO();
		regService = (ModuleServiceAsync) GWT.create(ModuleService.class);
		ServiceDefTarget target = (ServiceDefTarget) regService;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "RegisterService");
		registerUI();
	}

	void registerUI() {
		final RootPanel rootPanel = RootPanel.get("index.html");

		final AbsolutePanel absolutePanel = new AbsolutePanel();
		final Label titleLabel = new Label(constants.regTitle());
		rootPanel.get("title").add(titleLabel);

		codeCheck = new TextBox();
		rootPanel.get("code").add(codeCheck);
		pngImage = new Image("verify.png");
		int rand = Random.nextInt();
		verifyImage = new Image("verify.png");
		hPanel = new HorizontalPanel();
		hPanel.add(verifyImage);
		rootPanel.get("verifyImage").add(hPanel);

		final Button clickImage = new Button(constants.clickImage());
		rootPanel.get("reloadImage").add(clickImage);
		clickImage.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				codeCheck.setText("");
				hPanel.remove(verifyImage);
				int rand = Random.nextInt();
				String url = "verify.png?rand=" + rand;
				verifyImage.setUrl(url);
				hPanel.add(verifyImage);
				rootPanel.get("verifyImage").add(hPanel);
			}
		});

		final Button signup = new Button();
		rootPanel.get("signup").add(signup);
		signup.setText(constants.verify());
		signup.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				String errormsg = "";
				regService.isValidCode(codeCheck.getText(), new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						Window.alert("error" + caught.getMessage());
					}

					public void onSuccess(Boolean result) {
						if (result.booleanValue()) {
							Window.alert("Valid code.");
						} else {
							Window.alert("Invalid code.");
						}
					}
				});
			}
		});
	}
}