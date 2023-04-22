package com.example.library.steps;

import com.example.library.model.UserBookDto;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class LibraryGetUserBooksSteps {

    @Step("Запрос GET /library/users/{userId}/books: возвращаем список книг по клиенту")
    public List<UserBookDto> getUserBooks(Long userId, int expectedStatus) {
        return Arrays.asList(given()
                .pathParams("userId", userId)
                .when()
                .get("/library/users/{userId}/books")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .contentType(ContentType.JSON)
                .extract().response()
                .getBody().as(UserBookDto[].class));
    }

    @Step("Проверяем данные полученных книг клиента с ожидаемыми данными")
    public void getUserBooksResponseShouldBeCorrect(List<UserBookDto> actualUserBooks, List<UserBookDto> expectedUserBooks) {
        assertIterableEquals(expectedUserBooks, actualUserBooks);
    }
}