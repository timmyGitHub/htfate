package com.htfate.oauth2center.controller;

import com.htfate.utilcenter.util.UtilsReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

@RestController
@SessionAttributes({"authorizationRequest"})
public class IndexController {

    @Value("${custom.isZuul:true}")
    private boolean useZuul;
    @Value("${custom.resource-url:''}")
    private String resourceUrl;
    @Value("${custom.resource-zuul-url:''}")
    private String resourceZuulUrl;
    @Value("${custom.redirect-login-url:''}")
    private String redirectLoginUrl;
    @Value("${custom.redirect-grant-url:''}")
    private String redirectGrantUrl;

    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices tokenServices;


    @GetMapping("/exit/{access_token}")
    public Object removeToken(@PathVariable String access_token) {
        return UtilsReturnMsg.success(tokenServices.revokeToken(access_token));
    }

    /*@GetMapping()
    public ModelAndView index(@RequestParam Map<String, Object> model){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        SecurityContextHolderStrategy s = SecurityContextHolder.getContextHolderStrategy();

        ModelAndView mv = new ModelAndView();
        if (useZuul) {
            mv.addObject("contextPath",resourceZuulUrl);
        } else {
            mv.addObject("contextPath",resourceUrl);
        }
        mv.setViewName("login");
        return mv;
    }


    @GetMapping("/auth/login")
    public ModelAndView login(@RequestParam Map<String, Object> model, HttpServletRequest request, String error) {
        ModelAndView mv = new ModelAndView();
        if (useZuul) {
            mv.addObject("contextPath",resourceZuulUrl);
        } else {
            mv.addObject("contextPath",resourceUrl);
        }
        mv.setViewName("login");
        return mv;
    }

    @GetMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scope", authorizationRequest.getScope());
        view.setViewName("grant");
        return view;
    }

    @GetMapping("auth/error")
    public ModelAndView error(Map<String, Object> model, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("error");
        return mv;
    }*/
}
