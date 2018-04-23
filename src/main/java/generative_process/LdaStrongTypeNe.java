package generative_process;

import DBService.DBPostgreSQL;
import data.DataSet;
import data.ResultTable;

import java.util.ArrayList;


public class LdaStrongTypeNe {
    // количество слов в документе m, присвоенных теме k
    // n_m_k; doc_topic[di,ti]
    private long[][] docTopic;
    // длина документа
    // n_m; n_d[di,0]
    private long[] docLength;
    // количество совпадений термина t, присвоенного теме k в коллекции
    // n_k_t; word_topic[wi,ti]
    private long[][] topicWord;
    // количество терминов присвоенных теме k
    // n_k; topics[ti]
    private long[] topics;

    private DBPostgreSQL dbNew = DBPostgreSQL.instance();

    private int[] topicAssignment;

    private ArrayList<DataSet> rows;

    private final int NUMBERTOPIC = 100;

    public LdaStrongTypeNe(){
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

        int cur = 0;
        for (DataSet row : rows){
            for (int i = 0; i < row.getTf(); i++){
                if (row.getTypeNe() == -1){
                    topicAssignment[cur] = (int)(Math.random()*100);
                }
                else{
                    topicAssignment[cur] = row.getTypeNe();
                }
                cur++;
            }
        }

    }

    public void gererativeProcess(){
        System.out.println("gererativeProcess...");
        int corpusId = 0;
        for(DataSet row:rows) {
            for(int i = 0; i < row.getTf(); i++) {
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
        ArrayList<ItemTopic> arrayProbabilities;
        double total;
        int kNew;
        double r;
        double sum;
        int kLast;
        /*long arr[];
        int quality;
        int localQuality;*/
        System.out.println("gibbsSampling...");
        for(int iterL = 0; iterL < L; iterL++){
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

                    if (row.getTypeNe() != -1){
                        kNew = topicAssignment[cumI];
                    }

                    topicAssignment[cumI] = kNew;
                    topicWord[kNew][row.getWordID()] += 1;
                    docTopic[row.getDocID()][kNew] += 1;
                    topics[kNew] += 1;
                    docLength[row.getDocID()] += 1;
                    //row.setType_ne(kNew);

                    if (iterL == 500 || iterL == 999){
                        if (topicAssignment[cumI] == row.getTypeNe()){
                            ResultTable resultTable = new ResultTable(iterL,row.getDocID(),row.getWordID(),
                                    topicAssignment[cumI],row.getTypeNe(),1);
                            dbNew.addObject(resultTable);
                        }
                        else {
                            ResultTable resultTable = new ResultTable(iterL,row.getDocID(),row.getWordID(),
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
                arr = n_k_t[topicNum];
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
                System.out.println("Номер итерации с quality >= 5: " + iterL);
            }*/
            System.out.println("Iteration: " + iterL); // 17 секунд итерация; 3 минуты - 10 итераций; 5 часов - 1000 итераций;
        }

    }

}
