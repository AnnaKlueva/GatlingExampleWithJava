package shopizer.pages;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;
import static shopizer.SessionVariablesName.*;

public class Product {

    private static final String GET_ALL_PRODUCTS_URL = "/api/v1/products/";
    private static final String POST_PRODUCT_TO_CARD = "/api/v1/cart";
    private static final String PUT_PRODUCT_TO_CARD = "/api/v1/cart/#{"+BASKET_ID+"}";
    private static final String GET_CARD = "/api/v1/cart/#{" + BASKET_ID + "}";
    private static final String ADD_PRODUCT_BODY_PLACEHOLDER = "{\"product\": \"SKU_PLACEHOLDER\", \"quantity\": 1}";
    private static final String bodyAddProductRequest = ADD_PRODUCT_BODY_PLACEHOLDER.replace("SKU_PLACEHOLDER", "#{"+SKU+"}" );

    public static ChainBuilder selectProduct = exec(http("Select product from category: #{"+CATEGORY_ID+"}")
            .get(GET_ALL_PRODUCTS_URL)
            .queryParam("category", "#{"+CATEGORY_ID+"}")
            .check(
                    jsonPath("$.products..sku").findRandom().saveAs(SKU)
            )
    );

    private static ChainBuilder addProductToEmptyBasket =
            exec(http("Add #{" + SKU + "} to basket")
                    .post(POST_PRODUCT_TO_CARD)
                    .body(StringBody(bodyAddProductRequest))
                    .check(
                            jsonPath("$.code").saveAs(BASKET_ID)
                    )
            );

    private static ChainBuilder addProductToAlreadyExistedBasket =
            exec(http("Add #{"+SKU+"} to basket")
                    .put(PUT_PRODUCT_TO_CARD)
                    .body(StringBody(bodyAddProductRequest))
            );

    public static ChainBuilder addProductToBasket =
            doIfOrElse(session -> session.get("basketId") == null)
                    .then(addProductToEmptyBasket)
                    .orElse(addProductToAlreadyExistedBasket);

    public static ChainBuilder navigateToBasket = exec(http("Navigate to Basket")
            .get(GET_CARD)
            .check(status().is(200))
    );
}
