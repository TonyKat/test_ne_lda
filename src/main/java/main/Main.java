package main;

import DBService.DBPostgreSQL;
import generative_process.LDAOriginal;
import generative_process.LdaStrongTypeNe;

public class Main {

    public static void main(String[] args) {
        System.out.println("Проверка БД:");
        /*//Создаем массив случайных чисел
        long arr[] = new long[10];
        for(int i = 0; i <  arr.length; i++) {
            arr[i] =  (long)(Math.random() * 100);
            System.out.print(arr[i] + "  ");
        }
        System.out.print("\nSorted: \n");
        //Сортируем массив
        Arrays.sort(arr);
        //Выводим отсортированный массив на консоль.
        for (long anArr : arr) {
            System.out.print(anArr + "  ");
        }
        System.out.print("\n");
        long nK[][] = new long[2][10];
        for(int i = 0; i < 2; i++){
            System.out.print("row:\n");
            for(int j = 0; j < 10; j++){
                nK[i][j] = (long)(Math.random() * 100);
                System.out.print(nK[i][j] + " ");
            }
            System.out.print("\n");
        }
        arr = nK[0];
        //Сортируем массив
        System.out.print("\nSorted: \n");
        Arrays.sort(arr);
        //Выводим отсортированный массив на консоль.
        for (long anArr : arr) {
            System.out.print(anArr + "  ");
        }
        System.out.print("\nВ обратном порядке \n");
        for (int i = arr.length - 1; i >= 0; --i){
            System.out.print(arr[i] + "  ");
        }
        System.out.println("\n");
        long[] countries = {0,1,2,3,4,4,3,2,9};
        Long[] newArray = ArrayUtils.toObject(countries);
        ArrayIndexComparator comparator = new ArrayIndexComparator(newArray);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);
        for (Integer indexe : indexes) {
            System.out.print(indexe + " ");
        }

        //Arrays.sort
        INDArray nd = Nd4j.create(new float[]{1, 3, 2, 4}, new int[]{2, 2});
        nd.transpose();
        for(int i = 0; i < nd.size(0); i++){
            for(int j = 0; j <nd.size(1); j++){
                System.out.println(nd.getRow(i).getColumn(j));
            }
        }*/
        DBPostgreSQL dbNew = DBPostgreSQL.instance();

        dbNew.getObjectNewsRbc();
        dbNew.getObjectOrderedDict();
        //System.out.println(dbNew.getRowById(9).getWord() + dbNew.getRowById(9).getType_ne());

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
        LdaStrongTypeNe ldaStrongTypeNe = new LdaStrongTypeNe();
        ldaStrongTypeNe.gererativeProcess();
        ldaStrongTypeNe.gibbsSampling();

        /*LDAOriginal gp = new LDAOriginal();
        gp.gererativeProcess();
        gp.gibbsSampling();*/
        System.out.println("КОНЕЦ");

    }

}
