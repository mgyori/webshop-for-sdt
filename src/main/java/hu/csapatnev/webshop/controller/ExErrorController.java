package hu.csapatnev.webshop.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExErrorController implements ErrorController {
	@RequestMapping("/error")
    public String handleError(HttpServletRequest request, Exception ex, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		model.addAttribute("url", request.getRequestURL());
		model.addAttribute("msg", ex.getMessage());
		model.addAttribute("stack", ExceptionUtils.getStackTrace(ex));
		model.addAttribute("current", "error");
		
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value())
	            return "404";
	    }
	    
	    return "error";
    }
 
    @Override
    public String getErrorPath() {
        return null;
    }
}
