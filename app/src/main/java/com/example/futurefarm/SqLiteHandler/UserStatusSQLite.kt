package com.example.futurefarm.SqLiteHandler

import com.example.futurefarm.Login

class UserStatusSQLite {


    var id : Int = 0
    var login_status : Int = 0
    var user_id : String = ""

    constructor(login_status:Int,user_id:String){
        this.login_status = login_status
        this.user_id = user_id
    }

}