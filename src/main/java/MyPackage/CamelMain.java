package MyPackage;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelMain {

	public static void main(String[] args) {
		KafkaProducerRouteBuilder kafkaProducerBuilder = new KafkaProducerRouteBuilder();
		KafkaConsumerRouteBuilder kafkaConsumerRoute = new KafkaConsumerRouteBuilder();
		FileCopyRouteBuilder fileCopyRoute = new FileCopyRouteBuilder();
		CamelContext ctx = new DefaultCamelContext();
		try {
			// ctx.addRoutes(fileCopyRoute);
			// ctx.addRoutes(kafkaProducerBuilder);
			ctx.addRoutes(kafkaConsumerRoute);
			ctx.start();
			Thread.sleep(200000);
			System.out.println("Program End");
			ctx.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}