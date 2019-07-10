package org.kotlin.mpp.mobile.data.entity

import kotlinx.serialization.Serializable


@Serializable
data class AuthorizationRequest(
    // TODO change property names in accordance with convention
    val client_id: Int,
    val username: String,
    val password: String,
    val grant_type: String = "password",
    val scope: String = "*"
)