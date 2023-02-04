package shopizer;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static shopizer.BasicScenarios.users;

public class ShopizerSimulation extends Simulation {
    private static final String BASE_URL = "http://localhost:8081";
    HttpProtocolBuilder httpProtocol = http.baseUrl(BASE_URL).header("Content-Type", "application/json");

    //SystemProperties in secs
    private static final int RAMP_UP_USERS = Integer.parseInt(System.getProperty("rampUpUsers", "10"));
    private static final int RAMP_UP_TIME = Integer.parseInt(System.getProperty("rampUpTime", "10"));
    private static final int CONSTANT_LOAD_USERS = Integer.parseInt(System.getProperty("constantUsers", "10"));
    private static final int CONSTANT_LOAD_TIME = Integer.parseInt(System.getProperty("constantTime", "10"));

    //Additional parameters
    private static final int MAX_RESPONSE_TIME = 50; //ms
    private static final double PERCENTAGE_OF_SUCCESSFUL_RESULTS = 95.0; //%

    //Setup Load injection model
    {
        setUp(
                users.injectOpen(
                        rampUsersPerSec(0).to(RAMP_UP_USERS).during(RAMP_UP_TIME),
                        constantUsersPerSec(CONSTANT_LOAD_USERS).during(CONSTANT_LOAD_TIME),
                        rampUsersPerSec(RAMP_UP_USERS).to(0).during(RAMP_UP_TIME))
        ).protocols(httpProtocol)
         .assertions(
                 global().responseTime().max().lt(MAX_RESPONSE_TIME),//50 ms
                 global().successfulRequests().percent().gt(PERCENTAGE_OF_SUCCESSFUL_RESULTS)
         );
    }
}
