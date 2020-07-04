package bank;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ClearTextBankMain {
	
	/*
	 * Startet die ClearText-Bank, welche auf dem Kafka-Topic "bank03" lauscht und für
	 * einen CreditRequest ein Angebot zurücksendet 
	 */
    public static void main(String[] args) {

    	// Route zwischen LoanBroker und Bank aufbauen
        ClearTextBankRoute route = new ClearTextBankRoute();
        
        // Camel Context starten
        CamelContext ctx = new DefaultCamelContext();
        try{
            ctx.addRoutes(route);
            ctx.start();
            System.in.read();
            System.out.println("Program End");
            ctx.stop();
            ctx.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/*
 * Route zwischen LoanBroker und Bank
 */
class ClearTextBankRoute extends RouteBuilder
{
    @Override
    public void configure() throws Exception {
    	// Messages vom Topic "bank03" lesen
        String fromKafka = "kafka:bank03?brokers=localhost:9092&groupId=clearTextBank";
        
        // Angebot an Topic "loan-response" zurücksenden
        String toKafka = "kafka:loan-response?brokers=localhost:9092";
        
        // Route aufbauen
        from(fromKafka).process(new ClearTextBankProcessor()).to(toKafka);
    }
}

/*
 * Processor, welcher für einen CreditRequest ein Angebot berechnet
 */
class ClearTextBankProcessor implements Processor
{
    static int creditDuration = 10*12;	// 10 Jahre Kreditlaufzeit
    static double interestRate = 0.02;	// 2% Zinssatz (sehr gut)

    @Override
    public void process(Exchange exchange) throws Exception {
        
    	// Eingehende Message:
    	// requestedFunds 123.4,startCapital 123.4,monthlyIncome 123.4,creditScore 5
    	String msg = exchange.getIn().getBody(String.class);
        String fields[] = msg.split(",");
        
        System.out.println("Received the following message: " + msg);

        double requestedFunds = Double.parseDouble(fields[0].split(" ")[1]);
        //double startCapital = Double.parseDouble(fields[1].split(" ")[1]);
        //double monthlyIncome = Double.parseDouble(fields[2].split(" ")[1]);
        //int creditScore = Integer.parseInt(fields[3].split(" ")[1]);
        int correlationId = Integer.parseInt(fields[4].split(" ")[1]);

        double monthlyPremiums = requestedFunds / creditDuration;
        monthlyPremiums += monthlyPremiums * interestRate;
        
        // Antwortmessage erzeugen
        String responseClearText = "monthlyPremiums " + monthlyPremiums
                + ",durationInMonths " + creditDuration
                + ",grantedCredit " + requestedFunds
                + ",interestRatePerMonth " + interestRate
                + ",correlationId " + correlationId;
        
        System.out.println("Send the following response: " + responseClearText);
        
        // Angebotsmessage versenden
        exchange.getIn().setHeader("type","clearText");
        exchange.getIn().setBody(responseClearText);
    }
}