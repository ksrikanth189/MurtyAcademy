package com.murtyacademy.splash.interactor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by srikanth on 12/24/2018.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "email",
        "ContactsArray"
})
public class ContactsLIstReq {

    @JsonProperty("email")
    private String email;
    @JsonProperty("ContactsArray")
    private List<ContactsArray> contactsArray = null;

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("ContactsArray")
    public List<ContactsArray> getContactsArray() {
        return contactsArray;
    }

    @JsonProperty("ContactsArray")
    public void setContactsArray(List<ContactsArray> contactsArray) {
        this.contactsArray = contactsArray;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder({
            "name",
            "mobile"
    })
    public static class ContactsArray {

        @JsonProperty("name")
        private String name;
        @JsonProperty("mobile")
        private String mobile;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("mobile")
        public String getMobile() {
            return mobile;
        }

        @JsonProperty("mobile")
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

    }

}
