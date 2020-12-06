package com.trspo.main;

import com.trspo.main.enums.HorsemanStatus;
import com.trspo.main.enums.SportsCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FillData {
    private static final Random random = new Random();
    private static final List<String> peopleNames = Arrays.asList("Bogdan", "Julia", "Max", "Maria", "Frank", "Evelin");
    private static final List<String> horseName = Arrays.asList("Stormy", "Valensiya", "Sunshine", "Bullseye", "Jack");
    private static final List<Integer> salaries = Arrays.asList(15000, 25000, 45000, 120000);
    private static final List<Integer> consultationPrice = Arrays.asList(500,1500,4780, 12000);
    private static final List<HorsemanStatus> horsemanStatus = Arrays.asList(HorsemanStatus.values());
    private static final List<SportsCategory> sportsCategories = Arrays.asList(SportsCategory.values());
    private static final List<Integer> prices = Arrays.asList(200000, 350000, 500000,1240000);

    public String getPeopleName(){
        return peopleNames.get(random.nextInt(peopleNames.size()));
    }

    public String getHorseName(){
        return horseName.get(random.nextInt(horseName.size()));
    }

    public int getSalary(){
        return salaries.get(random.nextInt(salaries.size()));
    }

    public int getConsultationPrice(){
        return consultationPrice.get(random.nextInt(consultationPrice.size()));
    }

    public HorsemanStatus getHorsemanStatus(){
        return horsemanStatus.get(random.nextInt(horsemanStatus.size()));
    }

    public SportsCategory getSportCategory(){
        return sportsCategories.get(random.nextInt(sportsCategories.size()));
    }

    public int getPrice(){
        return prices.get(random.nextInt(prices.size()));
    }
}
