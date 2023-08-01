package com.example.cionicdemoapp.core_restful_services

enum class GenericErrorCode(val message: String) {
    NO_INTERNET("No Internet ..."),
    PAGE_NOT_FOUND("Page Not Found ..."),
    SOMETHING_WENT_WRONG("Some Thing Went Wrong ..."),
    LIMIT_EXCEEDED("You exceeded your current quota, please check your plan and billing details."),
}

