package com.example.library.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class BookSaveRequest implements Serializable {

    private String bookTitle;
    private AuthorDto author;
    private CustomerDto customer;
}
