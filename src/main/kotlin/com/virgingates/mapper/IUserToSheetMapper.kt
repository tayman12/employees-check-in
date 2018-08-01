package com.virgingates.mapper

interface IUserToSheetMapper {

    fun getSpreadSheetId(userId: String): String?
}