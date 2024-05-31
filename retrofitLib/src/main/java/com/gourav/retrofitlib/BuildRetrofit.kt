package com.gourav.retrofitlib

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.gourav.retrofitlib.constants.ErrorMessage
import com.gourav.retrofitlib.model.ResponseModel
import org.json.JSONTokener
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BuildRetrofit {
    fun getRetrofitInstance(BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getResponse(response: Response<JsonElement>): ResponseModel {
        val httpStatusCode = HttpStatusCode.values().firstOrNull {
                statusCode -> statusCode.code == response.code() } ?: HttpStatusCode.Unknown

        System.err.println(">>>>>>> status code  $httpStatusCode")
        System.err.println(">>>>>>> status code  ${httpStatusCode.code}")

        when (httpStatusCode.code) {
            in 200..202 -> {
                return ResponseModel(httpStatusCode.code, true, "", response.body())
            }
            in 400..499 -> {
                return ResponseModel(
                    httpStatusCode.code,
                    false,
                    httpStatusCode.name,
                    Gson().fromJson(response.errorBody().toString(), JsonElement::class.java))
            }
            in 500..599 -> {
                return ResponseModel(
                    httpStatusCode.code,
                    false,
                    httpStatusCode.name,
                    Gson().fromJson(response.errorBody().toString(), JsonElement::class.java))
            }
            999 -> {
                return  ResponseModel(httpStatusCode.code,false,httpStatusCode.name,
                    Gson().fromJson(response.errorBody().toString(), JsonElement::class.java))
            }
            else -> {
                return ResponseModel(
                    httpStatusCode.code,
                    false,
                    ErrorMessage.ERROR_UNDEFINED,
                    Gson().fromJson(response.errorBody().toString(), JsonElement::class.java)
                )
            }
        }
    }

    //Use if response is in JSONObject as in {...}
    fun <T : Any> convertToObject(response: String, type: Class<T>): T {
        val json = JSONTokener(response).nextValue()
        return Gson().fromJson(json.toString(), type)
    }

    //Use if response is in JSONArray as in [{...}]
    /*fun convertToList(response: String, type: Type?): List<Type> {
        val json = JSONTokener(response).nextValue()
        return if (json is JSONArray) {
            Gson().fromJson<List<Type>?>(json.toString(), type).toList()
        } else {
            Gson().fromJson(json.toString(), type)
        }
    }*/
}

enum class HttpStatusCode(val code: Int) {

    Unknown(-1),

    //Success
    Success(200),
    Created(201),
    Accepted(202),

    // Client Errors
    BadRequest(400),
    Unauthorized(401),
    PaymentRequired(402),
    Forbidden(403),
    NotFound(404),
    MethodNotAllowed(405),
    NotAcceptable(406),
    ProxyAuthenticationRequired(407),
    RequestTimeout(408),
    Conflict(409),
    Gone(410),
    LengthRequired(411),
    PreconditionFailed(412),
    PayloadTooLarge(413),
    UriTooLong(414),
    UnsupportedMediaType(415),
    RangeNotSatisfiable(416),
    ExpectationFailed(417),
    ImATeapot(418),
    MisdirectedRequest(421),
    UnProcessableEntity(422),
    Locked(423),
    FailedDependency(424),
    UpgradeRequired(426),
    PreconditionRequired(428),
    TooManyRequests(429),
    RequestHeaderFieldsTooLarge(431),
    UnavailableForLegalReasons(451),

    // Server Errors
    InternalServerError(500),
    NotImplemented(501),
    BadGateway(502),
    ServiceUnavailable(503),
    GatewayTimeout(504),
    HttpVersionNotSupported(505),
    VariantAlsoNegates(506),
    InsufficientStorage(507),
    LoopDetected(508),
    NotExtended(510),
    NetworkAuthenticationRequired(511),

    //force update
    ForceUpdate(999)
}