package shopizer;


import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;


import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static shopizer.pages.Basket.proceedToCheckout;
import static shopizer.pages.Home.*;
import static shopizer.pages.Product.*;

public class BasicScenarios {

    private static final Duration MIN_PAUSE = Duration.ofSeconds(1);
    private static final Duration ZERO_PAUSE = Duration.ofSeconds(0);

    //Test scenario builder
    public static ScenarioBuilder users = scenario("Anonymous users")
            .exec(goToHomePage)
            .pause(MIN_PAUSE)
            .exec(composeGetCategoryRequest(TABLE_CATEGORY))
            .exec(selectProduct)
            .pause(MIN_PAUSE)
            .exec(addProductToBasket)
            .pause(MIN_PAUSE)
            .randomSwitch().on(
                    Choice.withWeight(50.0,
                                exec(composeGetCategoryRequest(CHAIR_CATEGORY))
                                .exec(selectProduct)
                                .pause(MIN_PAUSE)
                                .exec(addProductToBasket)),
                    Choice.withWeight(50.0, pause(ZERO_PAUSE)))
            .randomSwitch().on(
                    Choice.withWeight(30.0, exec(navigateToBasket).exec(proceedToCheckout)),
                    Choice.withWeight(70.0, pause(ZERO_PAUSE)));
}
