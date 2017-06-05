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

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog(order = "001")
public class DummyChangeLog {

    @ChangeSet(order = "001", id = "1", author = "Anton Bondarenko")
    public void initWithDummyValues(MongoDatabase mongoDatabase) {
//        {"id":1,"name":"Red Hook Warehouse","space":200,"area":190,"address":"Саксаганского, 24","reserved":false,"category":"CONCERT","imageUri":"/1","priceRate":10}
//        {"id":2,"name":"Green Hook Warehouse","space":200,"area":320,"address":"Саксаганского, 24","reserved":false,"category":"FESTIVITY","imageUri":"/2","priceRate":10}
//        {"id":3,"name":"Blue Hook Warehouse","space":200,"area":500,"address":"Саксаганского, 24","reserved":false,"category":"PHOTO_SESSION","imageUri":"/3","priceRate":10}
    }

}
