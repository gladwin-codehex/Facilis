/*
 * Copyright 2016 Bobby
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.codehex.facilis.app;

/**
 * Created by Bobby on 09-05-2016
 */
public class Config {

    public static final String API_LOGIN = "http://codehex.officebuy.in/" +
            "auth/obtain-token/";
    public static final String API_FORGOT_PASS = "http://codehex.officebuy.in/" +
            "auth/password/reset/confirm/";
    public static final String API_VIEW_ORDERS = "http://codehex.officebuy.in/" +
            "view_orders/";

    public static final String PREF_USER = "user";

    public static final String KEY_PREF_FIRST_NAME = "first_name";
    public static final String KEY_PREF_LAST_NAME = "last_name";
    public static final String KEY_PREF_USER_ID = "user_id";
    public static final String KEY_PREF_ROLE = "role";
    public static final String KEY_PREF_TOKEN = "token";
    public static final String KEY_PREF_IS_FIRST_LOGIN = "is_first_login";

    public static final String KEY_API_USERNAME = "username";
    public static final String KEY_API_PASSWORD = "password";
    public static final String KEY_API_CLIENT = "client";
    public static final String KEY_API_FIRST_NAME = "first_name";
    public static final String KEY_API_LAST_NAME = "last_name";
    public static final String KEY_API_USER_ID = "user_id";
    public static final String KEY_API_TOKEN = "token";
    public static final String KEY_API_ROLE = "role";
    public static final String KEY_API_NON_FIELD_ERRORS = "non_field_errors";
    public static final String KEY_API_EMAIL = "email";
    public static final String KEY_API_KEY = "api_key";
    public static final String KEY_API_START = "start";
    public static final String KEY_API_END = "end";
    public static final String KEY_API_ID = "id";
    public static final String KEY_API_ORDER_ID = "order_id";
    public static final String KEY_API_POSTED_BY = "posted_by";
    public static final String KEY_API_POSTED_BY_ID = "id";
    public static final String KEY_API_POSTED_BY_FIRST_NAME = "first_name";
    public static final String KEY_API_POSTED_BY_LAST_NAME = "last_name";
    public static final String KEY_API_POSTED_DATE = "posted_date";
    public static final String KEY_API_DAYS = "days";
    public static final String KEY_API_LEAST_COST = "leastcost";
    public static final String KEY_API_AVERAGE = "average";
    public static final String KEY_API_COUNTER = "counter";
    public static final String KEY_API_USER_IMAGE = "user_image";
    public static final String KEY_API_DETAIL = "detail";
    public static final String KEY_API_USER = "user";
    public static final String KEY_API_ORDER = "order";
    public static final String KEY_API_BIDDING_TIME = "bidding_time";
    public static final String KEY_API_BID_COST = "bid_cost";
    public static final String KEY_API_BID_STATUS = "bid_status";
}
