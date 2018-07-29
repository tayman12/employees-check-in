package com.virgingates.utils

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import java.io.InputStreamReader

class GoogleSheetsUtil {

    private val TOKENS_DIRECTORY_PATH = "tokens"
    private val SCOPES = listOf(SheetsScopes.SPREADSHEETS)
    private val CREDENTIALS_FILE_PATH = "credentials.json"
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private val APPLICATION_NAME = "Google Sheets API Java Quickstart"

    fun readRange(spreadsheetId: String, range: String): List<List<Any>> {
        return buildSheets()
                .spreadsheets()
                .values()
                .get(spreadsheetId, range)
                .execute()
                .getValues()
    }

    fun writeRange(spreadsheetId: String, startCell: String, values: List<List<Any>>) {
        val body = ValueRange().setValues(values)

        buildSheets()
                .spreadsheets()
                .values()
                .update(spreadsheetId, startCell, body)
                .setValueInputOption("RAW")
                .execute()
    }

    private fun buildSheets(): Sheets {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        return Sheets.Builder(httpTransport, JSON_FACTORY, GoogleSheetsUtil().getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build()
    }

    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        val inputStream = GoogleSheetsUtil::class.java.classLoader.getResourceAsStream(CREDENTIALS_FILE_PATH)
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))

        val flow = GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(FileDataStoreFactory(java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()

        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }
}