package main;

import DBService.DBPostgreSQL;
import data.DataSet;
import generative_process.GenerativeProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        System.out.println("Проверка БД:");
        DBPostgreSQL dbNew = DBPostgreSQL.instance();

        dbNew.getObjectNewsRbc();
        dbNew.getObjectOrderedDict();

        /*System.out.println(dbNew.getRows().size());
        System.out.println(dbNew.getDictFull().size());
        List<Double> mas = new ArrayList<>();
        mas.add(0.0);
        mas.add(1.0);
        mas.add(2.0);
        mas.add(3.0);
        mas.add(4.0);
        int alfa = 10;
        int number_topic = 2;
        int q = 4;
        mas = mas.stream().map((s) -> (s + alfa)/(number_topic+q)).collect(Collectors.toList());
        System.out.println(mas);*/
        System.out.println("НАЧАЛО");

        GenerativeProcess gp = new GenerativeProcess();
        gp.gererativeProcess();
        gp.gibbsSampling();
        System.out.println("КОНЕЦ");

    }

}
