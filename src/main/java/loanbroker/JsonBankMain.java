package loanbroker;

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
    }

    public static class Response{
        public double monthlyPremiums;
        public int durationInMonths;
        public double grantedCredit;
        public double interestRatePerMonth;
    }

    public static void main(String[] args) {
        JsonBankRoute route = new JsonBankRoute();

        CamelContext ctx = new DefaultCamelContext();
        try{
            ctx.addRoutes(route);
            ctx.start();
            System.in.read();
            System.out.println("Program End");
            ctx.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class JsonBankRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        String fromKafka = "kafka:bank01?brokers=localhost:9092&groupId=groupA";
        String toKafka = "kafka:loan-response?brokers=localhost:9092";
        from(fromKafka).process(new JsonBankProcessor()); //.to(toKafka);
    }
}

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

    static int creditDuration = 10*12; // 10 years
    static double interestRate = 0.1; // 10%

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

        String responseJson = mapper.writeValueAsString(response);

        System.out.println("Send the following response: " + responseJson);
        exchange.getIn().setBody(responseJson);
        exchange.getIn().setHeader("type","json");
    }
}
