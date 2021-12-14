//package lib.web.bookstore.controllers;
//
//import java.util.Enumeration;
//import java.util.Map;
//import javax.servlet.http.HttpServletRequest;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//public class ErrorsController implements ErrorController {
//
//    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
//    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
//        ModelAndView errorPage = new ModelAndView("error");
//        String errorMsg = "";
//        int httpErrorCode = getErrorCode(httpRequest);
//        switch (httpErrorCode) {
//            case 400:
//                errorMsg = "The resource doesn't exists";
//                break;
//            case 403:
//                errorMsg = "You don't have permissions for access to this resource";
//                break;
//            case 401:
//                errorMsg = "Your user is not authorized";
//                break;
//            case 404:
//                errorMsg = "The resource doesn't exists";
//                break;
//            case 500:
//                errorMsg = "Internal error occurred ";
//                break;
//            default:
//                throw new AssertionError();
//        }
//        errorPage.addObject("code", httpErrorCode);
//        errorPage.addObject("message", errorMsg);
//        return errorPage;
//
//    }
//
//    private int getErrorCode(HttpServletRequest httpRequest) {
//        Map map = httpRequest.getParameterMap();
//        for (Object key : map.keySet()) {
//            String[] values = (String[]) map.get(key);
//            for (String value : values) {
//                System.out.println(key.toString() + ": " + value);
//            }
//        }
//        Enumeration<String> attributes = httpRequest.getAttributeNames();
//        while (attributes.hasMoreElements()) {
//            String key = attributes.nextElement();
//            System.out.println(key + ": " + httpRequest.getAttribute(key));
//        }
//        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
//
//    }
//
//}
