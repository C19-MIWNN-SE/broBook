package nl.miwnn.ch19.binarybros.brobook.handler;

/*
 * @author Mart Stukje
 * Handles all exceptions thrown in the controller classes of broBook
 * */

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BroBookExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException exception, Model model) {
        model.addAttribute("bericht", exception.getLocalizedMessage());
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception exception, Model model) {
        model.addAttribute("bericht", exception.getLocalizedMessage());
        return "error/500";
    }
}
