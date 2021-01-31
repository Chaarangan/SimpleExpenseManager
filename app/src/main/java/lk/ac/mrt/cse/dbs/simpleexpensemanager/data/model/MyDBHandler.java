package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;


public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_PATH = "/data/data/lk.ac.mrt.cse.dbs.simpleexpensemanager/databases";
    private static final String DATABASE_NAME = "mydatabase";

    //Table account
    public static final String TABLE_ACCOUNT = "accounts";
    public static final String COLUMN_ACCOUNT_ID = "_id";
    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";

    //Table transaction
    public static final String TABLE_TRANSACTION = "transactions";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACCOUNT_NUMBER = "accountNo";
    public static final String COLUMN_EXPENSE_TYPE = "expenseType";
    public static final String COLUMN_AMOUNT = "amount";

    SQLiteDatabase db;
    private final Context context;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public synchronized void close () {
        if (db != null) {
            db.close();
            super.close();
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        File dir = new File("/data/data/lk.ac.mrt.cse.dbs.simpleexpensemanager");
        if(!dir.exists()){
            dir.mkdirs();
        }
        File dir1 = new File("/data/data/lk.ac.mrt.cse.dbs.simpleexpensemanager/databases");
        if(!dir1.exists()){
            dir1.mkdirs();
        }
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_ACCOUNT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_ACCOUNT_NO + " VARCHAR, " + COLUMN_BANK_NAME
                + " VARCHAR, " + COLUMN_ACCOUNT_HOLDER_NAME + " VARCHAR, " + COLUMN_BALANCE + " DOUBLE )";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_TRANSACTION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE + " VARCHAR , " + COLUMN_ACCOUNT_NUMBER + " VARCHAR, " + COLUMN_EXPENSE_TYPE
                + " VARCHAR, " + COLUMN_AMOUNT + " DOUBLE, FOREIGN KEY (" + COLUMN_ACCOUNT_NUMBER + ") " +
                "      REFERENCES "+ TABLE_ACCOUNT + "(" + COLUMN_ACCOUNT_NUMBER + ") " +
                "         ON DELETE CASCADE " +
                "         ON UPDATE NO ACTION)";
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getWritableDatabase()
    {
        db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return db;
    }

    @Override
    public SQLiteDatabase getReadableDatabase()
    {
        db = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return db;
    }
}