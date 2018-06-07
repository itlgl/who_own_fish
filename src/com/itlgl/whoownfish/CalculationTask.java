package com.itlgl.whoownfish;

import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;

public class CalculationTask extends Thread {
    // 用于计算进度
    static final int TOTAL_PROGRESS = 120 * (WhoOwnFish.RANGE_END - WhoOwnFish.RANGE_START);
    static CountDownLatch countDownLatch = new CountDownLatch(TOTAL_PROGRESS);

    static volatile boolean complete = false;

    static final int[][] template = new int[5 * 4 * 3 * 2][5];
    static {
        // 生成一个模板1-5的遍历数据，供后边使用
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j == i) {
                    continue;
                }
                for (int k = 0; k < 5; k++) {
                    if (k == j || k == i) {
                        continue;
                    }
                    for (int l = 0; l < 5; l++) {
                        if (l == k || l == j || l == i) {
                            continue;
                        }
                        for (int m = 0; m < 5; m++) {
                            if (m == l || m == k || m == j || m == i) {
                                continue;
                            }
                            template[index][0] = i;
                            template[index][1] = j;
                            template[index][2] = k;
                            template[index][3] = l;
                            template[index][4] = m;
                            index++;
                        }
                    }
                }
            }
        }
//        // test
//        for(int i=0;i<template.length;i++) {
//            System.out.println(String.format("%s %s %s %s %s", template[i][0], template[i][1], template[i][2], template[i][3], template[i][4]));
//        }
    }

    int start, end;

    // [start ~ end),example:100~120,for (i = 100;i < 120; i++) { ... }
    public CalculationTask(int start, int end) {
        if(start < 0 || start >= 120 || end < 0 || end > 120) {
            throw new IllegalArgumentException("start和end参数范围错误，不能为：" + start + "-" + end);
        }
        this.start = start;
        this.end = end;
        setName("Thread-" + start + "-" + end);
    }

    @Override
    public void run() {
        System.out.println("线程" + getName() + "开始计算(" + start + "-" + end + ")的情况");

        Person[] answer = new Person[5];
        for (int i = 0; i < 5; i++) {
            answer[i] = new Person();
        }

        // 使用上边的模板数据，遍历生成answer，然后判断是否符合
        // 因为结果太大了，怕内存溢出，所以直接在遍历的时候判断
        int temLen = template.length;
        int[] tem = new int[5];
        outer: for (int i = end - 1; i >= start; i--) {
            for (int j = temLen - 1; j >= 0; j--) {
                for (int k = temLen - 1; k >= 0; k--) {
                    for (int l = temLen - 1; l >= 0; l--) {
                        for (int m = temLen - 1; m >= 0; m--) {
                            if(complete) {
                                break outer;
                            }

                            tem[0] = template[i][0];
                            tem[1] = template[j][0];
                            tem[2] = template[k][0];
                            tem[3] = template[l][0];
                            tem[4] = template[m][0];
                            answer[0].setValue(tem);

                            tem[0] = template[i][1];
                            tem[1] = template[j][1];
                            tem[2] = template[k][1];
                            tem[3] = template[l][1];
                            tem[4] = template[m][1];
                            answer[1].setValue(tem);

                            tem[0] = template[i][2];
                            tem[1] = template[j][2];
                            tem[2] = template[k][2];
                            tem[3] = template[l][2];
                            tem[4] = template[m][2];
                            answer[2].setValue(tem);

                            tem[0] = template[i][3];
                            tem[1] = template[j][3];
                            tem[2] = template[k][3];
                            tem[3] = template[l][3];
                            tem[4] = template[m][3];
                            answer[3].setValue(tem);

                            tem[0] = template[i][4];
                            tem[1] = template[j][4];
                            tem[2] = template[k][4];
                            tem[3] = template[l][4];
                            tem[4] = template[m][4];
                            answer[4].setValue(tem);

                            if (check(answer)) {
                                printAnswer(answer);
                                WhoOwnFish.onResult(answer);
                                complete = true;
                                break;
                            }
                        }
                    }
                }

                // in for j
                // 每次j循环完了之后，更新一下进度
                countDownLatch.countDown();
                printProgress();
            }
        }

        System.out.println(getName() + "执行完成");
    }

    static DecimalFormat df = new DecimalFormat("##.##%");

    static String currProgress = "";

    synchronized static void printProgress() {
        float progress = (TOTAL_PROGRESS - countDownLatch.getCount()) * 1.0f / TOTAL_PROGRESS;
        String progressFormat = df.format(progress);
        if(!progressFormat.equals(currProgress)) {
            currProgress = progressFormat;
            System.out.println("当前进度：" + currProgress);
        }
    }

    boolean check(Person[] answer) {
//        boolean tt = true;
//        if(tt) {
//            return tt;
//        }

        for (int i = 0; i < 5; i++) {
            // 1. 英国人住在红房子里
            if(answer[i].nationality == Nationality.ENGLAND && answer[i].houseColor != HouseColor.RED) {
                return false;
            }
            // 2. 瑞典人养了一条狗
            if(answer[i].nationality == Nationality.SWEDEN && answer[i].pet != Pet.DOG) {
                return false;
            }
            // 3. 丹麦人喝茶
            if(answer[i].nationality == Nationality.DENMARK && answer[i].drink != Drink.TEE) {
                return false;
            }
            // 6. 抽PALL MALL烟的人养了一只鸟
            if(answer[i].cigarette == Cigarette.PALL_MALL && answer[i].pet != Pet.BIRD) {
                return false;
            }
            // 7. 黄房子主人抽DUNHILL烟
            if(answer[i].houseColor == HouseColor.YELLOW && answer[i].cigarette != Cigarette.DUNHILL) {
                return false;
            }
            // 12. 抽BLUE MASTER烟的人喝啤酒
            if(answer[i].cigarette == Cigarette.BLUE_MASTER && answer[i].drink != Drink.BEER) {
                return false;
            }
            // 13. 德国人抽PRINCE烟
            if(answer[i].nationality == Nationality.GERMANY && answer[i].cigarette != Cigarette.PRINCE) {
                return false;
            }
        }

        // 4. 绿房子在白房子左边
        // 5. 绿房子主人喝咖啡
        {
            int greenIndex = 0, whiteIndex = 0;
            Person greenP = null;
            for (int i = 0; i < 5; i++) {
                if(answer[i].houseColor == HouseColor.GREEN) {
                    greenIndex = i;
                    greenP = answer[i];
                }
                if(answer[i].houseColor == HouseColor.WHITE) {
                    whiteIndex = i;
                }
            }
            if(greenIndex + 1 != whiteIndex) {
                return false;
            }
            if(greenP.drink != Drink.COFFEE) {
                return false;
            }
        }
        // 8. 住在中间那间房子的人喝牛奶
        if(answer[2].drink != Drink.MILK) {
            return false;
        }
        // 9. 挪威人住第一间房子
        if(answer[0].nationality != Nationality.NORWAY) {
            return false;
        }
        // 10. 抽混合烟的人住在养猫人的旁边
        {
            int admIndex = 0, catIndex = 0;
            for (int i = 0; i < 5; i++) {
                if(answer[i].cigarette == Cigarette.ADMIXTURE) {
                    admIndex = i;
                }
                if(answer[i].pet == Pet.CAT) {
                    catIndex = i;
                }
            }
            if((admIndex - catIndex) != 1 && (admIndex - catIndex) != -1) {
                return false;
            }
        }
        // 11. 养马人住在抽DUNHILL烟人的旁边
        {
            int horseIndex = 0, dunhillIndex = 0;
            for (int i = 0; i < 5; i++) {
                if(answer[i].cigarette == Cigarette.DUNHILL) {
                    dunhillIndex = i;
                }
                if(answer[i].pet == Pet.HORSE) {
                    horseIndex = i;
                }
            }
            if((horseIndex - dunhillIndex) != 1 && (horseIndex - dunhillIndex) != -1) {
                return false;
            }
        }
        // 14. 挪威人住在蓝房子旁边
        {
            int norwayIndex = 0, blueIndex = 0;
            for (int i = 0; i < 5; i++) {
                if(answer[i].nationality == Nationality.NORWAY) {
                    norwayIndex = i;
                }
                if(answer[i].houseColor == HouseColor.BLUE) {
                    blueIndex = i;
                }
            }
            if((norwayIndex - blueIndex) != 1 && (norwayIndex - blueIndex) != -1) {
                return false;
            }
        }
        // 15. 抽混合烟的人的邻居喝矿泉水
        {
            int admIndex = 0, waterIndex = 0;
            for (int i = 0; i < 5; i++) {
                if(answer[i].cigarette == Cigarette.ADMIXTURE) {
                    admIndex = i;
                }
                if(answer[i].drink == Drink.WATER) {
                    waterIndex = i;
                }
            }
            if((admIndex - waterIndex) != 1 && (admIndex - waterIndex) != -1) {
                return false;
            }
        }
        return true;
    }

    void printAnswer(Person[] answer) {
        System.out.println("找到一个结果：");
        Person whoOwnFish = null;
        for (int i = 0; i < answer.length; i++) {
            System.out.println(answer[i].toString());
            if(answer[i].pet == Pet.FISH) {
                whoOwnFish = answer[i];
            }
        }
        System.out.println(String.format("%s人养鱼", whoOwnFish.nationality.desc));
        System.out.println("---------------------");
    }
}
