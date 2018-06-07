package com.itlgl.whoownfish;

public enum Drink {
    TEE("茶"), COFFEE("咖啡"), MILK("牛奶"), BEER("啤酒"), WATER("矿泉水");

    final String desc;

    Drink(String desc) {
        this.desc = desc;
    }
}
