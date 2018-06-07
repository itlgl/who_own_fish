package com.itlgl.whoownfish;

public enum Cigarette {
    PALL_MALL("PALL MALL"), DUNHILL("DUNHILL"), BLUE_MASTER("BLUE MASTER"), PRINCE("PRINCE"), ADMIXTURE("混合烟");

    final String desc;

    Cigarette(String desc) {
        this.desc = desc;
    }
}
