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

package ua.yware.slace.web.rest;

import java.math.BigDecimal;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import ua.yware.slace.dao.PremiseRepository;
import ua.yware.slace.model.Premise;
import ua.yware.slace.model.enums.PremiseCategory;
import ua.yware.slace.service.dto.PremiseCategoryDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/premises")
@AllArgsConstructor
public class PremiseController {

    private final PremiseRepository premiseRepository;

    @GetMapping
    public Iterable<PremiseCategoryDto> getPremiseCategories() {
        return Arrays.asList(new PremiseCategoryDto(10L, PremiseCategory.CONCERT),
                new PremiseCategoryDto(24L, PremiseCategory.PHOTO_SESSION),
                new PremiseCategoryDto(58L, PremiseCategory.FESTIVITY),
                new PremiseCategoryDto(36L, PremiseCategory.PARTY),
                new PremiseCategoryDto(1L, PremiseCategory.CONFERENCE));
    }

    @GetMapping("/popular")
    public Iterable<Premise> getPopularPremises() {
        return Arrays.asList(
                createPremise("1", "1", "Red Hook Warehouse", 200, new BigDecimal(10),
                        "Саксаганского, 24", 190, PremiseCategory.CONCERT),
                createPremise("2", "2", "Green Hook Warehouse", 200, new BigDecimal(10),
                        "Саксаганского, 24", 320, PremiseCategory.FESTIVITY),
                createPremise("3", "3", "Blue Hook Warehouse", 200, new BigDecimal(10),
                        "Саксаганского, 24", 500, PremiseCategory.PHOTO_SESSION));

    }

    private Premise createPremise(String id, String imageId, String name, Integer space,
                                  BigDecimal priceRate, String address, Integer area, PremiseCategory category) {
        Premise premise = new Premise();
        premise.setImageUri("/" + imageId);
        premise.setName(name);
        premise.setAddress(address);
        premise.setSpace(space);
        premise.setArea(area);
        premise.setPriceRate(priceRate);
        premise.setId(id);
        premise.setCategory(category);
        return premise;
    }
}
