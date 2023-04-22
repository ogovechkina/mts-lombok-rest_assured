package com.example.library.steps;

import com.example.library.entity.Customer;
import com.example.library.model.CustomerSaveRequest;
import com.example.library.model.CustomerSaveResponse;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import java.lang.reflect.Type;

import static com.example.library.utils.AssertUtils.errorMsg;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibraryCustomerSaveSteps {

    @Step("Запрос POST /library/users/save: возвращаем ID сохраненного клиента")
    public CustomerSaveResponse postCustomerSave(CustomerSaveRequest request, int expectedStatus) {
        return given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/library/users/save")
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract().response()
                .getBody().as((Type) CustomerSaveResponse.class);
    }

    @Step("Проверяем ответ запроса на сохранение клиента")
    public void customerSaveResponseShouldBeCorrect(CustomerSaveResponse response) {
        assertTrue(response.getCustomerId() > 0, "customerId должно быть больше 0.");
    }

    @Step("Проверяем данные сохраненного клиента с данными отправленными в запросе на сохранение")
    public void savedCustomerDataShouldBeEqualsToRequestData(Customer savedCustomer, CustomerSaveRequest request) {
        assertEquals(request.getFirstName(), savedCustomer.getFirstName(), errorMsg("firstName"));
        if (request.getFirstName()!= null) {
            assertEquals(request.getFirstName(), savedCustomer.getFirstName(), errorMsg("firstName"));
        }
        if (request.getSecondName() != null) {
            assertEquals(request.getSecondName(), savedCustomer.getSecondName(), errorMsg("secondName"));
        }
    }
}