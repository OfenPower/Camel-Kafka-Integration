package TestPackage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class LoanRequestProcessorTest implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// Message mit getIn() holen:
		// - InOnly = Message fire and forget
		// - InOut = Message fire und reply erwarten
		// System.out.println("Process!");

		System.out.println("Process before getIn()");

		// LoanRequestMessage body = exchange.getIn().getBody(LoanRequestMessage.class);

		// LoanRequestMessage body = (LoanRequestMessage)
		// exchange.getIn().getBody(Object.class);

		String body = exchange.getIn().getBody(String.class);

		if (body == null) {
			System.out.println("body = null!");
		}

		System.out.println("Process after getIn()");
		System.out.println(body);

//		System.out.println("Received Credit Request: " + body.getCreditRequest());
//		System.out.println("Received Current Capital: " + body.getCurrentCapital());
//		System.out.println("Received Monthly Income: " + body.getMonthlyIncome());

		// exchange.getIn().setBody("Hello! " + body);

		// process blöaba

		// Neue (!) Output Message erzeugen:
		// getOut() erzeugt eine neue Message. Diese braucht den gleichen
		// Header/Attachments wie die In-Message
		// exchange.getOut().setBody("Message changed! " + body);
		// exchange.getOut().setHeaders(exchange.getIn().getHeaders());
	}

}
