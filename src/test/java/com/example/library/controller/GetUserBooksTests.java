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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Feature("Получение списка книг по клиенту")
@Link("/library/users/{userId}/books")
@Story("GET /library/users/{userId}/books. Метод получения списка книг по клиенту.")
class GetUserBooksTests extends BaseTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Позитивный тест /library/users/{userId}/books")
    @Description("Проверка успешного получения списка книг.")
    void successGetUserBooks() {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Степан");
        newCustomer.setSecondName("Ходоков");
        customerRepository.save(newCustomer);

        Author newAuthor1 = new Author();
        newAuthor1.setFirstName("Джорж");
        newAuthor1.setSecondName("Мартин");
        authorRepository.save(newAuthor1);

        Book newBook1 = new Book();
        newBook1.setBookTitle("Песнь льда и огня: Игра престолов");
        newBook1.setAuthor(newAuthor1);
        newBook1.setCustomer(newCustomer);
        bookRepository.save(newBook1);

        Author newAuthor2 = new Author();
        newAuthor2.setFirstName("Джорж");
        newAuthor2.setSecondName("Оруэлл");
        authorRepository.save(newAuthor2);

        Book newBook2 = new Book();
        newBook2.setBookTitle("Скотный двор");
        newBook2.setAuthor(newAuthor2);
        newBook2.setCustomer(newCustomer);
        bookRepository.save(newBook2);

        JsonPath response = given()
                .pathParams("userId", newCustomer.getId())
                .when()
                .get("/library/users/{userId}/books")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertFalse(response.getBoolean("$.isEmpty()"));
        assertEquals(2, response.getInt("$.size()"));
        assertEquals(newBook1.getId(), response.getLong("[0].id"));
        assertEquals(newAuthor1.getId(), response.getLong("[0].author.id"));
        assertEquals(newCustomer.getId(), response.getLong("[0].customer.id"));
        assertEquals(newBook2.getId(), response.getLong("[1].id"));
        assertEquals(newAuthor2.getId(), response.getLong("[1].author.id"));
        assertEquals(newCustomer.getId(), response.getLong("[1].customer.id"));
    }


    @Test
    @DisplayName("Негативный тест //library/users/{userId}/books")
    @Description("Проверка ошибки при получении списка книг по несуществующему клиенту.")
    void failuruGetBooksWithUnknownUserId() {
        JsonPath response = given()
                .pathParams("userId", -1)
                .when()
                .get("/library/users/{userId}/books")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertTrue(response.getBoolean("$.isEmpty()"));
    }
}