package cn.gdeiassistant.Aspect;

import cn.gdeiassistant.Annotation.CheckAuthentication;
import cn.gdeiassistant.Annotation.RestCheckAuthentication;
import cn.gdeiassistant.Pojo.Entity.Authentication;
import cn.gdeiassistant.Pojo.Result.DataJsonResult;
import cn.gdeiassistant.Pojo.Result.JsonResult;
import cn.gdeiassistant.Service.Authenticate.AuthenticateDataService;
import cn.gdeiassistant.Service.Token.LoginTokenService;
import cn.gdeiassistant.Constant.ErrorConstantUtils;
import cn.gdeiassistant.Tools.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Order(1)
public class AuthenticationAspect {

    @Autowired
    private LoginTokenService loginTokenService;

    @Autowired
    private AuthenticateDataService authenticateDataService;

    @Pointcut("@annotation(cn.gdeiassistant.Annotation.CheckAuthentication)")
    public void LogicAction() {

    }

    @Pointcut("@annotation(cn.gdeiassistant.Annotation.RestCheckAuthentication)")
    public void RestLogicAction() {

    }

    @Around("LogicAction() && @annotation(checkAuthentication)")
    public ModelAndView CheckUserAuthentication(ProceedingJoinPoint proceedingJoinPoint
            , CheckAuthentication checkAuthentication) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];
        String username = (String) request.getSession().getAttribute("username");
        Authentication authentication = authenticateDataService.QueryAuthenticationData(username);
        if (authentication != null) {
            //已经通过实名认证
            return (ModelAndView) proceedingJoinPoint.proceed(args);
        }
        //未完成实名认证，检查当前访问的单元模块是否要求实名认证
        if (Boolean.TRUE.equals(request.getServletContext().getAttribute("authentication."
                + checkAuthentication.name()))) {
            return new ModelAndView("Authenticate/tip");
        }
        return (ModelAndView) proceedingJoinPoint.proceed(args);
    }

    @Around("RestLogicAction() && @annotation(restCheckAuthentication)")
    public DataJsonResult RestCheckUserAuthentication(ProceedingJoinPoint proceedingJoinPoint
            , RestCheckAuthentication restCheckAuthentication) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];
        String username = null;
        if (request.getRequestURI().startsWith("/rest")) {
            String token = request.getParameter("token");
            if (StringUtils.isNotBlank(token)) {
                username = loginTokenService.ParseToken(token).get("username").asString();
            }
        } else {
            username = (String) request.getSession().getAttribute("username");
        }
        Authentication authentication = authenticateDataService.QueryAuthenticationData(username);
        if (authentication != null) {
            //已经通过实名认证
            Object result = proceedingJoinPoint.proceed(args);
            if (result instanceof DataJsonResult) {
                return (DataJsonResult) result;
            }
            return new DataJsonResult((JsonResult) result);
        }
        //未完成实名认证，检查当前访问的单元模块是否要求实名认证
        if (Boolean.TRUE.equals(request.getServletContext().getAttribute("authentication."
                + restCheckAuthentication.name()))) {
            return new DataJsonResult(new JsonResult(ErrorConstantUtils.NOT_AUTHENTICATION, false
                    , "请前往个人中心完成实名认证后再使用此功能"));
        }
        Object result = proceedingJoinPoint.proceed(args);
        if (result instanceof DataJsonResult) {
            return (DataJsonResult) result;
        }
        return new DataJsonResult((JsonResult) result);
    }

}
