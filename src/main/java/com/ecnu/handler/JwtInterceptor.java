package com.ecnu.handler;

import com.ecnu.common.R;
import com.ecnu.util.MyJsonUtil;
import com.ecnu.util.MySecurityUtil;
import com.ecnu.util.ThreadContextHolder;
import com.ecnu.vo.UserInfoVo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().toUpperCase().equals("OPTIONS")) {
            return true; // 通过所有OPTION请求
        } else {
            response.setContentType("application/json;charset=UTF-8"); //返回格式设置为json
            String token = request.getHeader("Authentication"); // 获取请求头中的token
            String msg;
            try {
                UserInfoVo userInfo = MySecurityUtil.parseUser(token);
                ThreadContextHolder.setHttpRequest(request);
                ThreadContextHolder.setHttpResponse(response);
                ThreadContextHolder.setUserInfo(userInfo);
                return true; // 通过验证
            } catch (SignatureException e) {
                msg = "无效签名";
            } catch (UnsupportedJwtException e) {
                msg = "不支持的签名";
            } catch (ExpiredJwtException e) {
                msg = "token过期";
            } catch (MalformedJwtException e) { // IllegalArgumentException
                msg = "不支持的签名格式";
            } catch (Exception e) {
                msg = "token无效";
            }

            String json = MyJsonUtil.object2JsonStr(R.error(-9, msg));
            response.getWriter().write(json);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadContextHolder.remove();
    }
}
