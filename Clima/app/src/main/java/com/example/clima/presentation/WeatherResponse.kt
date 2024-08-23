import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: Current,

    @SerializedName("daily")
    val daily: List<Daily>
)

data class Current(
    @SerializedName("temp")
    val temperature: Float,

    @SerializedName("humidity")
    val humidity: Int
)

data class Daily(
    @SerializedName("temp")
    val temp: Temp
)

data class Temp(
    @SerializedName("max")
    val max: Float,

    @SerializedName("min")
    val min: Float
)
