package com.example.university.usefull_stuff

class StringInt(str: String, i: Int) {
    var string: String = str
        get() {
            return field
        }
        set(value){
            field = value
        }
    var int : Int = i
        get() {
            return field
        }
        set(value){
            field = value
        }
}