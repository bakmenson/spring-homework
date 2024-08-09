package ru.gb.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ru.gb.exception.ResourceNotFoundException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ControllerApiResponse {

    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(
            description = "Запрашиваемый объект не найден",
            responseCode = "404",
            content = @Content(
                    schema = @Schema(implementation = ResourceNotFoundException.class)
            )
    )
    public @interface NotFoundResponse {}

    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(
            description = "Запрашиваемого объекта нет",
            responseCode = "204",
            content = @Content(
                    schema = @Schema(implementation = ResourceNotFoundException.class)
            )
    )
    public @interface NoContentResponse {}

    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(
            description = "Внутренняя ошибка",
            responseCode = "500",
            content = @Content(
                    schema = @Schema(implementation = Void.class)
            )
    )
    public @interface ServerErrorResponse {}

    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(
            description = "Успешный ответ",
            responseCode = "200"
    )
    // open api сам определяет возвращаемый тип (объявленный в сигнатуре метода)
    // schema = @Schema(implementation = "Some".class)
    public @interface OkResponse {
    }

}
