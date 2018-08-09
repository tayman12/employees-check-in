package com.virgingates.mapper

import io.vertx.core.logging.LoggerFactory
import java.util.*

class UserToSheetMapper : IUserToSheetMapper {

    private val logger = LoggerFactory.getLogger(UserToSheetMapper::class.java)

    private val userIdToSheetId = Properties()

    init {
        logger.info("Initializing user to sheet mapper")
        userIdToSheetId.load(this::class.java.classLoader.getResourceAsStream("sheetsMapping.properties"))
        logger.info("Initializing user to sheet mapper succeeded")
    }

    override fun getSpreadSheetId(userId: String): String? = if (userIdToSheetId.containsKey(userId)) {
        val spreadSheetId = userIdToSheetId.getValue(userId) as String
        logger.info("User $userId has been mapped to sheet $spreadSheetId")
        spreadSheetId
    } else {
        logger.info("User $userId could not be mapped to any sheet")
        null
    }
}
