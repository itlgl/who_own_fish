package com.itlgl.whoownfish;

public enum HouseColor {
    RED("红色"), GREEN("绿色"), WHITE("白色"), YELLOW("黄色"), BLUE("蓝色");

    final String desc;

    HouseColor(String desc) {
        this.desc = desc;
    }
}
