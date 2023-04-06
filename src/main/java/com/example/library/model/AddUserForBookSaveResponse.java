package com.example.library.model;

import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserForBookSaveResponse implements Serializable {

    private List<Long> booksId;

    private Long customerId;
}