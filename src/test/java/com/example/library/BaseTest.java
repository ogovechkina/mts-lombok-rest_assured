package com.example.library;

import com.example.library.steps.fixtures.LibraryDatabaseFixtureSteps;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected LibraryDatabaseFixtureSteps libraryDatabaseFixtureSteps;

    @BeforeEach
    void setupDatabase() {
        libraryDatabaseFixtureSteps = new LibraryDatabaseFixtureSteps();
    }

    @AfterEach
    void closeSession() {
        libraryDatabaseFixtureSteps.closeSession();
    }

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @AfterEach
    public void resetRestAssured() {
        RestAssured.reset();
    }
}
