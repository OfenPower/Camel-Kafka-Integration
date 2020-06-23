package GUI;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

import Message.LoanRequestMessage;
import TestPackage.LoanRequestBrokerRoute;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoanRequestMain extends Application {

	private double creditRequestValue;
	private double currentCapitalValue;
	private double monthlyIncomeValue;

	private Button requestLoanButton;

	// private boolean stopCamel = false;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Grid
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Hello there!");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		// Credit Request
		Label creditRequestLabel = new Label("Credit Request:");
		grid.add(creditRequestLabel, 0, 1);
		TextField creditRequestTextField = new TextField();
		grid.add(creditRequestTextField, 1, 1);

		// Current Capital
		Label currentCapitalLabel = new Label("Current Capital:");
		grid.add(currentCapitalLabel, 0, 2);
		TextField currentCapitalTextField = new TextField();
		grid.add(currentCapitalTextField, 1, 2);

		// Current Capital
		Label monthlyIncomeLabel = new Label("Monthly Income:");
		grid.add(monthlyIncomeLabel, 0, 3);
		TextField monthlyIncomeTextField = new TextField();
		grid.add(monthlyIncomeTextField, 1, 3);

		// Ergebnislabel
		Label result = new Label();
		grid.add(result, 1, 5);

		// Button
		requestLoanButton = new Button("Request Loan");
		requestLoanButton.setOnAction(e -> {
			// Loan Request starten, falls Input ints/doubles und nicht leer sind
			if (parseInputValues(creditRequestTextField.getText(), currentCapitalTextField.getText(),
					monthlyIncomeTextField.getText())) {
				startLoanRequest(creditRequestValue, currentCapitalValue, monthlyIncomeValue);
			}

		});
		grid.add(requestLoanButton, 1, 4);

		// Scene initialisieren und anzeigen
		Scene scene = new Scene(grid, 300, 275);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Loan Request");
		primaryStage.show();
	}

	public boolean parseInputValues(String cr, String cc, String mi) {
		String creditRequest = cr;
		String currentCapital = cc;
		String monthlyIncome = mi;

		if (!checkInput(creditRequest)) {
			System.out.println("Credit Request must not be empty and consist only of integer/double values");
			return false;
		}
		if (!checkInput(currentCapital)) {
			System.out.println("Current Capital must not be empty and consist only of integer/double values");
			return false;
		}
		if (!checkInput(monthlyIncome)) {
			System.out.println("Monthly Income must not be empty and consist only of integer/double values");
			return false;
		}

		creditRequestValue = Double.parseDouble(creditRequest);
		currentCapitalValue = Double.parseDouble(currentCapital);
		monthlyIncomeValue = Double.parseDouble(monthlyIncome);

		System.out.println("Input Values are valid!");
		System.out.println("Credit Request: " + creditRequestValue);
		System.out.println("Current Capital: " + currentCapitalValue);
		System.out.println("Monthly Income: " + monthlyIncomeValue);

		return true;
	}

	// Überprüft, ob der String "input" nicht leer ist und nur aus Zahlen besteht
	public boolean checkInput(String input) {
		return (!input.isEmpty() && input.matches("[0-9, /.]{2,}+"));
	}

	// Startet einen LoanRequest mit den Daten Credit Request, Current Capital und
	// Monthly Income
	public void startLoanRequest(double cr, double cc, double mi) {
		// Button disablen
		requestLoanButton.setVisible(false);

		try {
			// Loan Request Werte in Message-Objekt verpacken
			LoanRequestMessage loanRequestMessage = new LoanRequestMessage(cr, cc, mi);
			// CamelContext starten initialisieren
			CamelContext ctx = new DefaultCamelContext();
			// Producer Route zu Kafka aufbauen
			LoanRequestBrokerRoute toBroker = new LoanRequestBrokerRoute();
			ctx.addRoutes(toBroker);
			// Camel Context starten
			ctx.start();
			// loanRequestMessage Objekt an LoanRequestBrokerRoute schicken
			ProducerTemplate producerTemplate = ctx.createProducerTemplate();
			producerTemplate.sendBody("direct:start", loanRequestMessage);
			// Nach 5 Sek. Button wieder enablen
			Thread.sleep(5000);
			ctx.stop();
			requestLoanButton.setVisible(true);

//			if (stopCamel) {
//				ctx.stop();
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
