package com.itlgl.whoownfish;

/**
 * 根据这个网页的描述，http://www.fmddlmyy.cn/text20.html
 * 穷举法提前判断条件可以快速计算出结果
 */
public class WhoOwnFishV2 {
//    static final int[][] TEMPLATE = new int[5 * 4 * 3 * 2][5];
    // 国家-颜色-香烟-宠物-饮料
    static final Nationality[][] NatTemp = new Nationality[5 * 4 * 3 * 2][5];
    static final HouseColor[][] ColorTemp = new HouseColor[5 * 4 * 3 * 2][5];
    static final Cigarette[][] CigTemp = new Cigarette[5 * 4 * 3 * 2][5];
    static final Pet[][] PetTemp = new Pet[5 * 4 * 3 * 2][5];
    static final Drink[][] DrinkTemp = new Drink[5 * 4 * 3 * 2][5];

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

                            NatTemp[index][0] = Nationality.values()[i];
                            NatTemp[index][1] = Nationality.values()[j];
                            NatTemp[index][2] = Nationality.values()[k];
                            NatTemp[index][3] = Nationality.values()[l];
                            NatTemp[index][4] = Nationality.values()[m];

                            ColorTemp[index][0] = HouseColor.values()[i];
                            ColorTemp[index][1] = HouseColor.values()[j];
                            ColorTemp[index][2] = HouseColor.values()[k];
                            ColorTemp[index][3] = HouseColor.values()[l];
                            ColorTemp[index][4] = HouseColor.values()[m];

                            CigTemp[index][0] = Cigarette.values()[i];
                            CigTemp[index][1] = Cigarette.values()[j];
                            CigTemp[index][2] = Cigarette.values()[k];
                            CigTemp[index][3] = Cigarette.values()[l];
                            CigTemp[index][4] = Cigarette.values()[m];

                            PetTemp[index][0] = Pet.values()[i];
                            PetTemp[index][1] = Pet.values()[j];
                            PetTemp[index][2] = Pet.values()[k];
                            PetTemp[index][3] = Pet.values()[l];
                            PetTemp[index][4] = Pet.values()[m];

                            DrinkTemp[index][0] = Drink.values()[i];
                            DrinkTemp[index][1] = Drink.values()[j];
                            DrinkTemp[index][2] = Drink.values()[k];
                            DrinkTemp[index][3] = Drink.values()[l];
                            DrinkTemp[index][4] = Drink.values()[m];

                            index++;
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();

        excute();

        long timeEnd = System.currentTimeMillis();
        long excuteTime = timeEnd - timeStart;
        System.out.println("执行时间：" + excuteTime + "ms");
    }

    private static void excute() {
        Person[] answer = new Person[5];
        for (int i = 0; i < 5; i++) {
            answer[i] = new Person();
        }

        for (int i = 0; i < NatTemp.length; i++) {
            // 9. 挪威人住第一间房子
            if(NatTemp[i][0] != Nationality.NORWAY) {
                continue;
            }

            colorFor: for (int j = 0; j < ColorTemp.length; j++) {
                // 1. 英国人住在红房子里
                for (int j1 = 0; j1 < 5; j1++) {
                    if(NatTemp[i][j1] == Nationality.ENGLAND && ColorTemp[j][j1] != HouseColor.RED) {
                        continue colorFor;
                    }
                }
                // 4. 绿房子在白房子左边
                // 14. 挪威人住在蓝房子旁边
                {
                    int greenIndex = 0, whiteIndex = 0;
                    int norwayIndex = 0, blueIndex = 0;
                    for (int j2 = 0; j2 < 5; j2++) {
                        if(ColorTemp[j][j2] == HouseColor.GREEN) {
                            greenIndex = j2;
                        }
                        if(ColorTemp[j][j2] == HouseColor.WHITE) {
                            whiteIndex = j2;
                        }
                        if(NatTemp[i][j2] == Nationality.NORWAY) {
                            norwayIndex = j2;
                        }
                        if(ColorTemp[j][j2] == HouseColor.BLUE) {
                            blueIndex = j2;
                        }
                    }
                    if(greenIndex + 1 != whiteIndex) {
                        continue;
                    }
                    if((norwayIndex - blueIndex) != 1 && (norwayIndex - blueIndex) != -1) {
                        continue;
                    }
                }

                cigFor: for (int k = 0; k < CigTemp.length; k++) {
                    // 7. 黄房子主人抽DUNHILL烟
                    // 13. 德国人抽PRINCE烟
                    for (int k1 = 0; k1 < 5; k1++) {
                        if(CigTemp[k][k1] == Cigarette.DUNHILL && ColorTemp[j][k1] != HouseColor.YELLOW) {
                            continue cigFor;
                        }
                        if(CigTemp[k][k1] == Cigarette.PRINCE && NatTemp[i][k1] != Nationality.GERMANY) {
                            continue cigFor;
                        }
                    }

                    petFor: for (int l = 0; l < PetTemp.length; l++) {
                        // 2. 瑞典人养了一条狗
                        // 6. 抽PALL MALL烟的人养了一只鸟
                        // 10. 抽混合烟的人住在养猫人的旁边
                        // 11. 养马人住在抽DUNHILL烟人的旁边
                        {
                            int admIndex = 0, catIndex = 0;
                            int houseIndex = 0, dunhillIndex = 0;
                            for (int l1 = 0; l1 < 5; l1++) {
                                // 2
                                if(PetTemp[l][l1] == Pet.DOG && NatTemp[i][l1] != Nationality.SWEDEN) {
                                    continue petFor;
                                }
                                // 6
                                if(PetTemp[l][l1] == Pet.BIRD && CigTemp[k][l1] != Cigarette.PALL_MALL) {
                                    continue petFor;
                                }
                                // 10
                                if(PetTemp[l][l1] == Pet.CAT) {
                                    catIndex = l1;
                                }
                                // 10
                                if(CigTemp[k][l1] == Cigarette.ADMIXTURE) {
                                    admIndex = l1;
                                }
                                // 11
                                if(PetTemp[l][l1] == Pet.HORSE) {
                                    houseIndex = l1;
                                }
                                // 11
                                if(CigTemp[k][l1] == Cigarette.DUNHILL) {
                                    dunhillIndex = l1;
                                }
                            }
                            // 10
                            if((admIndex - catIndex) != 1 && (admIndex - catIndex) != -1) {
                                continue;
                            }
                            // 11
                            if((houseIndex - dunhillIndex) != 1 && (houseIndex - dunhillIndex) != -1) {
                                continue;
                            }
                        }

                        drinkFor: for (int m = 0; m < DrinkTemp.length; m++) {
                            // 3. 丹麦人喝茶
                            // 5. 绿房子主人喝咖啡
                            // 8. 住在中间那间房子的人喝牛奶
                            // 12. 抽BLUE MASTER烟的人喝啤酒
                            // 15. 抽混合烟的人的邻居喝矿泉水
                            int admIndex = 0, waterIndex = 0;
                            for (int m1 = 0; m1 < 5; m1++) {
                                // 3
                                if(DrinkTemp[m][m1] == Drink.TEE && NatTemp[i][m1] != Nationality.DENMARK) {
                                    continue drinkFor;
                                }
                                // 5
                                if(DrinkTemp[m][m1] == Drink.COFFEE && ColorTemp[j][m1] != HouseColor.GREEN) {
                                    continue drinkFor;
                                }
                                // 8
                                if(DrinkTemp[m][2] != Drink.MILK) {
                                    continue drinkFor;
                                }
                                // 12
                                if(DrinkTemp[m][m1] == Drink.BEER && CigTemp[k][m1] != Cigarette.BLUE_MASTER) {
                                    continue drinkFor;
                                }
                                // 15
                                if(DrinkTemp[m][m1] == Drink.WATER) {
                                    waterIndex = m1;
                                }
                                // 15
                                if(CigTemp[k][m1] == Cigarette.ADMIXTURE) {
                                    admIndex = m1;
                                }
                            }
                            if((admIndex - waterIndex) != 1 && (admIndex - waterIndex) != -1) {
                                continue;
                            }

                            // 所有的条件都判断完了,那么直接将结果输出
                            for (int n = 0; n < 5; n++) {
                                answer[n].setValue(ColorTemp[j][n], NatTemp[i][n], DrinkTemp[m][n], CigTemp[k][n], PetTemp[l][n]);
                            }
                            printResult(answer);
                        }
                    }
                }
            }
        }
    }

    static void printResult(Person[] answer) {
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
