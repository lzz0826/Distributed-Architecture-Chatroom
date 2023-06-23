package org.server.config.security;

import com.alibaba.fastjson.JSON;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 当用户未登录或者token失效访问接口时，自定义的返回结果。
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, AuthenticationException e)
        throws IOException, ServletException {

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().println(JSON.toJSONString(BaseResp.fail(StatusCode.NeedLogin)));
        httpServletResponse.getWriter().flush();
    }
}
