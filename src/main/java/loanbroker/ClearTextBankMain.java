package loanbroker;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ClearTextBankMain {

    public static void main(String[] args) {

        ClearTextBankRoute route = new ClearTextBankRoute();
        CamelContext ctx = new DefaultCamelContext();
        try{
            ctx.addRoutes(route);
            ctx.start();
            System.in.read();
            System.out.println("Program End");
            ctx.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class ClearTextBankRoute extends RouteBuilder
{
    @Override
    public void configure() throws Exception {
        String fromKafka = "kafka:bank03?brokers=localhost:9092&groupId=groupA";
        String toKafka = "kafka:loan-response?brokers=localhost:9092";
        from(fromKafka).process(new ClearTextBankProcessor()).to(toKafka);
    }
}

class ClearTextBankProcessor implements Processor
{
    // requestedFunds 123.4,startCapital 123.4,monthlyIncome 123.4,creditScore 5

    static int creditDuration = 10*12;
    static double interestRate = 0.02;

    @Override
    public void process(Exchange exchange) throws Exception {
        String msg = exchange.getIn().getBody(String.class);
        String fields[] = msg.split(",");

        System.out.println("Received the following message: " + msg);

        double requestedFunds = Double.parseDouble(fields[0].split(" ")[1]);
        //double startCapital = Double.parseDouble(fields[1].split(" ")[1]);
        //double monthlyIncome = Double.parseDouble(fields[2].split(" ")[1]);
        //int creditScore = Integer.parseInt(fields[3].split(" ")[1]);

        double monthlyPremiums = requestedFunds / creditDuration;
        monthlyPremiums += monthlyPremiums * interestRate;

        exchange.getIn().setHeader("type","clearText");
        exchange.getIn().setBody("monthlyPremiums " + monthlyPremiums
                + ", durationInMonths " + creditDuration
                + ", grantedCredit " + requestedFunds
                + ", interestRatePerMonth" + interestRate);
        System.out.println("Send the following response: " + exchange.getIn().getBody(String.class));
    }
}