package TestPackage;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelMainTest {
	public static void main(String[] args) {

		BrokerConsumerTestRoute testRoute = new BrokerConsumerTestRoute();
		CamelContext ctx = new DefaultCamelContext();
		try {
			// ctx.addRoutes(fileCopyRoute);
			// ctx.addRoutes(kafkaProducerBuilder);
			ctx.addRoutes(testRoute);
			ctx.start();
			Thread.sleep(5000);
			System.out.println("Program End");
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
