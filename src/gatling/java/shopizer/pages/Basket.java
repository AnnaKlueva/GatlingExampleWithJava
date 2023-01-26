package shopizer.pages;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;
import static shopizer.SessionVariablesName.BASKET_ID;

public class Basket {
    public static ChainBuilder proceedToCheckout = exec(http("Proceed to checkout")
            .post("/api/v1/cart/#{"+BASKET_ID+"}/shipping")
            .body(StringBody("{}"))
            .check(status().is(200))
    );
}
