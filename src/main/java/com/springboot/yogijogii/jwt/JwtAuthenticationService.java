package com.springboot.yogijogii.jwt;


import com.springboot.yogijogii.data.entity.Member;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface JwtAuthenticationService {

    Member authenticationToken(HttpServletRequest request);
}
