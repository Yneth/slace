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

package ua.yware.slace.dao.ops.mongo;

import java.util.List;

import ua.yware.slace.dao.ops.PremiseOperations;
import ua.yware.slace.model.Premise;
import ua.yware.slace.model.QPremise;
import ua.yware.slace.model.User;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class PremiseMongoOperations extends QuerydslRepositorySupport implements PremiseOperations {

    private static final QPremise PREMISE = QPremise.premise;

    /**
     * Creates a new {@link QuerydslRepositorySupport} for the given {@link MongoOperations}.
     *
     * @param operations must not be {@literal null}.
     */
    public PremiseMongoOperations(MongoOperations operations) {
        super(operations);
    }

    @Override
    public List<Premise> findPremisesReservedBy(User user) {
        return from(PREMISE).where(PREMISE.reservations.any().user.eq(user)).fetch();
    }

}
