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

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ua.yware.slace.model.Category;
import ua.yware.slace.model.Premise;

@Getter
@Setter
public class CategoryDto {

    private String name;

    private Integer count;

    public CategoryDto(Category category) {
        this.name = category.getName();
        List<Premise> premises = category.getPremises();
        this.count = premises != null ? premises.size() : 0;
    }

}
