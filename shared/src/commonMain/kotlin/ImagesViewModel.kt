import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.StockImage
import model.StockImageResponse


data class ImageUiState(
    val images: List<StockImage> = emptyList()
)

class ImagesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ImageUiState())
    var uiState = _uiState.asSharedFlow()

    init {
        updateImages()
    }

    private fun updateImages() {
        viewModelScope.launch {
            val images = getImages()
            _uiState.update {
                it.copy(images = images)
            }
        }
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend fun getImages(): List<StockImage> {
        val response = httpClient.get(Constants.PEXELS_URL + Constants.PEXELS_PHOTOS) {
            header(HttpHeaders.Authorization, Constants.PEXELS_API_KEY)
        }.body<StockImageResponse>()

        return response.photos
    }

}