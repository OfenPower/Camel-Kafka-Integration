package loanbroker;

import org.apache.camel.builder.RouteBuilder;

/*
 * Route zwischen Banken und LoanBroker, über welche alle CreditRequest Angebote empfangen, normalisiert und
 * aggregiert werden. Nach der Aggregation bleibt ein einziges Angebot übrig, welches an den
 * Client zurückgesendet wird.
 */
public class LoanBrokerResponseRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		// Step 1: Alle Angebote empfangen und zu einer .json Datei mit identischem Format normalisieren
		from("kafka:loan-response?brokers=localhost:9092&groupId=loanBroker")
		// Normalizer. Wandelt alle Angebotsdateiformate in .json um
		// 
		// Falls Angebot im .json Format -> normalizeJson() aufrufen
		// Falls Angebot im .json Format -> normalizeXml() aufrufen
		// Falls Angebot im .json Format -> normalizeClearText() aufrufen
		.choice()
			.when(simple("${header.type} == 'json'")).bean(Normalizer.class, "normalizeJson")
			.when(simple("${header.type} == 'xml'")).bean(Normalizer.class, "normalizeXml")
			.when(simple("${header.type} == 'clearText'")).bean(Normalizer.class, "normalizeClearText")
		.end()
		// Step 2: Aggregate, sodass aus allen Angeboten das Beste für den Kunden zurückgeliefert wird
		//
		// Correlation: Zusammengehörende Messages eines LoanRequests anhand deren "correlationId" ermitteln
		// Completeness Condition: 2 Stück: - Aggregierung ist fertig, wenn entweder 10sek vergangen sind, oder
		//								    - Alle erwarteten Messages auch wirklich ankamen und verarbeitet wurden
		// Aggregation Algorithm: siehe "BankResponseAggregationStrategy".
		// Als Ergebnis der Aggregation bleibt ein einziges, bestes Angebot übrig
		.aggregate(new CorrelationExpression(), new BankResponseAggregationStrategy()).completionTimeout(10000)
		// Step 3: Translator-Processor, welche unnötige Felder des Angebots für den Client entfernt und
		// die Angebotsmessage für den Client aufbereitet
		.process(new CleanUpProcessor())
		// Bestes Angebot an Client über das Kafka-Topic "broker-response" an den Client zurücksenden
		.to("kafka:broker-response?brokers=localhost:9092");
	}

}
