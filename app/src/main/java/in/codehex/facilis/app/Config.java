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

    public static final String API_DOMAIN_URL = "http://codehex.officebuy.in/";
    public static final String API_LOGIN = API_DOMAIN_URL + "auth/obtain-token/";
    public static final String API_FORGOT_PASS = API_DOMAIN_URL + "auth/password/reset/confirm/";
    public static final String API_VIEW_ORDERS = API_DOMAIN_URL + "view_orders/";
    public static final String API_VIEW_BID_ITEMS = API_DOMAIN_URL + "app/view_bid_cost/";
    public static final String API_HOME_PAGE_DEALS = API_DOMAIN_URL + "home_page_deals/";
    public static final String API_SET_PROFILE_PIC = API_DOMAIN_URL + "user/set_profile_pic/";

    public static final String TAB_TITLE[] = {"ALL DEALS", "HOT DEALS", "LAST MINUTE"};

    public static final int REQUEST_PICK_IMAGE = 27;

    public static final String PREF_USER = "user";
    public static final String KEY_PREF_FIRST_NAME = "first_name";
    public static final String KEY_PREF_LAST_NAME = "last_name";
    public static final String KEY_PREF_USER_ID = "user_id";
    public static final String KEY_PREF_COMPANY_ID = "company_id";
    public static final String KEY_PREF_CREDIT_STATUS = "credit_status";
    public static final String KEY_PREF_TOKEN = "token";
    public static final String KEY_PREF_ROLE = "role";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_PREF_IS_FIRST_LOGIN = "is_first_login";

    public static final String KEY_BUNDLE_FRAGMENT = "fragment";
    public static final String KEY_BUNDLE_ORDER_ID = "order_id";
    public static final String KEY_BUNDLE_BID_ID = "bid_id";

    public static final String KEY_FRAGMENT_ACTIVE = "active";
    public static final String KEY_FRAGMENT_PREVIOUS = "previous";
    public static final String KEY_FRAGMENT_SUCCESSFUL = "successful";
    public static final String KEY_FRAGMENT_ALL_DEALS = "all_deals";
    public static final String KEY_FRAGMENT_HOT_DEALS = "hot_deals";
    public static final String KEY_FRAGMENT_LAST_MINUTE = "last_minute";

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
    public static final String KEY_API_NAME = "name";
    public static final String KEY_API_QUANTITY = "quantity";
    public static final String KEY_API_BRAND = "brand";
    public static final String KEY_API_DESCRIPTION = "description";
    public static final String KEY_API_DEL_CHARGE = "del_charge";
    public static final String KEY_API_PERCENTAGE = "percentage";
    public static final String KEY_API_ITEM_AMOUNT = "item_amount";
    public static final String KEY_API_ERROR = "error";
    public static final String KEY_API_MESSAGE = "message";
    public static final String KEY_API_BID = "bid";
    public static final String KEY_API_DEAL_TYPE = "deal_type";
}
