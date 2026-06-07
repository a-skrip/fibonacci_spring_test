package com.skillbox.fibonacci.adapter.web;

public record ErrorMessageResponse(
        String status,
        String message
) {

    public static final ErrorMessageResponse INTERNAL_ERROR_MESSAGE_RESPONSE =
            new ErrorMessageResponse("critical", "Произошла неожиданная ошибка!");

    public static ErrorMessageResponse warn(String message) {
        return new ErrorMessageResponse("warning", message);
    }

    public static ErrorMessageResponse internalError() {
        return INTERNAL_ERROR_MESSAGE_RESPONSE;
    }
}
