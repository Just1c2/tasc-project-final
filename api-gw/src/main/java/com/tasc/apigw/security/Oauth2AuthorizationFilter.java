package com.tasc.apigw.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tasc.apigw.model.TascUserAuthentication;
import com.tasc.apigw.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import vn.tass.microservice.model.constants.AUTHENTICATION;
import vn.tass.microservice.model.userauthen.Role;
import vn.tass.microservice.redis.dto.UserLoginDTO;
import vn.tass.microservice.redis.repository.UserLoginRepository;

public class Oauth2AuthorizationFilter extends BasicAuthenticationFilter {

    UserLoginRepository userLoginRepository;
    public Oauth2AuthorizationFilter(
            AuthenticationManager authenticationManager , UserLoginRepository userLoginRepository) {
        super(authenticationManager);

        this.userLoginRepository = userLoginRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // lay ra token


        String token = HttpUtil.getValueFromHeader(request, AUTHENTICATION.HEADER.TOKEN);

        if (StringUtils.isBlank(token)){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        Optional<UserLoginDTO> userLoginDTO = userLoginRepository.findById(token);

        if (userLoginDTO.isEmpty()){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        UserLoginDTO userLoginDTOObject = userLoginDTO.get();

        String role = userLoginDTOObject.getRole();

        String uri = request.getRequestURI();

        AntPathMatcher adt = new AntPathMatcher();

            if (adt.match("/product/**", uri)){
                if (!role.equals("ADMIN")) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    return;
                }
            }
        if (adt.match("/category/**", uri)){
            if (!role.equals("ADMIN")) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }
        }
            if (adt.match(uri, "/createOrder")) {
                if (role.isBlank() || role.isEmpty()) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    return;
                }
            }

        UserDetailExtend userDetailExtend = new UserDetailExtend(userLoginDTOObject);

        TascUserAuthentication tascUserAuthentication = new TascUserAuthentication(userDetailExtend);

        SecurityContextHolder.getContext().setAuthentication(tascUserAuthentication);
        chain.doFilter(request, response);

    }
}


