package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydatabase.db";

    //Table account
    public static final String TABLE_ACCOUNT = "account";

    public static final String COLUMN_ACCOUNT_NO = "_accountNo";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";

    //Table transaction
    public static final String TABLE_TRANSACTION = "transaction";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACCOUNT_NUMBER = "accountNo";
    public static final String COLUMN_EXPENSE_TYPE = "expenseType";
    public static final String COLUMN_AMOUNT = "amount";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_ACCOUNT + "("
                + COLUMN_ACCOUNT_NO + " VARCHAR PRIMARY KEY, " + COLUMN_BANK_NAME
                + " VARCHAR, " + COLUMN_ACCOUNT_HOLDER_NAME + " VARCHAR, " + COLUMN_BALANCE + " REAL )";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_TRANSACTION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DATE + " VARCHAR , " + COLUMN_ACCOUNT_NUMBER + " VARCHAR, " + COLUMN_EXPENSE_TYPE
                + " VARCHAR, " + COLUMN_AMOUNT + " REAL, FOREIGN KEY (" + COLUMN_ACCOUNT_NO + ") \n" +
                "      REFERENCES "+ TABLE_ACCOUNT + "(" + COLUMN_ACCOUNT_NUMBER + ") \n" +
                "         ON DELETE CASCADE \n" +
                "         ON UPDATE NO ACTION, )";
        db.execSQL(CREATE_ACCOUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }
}