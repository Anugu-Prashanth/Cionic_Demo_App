package com.example.cionicdemoapp.repository.posts

import com.example.cionicdemoapp.BaseTest
import com.example.cionicdemoapp.core_restful_services.GenericErrorCode
import com.example.cionicdemoapp.core_restful_services.Result
import com.example.cionicdemoapp.core_restful_services.Status
import com.example.cionicdemoapp.core_restful_services.services.get_posts.GetPostsService
import com.example.cionicdemoapp.core_restful_services.services.get_posts.Post
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PostsRepositoryTest : BaseTest() {

    @InjectMockKs
    private lateinit var repositoryImpl: PostsRepository

    @MockK
    private lateinit var getPostsService: GetPostsService

    private lateinit var result: MutableList<Result<List<Post>>>


    private val title = "New feed"
    private val body = "hi hello hello"
    private val filter = ""
    private val id = 1
    private val userId = 9

    @Before
    fun setUp() {
        result = mutableListOf()
    }

    @Test
    fun test_success() {
        coEvery { getPostsService.getPosts() } returns Result(
            Status.SUCCESS, listOf(Post(id = id, title = title, body = body, userId = userId))
        )
        runBlocking { repositoryImpl.execute(filter = filter).collect{
            result.add(it)
        } }
        println(result.toString())
        assertNotNull(result.isNotEmpty())
        assertEquals(result[0].status, Status.LOADING)
        assertEquals(result[1].status, Status.SUCCESS)
        assertEquals(result[1].data!!.size, 1)
        assertEquals(result[1].data!![0].id, id)
        assertEquals(result[1].data!![0].title, title)
        assertEquals(result[1].data!![0].body, body)

    }

    @Test
    fun test_success_with_no_data() {
        coEvery { getPostsService.getPosts() } returns Result(
            Status.SUCCESS, null
        )
        runBlocking { repositoryImpl.execute(filter = filter).collect{
            result.add(it)
        } }
        assertNotNull(result.isNotEmpty())
        assertNull(result[0].data)
        assertEquals(result[1].status, Status.LOADING)
        assertEquals(result[1].status, Status.SUCCESS)
        assertEquals(result[1].data, null)
    }

    @Test
    fun test_error_when_SOMETHING_WENT_WRONG() {
        coEvery { getPostsService.getPosts() } returns Result(
            Status.ERROR, null,
            GenericErrorCode.SOMETHING_WENT_WRONG.message
        )
        runBlocking { repositoryImpl.execute(filter = filter).collect{
            result.add(it)
        } }
        println(result.toString())
        assertNull(result[0].data)
        assertEquals(result[0].status, Status.LOADING)
        assertEquals(result[1].status, Status.ERROR)
        assertEquals(result[1].data, null)
        assertEquals(result[1].message, GenericErrorCode.SOMETHING_WENT_WRONG.message)
    }

    @Test
    fun test_error_when_PAGE_NOT_FOUND() {
        coEvery { getPostsService.getPosts() } returns Result(
            Status.ERROR, null,
            GenericErrorCode.PAGE_NOT_FOUND.message
        )
        runBlocking { repositoryImpl.execute(filter = filter).collect{
            result.add(it)
        } }
        println(result.toString())
        assertNull(result[0].data)
        assertEquals(result[0].status, Status.LOADING)
        assertEquals(result[1].status, Status.ERROR)
        assertEquals(result[1].data, null)
        assertEquals(result[1].message, GenericErrorCode.PAGE_NOT_FOUND.message)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}