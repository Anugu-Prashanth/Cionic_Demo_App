package com.example.cionicdemoapp.core_restful_services.services.get_posts

import com.example.cionicdemoapp.BaseTest
import com.example.cionicdemoapp.core_restful_services.GenericErrorCode
import com.example.cionicdemoapp.core_restful_services.RestfulApiService
import com.example.cionicdemoapp.core_restful_services.Result
import com.example.cionicdemoapp.core_restful_services.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.internal.http.RealResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GetPostsServiceImplTest : BaseTest() {

    @OverrideMockKs
    private lateinit var getPostsServiceImpl: GetPostsServiceImpl

    @MockK
    private lateinit var restfulApiService: RestfulApiService

    @MockK
    private lateinit var responseBody: RealResponseBody

    @MockK
    private lateinit var response: Response<List<Post>>

    @MockK
    private lateinit var mediaType: MediaType

    private lateinit var result: Result<List<Post>>
    private val title = "New feed"
    private val body = "Abc abc abc abc"
    private val id = 100
    private val userId = 10

    @Before
    fun setUp() {
    }

    @Test
    fun test_success() {
        coEvery { restfulApiService.getPosts() } returns Response.success(
            listOf(Post(id = id, title = title, body = body, userId = 10)),
        )

        runBlocking {
            result = getPostsServiceImpl.getPosts()
        }
        println(result.toString())
        assertNotNull(result.data)
        assertEquals(result.status, Status.SUCCESS)
        assertEquals(result.data!![0].title, title)
        assertEquals(result.data!![0].body, body)
        assertEquals(result.data!![0].id, id)
        assertEquals(result.data!![0].userId, userId)

    }

    @Test
    fun test_failure_with_400() {
        mockErrorResponse()
        runBlocking {
            result = getPostsServiceImpl.getPosts()
        }
        println(result.toString())
        assertNull(result.data)
        assertEquals(result.status, Status.ERROR)
        assertEquals(result.message, GenericErrorCode.SOMETHING_WENT_WRONG.message)
    }

    private fun mockErrorResponse(errorCode: Int = 400) {
        every { responseBody.contentType() } returns mediaType
        every { responseBody.contentLength() } returns 100
        coEvery { restfulApiService.getPosts() } returns Response.error(
            errorCode, responseBody
        )
    }

    @Test
    fun test_failure_with_404() {
        mockErrorResponse(404)
        runBlocking {
            result = getPostsServiceImpl.getPosts()
        }
        println(result.toString())
        assertNull(result.data)
        assertEquals(result.status, Status.ERROR)
        assertEquals(result.message, GenericErrorCode.PAGE_NOT_FOUND.message)
    }

    @Test
    fun test_failure_with_409() {
        mockErrorResponse(409)
        runBlocking {
            result = getPostsServiceImpl.getPosts()
        }
        println(result.toString())
        assertNull(result.data)
        assertEquals(result.status, Status.ERROR)
        assertEquals(result.message, GenericErrorCode.SOMETHING_WENT_WRONG.message)
    }

    @Test
    fun test_failure_with_IOException() {
        coEvery { restfulApiService.getPosts() } throws IOException("No Network")

        runBlocking {
            result = getPostsServiceImpl.getPosts()
        }
        println(result.toString())
        assertNull(result.data)
        assertEquals(result.status, Status.ERROR)
        assertEquals(result.message, GenericErrorCode.NO_INTERNET.message)
    }

    @Test
    fun test_failure_with_Exception() {
        every { response.code() } returns 402
        every { response.message() } returns "Some thing wrong"
        coEvery { restfulApiService.getPosts() } throws HttpException(response)

        runBlocking {
            result = getPostsServiceImpl.getPosts()
        }
        println(result.toString())
        assertNull(result.data)
        assertEquals(result.status, Status.ERROR)
        assertEquals(result.message, GenericErrorCode.SOMETHING_WENT_WRONG.message)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

}