/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.yware.slace.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    private BigInteger id;

    private String login;

    private String password;

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private String city;

    private boolean receiveSms;

    private boolean receiveEmail;

    private String about;

    private String imageUri;

    private List<UserRole> roles = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
