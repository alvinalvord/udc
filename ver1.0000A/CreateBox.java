import java.text.*;
import java.util.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class CreateBox extends VBox implements View {

	private DatabaseControl dc;
	private CalendarModel calendar;

	private Label createLabel;
	private TextField createName;
	private HBox recurringHBox;
	private CheckBox recurring;
	private HBox daysHBox;
	private VBox tempVBox;
	private Label temp;
	private CheckBox days[];
	private HBox createDateHBox;
	private Label dateLabel;
	private VBox createDateVBox;
	private Label createDate;
	private HBox recurringDateHBox;
	private Label recurringDate;
	private VBox recurringFromDateVBox;
	private Label recurringFromDate;
	private Label recurringDateLabel;
	private TextField recurringToDate;
	private HBox createTimeHBox;
	private Label timeLabel;
	private ComboBox<String> createFrom;
	private Label createToLabel;
	private ComboBox<String> createTo;
	private HBox createButtonHBox;
	private Button save;
	private Button discard;

	private ArrayList<String> times;

	public CreateBox(DatabaseControl cvc, CalendarModel calendar) {
		super();

		this.dc = cvc;
		this.calendar = calendar;

		calendar.attach(this);

		initCreateBox();
		update();
		initHandlers();
	}

	private void initCreateBox() {
		setId("createBox");

		createLabel = new Label("Create View");
		createLabel.setId("DefaultLabel");

		createName = new TextField("");
		createName.setId("TextField1");
		createName.setMinSize(Screen.getPrimary().getBounds().getWidth() / 3.50, 55);

		recurringHBox = new HBox();
		recurringHBox.setSpacing(20);
		recurringHBox.setAlignment(Pos.CENTER_LEFT);

		recurring = new CheckBox();
		recurring.setText("Recurring");
		recurring.getStyleClass().add("CheckBox");

		daysHBox = new HBox();
		daysHBox.setSpacing(50);
		// daysHBox.setAlignment(Pos.CENTER_RIGHT);

		days = new CheckBox[7];
		for (int i = 0; i < days.length; i++) {
			tempVBox = new VBox();
			tempVBox.setAlignment(Pos.CENTER_LEFT);

			temp = new Label();
			temp.setId("NormalLabel");

			days[i] = new CheckBox();
			days[i].setText(null);
			days[i].getStyleClass().add("CheckBox");
			days[i].setDisable(true);

			if (i == 0)
				temp.setText("Sun");
			if (i == 1)
				temp.setText("Mon");
			if (i == 2)
				temp.setText("Tue");
			if (i == 3)
				temp.setText("Wed");
			if (i == 4)
				temp.setText("Thu");
			if (i == 5)
				temp.setText("Fri");
			if (i == 6)
				temp.setText("Sat");

			tempVBox.getChildren().addAll(temp, days[i]);
			daysHBox.getChildren().addAll(tempVBox);
		}

		recurringHBox.getChildren().addAll(recurring, daysHBox);

		createDateHBox = new HBox();
		createDateHBox.setSpacing(15);
		createDateHBox.setAlignment(Pos.CENTER_LEFT);

		dateLabel = new Label();
		dateLabel.setText("Date:");
		dateLabel.setId("DefaultLabel");

		createDateVBox = new VBox();
		createDateVBox.setMinSize(Screen.getPrimary().getBounds().getWidth() / 11.78, 39);
		createDateVBox.setId("DateVBox");

		createDate = new Label();
		createDate.setId("DefaultLabel");

		createDateVBox.getChildren().addAll(createDate);

		createDateHBox.getChildren().addAll(dateLabel, createDateVBox);

		recurringDateHBox = new HBox();
		recurringDateHBox.setSpacing(13);
		recurringDateHBox.setAlignment(Pos.CENTER_LEFT);

		recurringDate = new Label();
		recurringDate.setText("Date:");
		recurringDate.setId("DefaultLabel");

		recurringFromDateVBox = new VBox();
		recurringFromDateVBox.setMinWidth(Screen.getPrimary().getBounds().getWidth() / 11.78);
		recurringFromDateVBox.setId("DateVBox");

		recurringFromDate = new Label();
		recurringFromDate.setId("DefaultLabel");

		recurringFromDateVBox.getChildren().addAll(recurringFromDate);

		recurringDateLabel = new Label();
		recurringDateLabel.setText("to");
		recurringDateLabel.setId("DefaultLabel");

		recurringToDate = new TextField();
		recurringToDate.setText("MM/DD/YYYY");
		recurringToDate.setId("TextField");

		recurringDateHBox.getChildren().addAll(recurringDate, recurringFromDateVBox, recurringDateLabel,
				recurringToDate);

		createTimeHBox = new HBox();
		createTimeHBox.setSpacing(15);
		createTimeHBox.setAlignment(Pos.CENTER_LEFT);

		timeLabel = new Label();
		timeLabel.setText("Time:");
		timeLabel.setId("DefaultLabel");

		createFrom = new ComboBox<String>();
		createFrom.setMinWidth(Screen.getPrimary().getBounds().getWidth() / 11.78);
		createFrom.getStyleClass().add("ComboBox");

		createToLabel = new Label("to");
		createToLabel.setId("DefaultLabel");

		createTo = new ComboBox<String>();
		createTo.setMinWidth(Screen.getPrimary().getBounds().getWidth() / 11.78);
		createTo.getStyleClass().add("ComboBox");

		createTimeHBox.getChildren().addAll(timeLabel, createFrom, createToLabel, createTo);

		createButtonHBox = new HBox();
		createButtonHBox.setAlignment(Pos.CENTER_RIGHT);
		createButtonHBox.setSpacing(10);

		save = new Button("Save");
		save.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		save.getStyleClass().add("ButtonSave");

		discard = new Button("Discard");
		discard.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		discard.getStyleClass().add("ButtonDiscard");

		createButtonHBox.getChildren().addAll(save, discard);

		getChildren().addAll(createName, recurringHBox, createDateHBox, createTimeHBox, createButtonHBox);

		times = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			String hr = "";
			if (i < 10)
				hr = "0" + i;
			else
				hr += i;

			times.add(hr + ":00");
			times.add(hr + ":30");
		}

		for (String str : times) {
			createFrom.getItems().add(str);
			createTo.getItems().add(str);
		}
	}

	public void update() {
		createDate.setText("" + (calendar.getCurrentMonth().getMonth() + 1) + "/" + calendar.getSelectedDate() + "/"
				+ calendar.getCurrentYear());
		recurringFromDate.setText("" + (calendar.getCurrentMonth().getMonth() + 1) + "/" + calendar.getSelectedDate()
				+ "/" + calendar.getCurrentYear());
	}

	private void initHandlers() {
		recurring.setOnAction(e -> {
			if (!getChildren().isEmpty())
				getChildren().removeAll(getChildren());
			if (recurring.isSelected()) {
				getChildren().addAll(createName, recurringHBox, recurringDateHBox, createTimeHBox, createButtonHBox);
				for (int i = 0; i < days.length; i++)
					days[i].setDisable(false);
			} else if (!recurring.isSelected()) {
				getChildren().addAll(createName, recurringHBox, createDateHBox, createTimeHBox, createButtonHBox);
				for (int i = 0; i < days.length; i++) {
					days[i].setDisable(true);
					days[i].setSelected(false);
				}
			}
		});

		save.setOnAction(e -> {
			if (!recurring.isSelected()) {
				if (createName.getText().isEmpty() || createFrom.getSelectionModel().isEmpty() || createTo.getSelectionModel().isEmpty())
					invalidAlert ("Input is invalid/incomplete");
				else {
					SimpleDateFormat time = new SimpleDateFormat("HH:mm");
					try {
						if (time.parse(createFrom.getValue()).after(time.parse(createTo.getValue())))
							invalidAlert ("The start time " + createFrom.getValue() + " is after the end time " + createTo.getValue());
						else {
							int retVal = dc.createInsert(createName.getText(),
										"" + calendar.getCurrentYear() + "-" + (calendar.getCurrentMonth().getMonth() + 1) + "-"
										+ calendar.getSelectedDate(),
										createFrom.getValue() + ":00", createTo.getValue() + ":00",
										Integer.toString(DatabaseModel.getInstance().getUserID()));
							if (retVal == -1)
								System.out.println("time conflict");
							System.out.println(retVal);
						}
					}
					catch (Exception e1) {
						invalidAlert ("Time mismatch error");
					}
				}
			}

			else if (recurring.isSelected()) {
				boolean gb = false;
				for (CheckBox cb: days)
					if (cb.isSelected()) {
						gb = true;
						break;
					}
				
				if (createName.getText().isEmpty() || createFrom.getSelectionModel().isEmpty() || createTo.getSelectionModel().isEmpty() || recurringToDate.getText().isEmpty())
					invalidAlert ("Input is invalid/incomplete");
				else if (!gb)
					invalidAlert ("No recurring days selected");
				else {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat time = new SimpleDateFormat("HH:mm");
						Date sDate = sdf.parse(calendar.getCurrentYear() + "-" + (calendar.getCurrentMonth().getMonth() + 1)
								+ "-" + calendar.getSelectedDate());
						String[] sEnd = recurringToDate.getText().split("[/]+");
						Date eDate = sdf.parse(sEnd[2] + "-" + sEnd[0] + "-" + sEnd[1]);
						Calendar cal = Calendar.getInstance();	
						cal.setTime(sDate);
						int i = cal.get (Calendar.DAY_OF_WEEK);
						int retVal;
						
						if (time.parse(createFrom.getValue()).after(time.parse(createTo.getValue())))
							invalidAlert ("The start time " + createFrom.getValue() + " is after the end time " + createTo.getValue());
						else if (sDate.after(eDate))
							invalidAlert ("The start date " + recurringFromDate.getText() + " is after the end date " + recurringToDate.getText());
						else {
							while (sDate.compareTo(eDate) <= 0) {
								cal.setTime(sDate);
								//CREATE INSERT
								if (cal.get(Calendar.DAY_OF_WEEK) == i && days[i - 1].isSelected()) {
									retVal = dc.createInsert(createName.getText(),
											"" + cal.get (Calendar.YEAR) + "-" + (cal.get (Calendar.MONTH) + 1) + "-"
													+ cal.get (Calendar.DAY_OF_MONTH),
											createFrom.getValue() + ":00", createTo.getValue() + ":00",
											Integer.toString(DatabaseModel.getInstance().getUserID()));
									if (retVal == -1)
										System.out.println("time conflict");
								}
								if (i == 7)
									i = 0;
								i++;
								//DAY COUNTER
								cal.add(Calendar.DAY_OF_MONTH, 1);
								String dayIncrement = sdf.format(cal.getTime());
								sDate = sdf.parse(dayIncrement);
							}
						}
					} catch (Exception e1) {
						invalidAlert ("Invalid date format");
					}
				}
			}
		});

		discard.setOnAction(e -> {
			if (recurring.isSelected())
				recurring.fire();
			createName.setText("");
			createFrom.getSelectionModel().select(0);
			createTo.getSelectionModel().select(0);
			recurringToDate.setText("");
			update();
		});
	}
	
	public void invalidAlert (String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait().filter(response -> response == ButtonType.OK ).ifPresent(response -> {return;});
	}
}