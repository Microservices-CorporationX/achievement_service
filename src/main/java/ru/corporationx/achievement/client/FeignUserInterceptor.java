package ru.corporationx.achievement.client;

import ru.corporationx.achievement.config.context.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeignUserInterceptor implements RequestInterceptor {

    private final UserContext userContext;

    @Override
    public void apply(RequestTemplate template) {
        template.header("x-user-id", String.valueOf(userContext.getUserId()));
    }
}
