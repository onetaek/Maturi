package com.maturi.util.argumentresolver;

import com.maturi.util.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * ArgumentResolver를 구현하기 하기 위해
 * HandlerMethodArgumentResolver인터페이스를 상속 받으면 2개의 메소드를 구현해야한다.
 * 1. supportsParameter : 이 메소드의  return 값이 true여야 다음 단계(resolveArgument메소드 수행)로 넘어갈 수 있다.
 * 2. resolveArgument : 원하는 로직을 실행 후 리턴 값으로 전달한다.
 */
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //Login어노테이션이 있는지를 확인 있으면 true 없으면 false
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        //파라미터의 타입이 Long인지 확인 Long이면 true 아니면 false
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        //위의 두 값들이 모두 true이면 통과!
        return hasLoginAnnotation && hasLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //webRequest가 뭔지는 정확하게 모르겠다. 인강에서도 그냥 침 어쨌든 request값을 사용할 수 있는듯
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if(session == null){
            return null;
        }
        return session.getAttribute(SessionConst.MEMBER_ID);
    }
}
