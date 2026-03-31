package com.example.stylish.domain.util
// Ye base class hai, jsike andar hum 4 type ke result define kar rhe hain.
sealed class Result<out T>{
    data object Idle : Result<Nothing>() // nothing happen till now
    data object Loading: Result<Nothing>() // Operation ongoing (jaise Api request ja rhi hain)
    data class Success<T>(val data:T) : Result<T>() // operation succeed and we got data
    data class Failure(val message:String):Result<Nothing>() // Operation failed and we got error msg
}

/*
data class Success<T>(val data: T): Result<T>()

jab kaam success ho gaya ho aur apko result mila ho-
T, kisi bhi type ka ho sakta hai (String , User , List<Products>, etc.)
 */
