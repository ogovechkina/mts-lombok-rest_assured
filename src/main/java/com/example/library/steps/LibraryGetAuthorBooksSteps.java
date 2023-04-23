package com.example.library.steps;

import com.example.library.model.AuthorBookDto;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class LibraryGetAuthorBooksSteps {

    @Step("Запрос GET /library/authors/{authorId}/books: возвращаем список книг по автору")
    public List<AuthorBookDto> getAuthorBooks(Long authorId, int expectedStatus) {
        return Arrays.asList(given()
                .pathParams("authorId", authorId)
                .when()
                .get("/library/authors/{authorId}/books")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .contentType(ContentType.JSON)
                .extract().response()
                .getBody().as(AuthorBookDto[].class));
    }

    @Step("Проверяем данные полученных книг клиента с ожидаемыми данными")
    public void getAuthorBooksResponseShouldBeCorrect(List<AuthorBookDto> actualAuthorBooks, List<AuthorBookDto> expectedAuthorBooks) {
        assertIterableEquals(expectedAuthorBooks, actualAuthorBooks);
    }
}