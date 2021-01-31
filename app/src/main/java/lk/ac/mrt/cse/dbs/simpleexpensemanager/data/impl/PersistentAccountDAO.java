/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistentAccountDAO implements AccountDAO {
    SQLiteOpenHelper sqLiteOpenHelper;

    @Override
    public List<String> getAccountNumbersList(){
        String query = "Select accountNo FROM  accounts";

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        List<String> account_numbers = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                account_numbers.add(cursor.getString(0));
            }
            cursor.close();
        } else {
            account_numbers = null;
        }
        db.close();
        return account_numbers;
    }

    @Override
    public List<Account> getAccountsList(){
        String query = "Select * FROM accounts";

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        List<Account> accounts = new ArrayList<Account>();


        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
                accounts.add(account);
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            accounts = null;
        }
        db.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String query = "Select * FROM accounts where _accountNo = " + accountNo + "\"";

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        Account account;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            account = new Account(accountNo, cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            cursor.close();
        } else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        db.close();
        return account;
    }

    @Override
    public void addAccount(Account account){
        ContentValues values = new ContentValues();
        values.put("accountNo", account.getAccountNo());
        values.put("bankName", account.getBankName());
        values.put("accountHolderName", account.getAccountHolderName());
        values.put("balance", account.getBalance());

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        db.insert("accounts", null, values);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo)  throws InvalidAccountException {
        String query = "Select * FROM accounts WHERE accountNo = " + accountNo + "\"";

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete("accounts", "accountNo" + " = ?",
                    new String[] { String.valueOf(accountNo)});
            cursor.close();
        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String query = "Select balance FROM accounts where _accountNo = " + accountNo + "\"";

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            double oldBalance = Integer.parseInt(cursor.getString(3));

            double newBalance = 0;
            if(expenseType.equals("EXPENSE")){
                newBalance = oldBalance - amount;
            }
            else{
                newBalance = oldBalance + amount;
            }

            String query1 = "UPDATE accounts SET balance = " + newBalance + " WHERE accountNo = " + accountNo;
            db.execSQL(query1);
            cursor.close();
        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        db.close();
    }
}
