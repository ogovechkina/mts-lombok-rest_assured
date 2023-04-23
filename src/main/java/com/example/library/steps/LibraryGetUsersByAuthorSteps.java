package com.example.library.steps;

import com.example.library.model.CustomerDto;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class LibraryGetUsersByAuthorSteps {

    @Step("Запрос GET /library/authors/{authorId}/users: возвращаем список клиентов по автору")
    public List<CustomerDto> getCustomersByAuthor(Long authorId, int expectedStatus) {
        return Arrays.asList(given()
                .pathParams("authorId", authorId)
                .when()
                .get("/library/authors/{authorId}/users")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .contentType(ContentType.JSON)
                .extract().response()
                .getBody().as(CustomerDto[].class));
    }

    @Step("Проверяем данные полученных клиентов с ожидаемыми данными")
    public void geCustomersByAuthorResponseShouldBeCorrect(List<CustomerDto> actualCustomers, List<CustomerDto> expectedCustomers) {
        assertIterableEquals(expectedCustomers, actualCustomers);
    }
}