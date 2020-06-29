package loanbroker;

import org.apache.camel.builder.RouteBuilder;

public class LoanBrokerRoute extends RouteBuilder {

	@Override
	public void configure() {

		String fromKafka = "kafka:loan-request?brokers=localhost:9092&groupId=groupA&valueDeserializer=loanbroker.LoanRequestMessageDeserializer&sessionTimeoutMs=30000";
		from(fromKafka)
				// translate the LoanRequestMessage to canonical Json model
				/*
				{
					"creditRequest" : 123.4,
					"currentCapital" : 123.4,
					"monthlyIncome" : 123.4,
				}
				 */
				.process(new LoanRequestToJsonProcessor())
				// calculate a credit score and add it as a new field
				/*
				{
					"creditRequest" : 123.4,
					"currentCapital" : 123.4,
					"monthlyIncome" : 123.4,
					"creditScore" : 5
				}
				 */
				.process(new CreditAgencyProcessor())
				// check Credit Score and add recipient banks according to the credit score
				/*
				{
					"creditRequest" : 123.4,
					"currentCapital" : 123.4,
					"monthlyIncome" : 123.4,
					"creditScore" : 5,
					"bankList" : [bank01, bank02, bank03]
				}
				*/
				.process(new RuleBasedBankProcessor())
				// send the request to the three banks and prepare the reply message
				.multicast().parallelProcessing()
					.bean(DynamicRouterBean.class,"route")
				.end();
				
		// Each bank processor will process the message and put the response message
		// back
		from("direct:bank01").process(new ToJsonBankTranslator()).to("kafka:bank01?brokers=localhost:9092");
		from("direct:bank02").process(new ToXmlBankTranslator()).to("kafka:bank02?brokers=localhost:9092");
		from("direct:bank03").process(new ToClearTextBankTranslator()).to("kafka:bank03?brokers=localhost:9092");
		
		
//		
//		from("kafka:loan-response?brokers=localhost:9092&groupId=groupA")
//			.choice()
//				.when(simple("${header.type}==json")).bean(Normalizer.class, "normalizeJson")
//				.when(simple("${header.type}==xml")).bean(Normalizer.class, "normalizeXml")
//				.when(simple("${header.type}==clearText")).bean(Normalizer.class, "normalizeClearText")
//			.end();
		
		
		
		//process(new PrintMessageProcessor());
		
		//from("kafka:bank-response?brokers=localhost:9092&groupId=groupA&")
			//.process(new Normalizer())
			//.aggregate(new BankResponseAggregationStrategy())
		//.to("kafka:client-response?brokers=localhost:9092&groupId=groupA&");
	}

}