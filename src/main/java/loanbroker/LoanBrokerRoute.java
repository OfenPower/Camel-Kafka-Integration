package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class LoanBrokerRoute extends RouteBuilder {

	@Override
	public void configure() {

		from("jms:queue:loan")
				// let the credit agency do the first work
				.process(new CreditAgencyProcessor())
				// send the request to the three banks
				.multicast(new BankResponseAggregationStrategy()).parallelProcessing()
				.to("jms:queue:bank1", "jms:queue:bank2", "jms:queue:bank3").end()
				// and prepare the reply message
				.process(new ReplyProcessor());

		// Each bank processor will process the message and put the response message
		// back
		from("jms:queue:bank1").process(new BankProcessor("bank1"));
		from("jms:queue:bank2").process(new BankProcessor("bank2"));
		from("jms:queue:bank3").process(new BankProcessor("bank3"));
	}

}