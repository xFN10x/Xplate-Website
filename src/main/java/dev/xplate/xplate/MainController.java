package dev.xplate.xplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @GetMapping("/**")
    public ResponseEntity<?> getNormal(HttpServletRequest req) {
        Path pathDirect = Path.of(XplateApp.WEBSITE_PATH, req.getRequestURI());
        Path pathWithoutHTMLExtension = Path.of(XplateApp.WEBSITE_PATH, req.getRequestURI() + ".html");
        if (req.getRequestURI().equals("/"))
            pathDirect = Path.of(XplateApp.WEBSITE_PATH, "index.html");
        if (pathDirect.toFile().exists()) {
            try {
                return new ResponseEntity<>(Files.readAllBytes(pathDirect),
                        HttpStatus.OK);
            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE,
                        "500 on resource: " + req.getRequestURI() + " (" + pathDirect + ")", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (pathWithoutHTMLExtension.toFile().exists()) {
            try {
                return new ResponseEntity<>(Files.readAllBytes(pathWithoutHTMLExtension),
                        HttpStatus.OK);
            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE,
                        "500 on resource: " + req.getRequestURI() + " (" + pathDirect + ")", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            Logger.getGlobal().warning("404 on resource: " + req.getRequestURI() + " (" + pathDirect + ")");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
