package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Author;
import com.example.library.model.AuthorSaveRequest;
import com.example.library.model.AuthorSaveResponse;
import com.example.library.steps.LibraryAuthorSaveSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


@Link("/library/authors/save")
@Feature("Сохранение автора")
@Story("POST /library/authors/save. Метод сохранения нового автора.")
class AuthorSaveTests extends BaseTest {

    private final LibraryAuthorSaveSteps authorSaveSteps = new LibraryAuthorSaveSteps();
    @Test
    @DisplayName("Позитивный тест /library/authors/save")
    @Description("Проверка успешного сохраниения автора с корректными параметрами.")
    void successAuthorSave() {

        AuthorSaveRequest request = AuthorSaveRequest.builder()
                .firstName("Фёдор")
                .secondName("Достоевский")
                .build();

        AuthorSaveResponse response = authorSaveSteps.postAuthorSave(request, 200);

        authorSaveSteps.authorSaveResponseShouldBeCorrect(response);

        Author savedAuthor = libraryDatabaseFixtureSteps.getAutorId(response.getAuthorId());
        authorSaveSteps.savedAuthorDataShouldBeEqualsToRequestData(savedAuthor, request);
    }

    @DisplayName("Негативный тест /library/authors/save")
    @Description("Проверка на получение ошибки при попытке сохраниенить автора с некорректными параметрами.")
    @ParameterizedTest (name = "{displayName} [{index}] Параметры: firstName=[{0}], secondName=[{1}]")
    @CsvSource(value = {
            "N/A,Толстой",
            "Лев,N/A",
            "' ',Толстой",
            "Лев,' '"
    }, nullValues = {"N/A"})
    void failureAuthorSave(String firstName, String secondName) {
        AuthorSaveRequest request = AuthorSaveRequest.builder()
                .firstName(firstName)
                .secondName(secondName)
                .build();

        authorSaveSteps.postAuthorSave(request, 500);
    }
}