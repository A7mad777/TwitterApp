package com.example.future.twitterapp.Model

class Ticket {
    var tweetId : String ?= null
    var tweetText : String ?= null
    var tweetImageUrl : String ?= null
    var tweetPersonUID : String ?= null



    constructor(tweetId : String?,tweetText: String?,tweetImageUrl : String?,tweetPersonUID: String?) {
        this.tweetId = tweetId
        this.tweetText = tweetText
        this.tweetImageUrl = tweetImageUrl
        this.tweetPersonUID = tweetPersonUID

    }
}