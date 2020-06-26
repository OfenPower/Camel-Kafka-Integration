package loanbroker;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class LoanBrokerMain {

	public static void main(String[] args) {

		LoanBrokerRoute loanBrokerRoute = new LoanBrokerRoute();

		CamelContext ctx = new DefaultCamelContext();
		try {
			ctx.addRoutes(loanBrokerRoute);
			/*
			 * // ctx.addRoutes(loanBrokerRoute); ctx.addRoutes(new RouteBuilder() {
			 * 
			 * @Override public void configure() throws Exception { // Kafka Consumer
			 * Endpoint URI String fromKafka =
			 * "kafka:loan-request?brokers=localhost:9092&groupId=groupA&valueDeserializer=loanbroker.LoanRequestMessageDeserializer";
			 * 
			 * // LoanRequestMessage Objekt von Kafka consumen und processen
			 * from(fromKafka).process(new LoanRequestToStringProcessor()).to("file:dest");
			 * 
			 * } });
			 */
			ctx.start();
			System.in.read();
			System.out.println("Program End");
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}