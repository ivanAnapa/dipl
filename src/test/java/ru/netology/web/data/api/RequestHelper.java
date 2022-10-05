package ru.netology.web.data.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RequestHelper {

    private final String buyEndpoint = "/api/v1/pay";
    private final String buyWithCreditEndpoint = "/api/v1/credit";

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static String buyReq(String cardNumber,
                                String cardYear,
                                String cardMonth,
                                String cardOwner,
                                String cardCvc) {
        Map<String, Object> map = new HashMap<>();
        map.put("number", cardNumber);
        map.put("year", cardYear);
        map.put("month", cardMonth);
        map.put("holder", cardOwner);
        map.put("cvc", cardCvc);

        return given()
                .spec(requestSpec)
                .body(map)
                .when()
                .post(buyEndpoint)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();
    }

    public static String buyWithCreditReq(String cardNumber,
                                          String cardYear,
                                          String cardMonth,
                                          String cardOwner,
                                          String cardCvc) {
        Map<String, Object> map = new HashMap<>();
        map.put("number", cardNumber);
        map.put("year", cardYear);
        map.put("month", cardMonth);
        map.put("holder", cardOwner);
        map.put("cvc", cardCvc);

        return given()
                .spec(requestSpec)
                .body(map)
                .when()
                .post(buyWithCreditEndpoint)
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();
    }

}
