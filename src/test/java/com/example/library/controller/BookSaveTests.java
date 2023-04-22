package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Customer;
import com.example.library.model.AuthorDto;
import com.example.library.model.BookSaveRequest;
import com.example.library.model.BookSaveResponse;
import com.example.library.model.CustomerDto;
import com.example.library.steps.LibraryBookSaveSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@Feature("Сохранение книги")
@Link("/library/books/save")
@Story("POST /library/books/save. Метод сохранения новой книги.")
class BookSaveTests extends BaseTest {

    private final LibraryBookSaveSteps bookSaveSteps = new LibraryBookSaveSteps();

    @Test
    @DisplayName("Позитивный тест /library/books/save")
    @Description("Проверка успешного сохраниения книги, с корректными параметрами.")
    void successBookSave() {
        Author chuckPalahniuk = libraryDatabaseFixtureSteps.insertAuthor("Чак", "Паланик");
        Customer ivanIvanov = libraryDatabaseFixtureSteps.insertCustomer("Иван", "Иванов");

        BookSaveRequest request = BookSaveRequest.builder()
                .bookTitle("Бойцовский клуб")
                .author(new AuthorDto(chuckPalahniuk.getId()))
                .customer(new CustomerDto(ivanIvanov.getId()))
                .build();

        BookSaveResponse response = bookSaveSteps.postBookSave(request, 200);

        bookSaveSteps.bookSaveResponseShouldBeCorrect(response);

        Book savedBook = libraryDatabaseFixtureSteps.getBookById(response.getBookId());
        bookSaveSteps.savedBookDataShouldBeEqualsToRequestData(savedBook, request);
    }

    @DisplayName("Негативный тест /library/books/save")
    @Description("Проверка ошибки при сохраниении книги с некорректным автором.")
    @ParameterizedTest(name = "{displayName} [{index}] Параметры: authorId=[{0}].")
    @NullSource
    @ValueSource(longs = {-1})
    void failureBookSave(Long authorId) {
        BookSaveRequest request = BookSaveRequest.builder()
                .bookTitle("Бойцовский клуб")
                .author(new AuthorDto(authorId))
                .build();

        bookSaveSteps.postBookSave(request, 500);
    }

    @DisplayName("Негативный тест /library/books/save")
    @Description("Проверка ошибки при сохраниении книги с некорректным названием.")
    @ParameterizedTest(name = "{displayName} [{index}] Параметры: bookTitle=[{0}].")
    @NullSource
    @ValueSource(strings = {" "})
    void failureBookSave(String bookTitle) {

        Author chuckPalahniuk = libraryDatabaseFixtureSteps.insertAuthor("Чак", "Паланик");

        BookSaveRequest request = BookSaveRequest.builder()
                .bookTitle(bookTitle)
                .author(new AuthorDto(chuckPalahniuk.getId()))
                .build();

        bookSaveSteps.postBookSave(request, 500);
    }
}