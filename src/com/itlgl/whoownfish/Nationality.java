package com.itlgl.whoownfish;

public enum Nationality {
    ENGLAND("英国"), SWEDEN("瑞典"), DENMARK("丹麦"), NORWAY("挪威"), GERMANY("德国");

    final String desc;

    Nationality(String desc) {
        this.desc = desc;
    }
}
