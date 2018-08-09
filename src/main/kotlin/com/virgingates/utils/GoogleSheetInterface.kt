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
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.ValueRange
import io.vertx.core.logging.LoggerFactory
import java.io.File
import java.io.InputStreamReader
import java.util.*

class GoogleSheetInterface {

    private val logger = LoggerFactory.getLogger(GoogleSheetInterface::class.java)

    private val tokensPath = "tokens"
    private val scopes = listOf(SheetsScopes.SPREADSHEETS)
    private val credentialsFilePath = "credentials.json"
    private val jsonFactory = JacksonFactory.getDefaultInstance()
    private val applicationName = "Google Sheets API Java Quickstart"

    fun readRange(spreadsheetId: String, range: String): List<List<Any>> = try {
        logger.info("Reading range $range from sheet $spreadsheetId")
        buildSheets()
                .spreadsheets()
                .values()
                .get(spreadsheetId, range)
                .execute()
                .getValues()
    } catch (e: IllegalStateException) {
        Arrays.asList()
    }

    fun writeRange(spreadsheetId: String, startCell: String, values: List<List<Any>>) {
        logger.info("Writing ${values.size} row starting from cell $startCell into sheet $spreadsheetId")
        val body = ValueRange().setValues(values)

        buildSheets()
                .spreadsheets()
                .values()
                .update(spreadsheetId, startCell, body)
                .setValueInputOption("RAW")
                .execute()
    }

    fun createSheet(): String {
        val spreadsheetId = buildSheets().spreadsheets().create(Spreadsheet()).execute().spreadsheetId
        logger.info("Sheet with id $spreadsheetId has been created successfully")
        return spreadsheetId
    }

    private fun buildSheets(): Sheets {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        return Sheets.Builder(httpTransport, jsonFactory, getCredentials(httpTransport))
                .setApplicationName(applicationName)
                .build()
    }

    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        val inputStream = GoogleSheetInterface::class.java.classLoader.getResourceAsStream(credentialsFilePath)
        val clientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(inputStream))
        val token = File(tokensPath)

        val flow = GoogleAuthorizationCodeFlow
                .Builder(HTTP_TRANSPORT, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(FileDataStoreFactory(token))
                .setAccessType("offline")
                .build()

        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }
}
