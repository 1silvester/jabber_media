package com.bham.fsd.assignments.jabberserver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainPageController implements Initializable {
	@FXML
	private BorderPane mainPane;
	@FXML
	private Rectangle connectionBox;
	@FXML
	private TextField typeUsername;
	@FXML
	private Button signIn;
	@FXML
	private Button register;
	@FXML
	private Rectangle signInBox;
	@FXML
	private Label signInLabel;
	@FXML
	private Button signOut;
	@FXML
	private TextField typePost;
	@FXML
	private Button post;
	@FXML
	private HBox hboxSignIn;
	@FXML
	private HBox hboxSignOut;
	@FXML
	private HBox hboxPost;
	@FXML
	private Pane pane1;
	@FXML
	private Pane pane2;
	@FXML
	private ListView<Jab> tlList;
	@FXML
	private ListView<Follow> followList;
	
	
	public Client clientSocket = new Client();
	public ArrayList<Thread> threads;
	public ArrayList<ArrayList<String>> tlData = new ArrayList<ArrayList<String>>();
	public ArrayList<ArrayList<String>> followData = new ArrayList<ArrayList<String>>();
	public long timeout = 300;
	public long delay = 2000;
	public long refresh = 2000;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainPane.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		pane1.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
		pane2.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY)));
		hboxSignIn.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		hboxSignOut.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		hboxPost.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		signOut.setVisible(false);
		hboxPost.setVisible(false);
		pane1.setVisible(false);
		pane2.setVisible(false);
		
		if (clientSocket.isConnected()) {
			connectionBox.setFill(Color.GREEN);
			connectionBox.setStroke(Color.GREEN);
		}
		else {
			connectionBox.setFill(Color.RED);
			connectionBox.setStroke(Color.RED);
		}
		Thread t = new Thread(clientSocket);
		t.start();
		
	}

	// Event Listener on Button[#signIn].onAction
	@FXML
	public void signIn(ActionEvent event) throws Exception {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				try {
					String requestSI = "signin " + typeUsername.getText();
					clientSocket.sendMessage(requestSI);
					TimeUnit.MILLISECONDS.sleep(timeout);
					if (clientSocket.getResponse().equals("signedin")) {
						signInBox.setFill(Color.GREEN);
						signInBox.setStroke(Color.GREEN);
						setSignInLabel("SIGNED IN AS: " + typeUsername.getText());
						updateTimeline();
						updateFollows();
						refreshTimeline();
						signOut.setVisible(true);
						hboxPost.setVisible(true);
						pane1.setVisible(true);
						pane2.setVisible(true);
						//hboxSignIn.setVisible(false);
						typeUsername.setVisible(false);
						typeUsername.setManaged(false);
						signIn.setVisible(false);
						signIn.setManaged(false);
						register.setVisible(false);
						register.setManaged(false);
						Label welcome = new Label();
						welcome.setText("WELCOME TO JABBER");
						welcome.setFont(Font.font("Century Gothic", FontWeight.BOLD, 40));
						welcome.setPrefWidth(860);
						welcome.setPrefHeight(60);
						welcome.setAlignment(Pos.CENTER);
						hboxSignIn.getChildren().add(welcome);
					}
					else if(clientSocket.getResponse().equals("unknown-user")) {
						// present error window
						Stage stage = (Stage) mainPane.getScene().getWindow();
						AlertType type = AlertType.ERROR;
						Alert alert = new Alert(type, "");
						alert.initModality(Modality.APPLICATION_MODAL);
						alert.initOwner(stage);
						alert.getDialogPane().setContentText("INCORRECT USERNAME. TRY AGAIN OR REGISTER");
						alert.showAndWait();
						typeUsername.clear();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	// Event Listener on Button[#register].onAction
	@FXML
	public void register(ActionEvent event) {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				try {
					String requestSI = "register " + typeUsername.getText();
					clientSocket.sendMessage(requestSI);
					TimeUnit.MILLISECONDS.sleep(timeout);
					if (clientSocket.getResponse().equals("signedin")) {
						signInBox.setFill(Color.GREEN);
						signInBox.setStroke(Color.GREEN);
						setSignInLabel("REGISTERED AND SIGNED IN AS: " + typeUsername.getText());
						updateTimeline();
						updateFollows();
						refreshTimeline();
						signOut.setVisible(true);
						hboxPost.setVisible(true);
						pane1.setVisible(true);
						pane2.setVisible(true);
						//hboxSignIn.setVisible(false);
						typeUsername.setVisible(false);
						typeUsername.setManaged(false);
						signIn.setVisible(false);
						signIn.setManaged(false);
						register.setVisible(false);
						register.setManaged(false);
						Label welcome = new Label();
						welcome.setText("WELCOME TO JABBER");
						welcome.setFont(Font.font("Century Gothic", FontWeight.BOLD, 40));
						welcome.setPrefWidth(860);
						welcome.setPrefHeight(60);
						welcome.setAlignment(Pos.CENTER);
						hboxSignIn.getChildren().add(welcome);
					}
					else if(clientSocket.getResponse().equals("unknown-user")) {
						// present error window
						Stage stage = (Stage) mainPane.getScene().getWindow();
						AlertType type = AlertType.ERROR;
						Alert alert = new Alert(type, "");
						alert.initModality(Modality.APPLICATION_MODAL);
						alert.initOwner(stage);
						alert.getDialogPane().setContentText("TRY AGAIN");
						alert.showAndWait();
						typeUsername.clear();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Event Listener on Button[#signOut].onAction
	@FXML
	public void signOut(ActionEvent event) throws Exception {
		String requestSO = "signout";
		clientSocket.sendMessage(requestSO);
		Platform.exit();
	}
	
	// Event Listener on Button[#post].onAction
	@FXML
	public void post(ActionEvent event) {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				try {
					String requestSI = "post " + typePost.getText();
					clientSocket.sendMessage(requestSI);
					TimeUnit.MILLISECONDS.sleep(timeout);
					if (clientSocket.getResponse().equals("posted")) {
						typePost.clear();
					}
					updateTimeline();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(clientSocket.getResponse());
			}
		});
	}
	
	public void setSignInLabel (String newStatus) {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				signInLabel.setText(newStatus);
			}
		});
	}
	
	public ArrayList<ArrayList<String>> getTimeline() {
		// send the message, receive the message and the data
		try {
			ArrayList<ArrayList<String>> tlData = new ArrayList<ArrayList<String>>();
			clientSocket.sendMessage("timeline");
			TimeUnit.MILLISECONDS.sleep(timeout);
			if(clientSocket.getResponse().equals("timeline")) {
				tlData = clientSocket.getData();
				this.tlData = clientSocket.getData();
			}
			return tlData;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return this.tlData;
		}
	}
	
	public void updateTimeline() {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				// get the timeline arraylist and show on gui with buttons
				ArrayList<ArrayList<String>> tlData = getTimeline();
				ArrayList<Jab> fullTL = new ArrayList<Jab>();
				for (int k = 0; k < tlData.size(); k++) {
					fullTL.add(new Jab(tlData.get(k)));
				}
				tlList.getItems().clear();
				tlList.getItems().addAll(fullTL);
				tlList.setCellFactory(new JabCellFactory());
			}
		});
	}
	
	public ArrayList<ArrayList<String>> getFollows() {
		// send the message, receive the message and the who to follow list
		try {
			ArrayList<ArrayList<String>> followData = new ArrayList<ArrayList<String>>();
			clientSocket.sendMessage("users");
			TimeUnit.MILLISECONDS.sleep(timeout);
			if(clientSocket.getResponse().equals("users")) {
				followData = clientSocket.getData();
				this.followData = clientSocket.getData();
			}
			return followData;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return this.followData;
		}
	}
	
	public void updateFollows() {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				// get the follow arraylist and show on gui with buttons
				ArrayList<ArrayList<String>> followData = getFollows();
				ArrayList<Follow> followAL = new ArrayList<Follow>();
				for (int k = 0; k < followData.size(); k++) {
					followAL.add(new Follow(followData.get(k)));
				}
				followList.getItems().clear();
				followList.getItems().addAll(followAL);
				followList.setCellFactory(new FollowCellFactory());
			}
		});
	}
	
	public void refreshTimeline() {
		Timer randTimer = new Timer();
		randTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateTimeline();
			}
		}, delay, refresh);
	}
	
	public class Jab {
		
		private String username;
		private String text;
		private String jabID;
		private String likes;
		private HBox hbox = new HBox();
	    private Label label = new Label("(empty)");
	    private Pane pane = new Pane();
	    private Image img = new Image("star.png");
		private ImageView view = new ImageView(img);
	    private Button button = new Button();
	    private String lastItem;
		
		public Jab(ArrayList<String> jab) {
			this.username = jab.get(0);
			this.text = jab.get(1);
			this.jabID = jab.get(2);
			this.likes = jab.get(3);
			view.setFitHeight(25);
			view.setPreserveRatio(true);
			button.setGraphic(view);
			button.setText(this.likes);
			button.setPadding(new Insets(2,5,2,2));
			label.setFont(new Font(15.0));
			label.setWrapText(true);
			label.maxWidth(350);
			hbox.getChildren().addAll(label, pane, button);
			HBox.setHgrow(pane, Priority.ALWAYS);
			hbox.setAlignment(Pos.CENTER);
			hbox.maxWidth(460);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println(toTLPrint() + " with likes: " + getLikes());
					try {
						clientSocket.sendMessage("like " + jabID);
						TimeUnit.MILLISECONDS.sleep(timeout);
						if (clientSocket.getResponse().equals("posted")) {
							updateTimeline();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(clientSocket.getResponse());
				}
			});
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getJabID() {
			return jabID;
		}

		public void setJabID(String jabID) {
			this.jabID = jabID;
		}

		public String getLikes() {
			return likes;
		}

		public void setLikes(String likes) {
			this.likes = likes;
		}
		
		public HBox getHbox() {
			return hbox;
		}

		public void setHbox(HBox hbox) {
			this.hbox = hbox;
		}

		public Label getLabel() {
			return label;
		}

		public void setLabel(Label label) {
			this.label = label;
		}

		public Pane getPane() {
			return pane;
		}

		public void setPane(Pane pane) {
			this.pane = pane;
		}

		public ImageView getView() {
			return view;
		}

		public void setView(ImageView view) {
			this.view = view;
		}

		public Button getButton() {
			return button;
		}

		public void setButton(Button button) {
			this.button = button;
		}

		public String toTLPrint() {
			return username + ": " + text;
		}
	}
	
	public class JabCell extends ListCell<Jab> {
		
		@Override
		public void updateItem(Jab item, boolean empty) {
			super.updateItem(item, empty);
			String lastItem;
			setText(null);
			if (empty) {
				
			}
			else {
				lastItem = item.getText();
				item.getLabel().setText(item.toTLPrint());
				item.getButton().setText(item.getLikes());
				setGraphic(item.getHbox());
			}
		}
	}
	
	public class JabCellFactory implements Callback<ListView<Jab>, ListCell<Jab>> {
		@Override
		public ListCell<Jab> call(ListView<Jab> listview) {
			return new JabCell();
		}
	}
	
	public class Follow {
		private String username;
		private HBox hbox = new HBox();
	    private Label label = new Label("(empty)");
	    private Pane pane = new Pane();
	    private Image img = new Image("follow.png");
		private ImageView view = new ImageView(img);
	    private Button button = new Button();
	    private String lastItem;
	    
	    public Follow(ArrayList<String> follow) {
			this.username = follow.get(0);
			view.setFitHeight(30);
			view.setPreserveRatio(true);
			button.setGraphic(view);
			label.setFont(new Font(18.0));
			hbox.getChildren().addAll(label, pane, button);
			HBox.setHgrow(pane, Priority.ALWAYS);
			hbox.setAlignment(Pos.CENTER);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("Follow: " + username);
					try {
						clientSocket.sendMessage("follow " + username);
						TimeUnit.MILLISECONDS.sleep(timeout);
						if (clientSocket.getResponse().equals("posted")) {
							updateTimeline();
							updateFollows();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(clientSocket.getResponse());
				}
			});
		}
	    
	    public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
		
		public HBox getHbox() {
			return hbox;
		}

		public void setHbox(HBox hbox) {
			this.hbox = hbox;
		}

		public Label getLabel() {
			return label;
		}

		public void setLabel(Label label) {
			this.label = label;
		}

		public Pane getPane() {
			return pane;
		}

		public void setPane(Pane pane) {
			this.pane = pane;
		}

		public ImageView getView() {
			return view;
		}

		public void setView(ImageView view) {
			this.view = view;
		}

		public Button getButton() {
			return button;
		}

		public void setButton(Button button) {
			this.button = button;
		}
	}
	
	public class FollowCell extends ListCell<Follow> {
		
		@Override
		public void updateItem(Follow item, boolean empty) {
			super.updateItem(item, empty);
			String lastItem;
			setText(null);
			if (empty) {
				
			}
			else {
				lastItem = item.getUsername();
				item.getLabel().setText(item.getUsername());
				setGraphic(item.getHbox());	
			}	
		}
	}
	
	public class FollowCellFactory implements Callback<ListView<Follow>, ListCell<Follow>> {
		@Override
		public ListCell<Follow> call(ListView<Follow> listview) {
			return new FollowCell();
		}

	}
}
