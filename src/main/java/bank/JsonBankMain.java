package bank;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonBankMain {

    public static class Request{
        public double requestedFunds;
        public double startCapital;
        public double monthlyIncome;
        public int creditScore;
        public int correlationId;
    }

    public static class Response{
        public double monthlyPremiums;
        public int durationInMonths;
        public double grantedCredit;
        public double interestRatePerMonth;
        public int correlationId;
    }
    
    /*
	 * Startet die Json-Bank, welche auf dem Kafka-Topic "bank01" lauscht und für
	 * einen CreditRequest ein Angebot zurücksendet 
	 */
    public static void main(String[] args) {
    	// Route zwischen LoanBroker und Bank 
        JsonBankRoute route = new JsonBankRoute();
        
        // Camel Context starten
        CamelContext ctx = new DefaultCamelContext();
        try{
            ctx.addRoutes(route);
            ctx.start();
            System.in.read();
            System.out.println("Program End");
            ctx.stop();
            ctx.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/*
 * Route zwischen LoanBroker und Bank
 */
class JsonBankRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
    	// Messages vom Topic "bank01" lesen
        String fromKafka = "kafka:bank01?brokers=localhost:9092&groupId=jsonBank";
        
        // Angebot an Topic "loan-response" zurücksenden
        String toKafka = "kafka:loan-response?brokers=localhost:9092";
        
        // Route aufbauen
        from(fromKafka).process(new JsonBankProcessor()).to(toKafka);
    }
}

/*
 * Processor, welcher für einen CreditRequest ein Angebot berechnet
 */
class JsonBankProcessor implements Processor{

    /* IN
    {
        "requestedFunds" : 123.4,
        "startCapital" : 123.4,
        "monthlyIncome" : 123.4,
        "creditScore" : 5
    }
    OUT
    {
        "monthlyPremiums" : 123.4,
        "durationInMonths" : 1,
        "grantedCredit" : 123.4,
        "interestRatePerMonth" : 123.4
    }
    */

    static int creditDuration = 10*12; // 10 Jahre Laufzeit
    static double interestRate = 0.1; // 10% Zinssatz (schlecht)

    @Override
    public void process(Exchange exchange) throws Exception {
        String json = exchange.getIn().getBody(String.class);

        System.out.println("Received the following message: " + json);

        ObjectMapper mapper = new ObjectMapper();

        JsonBankMain.Request request = mapper.readValue(json,JsonBankMain.Request.class);
        JsonBankMain.Response response = new JsonBankMain.Response();



        response.durationInMonths = creditDuration;
        response.grantedCredit = request.requestedFunds;
        response.interestRatePerMonth = interestRate;
        response.monthlyPremiums = request.requestedFunds / response.durationInMonths;
        response.monthlyPremiums += response.monthlyPremiums * response.interestRatePerMonth;
        response.correlationId = request.correlationId;

        String responseJson = mapper.writeValueAsString(response);

        System.out.println("Send the following response: " + responseJson);
        
        // Angebotsmessage versenden
        exchange.getIn().setHeader("type","json");
        exchange.getIn().setBody(responseJson);
    }
}
