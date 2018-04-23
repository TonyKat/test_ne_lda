package DBService;

import data.DataSet;
import data.OrderedDict;
import data.ResultTable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;

public class DBPostgreSQL {
    private SessionFactory sessionfactory;
    private Transaction trx;
    private static DBPostgreSQL dbPostgreSQL;
    private ArrayList<DataSet> rows;
    private ArrayList<OrderedDict> dictFull;

    private DBPostgreSQL() {
        Configuration config = new Configuration();
        config.addAnnotatedClass(DataSet.class);
        config.addAnnotatedClass(OrderedDict.class);
        config.addAnnotatedClass(ResultTable.class);
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/texts_politics");
        /*
        con = psycopg2.connect(database="texts_politics", user="postgres", password="197346qaz", host="127.0.0.1",
                           port="5432", )
        */
        config.setProperty("hibernate.connection.username", "postgres");
        config.setProperty("hibernate.connection.password", "197346qaz");
        config.setProperty("hibernate.show_sql", "false");
        config.setProperty("hibernate.hbm2ddl.auto", "update");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(config.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        sessionfactory = config.buildSessionFactory(serviceRegistry);

    }

    public static DBPostgreSQL instance() {
        if (dbPostgreSQL == null)
            dbPostgreSQL = new DBPostgreSQL();
        return (DBPostgreSQL) dbPostgreSQL;
    }

    public void getObjectNewsRbc(){
        Session session = sessionfactory.openSession();
        List row = session.createCriteria(DataSet.class).list();
        rows = (ArrayList<DataSet>) row;
        session.close();
    }

    public void getObjectOrderedDict(){
        Session session = sessionfactory.openSession();
        List row = session.createCriteria(OrderedDict.class).list();
        dictFull = (ArrayList<OrderedDict>) row;
        session.close();
    }

    public void addObject(Object object){
            Session session = sessionfactory.openSession();
            trx = session.beginTransaction();
            session.merge(object);
            trx.commit();
            session.close();
    }

    public long getIdByWord(String word) {
        Session session = sessionfactory.openSession();
        OrderedDict orderedDict = (OrderedDict) session.createCriteria(OrderedDict.class)
                .add(Restrictions.eq("word", word)).uniqueResult();
        session.close();
        return orderedDict.getId();
    }

    public ArrayList<DataSet> getRows() {
        return rows;
    }

    public void setRows(ArrayList<DataSet> rows) {
        this.rows = rows;
    }

    public ArrayList<OrderedDict> getDictFull() {
        return dictFull;
    }

    public OrderedDict getRowById(int id) {
        Session session = sessionfactory.openSession();
        OrderedDict orderedDict = (OrderedDict) session.get(OrderedDict.class, id);
        session.close();
        return orderedDict;
    }

    public void setDictFull(ArrayList<OrderedDict> dictFull) {
        this.dictFull = dictFull;
    }
}

