package com.example.library.model;

import com.example.library.entity.Customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerSaveResponse implements Serializable {

    private Long customerId;
}
