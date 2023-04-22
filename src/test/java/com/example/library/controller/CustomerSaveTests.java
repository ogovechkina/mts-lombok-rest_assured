package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Customer;
import com.example.library.model.CustomerSaveRequest;
import com.example.library.model.CustomerSaveResponse;
import com.example.library.steps.LibraryCustomerSaveSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


@Link("/library/users/save")
@Feature("Сохранение клиента")
@Story("POST /library/users/save. Метод сохранения нового клиента.")
class CustomerSaveTests extends BaseTest {

    private final LibraryCustomerSaveSteps customerSaveSteps = new LibraryCustomerSaveSteps();
    @Test
    @DisplayName("Позитивный тест /library/users/save")
    @Description("Проверка успешного сохраниения клиента с корректными параметрами.")
    void successCustomerSave() {

        CustomerSaveRequest request = CustomerSaveRequest.builder()
                .firstName("Геннадий")
                .secondName("Букин")
                .build();

        CustomerSaveResponse response = customerSaveSteps.postCustomerSave(request, 200);
        customerSaveSteps.customerSaveResponseShouldBeCorrect(response);

        Customer savedCustomer = libraryDatabaseFixtureSteps.getCustomerId(response.getCustomerId());
        customerSaveSteps.savedCustomerDataShouldBeEqualsToRequestData(savedCustomer, request);
    }

    @DisplayName("Негативный тест /library/users/save")
    @Description("Проверка на получение ошибки при попытке сохраниенить клиента с некорректными параметрами.")
    @ParameterizedTest(name = "{displayName} [{index}] Параметры: firstName=[{0}], secondName=[{1}]")
    @CsvSource(value = {
            "N/A,Букин",
            "Геннадий,N/A",
            "' ',Букин",
            "Геннадий,' '"
    }, nullValues = {"N/A"})
    void failureCustomerSave(String firstName, String secondName) {
        CustomerSaveRequest request = CustomerSaveRequest.builder()
                .firstName(firstName)
                .secondName(secondName)
                .build();

        customerSaveSteps.postCustomerSave(request, 500);
    }
}