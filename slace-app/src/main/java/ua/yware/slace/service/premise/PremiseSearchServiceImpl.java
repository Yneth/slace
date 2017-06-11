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

package ua.yware.slace.service.premise;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import ua.yware.slace.model.Premise;
import ua.yware.slace.model.QPremise;
import ua.yware.slace.search.PremiseSearchRepository;
import ua.yware.slace.web.rest.form.PremiseSearchForm;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PremiseSearchServiceImpl implements PremiseSearchService {

    private final PremiseSearchRepository premiseRepository;

    @Override
    public List<Premise> findAllWildcard(String keyword) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Premise> findAll(PremiseSearchForm search) {
        BooleanBuilder root = new BooleanBuilder();

        if (search.getDate() != null) {
//            root.and(QPremise.premise.reservations.any().)
        }
        if (search.isPriceRangeSpecified()) {
            root.and(QPremise.premise.priceRate.between(
                    search.getPriceFrom(), search.getPriceTo()));
        }
        if (search.isAreaRangeSpecified()) {
            root.and(QPremise.premise.area.between(
                    search.getAreaFrom(), search.getAreaTo()));
        }
        if (search.isPlaceRangeSpecified()) {
            root.and(QPremise.premise.space.between(
                    search.getPlaceFrom(), search.getPlaceTo()));
        }
        if (search.getCity() != null) {
            root.and(QPremise.premise.city.name.like(search.getCity()));
        }
        if (search.getCategory() != null) {
            root.and(QPremise.premise.categories.any().name.like(search.getCategory()));
        }

        return premiseRepository.findAll(root);
    }

}
