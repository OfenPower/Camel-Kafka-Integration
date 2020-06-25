package loanbroker;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class LoanBrokerMain {

	public static void main(String[] args) {

		// !!! ToDo: Diese Route hier einbinden und so verändern, dass sie von Kafka
		// liest !!!
		//
		// LoanBrokerRoute loanBrokerRoute = new LoanBrokerRoute();

		CamelContext ctx = new DefaultCamelContext();
		try {
			// ctx.addRoutes(loanBrokerRoute);
			ctx.addRoutes(new RouteBuilder() {

				@Override
				public void configure() throws Exception {
					// Kafka Consumer Endpoint URI
					String fromKafka = "kafka:loan-request?brokers=localhost:9092&groupId=groupA&valueDeserializer=loanbroker.LoanRequestMessageDeserializer";

					// LoanRequestMessage Objekt von Kafka consumen und processen
					from(fromKafka).process(new LoanRequestToStringProcessor()).to("file:dest");

				}
			});
			ctx.start();
			Thread.sleep(5000);
			System.out.println("Program End");
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}