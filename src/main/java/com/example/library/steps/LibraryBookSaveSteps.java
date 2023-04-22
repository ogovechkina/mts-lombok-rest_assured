package com.example.library.steps;

import com.example.library.entity.Book;
import com.example.library.model.BookSaveRequest;
import com.example.library.model.BookSaveResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static com.example.library.utils.AssertUtils.errorMsg;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibraryBookSaveSteps {

    @Step("Запрос POST /library/books/save: возвращаем ID сохраненной книги")
    public BookSaveResponse postBookSave(BookSaveRequest request, int expectedStatus) {
        return given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/library/books/save")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract().response()
                .getBody().as(BookSaveResponse.class);
    }

    @Step("Проверяем ответ запроса на сохранение книги")
    public void bookSaveResponseShouldBeCorrect(BookSaveResponse response) {
        assertTrue(response.getBookId() > 0, "bookId должно быть больше 0.");
    }

    @Step("Проверяем данные сохраненной книги с данными отправленными в запросе на сохранение")
    public void savedBookDataShouldBeEqualsToRequestData(Book savedBook, BookSaveRequest request) {
        assertEquals(request.getBookTitle(), savedBook.getBookTitle(), errorMsg("bookTitle"));
        if (request.getAuthor() != null) {
            assertEquals(request.getAuthor().getId(), savedBook.getAuthorId(), errorMsg("authorId"));
        }
        if (request.getCustomer() != null) {
            assertEquals(request.getCustomer().getId(), savedBook.getCustomerId(), errorMsg("customerId"));
        }
    }
}