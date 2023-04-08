package com.example.library.controller;

import com.example.library.BaseTest;
import com.example.library.entity.Customer;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Link("/library/users/save")
@Feature("Сохранение клиента")
@Story("POST /library/users/save. Метод сохранения нового клиента.")
class CustomerSaveTests extends BaseTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Позитивный тест /library/users/save")
    @Description("Проверка успешного сохраниения клиента с корректными параметрами.")
    void successCustomerSave() {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Василий");
        newCustomer.setSecondName("Курочкин");

        JsonPath response = given()
                .body(newCustomer)
                .contentType(ContentType.JSON)
                .when()
                .post("/library/users/save")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath();

        long newCustomerId = response.getLong("customerId");
        assertTrue(newCustomerId > 0);

        Customer savedCustomer = customerRepository.findById(newCustomerId).get();
        assertEquals(newCustomer.getFirstName(), savedCustomer.getFirstName());
        assertEquals(newCustomer.getSecondName(), savedCustomer.getSecondName());
        assertTrue(savedCustomer.getBooks().isEmpty());
    }
}