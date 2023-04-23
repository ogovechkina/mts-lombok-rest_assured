package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Author;
import com.example.library.entity.Customer;
import com.example.library.model.CustomerDto;
import com.example.library.steps.LibraryGetUsersByAuthorSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

@Feature("Получение клиентов по автору")
@Link("/library/authors/{authorId}/users")
@Story("GET /library/authors/{authorId}/users. Метод получения списка клиентов по автору.")
class GetUsersByAuthorTests extends BaseTest {

    private final LibraryGetUsersByAuthorSteps getUsersByAuthorSteps = new LibraryGetUsersByAuthorSteps();

    @Test
    @DisplayName("Позитивный тест /library/authors/{authorId}/users")
    @Description("Проверка успешного получения списка клиентов по автору.")
    void successGetUsersByAuthor() {

        Customer gennadyBukin = libraryDatabaseFixtureSteps.insertCustomer("Геннадий", "Букин");
        Customer daryaBukina = libraryDatabaseFixtureSteps.insertCustomer("Даша", "Букина");
        Author georgeMartin = libraryDatabaseFixtureSteps.insertAuthor("Джорж", "Мартин");
        libraryDatabaseFixtureSteps.insertBook("Песнь льда и огня: Игра престолов", georgeMartin.getId(), gennadyBukin.getId());
        libraryDatabaseFixtureSteps.insertBook("Песнь льда и огня: Буря мечей", georgeMartin.getId(), daryaBukina.getId());


        List<CustomerDto> actualCustomerByAuthor = getUsersByAuthorSteps.getCustomersByAuthor(georgeMartin.getId(), 200);

        List<CustomerDto> expectedCustomerByAuthor = Arrays.asList(
                CustomerDto.builder()
                        .id(gennadyBukin.getId())
                        .firstName(gennadyBukin.getFirstName())
                        .secondName(gennadyBukin.getSecondName())
                        .build(),
                CustomerDto.builder()
                        .id(daryaBukina.getId())
                        .firstName(daryaBukina.getFirstName())
                        .secondName(daryaBukina.getSecondName())
                        .build());
        getUsersByAuthorSteps.geCustomersByAuthorResponseShouldBeCorrect(actualCustomerByAuthor, expectedCustomerByAuthor);
    }

    @DisplayName("Негативный тест /library/authors/{0}/users")
    @Description("Проверка ошибки при получении списка клиентов по несуществующему автору.")
    @ParameterizedTest(name = "{displayName} [{index}] Параметры: authorId=[{0}]")
    @ValueSource(longs = {-1})
    void failuruGetUsersByAuthorWithUnknownAuthorId(Long authorId) {
        getUsersByAuthorSteps.getCustomersByAuthor(authorId, 404);
    }
}