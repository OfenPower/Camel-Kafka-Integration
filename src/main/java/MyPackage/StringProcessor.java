package MyPackage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class StringProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {
		// Message mit getIn() holen:
		// - InOnly = Message fire and forget
		// - InOut = Message fire und reply erwarten
		// System.out.println("Process!");

		String body = exchange.getIn().getBody(String.class);
		System.out.println(body);

		// exchange.getIn().setBody("Hello! " + body);

		// process blöaba

		// Neue (!) Output Message erzeugen:
		// getOut() erzeugt eine neue Message. Diese braucht den gleichen
		// Header/Attachments wie die In-Message
		// exchange.getOut().setBody("Message changed! " + body);
		// exchange.getOut().setHeaders(exchange.getIn().getHeaders());
	}
}
