package MyPackage;

import org.apache.camel.builder.RouteBuilder;

public class FileCopyRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		// Liest file aus /src/data und schreibt sie zu /dest
		from("file:src/data?noop=true").process(new StringProcessor()).to("file:dest");

	}

}
