package generative_process;

import DBService.DBPostgreSQL;
import data.DataSet;
import data.ResultTable;

import java.util.ArrayList;

public class GenerativeProcess {
    // количество слов в документе m, присвоенных теме k
    //private ArrayList<ArrayList<Integer>> n_m_k; doc_topic[di,ti]
    private long[][] n_m_k;
    // длина документа
    //private ArrayList<Integer> n_m; n_d[di,0]
    private long[] n_m;
    // количество совпадений термина t, присвоенного теме k в коллекции
    //private ArrayList<ArrayList<Integer>> n_k_t; word_topic[wi,ti]
    private long[][] n_k_t;
    // количество терминов присвоенных теме k
    //private ArrayList<Integer> n_k; topics[ti]
    private long[] n_k;

    private DBPostgreSQL dbNew = DBPostgreSQL.instance();

    private int[] topic_assignment;

    private ArrayList<DataSet> rows;

    private final int NUMBERTOPIC = 100;

    public GenerativeProcess(){
        int sizeDocs = dbNew.getRows().size();
        int sizeDictFull = dbNew.getDictFull().size();

        n_m_k = new long[sizeDocs][NUMBERTOPIC];
        n_m = new long[sizeDocs];
        n_k_t = new long[sizeDictFull][NUMBERTOPIC];
        n_k = new long[NUMBERTOPIC];

        rows = dbNew.getRows();

        int sumTf = 0;
        for (DataSet row : rows) {
            sumTf += row.getTf();
        }
        topic_assignment = new int[sumTf];
        for(int i = 0; i < sumTf; i++){
            topic_assignment[i] = (int)(Math.random()*100);
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

        //int[] topic_assignment = new int[];// np.random.randint(0, number_topic, sum(rows[:, 2]), dtype=np.int)
        //corpus_id = 0
        /*
        #doc_id_ = rows[i][0]  # docID
        #word_id_ = rows[i][1]  # wordID
        #tf_word_ = rows[i][2]  # tf
        #type_ne_word_ = rows[i][3]  # type_ne = [0:6] or -1'''
        #topic_id = rows[i][4]  # topicID
         */
        System.out.println("ya tut");
        int corpus_id = 0;
        for(DataSet row:rows) {
            for(int i = 0; i < row.getTf(); i++) {
                //row.setTopicID(topic_assignment[corpus_id]);
                n_m_k[row.getDocID()][topic_assignment[corpus_id]] += 1;
                n_m[row.getDocID()] += 1;
                n_k_t[row.getWordID()][topic_assignment[corpus_id]] += 1;
                n_k[topic_assignment[corpus_id]] += 1;

                corpus_id += 1;

            }
        }

    }
    public void gibbsSampling(){
        int L = 500;
        double alpha = 50 / NUMBERTOPIC;
        double beta = 0.01;
        int cum_i;
        double p;
        ArrayList<ItemTopic> aP = new ArrayList<>();
        double total;
        int k_new;
        double r;
        double sum;
        int k_last;
        for(int iter_L = 0; iter_L < L; iter_L++){
            cum_i = 0;
            for(DataSet row:rows){
                for(int j = 0; j < row.getTf(); j++){
                    n_k_t[row.getWordID()][topic_assignment[cum_i]] -= 1;
                    n_m_k[row.getDocID()][topic_assignment[cum_i]] -= 1;
                    n_k[topic_assignment[cum_i]] -= 1;
                    n_m[row.getDocID()] -= 1;

                    total = 0.0;
                    aP = new ArrayList<>();
                    for(int numK = 0; numK < NUMBERTOPIC; numK++){
                        p = ((int)n_k_t[row.getWordID()][numK] + beta)/(int)n_k[numK] + beta*dbNew.getDictFull().size()*
                                ((int)n_m_k[row.getDocID()][numK] + alpha)/(int)n_m[row.getDocID()] + alpha * NUMBERTOPIC;

                        aP.add(new ItemTopic(numK,p));

                        total += p;
                    }

                    k_new = -1;
                    r = Math.random();
                    sum = 0.0;
                    k_last = -1;

                    for(ItemTopic t:aP )
                    {
                        sum += t.getProbability()/total;
                        k_last = t.getTopicID();
                        if (sum > r)
                        {
                            k_new = k_last;
                            break;
                        }
                    }
                    if (k_new == -1)
                        k_new = k_last;

                    if (row.getType_ne() != -1 && row.getType_ne() == topic_assignment[cum_i]){
                        k_new = topic_assignment[cum_i];
                    }

                    topic_assignment[cum_i] = k_new;
                    n_k_t[row.getWordID()][k_new] += 1;
                    n_m_k[row.getDocID()][k_new] += 1;
                    n_k[k_new] += 1;
                    n_m[row.getDocID()] += 1;
                    //row.setType_ne(k_new);

                    if (iter_L == 100 || iter_L == 200 || iter_L == 300 || iter_L == 400 || iter_L == 499){
                        if (topic_assignment[cum_i] == row.getType_ne()){
                            ResultTable resultTable = new ResultTable(iter_L,row.getDocID(),row.getWordID(),
                                    topic_assignment[cum_i],row.getType_ne(),1);
                            dbNew.addObject(resultTable);
                        }
                        else {
                            ResultTable resultTable = new ResultTable(iter_L,row.getDocID(),row.getWordID(),
                                    topic_assignment[cum_i],row.getType_ne(),0);
                            dbNew.addObject(resultTable);
                        }
                    }


                    cum_i += 1;
                    }

            }

            System.out.println(iter_L); // 17 секунд итерация; 3 минуты - 10 итераций
        }

    }

}
