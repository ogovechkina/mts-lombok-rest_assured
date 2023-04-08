package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Author;
import com.example.library.repository.AuthorRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Link("/library/authors/save")
@Feature("Сохранение автора")
@Story("POST /library/authors/save. Метод сохранения нового автора.")
class AuthorSaveTests extends BaseTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Позитивный тест /library/authors/save")
    @Description("Проверка успешного сохраниения автора с корректными параметрами.")
    void successAuthorSave() {
        Author newAuthor = new Author();
        newAuthor.setFirstName("Фёдор");
        newAuthor.setSecondName("Достоевский");

        JsonPath response = given()
                .body(newAuthor)
                .contentType(ContentType.JSON)
                .when()
                .post("/library/authors/save")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        long newAuthorId = response.getLong("authorId");
        assertTrue(newAuthorId > 0);

        Author savedAuthor = authorRepository.findById(newAuthorId).get();
        assertEquals(newAuthor.getFirstName(), savedAuthor.getFirstName());
        assertEquals(newAuthor.getSecondName(), savedAuthor.getSecondName());
        assertTrue(savedAuthor.getBooks().isEmpty());
    }
}