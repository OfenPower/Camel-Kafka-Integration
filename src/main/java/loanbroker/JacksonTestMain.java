package loanbroker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class JacksonTestMain {

    public static void main(String[] args) {

        CamelContext ctx = new DefaultCamelContext();

        try {
            ctx.addRoutes(new JsonRouteTest());
            ctx.addRoutes(new JsonRouteConsumer());
            ctx.start();
            ProducerTemplate producer = ctx.createProducerTemplate();
            producer.start();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.createObjectNode();
            ObjectNode obj = (ObjectNode)node;
            obj.put("creditRequest",123.456);
            obj.put("currentCapital",456.789);
            obj.put("monthlyIncome",0.0);


            producer.sendBody("direct:start",node.toString());


            System.in.read();
            ctx.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

class JsonRouteTest extends RouteBuilder
{
    @Override
    public void configure() throws Exception {
        from("direct:start").to("kafka:json?brokers=localhost:9092");
    }
}

class JsonRouteConsumer extends RouteBuilder
{
    @Override
    public void configure() throws Exception {
        from("kafka:json?brokers=localhost:9092&groupId=123").process(new JsonProcessor());
    }
}

class JsonProcessor implements Processor
{
    @Override
    public void process(Exchange exchange) throws Exception {
        String jsonString = exchange.getIn().getBody(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);
        System.out.println("CreditRequest: " + node.get("creditRequest"));
    }
}