import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.StockImage

@Composable
fun App() {
    MaterialTheme {
        val imagesViewModel = getViewModel(Unit, viewModelFactory { ImagesViewModel() })

        Navigator(
            screen = ImagesPage(index = 0, imagesViewModel)
        ) {
            SlideTransition(it)
        }
    }
}

data class ImagesPage(
    val index: Int, val imagesViewModel: ImagesViewModel
) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val uiState by imagesViewModel.uiState.collectAsState(initial = ImageUiState())

        AnimatedVisibility(uiState.images.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                content = {
                    items(uiState.images) {
                        StockImageCell(it) { url ->
                            navigator.push(ImagesDetailPage(url))
                        }
                    }
                }
            )
        }
    }
}

data class ImagesDetailPage(val imageUrl: String) : Screen {
    @Composable
    override fun Content() {
        AnimatedVisibility(imageUrl.isNotEmpty()) {
            KamelImage(
                resource = asyncPainterResource(imageUrl),
                contentDescription = "imageUrl",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
            )
        }
    }
}

@Composable
fun StockImageCell(it: StockImage, navigateToImageView: (String) -> Unit) {
    KamelImage(
        resource = asyncPainterResource(it.src.medium),
        contentDescription = "${it.alt} by ${it.photographer}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).clickable {
            navigateToImageView(it.src.medium)
        }
    )
}

expect fun getPlatformName(): String