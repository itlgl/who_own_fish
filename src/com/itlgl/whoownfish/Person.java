package com.itlgl.whoownfish;

public class Person {
    HouseColor houseColor;
    Nationality nationality;
    Drink drink;
    Cigarette cigarette;
    Pet pet;

    public Person() {

    }

    public void setValue(HouseColor houseColor, Nationality nationality, Drink drink, Cigarette cigarette, Pet pet) {
        this.houseColor = houseColor;
        this.nationality = nationality;
        this.drink = drink;
        this.cigarette = cigarette;
        this.pet = pet;
    }

    public void setValue(int[] values) {
        this.houseColor = HouseColor.values()[values[0]];
        this.nationality = Nationality.values()[values[1]];
        this.drink = Drink.values()[values[2]];
        this.cigarette = Cigarette.values()[values[3]];
        this.pet = Pet.values()[values[4]];
    }

    @Override
    public String toString() {
        return String.format("%s人,住%s房子,喝%s,抽%s,养%s", nationality.desc, houseColor.desc, drink.desc, cigarette.desc, pet.desc);
    }
}
