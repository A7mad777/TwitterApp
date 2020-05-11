package com.example.future.twitterapp.Model

class PostInfo {
    var UserId : String ?= null
    var text : String ?= null
    var DownLoadUrl : String ?= null


    constructor(UserId: String?, text: String?, DownLoadUrl: String?) {
        this.UserId = UserId
        this.text = text
        this.DownLoadUrl = DownLoadUrl
    }
}