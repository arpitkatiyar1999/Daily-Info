package com.example.dailyinfo.ui.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dailyinfo.R
import com.example.dailyinfo.beans.ArticleSortDataBean
import com.example.dailyinfo.beans.ArticlesBean
import com.example.dailyinfo.request_response_state.RequestStatusEnum
import com.example.dailyinfo.ui.common.SpacerHeight4
import com.example.dailyinfo.ui.common.SpacerHeight8
import com.example.dailyinfo.ui.common.SpacerWidth12
import com.example.dailyinfo.ui.common.SpacerWidth4
import com.example.dailyinfo.ui.common.TextBold11
import com.example.dailyinfo.ui.common.TextBold14
import com.example.dailyinfo.ui.common.TextNormal11
import com.example.dailyinfo.ui.common.TextNormal12
import com.example.dailyinfo.ui.common.shimmer
import com.example.dailyinfo.ui.navigation.Screens
import com.example.dailyinfo.utils.FunctionHelper
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavController) {
    val viewModel: NewsViewModel = hiltViewModel()
    val context = LocalContext.current
    if (!viewModel.isArticleSortListInitialized) {
        viewModel.setUpValues(FunctionHelper.getArticleSortList(context))
    }
    val snackBarHostState = SnackbarHostState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState =
        SheetState(initialValue = SheetValue.Hidden, skipPartiallyExpanded = true)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        snackbarHostState = snackBarHostState,
        bottomSheetState = sheetState
    )
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetSwipeEnabled = false,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetTonalElevation = 0.dp,
        sheetShadowElevation = 6.dp,
        sheetContent = {
            BottomSheetSection(viewModel) {
                coroutineScope.launch {
                    sheetState.hide()
                }
            }
        },
        topBar = {
            Surface(shadowElevation = 3.dp) {
                TopAppBar(title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }, actions = {
                    IconButton(onClick = { viewModel.getNewsFeedList(true) }) {
                        Image(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh_data)
                        )
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            if (sheetState.isVisible) {
                                sheetState.hide()
                            } else {
                                sheetState.show()
                            }
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_sort),
                            contentDescription = stringResource(R.string.sort_data)
                        )
                    }
                })
            }
        }) {
        Column {
            HandleNewsArticleResponse(viewModel, navController)
        }
    }
    LaunchedEffect(key1 = viewModel.snackBarMessageState.value) {
        if (viewModel.snackBarMessageState.value.isNotBlank()) {
            snackBarHostState.showSnackbar(viewModel.snackBarMessageState.value)
            viewModel.snackBarMessageState.value = ""
        }
    }

}

@Composable
fun BottomSheetSection(viewModel: NewsViewModel, onClick: () -> Unit) {
    Column {
        viewModel.articleSortList.forEachIndexed { index, articleSortDataBean ->
            BottomSheetItem(
                articleSortDataBean = articleSortDataBean,
                viewModel.currentSelectedArticleFilter.value == index
            ) {
                viewModel.updateList(index)
                onClick()
            }
        }
    }
}


@Composable
fun BottomSheetItem(
    articleSortDataBean: ArticleSortDataBean,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .background(if (isSelected) Color.LightGray.copy(alpha = .2f) else Color.Transparent)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TextBold14(text = articleSortDataBean.title)
                TextNormal12(text = articleSortDataBean.description)
            }
            if (isSelected) {
                Image(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.selected),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
        }
        Divider()
    }

}

@Composable
private fun HandleNewsArticleResponse(viewModel: NewsViewModel, navController: NavController) {
    val newsResponseState = viewModel.newsFeedResponseStateFow.collectAsState().value
    var isErrorHandled by remember {
        mutableStateOf(false)
    }
    when (newsResponseState.status) {
        RequestStatusEnum.LOADING -> {
            isErrorHandled = false
            NewsFeedUiLoading()
        }

        RequestStatusEnum.EXCEPTION -> {
            if (!isErrorHandled) {
                viewModel.snackBarMessageState.value =
                    if (newsResponseState.message.isNullOrBlank()) {
                        stringResource(R.string.some_error_occurred_please_try_again_later)
                    } else {
                        newsResponseState.message
                    }
                isErrorHandled = true
            }
        }

        RequestStatusEnum.SUCCESS -> {
            NewsFeedUI(viewModel.articleList.value, viewModel, navController)
        }

        RequestStatusEnum.NONE -> {

        }
    }
}

@Composable
private fun NewsFeedUiLoading() {
    Column(modifier = Modifier.padding(16.dp)) {
        repeat(4) {
            NewsItemLoading()
        }
    }
}


@Composable
private fun NewsItemLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .shimmer()
    ) {}
}

@Composable
private fun NewsFeedUI(
    articlesList: List<ArticlesBean>,
    viewModel: NewsViewModel,
    navController: NavController
) {
    if (articlesList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(
                    R.string.no_result_found
                ),
            )
            Text(text = stringResource(R.string.currently_no_news_available), fontSize = 14.sp)
        }
    } else {
        LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
            items(articlesList) { article ->
                NewsItem(article = article, viewModel = viewModel, navController)
            }
        }
    }
}


@Composable
private fun NewsItem(
    article: ArticlesBean,
    viewModel: NewsViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier
            .clickable {
                val encodedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                if (encodedUrl != null) {
                    navController.navigate(Screens.WebViewScreen.route + "/$encodedUrl")
                } else {
                    viewModel.snackBarMessageState.value =
                        context.getString(R.string.no_details_available)
                }
            }
            .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(1f)) {
                    TextBold11(text = stringResource(R.string.author))
                    SpacerWidth4()
                    TextNormal11(text = article.author ?: "")
                    SpacerWidth12()
                }
                TextBold11(text = FunctionHelper.getTimeTextFromDate(article.publishedAt ?: ""))
            }
            SpacerHeight8()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = article.title ?: "",
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    SpacerHeight4()
                    Text(
                        text = article.description ?: "",
                        fontSize = 12.sp,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                    SpacerHeight8()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextBold11(text = stringResource(R.string.sources))
                        SpacerWidth4()
                        TextNormal11(text = article.sourceBean?.name ?: "")
                    }
                }
                SpacerWidth12()
                NewsItemImage(article.urlToImage)
            }
        }
    }
}


@Composable
private fun NewsItemImage(urlToImage: String?) {
    var isLoading by remember {
        mutableStateOf(false)
    }
    val modifier = Modifier
        .width(100.dp)
        .height(80.dp)
        .clip(RoundedCornerShape(12.dp))
    AsyncImage(
        model = urlToImage,
        modifier = if (isLoading) modifier.shimmer() else modifier,
        contentDescription = stringResource(R.string.article_image),
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.ic_default_image),
        onLoading = {
            isLoading = true
        },
        onSuccess = {
            isLoading = false
        },
        onError = {
            isLoading = false
        }
    )
}

