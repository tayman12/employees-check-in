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


val JSON_FACTORY = JacksonFactory.getDefaultInstance()
val APPLICATION_NAME = "Google Sheets API Java Quickstart"

class GoogleAuthorizeUtil {

  private val TOKENS_DIRECTORY_PATH = "tokens"
  private val SCOPES = listOf(SheetsScopes.SPREADSHEETS)
  private val CREDENTIALS_FILE_PATH = "credentials.json"

  fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
    val inputStream = GoogleAuthorizeUtil::class.java.classLoader.getResourceAsStream(CREDENTIALS_FILE_PATH)
    val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))

    // Build flow and trigger user authorization request.
    val flow = GoogleAuthorizationCodeFlow.Builder(
      HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
      .setDataStoreFactory(FileDataStoreFactory(java.io.File(TOKENS_DIRECTORY_PATH)))
      .setAccessType("offline")
      .build()
    return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
  }

  fun readRange(spreadsheetId: String, range: String): List<List<Any>> {
    val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleAuthorizeUtil().getCredentials(HTTP_TRANSPORT))
      .setApplicationName(APPLICATION_NAME)
      .build()
    val response = service.spreadsheets().values()
      .get(spreadsheetId, range)
      .execute()
    return response.getValues()

  }

  fun writeRange(spreadsheetId: String, range: String, values: ValueRange) {
    val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleAuthorizeUtil().getCredentials(HTTP_TRANSPORT))
      .setApplicationName(APPLICATION_NAME)
      .build()
    service.spreadsheets().values()
      .update(spreadsheetId, range, values)
      .execute()
  }
}

fun main(args: Array<String>) {
  val g = GoogleAuthorizeUtil()
  val data = ValueRange();
  data.put("Class Data!A4", "haytham")

  g.writeRange("1lHtOlfWaucCkncNsMdKGReV92ipbcLkuWxfPLgHIP48", "Class Data!A4", data)

  val values = GoogleAuthorizeUtil().readRange("1lHtOlfWaucCkncNsMdKGReV92ipbcLkuWxfPLgHIP48", "Class Data!A1")
  if (values == null || values!!.isEmpty()) {
    println("No data found.")
  } else {
    for (row in values!!) {
      System.out.printf("%s\n", row.get(0))
    }
  }
}
