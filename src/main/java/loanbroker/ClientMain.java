
package loanbroker;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ClientMain {

	public static void main(String[] args) {
		JFrame frame = new JFrame();

		JLabel label1 = new JLabel("Credit Request");
		JLabel label2 = new JLabel("Current Capital");
		JLabel label3 = new JLabel("Monthly Income");
		JTextField text1 = new JTextField();
		JTextField text2 = new JTextField();
		JTextField text3 = new JTextField();
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

		frame.setLayout(new GridLayout(7, 1));
		frame.setSize(640, 480);
		frame.setVisible(true);

	}

	// Startet einen LoanRequest mit den Daten Credit Request, Current Capital und
	// Monthly Income
	public static void startLoanRequest(double cr, double cc, double mi) {
		try {
			// Loan Request Werte in Message-Objekt verpacken
			LoanRequestMessage loanRequestMessage = new LoanRequestMessage(cr, cc, mi);

			// CamelContext starten initialisieren
			CamelContext ctx = new DefaultCamelContext();

			// Producer Route zum Broker über Kafka aufbauen
			ctx.addRoutes(new RouteBuilder() {

				@Override
				public void configure() throws Exception {
					// Kafka Producer Endpoint URI
					String toKafka = "kafka:loan-request?brokers=localhost:9092&serializerClass=loanbroker.LoanRequestMessageSerializer";

					// Alles was an "direct:start" geschickt wird an Kafka weiterleiten
					from("direct:start").to(toKafka);

				}
			});

			// Camel Context starten
			ctx.start();

			// loanRequestMessage Objekt an LoanRequestBrokerRoute schicken
			ProducerTemplate producerTemplate = ctx.createProducerTemplate();
			producerTemplate.start();
			producerTemplate.sendBody("direct:start", loanRequestMessage);

			Thread.sleep(5000);

			// Producer und CamelContext schließen
			producerTemplate.stop();
			ctx.stop();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Überprüft, ob der String "input" nicht leer ist und nur aus Zahlen besteht
	public boolean checkInput(String input) {
		return (!input.isEmpty() && input.matches("[0-9, /.]{2,}+"));
	}

}
