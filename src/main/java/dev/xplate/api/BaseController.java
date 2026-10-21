package dev.xplate.api;

import com.google.gson.JsonSyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BaseController {
    @GetMapping()
    public ResponseEntity<?> getElementsMade() throws JsonSyntaxException {
        return ResponseEntity.status(301).header("Location", "https://xplate.dev").build();
    }
}
