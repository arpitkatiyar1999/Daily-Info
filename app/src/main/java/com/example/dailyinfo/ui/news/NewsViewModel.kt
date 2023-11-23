package com.example.dailyinfo.ui.news

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.dailyinfo.beans.ArticleSortDataBean
import com.example.dailyinfo.beans.ArticlesBean
import com.example.dailyinfo.mappers.NewsFeedContentBeanMapper
import com.example.dailyinfo.request_response_state.ResponseState
import com.example.dailyinfo.ui.base.BaseViewModel
import com.example.dailyinfo.utils.SharedPreferenceHelper
import com.example.domain.request_response_state.NetworkResponseStatusEnum
import com.example.domain.use_case.ClearAllArticlesUseCase
import com.example.domain.use_case.GetAllArticlesUseCase
import com.example.domain.use_case.InsertArticlesListUseCase
import com.example.domain.use_case.NewsFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsFeedUseCase: NewsFeedUseCase,
    private val clearAllArticlesUseCase: ClearAllArticlesUseCase,
    private val getAllArticlesUseCase: GetAllArticlesUseCase,
    private val insertArticlesListUseCase: InsertArticlesListUseCase,
    private val mapper: NewsFeedContentBeanMapper,
    private val sharedPreference: SharedPreferenceHelper
) : BaseViewModel() {
    private val _newsFeedResponseStateFow: MutableStateFlow<ResponseState<Nothing>> =
        MutableStateFlow(ResponseState.none())
    val newsFeedResponseStateFow: StateFlow<ResponseState<Nothing>> get() = _newsFeedResponseStateFow

    val snackBarMessageState = mutableStateOf("")
    lateinit var articleSortList: ArrayList<ArticleSortDataBean>
    var isArticleSortListInitialized = false

    val articleList = mutableStateOf(listOf<ArticlesBean>())

    lateinit var currentSelectedArticleFilter: MutableState<Int>


    fun setUpValues(articleSortList: ArrayList<ArticleSortDataBean>) {
        this.articleSortList = articleSortList
        isArticleSortListInitialized = true
        currentSelectedArticleFilter =
            mutableIntStateOf(sharedPreference.currentSelectedArticleSortFilter)
        getNewsFeedList(sharedPreference.isNoRoomDataPresent)
    }


    //sorts the list based on selection
    fun updateList(index: Int) {
        val sortedList: List<ArticlesBean>
        if (index !in 0..1) {
            return
        }
        if (index == 0) {
            sortedList = articleList.value.sortedByDescending {
                it.publishedAt?.let { date ->
                    SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                        Locale.getDefault()
                    ).parse(date)
                }
            }
        } else {
            sortedList = articleList.value.sortedBy {
                it.publishedAt?.let { date ->
                    SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                        Locale.getDefault()
                    ).parse(date)
                }
            }
        }
        articleList.value = sortedList
        sharedPreference.currentSelectedArticleSortFilter = index
        currentSelectedArticleFilter.value = index
    }


    /**
     * tries to get data from db then if list is empty tries to get it from remote
     * but if isForceRefresh is true it will only tries to get data from remote
     * isForce Refresh is only true only if user press refresh icon on home screen or app is first time opened
     */
    fun getNewsFeedList(isForceRefresh: Boolean = false) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _newsFeedResponseStateFow.value = ResponseState.loading()
                if (isForceRefresh) {
                    getNewsFeedList()
                } else {
                    val articlesContent = getAllArticlesUseCase.invoke()
                    if (articlesContent.isEmpty()) {
                        getNewsFeedList()
                    } else {
                        val articlesBeanList = arrayListOf<ArticlesBean>()
                        articlesContent.forEach {
                            mapper.transform(it)?.let { it1 -> articlesBeanList.add(it1) }
                        }
                        articleList.value = articlesBeanList
                        updateList(sharedPreference.currentSelectedArticleSortFilter)
                        _newsFeedResponseStateFow.value =
                            ResponseState.success(null)
                    }
                }
            }
        }

    }

    private fun getNewsFeedList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = newsFeedUseCase.invoke()
                when (response.status) {
                    NetworkResponseStatusEnum.EXCEPTION -> {
                        _newsFeedResponseStateFow.value =
                            ResponseState.error(response.message ?: "")
                    }

                    NetworkResponseStatusEnum.SUCCESS -> {
                        if (response.data?.status == "ok") {
                            val articlesContent = response.data?.articles
                            if (articlesContent != null) {
                                //clear db before inserting new list
                                clearAllArticlesUseCase.invoke()
                                    // insert list to db
                                val insertedList =
                                    insertArticlesListUseCase.invoke(articlesContent)
                                sharedPreference.isNoRoomDataPresent =
                                    insertedList.size != articlesContent.size
                            }
                            val articlesBeanList = arrayListOf<ArticlesBean>()
                            articlesContent?.forEach {
                                mapper.transform(it)?.let { it1 -> articlesBeanList.add(it1) }
                            }
                            articleList.value = articlesBeanList
                            updateList(sharedPreference.currentSelectedArticleSortFilter)
                            _newsFeedResponseStateFow.value =
                                ResponseState.success(null)
                        } else {
                            _newsFeedResponseStateFow.value =
                                ResponseState.error("")
                        }
                    }
                }
            }
        }
    }
}