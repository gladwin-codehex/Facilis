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

package in.codehex.facilis.model;

/**
 * Created by Bobby on 10-05-2016
 */
public class BidItem {

    private int id, orderId, orderOrderId, postedById, bidCost;
    private String postedByFirstName, postedByLastName,
            orderBiddingTime, days, postedDate, userImg;
    private boolean bidStatus;
    private int statusColor;

    public BidItem(int id, int orderId, int orderOrderId, int postedById, int bidCost,
                   String postedByFirstName, String postedByLastName,
                   String orderBiddingTime, String days, String postedDate,
                   String userImg, boolean bidStatus) {
        this.id = id;
        this.orderId = orderId;
        this.orderOrderId = orderOrderId;
        this.postedById = postedById;
        this.bidCost = bidCost;
        this.postedByFirstName = postedByFirstName;
        this.postedByLastName = postedByLastName;
        this.orderBiddingTime = orderBiddingTime;
        this.days = days;
        this.postedDate = postedDate;
        this.userImg = userImg;
        this.bidStatus = bidStatus;
    }

    public BidItem(int id, int orderId, int orderOrderId, int postedById, int bidCost,
                   String postedByFirstName, String postedByLastName, String orderBiddingTime,
                   String days, String postedDate, String userImg, boolean bidStatus,
                   int statusColor) {
        this.id = id;
        this.orderId = orderId;
        this.orderOrderId = orderOrderId;
        this.postedById = postedById;
        this.bidCost = bidCost;
        this.postedByFirstName = postedByFirstName;
        this.postedByLastName = postedByLastName;
        this.orderBiddingTime = orderBiddingTime;
        this.days = days;
        this.postedDate = postedDate;
        this.userImg = userImg;
        this.bidStatus = bidStatus;
        this.statusColor = statusColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderOrderId() {
        return orderOrderId;
    }

    public void setOrderOrderId(int orderOrderId) {
        this.orderOrderId = orderOrderId;
    }

    public int getPostedById() {
        return postedById;
    }

    public void setPostedById(int postedById) {
        this.postedById = postedById;
    }

    public int getBidCost() {
        return bidCost;
    }

    public void setBidCost(int bidCost) {
        this.bidCost = bidCost;
    }

    public String getPostedByFirstName() {
        return postedByFirstName;
    }

    public void setPostedByFirstName(String postedByFirstName) {
        this.postedByFirstName = postedByFirstName;
    }

    public String getPostedByLastName() {
        return postedByLastName;
    }

    public void setPostedByLastName(String postedByLastName) {
        this.postedByLastName = postedByLastName;
    }

    public String getOrderBiddingTime() {
        return orderBiddingTime;
    }

    public void setOrderBiddingTime(String orderBiddingTime) {
        this.orderBiddingTime = orderBiddingTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public boolean isBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(boolean bidStatus) {
        this.bidStatus = bidStatus;
    }

    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }
}
