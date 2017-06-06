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

package ua.yware.slace.config.changeset;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import ua.yware.slace.model.Category;
import ua.yware.slace.model.User;
import ua.yware.slace.model.UserRole;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@ChangeLog(order = "001")
public class InitialChangeLog {

    @ChangeSet(order = "001", id = "1", author = "Anton Bondarenko")
    public void initCategories(MongoTemplate mongoTemplate) {
        Category concert = new Category("concert");
        Category festivity = new Category("festivity");
        Category party = new Category("party");
        Category conference = new Category("conference");
        Category photoSession = new Category("photo_session");
        mongoTemplate.insertAll(Arrays.asList(concert, festivity, party, conference, photoSession));
    }

    @ChangeSet(order = "001", id = "2", author = "Anton Bondarenko")
    public void initUserRoles(MongoTemplate mongoTemplate) {
        UserRole admin = new UserRole();
        admin.setRoleName("admin");

        UserRole user = new UserRole();
        user.setRoleName("user");
        mongoTemplate.insertAll(Arrays.asList(admin, user));
    }

    @ChangeSet(order = "001", id = "3", author = "Anton Bondarenko")
    public void initUsers(MongoTemplate mongoTemplate) {
        User admin = new User();
        admin.setId(BigInteger.ZERO);
        admin.setEmail("admin@admin.com");

        admin.setLogin("admin");
        admin.setPassword("$2a$10$Z2xkJqu62r.0DZ0xn/24AurLIKKZrxgT4a1iNYrr9p3reRbZsfB1W");

        UserRole adminRole = mongoTemplate.findOne(new Query(Criteria.where("roleName").is("admin")), UserRole.class);
        admin.setRoles(Collections.singletonList(adminRole));

        User user = new User();
        user.setId(BigInteger.ONE);
        user.setEmail("user@user.com");

        user.setLogin("user");
        user.setPassword("$2a$10$hH5QaV01tEq90KqsQP.00exZbEFFcXmkI7CmEuTIjkGfUL4JOI3Na");

        UserRole userRole = mongoTemplate.findOne(new Query(Criteria.where("roleName").is("user")), UserRole.class);
        user.setRoles(Collections.singletonList(userRole));

        mongoTemplate.insertAll(Arrays.asList(admin, user));
    }

}
