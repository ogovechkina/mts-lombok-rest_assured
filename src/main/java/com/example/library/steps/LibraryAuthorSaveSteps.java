package com.example.library.steps;

import com.example.library.entity.Author;
import com.example.library.model.AuthorSaveRequest;
import com.example.library.model.AuthorSaveResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import java.lang.reflect.Type;

import static com.example.library.utils.AssertUtils.errorMsg;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibraryAuthorSaveSteps {

    @Step("Запрос POST /library/author/save: возвращаем ID сохраненного автора")
    public AuthorSaveResponse postAuthorSave(AuthorSaveRequest request, int expectedStatus) {
        return given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/library/authors/save")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract().response()
                .getBody().as((Type) AuthorSaveResponse.class);
    }

    @Step("Проверяем ответ запроса на сохранение автора")
    public void authorSaveResponseShouldBeCorrect(AuthorSaveResponse response) {
        assertTrue(response.getAuthorId() > 0, "authorId должно быть больше 0.");
    }

    @Step("Проверяем данные сохраненного автора с данными отправленными в запросе на сохранение")
    public void savedAuthorDataShouldBeEqualsToRequestData(Author savedAuthor, AuthorSaveRequest request) {
        assertEquals(request.getFirstName(), savedAuthor.getFirstName(), errorMsg("firstName"));
        if (request.getFirstName()!= null) {
            assertEquals(request.getFirstName(), savedAuthor.getFirstName(), errorMsg("firstName"));
        }
        if (request.getSecondName() != null) {
            assertEquals(request.getSecondName(), savedAuthor.getSecondName(), errorMsg("secondName"));
        }
    }
}