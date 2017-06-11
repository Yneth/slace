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

package ua.yware.slace.service.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PremiseDto {

    private BigInteger id;

    private String name;

    private Integer space;

    private Integer area;

    private String address;

    private List<String> categories = new ArrayList<>();

    private String imageUri;

    private BigDecimal priceRate;

    private int totalEstimation;

    private List<CommentDto> comments = new ArrayList<>();

    private List<String> equipment;

    private Map<String, String> schedule;

    private String description;

    private String about;

}
