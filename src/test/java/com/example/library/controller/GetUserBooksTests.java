package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Customer;
import com.example.library.model.AuthorDto;
import com.example.library.model.CustomerDto;
import com.example.library.model.UserBookDto;
import com.example.library.steps.LibraryGetUserBooksSteps;
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

@Feature("Получение списка книг по клиенту")
@Link("/library/users/{userId}/books")
@Story("GET /library/users/{userId}/books. Метод получения списка книг по клиенту.")
class GetUserBooksTests extends BaseTest {

    private final LibraryGetUserBooksSteps getUserBooksSteps = new LibraryGetUserBooksSteps();

    @Test
    @DisplayName("Позитивный тест /library/users/{userId}/books")
    @Description("Проверка успешного получения списка книг.")
    void successGetUserBooks() {

        Author georgeMartin = libraryDatabaseFixtureSteps.insertAuthor("Джорж", "Мартин");
        Author georgeOrwell = libraryDatabaseFixtureSteps.insertAuthor("Джорж", "Оруэлл");
        Customer gennadyBukin = libraryDatabaseFixtureSteps.insertCustomer("Геннадий", "Букин");
        Book expectedBook1 = libraryDatabaseFixtureSteps.insertBook("Песнь льда и огня: Игра престолов", georgeMartin.getId(), gennadyBukin.getId());
        libraryDatabaseFixtureSteps.insertBook("Песнь льда и огня: Буря мечей", georgeMartin.getId(), null);
        Book expectedBook2 = libraryDatabaseFixtureSteps.insertBook("Дни в Бирме", georgeOrwell.getId(), gennadyBukin.getId());
        libraryDatabaseFixtureSteps.insertBook("Фунты лиха в Париже и Лондоне", georgeOrwell.getId(), null);

        List<UserBookDto> actualUserBooks = getUserBooksSteps.getUserBooks(gennadyBukin.getId(), 200);

        List<UserBookDto> expectedUserBooks = Arrays.asList(
                UserBookDto.builder()
                        .id(expectedBook1.getId())
                        .bookTitle(expectedBook1.getBookTitle())
                        .author(new AuthorDto(expectedBook1.getAuthorId()))
                        .customer(new CustomerDto(expectedBook1.getCustomerId()))
                        .build(),
                UserBookDto.builder()
                        .id(expectedBook2.getId())
                        .bookTitle(expectedBook2.getBookTitle())
                        .author(new AuthorDto(expectedBook2.getAuthorId()))
                        .customer(new CustomerDto(expectedBook2.getCustomerId()))
                        .build());
        getUserBooksSteps.getUserBooksResponseShouldBeCorrect(actualUserBooks, expectedUserBooks);
    }


    @DisplayName("Негативный тест /library/users/{userId}/books")
    @Description("Проверка ошибки при получении списка книг по некорректному клиенту.")
    @ParameterizedTest(name = "{displayName} [{index}] Параметры: customerId=[{0}].")
    @NullSource
    @ValueSource(longs = {-1})
    void failuruGetBooksWithUnknownUserId(Long customerId) {
        getUserBooksSteps.getUserBooks(customerId, 404);
    }
}