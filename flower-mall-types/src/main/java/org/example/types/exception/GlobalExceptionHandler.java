package org.example.types.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.types.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-13 01:00
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截我们自定义的业务异常 (AppException)
     */
    @ExceptionHandler(AppException.class)
    public Result<String> handleAppException(AppException e) {
        log.error("业务异常: code={}, info={}", e.getCode(), e.getInfo());
        // 返回我们在 AOP 里抛出的 code (4003) 和 info (无权访问...)
        return Result.error(e.getCode(), e.getInfo());
    }

    /**
     * 拦截其他未知的异常 (兜底)
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error("500", "系统繁忙，请稍后重试");
    }
}
