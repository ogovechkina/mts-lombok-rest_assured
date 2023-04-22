package com.example.library.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class AuthorSaveRequest implements Serializable {

    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("secondName")
    private String secondName;
}
