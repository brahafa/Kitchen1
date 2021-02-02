package com.dalpak.bringit.models;

import java.util.ArrayList;
import java.util.List;

public class OrdersByStatusModel {
        private List<OrderModel> received = new ArrayList<>();
        private List<OrderModel> preparing = new ArrayList<>();
        private List<OrderModel> cooking = new ArrayList<>();
        private List<OrderModel> packing = new ArrayList<>();
        private List<OrderModel> sent = new ArrayList<>();

        public List<OrderModel> getReceived() {
            return received;
        }

        public void setReceived(List<OrderModel> received) {
            this.received = received;
        }

        public List<OrderModel> getPreparing() {
            return preparing;
        }

        public void setPreparing(List<OrderModel> preparing) {
            this.preparing = preparing;
        }

        public List<OrderModel> getCooking() {
            return cooking;
        }

        public void setCooking(List<OrderModel> cooking) {
            this.cooking = cooking;
        }

        public List<OrderModel> getPacking() {
            return packing;
        }

        public void setPacking(List<OrderModel> packing) {
            this.packing = packing;
        }

        public List<OrderModel> getSent() {
            return sent;
        }

        public void setSent(List<OrderModel> sent) {
            this.sent = sent;
        }

    }
