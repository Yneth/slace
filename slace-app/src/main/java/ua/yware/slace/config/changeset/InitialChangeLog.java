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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import ua.yware.slace.model.Category;
import ua.yware.slace.model.City;
import ua.yware.slace.model.Premise;
import ua.yware.slace.model.Reservation;
import ua.yware.slace.model.User;
import ua.yware.slace.model.UserRole;
import ua.yware.slace.model.enums.ReservationStatus;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@ChangeLog(order = "001")
public class InitialChangeLog {

    @ChangeSet(order = "001", id = "1", author = "Anton Bondarenko")
    public void initCategories(MongoTemplate template) {
        Category concert = new Category("concert");
        Category festivity = new Category("festivity");
        Category party = new Category("party");
        Category conference = new Category("conference");
        Category photoSession = new Category("photo_session");
        template.insertAll(Arrays.asList(concert, festivity, party, conference, photoSession));
    }

    @ChangeSet(order = "002", id = "2", author = "Anton Bondarenko")
    public void initUserRoles(MongoTemplate template) {
        UserRole admin = new UserRole();
        admin.setRoleName("admin");

        UserRole user = new UserRole();
        user.setRoleName("user");
        template.insertAll(Arrays.asList(admin, user));
    }

    @ChangeSet(order = "003", id = "3", author = "Anton Bondarenko")
    public void initUsers(MongoTemplate template) {
        User admin = new User();
        admin.setId(BigInteger.ZERO);
        admin.setEmail("admin@admin.com");

        admin.setLogin("admin");
        admin.setPassword("$2a$10$Z2xkJqu62r.0DZ0xn/24AurLIKKZrxgT4a1iNYrr9p3reRbZsfB1W");

        UserRole adminRole = template.findOne(new Query(Criteria.where("roleName").is("admin")), UserRole.class);
        admin.setRoles(Collections.singletonList(adminRole));

        UserRole userRole = template.findOne(new Query(Criteria.where("roleName").is("user")), UserRole.class);

        User user = new User();
        user.setId(BigInteger.ONE);
        user.setEmail("user@user.com");
        user.setLogin("user");
        user.setPassword("$2a$10$hH5QaV01tEq90KqsQP.00exZbEFFcXmkI7CmEuTIjkGfUL4JOI3Na");
        user.setRoles(Collections.singletonList(userRole));

        User user1 = new User();
        user1.setId(new BigInteger("2"));
        user1.setEmail("user1@user.com");
        user1.setLogin("user1");
        user1.setPassword("$2a$10$hH5QaV01tEq90KqsQP.00exZbEFFcXmkI7CmEuTIjkGfUL4JOI3Na");
        user1.setRoles(Collections.singletonList(userRole));

        template.insertAll(Arrays.asList(admin, user, user1));
    }

    @ChangeSet(order = "004", id = "4", author = "Anton Bondarenko")
    public void initCities(MongoTemplate template) {
        template.insertAll(Arrays.asList(
                new City(new BigInteger("0"), "Kyiv"),
                new City(new BigInteger("1"), "Venice"),
                new City(new BigInteger("2"), "Warsaw")));
    }

    @ChangeSet(order = "005", id = "5", author = "Anton Bondarenko")
    public void initPremises(MongoTemplate template) {
        Premise premise = new Premise();
        premise.setId(new BigInteger("1"));
        premise.setName("Green warehouse");
        premise.setSpace(100);
        premise.setAbout("A lot of text ...");
        premise.setDescription("Full of cute words");
        premise.setArea(100);
        premise.setCity(template.findOne(new Query(Criteria.where("id").is("0")), City.class));
        premise.setPriceRate(new BigDecimal("123.50"));
        premise.setOwner(template.findOne(new Query(Criteria.where("id").is("1")), User.class));

        User user = template.findOne(new Query(Criteria.where("id").is("2")), User.class);
        Reservation premiseReservation = new Reservation();
        premiseReservation.setId(new BigInteger("1"));
        premiseReservation.setUser(user);
        premiseReservation.setPremise(premise);
        premiseReservation.setPriceRate(premise.getPriceRate());
        premiseReservation.setFrom(LocalDateTime.now());
        premiseReservation.setTo(LocalDateTime.now().plusDays(2));
        premiseReservation.setReservationStatus(ReservationStatus.CREATED);

        premise.getReservations().add(premiseReservation);

        template.insert(premise);
        template.insert(premiseReservation);
    }

}
