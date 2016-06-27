/*
 * Copyright (C) 2011-2015 PILE Project, Inc. <dev@pileproject.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pileproject.drive.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Management Helper
 *
 * @author <a href="mailto:tatsuyaw0c@gmail.com">Tatsuya Iwanari</a>
 * @version 1.0 4-June-2013
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    // -- program data to be executed --
    public static final String TBL_PROGRAM_DATA = "program_data";
    // class name of block
    public static final String BLOCK_NAME = "block_name";
    // left (x) of block
    public static final String BLOCK_LEFT = "block_left";
    // top (y) of block
    public static final String BLOCK_TOP = "block_top";
    // right (x) of block
    public static final String BLOCK_RIGHT = "block_right";
    // bottom (y) of block
    public static final String BLOCK_BOTTOM = "block_bottom";
    // value of block
    // e.g. how long the block lasts
    public static final String BLOCK_NUM = "block_num";
    // -- list of saved program names --
    public static final String TBL_SAVED_PROGRAMS = "saved_programs";
    // name of program
    public static final String PROGRAM_NAME = "program_name";
    // sample (1) or not (0)
    public static final String IS_SAMPLE = "is_sample";
    public static final String CREATED_AT = "created_at";
    // -- saved program data --
    // use the same columns in TBL_PROGRAM_DATA
    public static final String TBL_SAVED_PROGRAM_DATA = "saved_program_data";
    // foreign key (TBL_SAVED_PROGRAM)
    public static final String SAVED_PROGRAM_ID = "saved_program_id";

    // database name
    private static final String DB_NAME = "Mity.db";
    private static final int DB_VERSION = 3;
    private static DatabaseOpenHelper mInstance = null;
    private int mWritableDatabaseCount = 0;

    private static final String ID_DEFINITION =
            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";

    /**
     * Constructor
     *
     * @param context The context of Activity that calls this manager
     */
    private DatabaseOpenHelper(Context context) {
        // Context, Database Name, Cursor Factory, Database Version
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Return Singleton
     *
     * @param context The context of Activity that calls this manager
     * @return DatabaseOpenHelper The instance of this class
     */
    synchronized static public DatabaseOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseOpenHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    synchronized public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        if (db != null) {
            ++mWritableDatabaseCount;
        }
        return db;
    }

    /**
     * Close database
     *
     * @param database The target database
     */
    synchronized public void closeWritableDatabase(SQLiteDatabase database) {
        if (mWritableDatabaseCount > 0 && database != null) {
            --mWritableDatabaseCount;
            if (mWritableDatabaseCount == 0) {
                database.close();
            }
        }
    }

    /**
     * Create database if the database doesn't exist
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                           + TBL_PROGRAM_DATA
                           + " (" + ID_DEFINITION
                           + BLOCK_NAME + " TEXT NOT NULL, "
                           + BLOCK_LEFT + " INTEGER NOT NULL, "
                           + BLOCK_TOP + " INTEGER NOT NULL, "
                           + BLOCK_RIGHT + " INTEGER NOT NULL, "
                           + BLOCK_BOTTOM + " INTEGER NOT NULL, "
                           + BLOCK_NUM + " INTEGER NOT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_SAVED_PROGRAMS
                           + " (" + ID_DEFINITION
                           + PROGRAM_NAME + " TEXT NOT NULL, "
                           + IS_SAMPLE + " INTEGER NOT NULL, "
                           + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_SAVED_PROGRAM_DATA
                           + " (" + ID_DEFINITION
                           + SAVED_PROGRAM_ID + " INTEGER NOT NULL, "
                           + BLOCK_NAME + " TEXT NOT NULL, "
                           + BLOCK_LEFT + " INTEGER NOT NULL, "
                           + BLOCK_TOP + " INTEGER NOT NULL, "
                           + BLOCK_RIGHT + " INTEGER NOT NULL, "
                           + BLOCK_BOTTOM + " INTEGER NOT NULL, "
                           + BLOCK_NUM + " INTEGER NOT NULL);");
    }

    /**
     * Update database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DB_VERSION) {
            db.execSQL("DROP TABLE " + TBL_PROGRAM_DATA + ";");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_PROGRAM_DATA
                               + " (" + ID_DEFINITION
                               + BLOCK_NAME + " TEXT NOT NULL, "
                               + BLOCK_LEFT + " INTEGER NOT NULL, "
                               + BLOCK_TOP + " INTEGER NOT NULL, "
                               + BLOCK_RIGHT + " INTEGER NOT NULL, "
                               + BLOCK_BOTTOM + " INTEGER NOT NULL, "
                               + BLOCK_NUM + " INTEGER NOT NULL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_SAVED_PROGRAMS
                               + " (" + ID_DEFINITION
                               + PROGRAM_NAME + " TEXT NOT NULL, "
                               + IS_SAMPLE + " INTEGER NOT NULL, "
                               + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TBL_SAVED_PROGRAM_DATA
                               + " (" + ID_DEFINITION
                               + SAVED_PROGRAM_ID + " INTEGER NOT NULL, "
                               + BLOCK_NAME + " TEXT NOT NULL, "
                               + BLOCK_LEFT + " INTEGER NOT NULL, "
                               + BLOCK_TOP + " INTEGER NOT NULL, "
                               + BLOCK_RIGHT + " INTEGER NOT NULL, "
                               + BLOCK_BOTTOM + " INTEGER NOT NULL, "
                               + BLOCK_NUM + " INTEGER NOT NULL);");
        }
    }

}