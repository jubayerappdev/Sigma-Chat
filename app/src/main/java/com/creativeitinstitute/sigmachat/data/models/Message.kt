package com.creativeitinstitute.sigmachat.data.models

interface Message {
    var senderID:String
    var receiver:String
    var msgID:String
}

data class TextMessages(
    var text:String?=null,
    override var senderID: String,
    override var receiver: String,
    override var msgID: String
):Message


data class ImageMessage(
    var imageLink:String = "",
    override var senderID: String,
    override var receiver: String,
    override var msgID: String
):Message
