
package client;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import loanbroker.LoanRequestMessage;

public class ClientMain {

	public static void main(String[] args) {
		/*
		 * UI für Client Loan Request starten
		 */
		JFrame frame = new JFrame();

		JLabel label1 = new JLabel("Credit Request");
		JLabel label2 = new JLabel("Current Capital");
		JLabel label3 = new JLabel("Monthly Income");
		JTextField text1 = new JTextField();
		JTextField text2 = new JTextField();
		JTextField text3 = new JTextField();
		//JTextField text4 = new JTextField("Received Offer:");
		//JTextField text5 = new JTextField("Received Offer:");
		JButton button = new JButton("Send Request");

		button.addActionListener(e -> {
			double creditRequest = Double.parseDouble(text1.getText());
			double currentCapital = Double.parseDouble(text2.getText());
			double monthlyIncome = Double.parseDouble(text3.getText());
			startLoanRequest(creditRequest, currentCapital, monthlyIncome);
		});

		frame.add(label1);
		frame.add(text1);
		frame.add(label2);
		frame.add(text2);
		frame.add(label3);
		frame.add(text3);
		frame.add(button);
		//frame.add(text4);
		//frame.add(text5);

		frame.setLayout(new GridLayout(7, 1));
		frame.setSize(640, 480);
		frame.setVisible(true);

	}

	// Startet einen LoanRequest mit den Daten Credit Request, Current Capital und
	// Monthly Income
	public static void startLoanRequest(double creditRequest, double currentCapital, double monthlyIncome) {
		try {
			// Loan Request Werte in Message-Objekt verpacken
			// Ein LoanRequest besteht aus
			// - CreditRequest
			// - CurrentCapital 
			// - monatliches Einkommen
			LoanRequestMessage loanRequestMessage = new LoanRequestMessage(creditRequest, currentCapital, monthlyIncome);

			// CamelContext starten initialisieren
			CamelContext ctx = new DefaultCamelContext();

			// Producer Route zum Loan Broker über Kafka aufbauen
			ctx.addRoutes(new RouteBuilder() {

				@Override
				public void configure() throws Exception {
					// Kafka Producer Endpoint URI
					String toKafka = "kafka:loan-request?brokers=localhost:9092&serializerClass=loanbroker.LoanRequestMessageSerializer";

					// Alles was an "direct:start" geschickt wird an Kafka weiterleiten
					from("direct:start").to(toKafka);

				}
			});
			
			// Rückroute für die Antwort des Brokers mit dem Bankangebot anlegen
			ctx.addRoutes(new RouteBuilder() {

				@Override
				public void configure() throws Exception {
					// Kafka Producer Endpoint URI
					String fromKafka = "kafka:broker-response?brokers=localhost:9092";

					// 
					from(fromKafka).process(e -> {
						String answer = e.getIn().getBody(String.class);
						System.out.println(answer);
					});
					
				}
						
			});
					

			// Camel Context starten
			ctx.start();

			// loanRequestMessage Objekt an direct:start und damit an Kafka schicken
			ProducerTemplate producerTemplate = ctx.createProducerTemplate();
			producerTemplate.start();
			producerTemplate.sendBody("direct:start", loanRequestMessage);
			
			System.in.read();
			//Thread.sleep(2000);

			// Producer und CamelContext schließen
			producerTemplate.stop();
			ctx.stop();
			ctx.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Überprüft, ob der String "input" nicht leer ist und nur aus Zahlen besteht
	public boolean checkInput(String input) {
		return (!input.isEmpty() && input.matches("[0-9, /.]{2,}+"));
	}

}
