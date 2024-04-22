package com.example.honeystocktake.api

import com.google.gson.annotations.SerializedName

data class Datamodel(
    @SerializedName("LOCATION")
    var locationValue: String, // Change field name to match the JSON key "SHEET_NAME"
    @SerializedName("")
    var plateValue: String, // Change field name to match the JSON key "STOCKNUM"
    @SerializedName("LENGTH_RECT")
    var length: String, // Change field name to match the JSON key "LENGTH_RECT"
    @SerializedName("WIDTH_RECT")
    var width: String, // Change field name to match the JSON key "WIDTH_RECT"
    @SerializedName("THICKNESS")
    var thickness: String, // Change field name to match the JSON key "THICKNESS"
    // Grade of plate (if not provided in the JSON, it will default to "N/A")
    @SerializedName("STOCKNUM")
    var grade: String = "N/A",
    // Name of the sheet for picture retrieval use only (if not provided in the JSON, it will default to "N/A")
    @SerializedName("SHEET_NAME")
    var plateSname: String = "N/A",
    // What is known to be wrong with the plate (if not provided in the JSON, it will default to 0)
    @SerializedName("QUERY")
    var query: Int = 0,
    // What is the last known person to take stock
    @SerializedName("ST_BY")
    var stockBy: String = "0",
    // What is last stock take date
    @SerializedName("ST_DATETIME")
    var stockDate: String = "0",
    // Is it a full plate
    @SerializedName("FULLPLATE")
    var fullPlate: Int = 0

)

//data class UserData (
//    var name: String,
//    var level: Int
//)


data class ApiResponse(
    @SerializedName("USERNAME") val username: String?,
    @SerializedName("LEVELNO") val levelNo: Int?
)


data class spinnerListData(
    @SerializedName("REASON") val reason: String?,
    @SerializedName("VALUE") val value: Int?
)

data class Location(
    @SerializedName("LOCATION") val location: String?
)

data class bulk(
    @SerializedName("LOCATION")
    var locationValue: String, // Change field name to match the JSON key "SHEET_NAME"
    @SerializedName("")
    var plateValue: String, // Change field name to match the JSON key "STOCKNUM"
    @SerializedName("LENGTH_RECT")
    var length: String, // Change field name to match the JSON key "LENGTH_RECT"
    @SerializedName("WIDTH_RECT")
    var width: String, // Change field name to match the JSON key "WIDTH_RECT"
    @SerializedName("THICKNESS")
    var thickness: String, // Change field name to match the JSON key "THICKNESS"
    // Grade of plate (if not provided in the JSON, it will default to "N/A")
    @SerializedName("STOCKNUM")
    var grade: String = "N/A",
    // Name of the sheet for picture retrieval use only (if not provided in the JSON, it will default to "N/A")
    @SerializedName("SHEET_NAME")
    var plateSname: String = "N/A",
    // What is known to be wrong with the plate (if not provided in the JSON, it will default to 0)
    @SerializedName("QUERY")
    var query: Int = 0,
    // What is the last known person to take stock
    @SerializedName("ST_BY")
    var stockBy: String = "0",
    // What is last stock take date
    @SerializedName("ST_DATETIME")
    var stockDate: String = "0",
    // Is it a full plate
    @SerializedName("FULLPLATE")
    var fullPlate: Int = 0
)