package com.example.futurefarm.SqLiteHandler

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_user_registration.*
import java.security.AccessControlContext



class SqLiteDatabaseHAndler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
//        val createTable = "CREATE TABLE" + TABLE_NAME +" (" +
//                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
//                COL_USERID + "INTEGER," +
//                COL_LOGIN + "INTEGER)"
//
//        db.execSQL(createTable)
        db.execSQL("CREATE TABLE USER_STATE(_id integer primary key autoincrement, User_Id TEXT ,Login_Status integer)")
//        db.execSQL("INSERT INTO USER_STATE(User_Id, Login_Status)VALUES('2021','1')")

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//        db.execSQL(SQL_DELETE_ENTRIES)
//        onCreate(db)
        TODO("Not yet implemented")
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

//    fun insertData(userStatus : UserStatusSQLite){
//        val db = this.writableDatabase
//        val cv = ContentValues()
//        cv.put(COL_LOGIN,userStatus.login_status)
//        cv.put(COL_USERID,userStatus.user_id)
//        var result = db.insert(TABLE_NAME,null,cv)
//
//    }
       companion object{
        private val DATABASE_NAME = "MyDB"
        private val DATABASE_VERSION = 1
//        private val TABLE_NAME = "Status"
//        private val COL_LOGIN = "LoginStatus"
//        private val COL_USERID = "User_id"
//        private val COL_ID = "_id"

    }
}

