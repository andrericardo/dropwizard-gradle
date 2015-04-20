package com.example.helloworld.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 {
 "id": 1,
 "content": "Hi!"
 }
 The id field is a unique identifier for the saying, and content is the textual representation of the saying. (Thankfully, this is a fairly straight-forward industry standard.)

 To model this representation, we’ll create a representation class:
 */
public class Saying {
    @JsonProperty
    private long id;

    @JsonProperty
    @Length(max = 3)
    private String content;

    private Saying() {
        // Jackson deserialization
    }

    public Saying(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
