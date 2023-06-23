package org.server.config.security;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.server.entity.CustomUserDetails;
import org.server.service.JwtCacheService;
import org.server.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 在用户名和密码校验前添加的过滤器，如果请求中有jwt的token且有效，会取出token中的用户名，然后调用SpringSecurity的API进行登录操作。
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Resource
    private JwtService jwtService;

    @Resource
    private JwtCacheService jwtCacheService;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, FilterChain filterChain)
        throws ServletException, IOException {
        String jwtToken = httpServletRequest.getHeader(this.tokenHeader);

        if (jwtToken != null) {
            String username = jwtService.getUsernameFromToken(jwtToken);

            logger.info("checking authentication for user " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // It is not compelling necessary to load the use details from the database. You could also store the information
                // in the token and read it from it. It's up to you ;)
                CustomUserDetails customUserDetails = jwtCacheService.getUserByJwtToken(jwtToken);

                // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                // the database compellingly. Again it's up to you ;)
                if (customUserDetails != null && jwtService.validateToken(jwtToken, customUserDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    logger.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
