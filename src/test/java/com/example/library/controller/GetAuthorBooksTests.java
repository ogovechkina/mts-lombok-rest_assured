package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Customer;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CustomerRepository;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Feature("Получение списка книг по автору")
@Link("/library/authors/{authorId}/books")
@Story("GET /library/authors/{authorId}/books. Метод получения списка книг по автору.")
class GetAuthorBooksTests extends BaseTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Позитивный тест /library/authors/{authorId}/books")
    @Description("Проверка успешного получения списка книг по автору.")
    void successGetAuthorBooks() {

        Author newAuthor = new Author();
        newAuthor.setFirstName("Джорж");
        newAuthor.setSecondName("Мартин");
        authorRepository.save(newAuthor);

        Book newBook1 = new Book();
        newBook1.setBookTitle("Песнь льда и огня: Игра престолов");
        newBook1.setAuthor(newAuthor);
        bookRepository.save(newBook1);

        Book newBook2 = new Book();
        newBook2.setBookTitle("Буря мечей");
        newBook2.setAuthor(newAuthor);
        bookRepository.save(newBook2);

        JsonPath response = given()
                .pathParams("authorId", newAuthor.getId())
                .when()
                .get("/library/authors/{authorId}/books")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertFalse(response.getBoolean("$.isEmpty()"));
        assertEquals(2, response.getInt("$.size()"));
        assertEquals(newBook1.getId(), response.getLong("[0].id"));
        assertEquals(newAuthor.getId(), response.getLong("[0].author.id"));
        assertEquals(newBook2.getId(), response.getLong("[1].id"));
        assertEquals(newAuthor.getId(), response.getLong("[1].author.id"));
    }


    @Test
    @DisplayName("Негативный тест /library/authors/{authorId}/books")
    @Description("Проверка ошибки при получении списка книг по несуществующему автору.")
    void failureGetBooksWithUnknownAuthorId() {
        JsonPath response = given()
                .pathParams("authorId", -1)
                .when()
                .get("/library/authors/{authorId}/books")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertTrue(response.getBoolean("$.isEmpty()"));
    }
}