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

package ua.yware.slace.web.rest.form;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import ua.yware.slace.model.Category;

@Getter
public class PremiseForm {

    private String name;

    private Integer space;

    private Integer area;

    private String address;

    private List<Category> categories;

    private BigDecimal priceRate;

    private List<String> equipment;

    private Map<String, String> schedule;

    private String description;

    private String about;

}
