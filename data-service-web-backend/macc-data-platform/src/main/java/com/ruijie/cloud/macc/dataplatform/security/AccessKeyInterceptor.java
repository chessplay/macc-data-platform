package com.ruijie.cloud.macc.dataplatform.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.cloud.dc.core.domain.R;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AccessKeyInterceptor implements HandlerInterceptor {

    private final PermissionControlService permissionControlService;

    public AccessKeyInterceptor(PermissionControlService permissionControlService) {
        this.permissionControlService = permissionControlService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否为HandlerMethod（即Controller方法）
        ObjectMapper objectMapper=new ObjectMapper();
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            if (method.isAnnotationPresent(RequireAccessKey.class)) {
                // 从请求中获取accessKey
                String accessKey = request.getParameter("access_key");

                // 检查accessKey是否传递
                if (accessKey == null || accessKey.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(R.fail(HttpServletResponse.SC_BAD_REQUEST,"没传access_key")));
                    return false; // 拦截请求
                }

                // 校验accessKey
                if (!permissionControlService.isAccess(accessKey)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(R.fail(HttpServletResponse.SC_UNAUTHORIZED,"access_key校验失败")));
                    return false; // 拦截请求
                }
            }
        }

        return true; // 放行请求
    }
}