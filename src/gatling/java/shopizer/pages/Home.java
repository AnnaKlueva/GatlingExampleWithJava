package shopizer.pages;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.http.HttpDsl.http;
import static shopizer.SessionVariablesName.CATEGORY_ID;

public class Home {
    private static final String GET_HOME_PAGE_URL = "/api/v1/store/DEFAULT";
    private static final String GET_ALL_CATEGORIES_URL = "/api/v1/category/";
    public static final String TABLE_CATEGORY = "tables";
    public static final String CHAIR_CATEGORY = "chairs";

    public static ChainBuilder goToHomePage = exec(http("Go to home page").get(GET_HOME_PAGE_URL));

    public static ChainBuilder composeGetCategoryRequest(String categoryName) {
        return exec(http("Open "+categoryName+" category page")
                .get(GET_ALL_CATEGORIES_URL)
                .check(
                        jsonPath("$.*[?(@.code == \""+categoryName+"\")].id").saveAs(CATEGORY_ID)
                ));
    }
}
