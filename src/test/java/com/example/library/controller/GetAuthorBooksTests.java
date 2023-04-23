package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Customer;
import com.example.library.model.AuthorBookDto;
import com.example.library.model.AuthorDto;
import com.example.library.model.CustomerDto;
import com.example.library.steps.LibraryGetAuthorBooksSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

@Feature("Получение списка книг по автору")
@Link("/library/authors/{authorId}/books")
@Story("GET /library/authors/{authorId}/books. Метод получения списка книг по автору.")
class GetAuthorBooksTests extends BaseTest {

    private final LibraryGetAuthorBooksSteps getAuthorBooksSteps = new LibraryGetAuthorBooksSteps();
    @Test
    @DisplayName("Позитивный тест /library/authors/{authorId}/books")
    @Description("Проверка успешного получения списка книг по автору.")
    void successGetAuthorBooks() {

        Author georgeMartin = libraryDatabaseFixtureSteps.insertAuthor("Джорж", "Мартин");
        Customer gennadyBukin = libraryDatabaseFixtureSteps.insertCustomer("Геннадий", "Букин");
        Book expectedBook1 = libraryDatabaseFixtureSteps.insertBook("Песнь льда и огня: Игра престолов", georgeMartin.getId(), gennadyBukin.getId());
        Book expectedBook2 = libraryDatabaseFixtureSteps.insertBook("Песнь льда и огня: Буря мечей", georgeMartin.getId(), null);


        List<AuthorBookDto> actualAuthorBooks = getAuthorBooksSteps.getAuthorBooks(georgeMartin.getId(), 200);

        List<AuthorBookDto> expectedAuthorBooks = Arrays.asList(
                AuthorBookDto.builder()
                        .id(expectedBook1.getId())
                        .bookTitle(expectedBook1.getBookTitle())
                        .author(new AuthorDto(expectedBook1.getAuthorId()))
                        .customer(new CustomerDto(expectedBook1.getCustomerId()))
                        .build(),
                AuthorBookDto.builder()
                        .id(expectedBook2.getId())
                        .bookTitle(expectedBook2.getBookTitle())
                        .author(new AuthorDto(expectedBook2.getAuthorId()))
                        .customer(new CustomerDto(expectedBook2.getCustomerId()))
                        .build());
        getAuthorBooksSteps.getAuthorBooksResponseShouldBeCorrect(actualAuthorBooks, expectedAuthorBooks);
    }


    @DisplayName("Негативный тест /library/authors/{authorId}/books")
    @Description("Проверка ошибки при получении списка книг по некорректному автору.")
    @ParameterizedTest(name = "{displayName} [{index}] Параметры: authorId=[{0}].")
    @NullSource
    @ValueSource(longs = {-1})
    void failuruGetBooksWithUnknownAuthorId(Long authorId) {
        getAuthorBooksSteps.getAuthorBooks(authorId, 404);
    }
}