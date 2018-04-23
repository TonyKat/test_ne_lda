package generative_process;

import DBService.DBPostgreSQL;
import data.DataSet;
import data.ResultTable;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.*;

public class LDAOriginal {
    // количество слов в документе m, присвоенных теме k
    //private ArrayList<ArrayList<Integer>> n_m_k; doc_topic[di,ti]
    private long[][] docTopic;
    // длина документа
    //private ArrayList<Integer> n_m; n_d[di,0]
    private long[] docLength;
    // количество совпадений термина t, присвоенного теме k в коллекции
    //private ArrayList<ArrayList<Integer>> n_k_t; word_topic[wi,ti]
    private long[][] topicWord;
    // количество терминов присвоенных теме k
    //private ArrayList<Integer> n_k; topics[ti]
    private long[] topics;

    private DBPostgreSQL dbNew = DBPostgreSQL.instance();

    private int[] topicAssignment;

    private ArrayList<DataSet> rows;

    private final int NUMBERTOPIC = 100;

    public LDAOriginal(){
        int sizeDocs = dbNew.getRows().size();
        int sizeDictFull = dbNew.getDictFull().size();

        docTopic = new long[sizeDocs][NUMBERTOPIC];
        docLength = new long[sizeDocs];
        topicWord = new long[NUMBERTOPIC][sizeDictFull];
        topics = new long[NUMBERTOPIC];

        rows = dbNew.getRows();

        int sumTf = 0;
        for (DataSet row : rows) {
            sumTf += row.getTf();
        }
        topicAssignment = new int[sumTf];
        for(int i = 0; i < sumTf; i++){
            topicAssignment[i] = (int)(Math.random()*100);
        }
    }

    public void gererativeProcess(){
        /*whatNumberTypeNe.put("Адрес",0);
        whatNumberTypeNe.put("Дата",1);
        whatNumberTypeNe.put("Местоположение",2);
        whatNumberTypeNe.put("Деньги",3);
        whatNumberTypeNe.put("Имя",4);
        whatNumberTypeNe.put("Организация",5);
        whatNumberTypeNe.put("Персона",6);*/

        //int[] topicAssignment = new int[];// np.random.randint(0, number_topic, sum(rows[:, 2]), dtype=np.int)
        //corpus_id = 0
        /*
        #doc_id_ = rows[i][0]  # docID
        #word_id_ = rows[i][1]  # wordID
        #tf_word_ = rows[i][2]  # tf
        #type_ne_word_ = rows[i][3]  # type_ne = [0:6] or -1'''
        #topic_id = rows[i][4]  # topicID
         */
        System.out.println("gererativeProcess...");
        int corpusId = 0;
        for(DataSet row:rows) {
            for(int i = 0; i < row.getTf(); i++) {
                //row.setTopicID(topicAssignment[corpusId]);
                docTopic[row.getDocID()][topicAssignment[corpusId]] += 1;
                docLength[row.getDocID()] += 1;
                topicWord[topicAssignment[corpusId]][row.getWordID()] += 1;
                topics[topicAssignment[corpusId]] += 1;

                corpusId += 1;

            }
        }

    }
    public void gibbsSampling(){
        int L = 1000;
        double alpha = 50 / NUMBERTOPIC;
        double beta = 0.01;
        int cumI;
        double p;
        ArrayList<ItemTopic> arrayProbabilities = new ArrayList<>();
        double total;
        int kNew;
        double r;
        double sum;
        int kLast;
        /*long arr[];
        int quality;
        int localQuality;*/
        System.out.println("gibbsSampling...");
        for(int iter_L = 0; iter_L < L; iter_L++){
            cumI = 0;
            for(DataSet row:rows){
                for(int j = 0; j < row.getTf(); j++){
                    topicWord[topicAssignment[cumI]][row.getWordID()] -= 1;
                    docTopic[row.getDocID()][topicAssignment[cumI]] -= 1;
                    topics[topicAssignment[cumI]] -= 1;
                    docLength[row.getDocID()] -= 1;

                    total = 0.0;
                    arrayProbabilities = new ArrayList<>();
                    for(int numK = 0; numK < NUMBERTOPIC; numK++){
                        p = ((int)topicWord[numK][row.getWordID()] + beta)/((int)topics[numK] + beta*dbNew.getDictFull().size())*
                                ((int)docTopic[row.getDocID()][numK] + alpha)/((int)docLength[row.getDocID()] + alpha * NUMBERTOPIC);

                        arrayProbabilities.add(new ItemTopic(numK,p));

                        total += p;
                    }

                    kNew = -1;
                    r = Math.random();
                    sum = 0.0;
                    kLast = -1;

                    for(ItemTopic t:arrayProbabilities )
                    {
                        sum += t.getProbability()/total;
                        kLast = t.getTopicID();
                        if (sum > r)
                        {
                            kNew = kLast;
                            break;
                        }
                    }
                    if (kNew == -1)
                        kNew = kLast;

                    if (row.getTypeNe() != -1 && row.getTypeNe() == topicAssignment[cumI]){
                        kNew = topicAssignment[cumI];
                    }

                    topicAssignment[cumI] = kNew;
                    topicWord[kNew][row.getWordID()] += 1;
                    docTopic[row.getDocID()][kNew] += 1;
                    topics[kNew] += 1;
                    docLength[row.getDocID()] += 1;
                    //row.setType_ne(kNew);

                    if (iter_L == 500 || iter_L == 999){
                        if (topicAssignment[cumI] == row.getTypeNe()){
                            ResultTable resultTable = new ResultTable(iter_L,row.getDocID(),row.getWordID(),
                                    topicAssignment[cumI],row.getTypeNe(),1);
                            dbNew.addObject(resultTable);
                        }
                        else {
                            ResultTable resultTable = new ResultTable(iter_L,row.getDocID(),row.getWordID(),
                                    topicAssignment[cumI],row.getTypeNe(),0);
                            dbNew.addObject(resultTable);
                        }
                    }


                    cumI += 1;
                    }

            }
            // написать проверку на совпадения NE в топ 20 слов темы
            /*quality = 0;
            for(int topicNum = 1; topicNum < 7; topicNum++){
                arr = topicWord[topicNum];
                Long[] newArray = ArrayUtils.toObject(arr);
                ArrayIndexComparator comparator = new ArrayIndexComparator(newArray);
                Integer[] indexes = comparator.createIndexArray();
                Arrays.sort(indexes, comparator);
                localQuality = 0;
                for(int i = indexes.length - 1; i >= indexes.length - 21; i--){
                    if (dbNew.getRowById(indexes[i]).getTypeNe() == topicNum){
                        localQuality++;
                    }
                }
                if (localQuality >= 5){
                    quality++;
                }
            }
            if (quality >= 5){
                System.out.println("Номер итерации с quality >= 5: " + iter_L);
            }*/
            System.out.println(iter_L); // 17 секунд итерация; 3 минуты - 10 итераций; 5 часов - 1000 итераций;
        }

    }

}
