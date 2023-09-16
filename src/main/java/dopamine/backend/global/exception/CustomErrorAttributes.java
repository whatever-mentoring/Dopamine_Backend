package dopamine.backend.global.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private static final String ERROR_INTERNAL_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        storeErrorAttributes(request, ex);
        return null;
    }

    private void storeErrorAttributes(HttpServletRequest request, Exception ex) {
        request.setAttribute(ERROR_INTERNAL_ATTRIBUTE, ex);
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> result = super.getErrorAttributes(webRequest, options);
        Exception ex = (Exception) webRequest.getAttribute(ERROR_INTERNAL_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        result.remove("timestamp");
        if(ex != null && ex.toString().contains("default message")) {
            String[] strings = ex.toString().split("default message ");
            String message = "";
            if(strings.length > 2) message = strings[strings.length-1].replace("[", "").replace("]", "");
            result.put("message", message);
        }
        return result;
    }
}