import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.StockImage

@Composable
fun App() {
    MaterialTheme {
        val imagesViewModel = getViewModel(Unit, viewModelFactory { ImagesViewModel() })
        ImagesPage(imagesViewModel)
    }
}

@Composable
fun ImagesPage(imagesViewModel: ImagesViewModel) {
    val uiState by imagesViewModel.uiState.collectAsState(initial = ImageUiState())

    AnimatedVisibility(uiState.images.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            content = {
                items(uiState.images) {
                    StockImageCell(it)
                }
            }
        )
    }

}

@Composable
fun StockImageCell(it: StockImage) {
    KamelImage(
        asyncPainterResource(it.src.medium),
        "${it.alt} by ${it.photographer}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
    )
}

expect fun getPlatformName(): String