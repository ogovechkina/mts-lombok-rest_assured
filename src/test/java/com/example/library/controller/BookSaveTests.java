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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Feature("Сохранение книги")
@Link("/library/books/save")
@Story("POST /library/books/save. Метод сохранения новой книги.")
class BookSaveTests extends BaseTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Позитивный тест /library/books/save")
    @Description("Проверка успешного сохраниения книги, с корректными параметрами.")
    void successBookSave() {
        Author newAuthor = new Author();
        newAuthor.setFirstName("Чак");
        newAuthor.setSecondName("Паланик");
        authorRepository.save(newAuthor);

        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Иван");
        newCustomer.setSecondName("Иванов");
        customerRepository.save(newCustomer);

        Book newBook = new Book();
        newBook.setBookTitle("Бойцовский клуб");
        newBook.setAuthor(newAuthor);
        newBook.setCustomer(newCustomer);

        JsonPath response = given()
                .body(newBook)
                .contentType(ContentType.JSON)
                .when()
                .post("/library/books/save")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        long newBookId = response.getLong("bookId");
        assertTrue(newBookId > 0);

        Book savedBook = bookRepository.findById(newBookId).get();
        assertEquals(newBook.getBookTitle(), savedBook.getBookTitle());
        assertEquals(newAuthor.getId(), savedBook.getAuthor().getId());
        assertEquals(newCustomer.getId(), savedBook.getCustomer().getId());
    }

    @DisplayName("Негативный тест /library/books/save")
    @Description("Проверка ошибки при сохраниении книги с некорректным автором.")
    @ParameterizedTest(name = "{displayName} [{index}] Параметры: authorId=[{0}].")
    @NullSource
    @ValueSource(longs = {-1})
    void failureBookSave(Long authorId) {
        Author author = new Author();
        author.setId(authorId);

        Book newBook = new Book();
        newBook.setBookTitle("Бойцовский клуб");
        newBook.setAuthor(author);

        given()
                .body(newBook)
                .contentType(ContentType.JSON)
                .when()
                .post("/library/books/save")
                .then()
                .assertThat()
                .statusCode(500);
    }
}