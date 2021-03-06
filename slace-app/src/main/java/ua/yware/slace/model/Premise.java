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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Premise {

    @Id
    private BigInteger id;

    private String name;

    private Integer space;

    private Integer area;

    private Double lon;

    private Double lat;

    private City city;

    private String address;

    @DBRef(lazy = true)
    private List<Category> categories = new ArrayList<>();

    private String imageUri;

    private BigDecimal priceRate;

    private int totalEstimation;

    private List<Comment> comments = new ArrayList<>();

    private List<String> equipment;

    private Map<String, String> schedule;

    private String description;

    private String about;

    @DBRef(lazy = true)
    private User owner;

    @DBRef(lazy = true)
    private List<Reservation> reservations = new ArrayList<>();

    public void removeComment(BigInteger id) {
        comments = getComments()
                .stream()
                .filter(c -> c.getId().equals(id))
                .collect(Collectors.toList());
    }

}
