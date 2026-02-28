package buralek.assignment.ground.application.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {
    // TODO make a custom handling of custom exceptions
}
