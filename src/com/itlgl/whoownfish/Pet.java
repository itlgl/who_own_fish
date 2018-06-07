package com.itlgl.whoownfish;

public enum Pet {
    DOG("狗"), BIRD("鸟"), CAT("猫"), HORSE("马"), FISH("鱼");

    final String desc;

    Pet(String desc) {
        this.desc = desc;
    }
}
