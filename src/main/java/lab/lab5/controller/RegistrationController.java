package lab.lab5.controller;

import lab.lab5.dto.RegistrationRequest;
import lab.lab5.service.RegistrationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping(
            value = "/registration",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Map<String, String>> register(
            @RequestBody RegistrationRequest request) {

        return registrationService.register(request)
                .map(user -> Map.of(
                        "message", "Registration successful",
                        "username", user.getUsername()
                ));
    }
}
