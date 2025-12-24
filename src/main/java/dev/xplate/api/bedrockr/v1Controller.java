package dev.xplate.api.bedrockr;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonSyntaxException;

import dev.xplate.api.bedrockr.data.SavedbedrockRData;
import dev.xplate.api.bedrockr.data.SavedbedrockRData.ElementMade;
import dev.xplate.api.bedrockr.data.SavedbedrockRData.WorkspaceMade;

@RestController
@RequestMapping("/bedrockr/v1")
public class v1Controller {

    //elements made

    @PostMapping("/elementMade")
    public void newElementMade(@RequestBody ElementMade newElementMade) throws JsonSyntaxException, IOException {
        SavedbedrockRData savedData = (SavedbedrockRData) new SavedbedrockRData().load();

        savedData.addElementMade(newElementMade);
    }

    @GetMapping("/elementMade")
    public ResponseEntity<ElementMade[]> getElementsMade() throws JsonSyntaxException, IOException {
        SavedbedrockRData savedData = (SavedbedrockRData) new SavedbedrockRData().load();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(savedData.getElementsMade());
    }

    @GetMapping("/elementMade/{index}")
    public ResponseEntity<ElementMade> getElementMadeByIndex(@PathVariable("index") int index)
            throws JsonSyntaxException, IOException {
        SavedbedrockRData savedData = (SavedbedrockRData) new SavedbedrockRData().load();
        ElementMade elementMade = savedData.getElementMade(index);
        if (elementMade == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(elementMade);
    }

    //workspaces made

    @PostMapping("/workspaceMade")
    public void newWorkspaceMade(@RequestBody WorkspaceMade newWorkspaceMade) throws JsonSyntaxException, IOException {
        SavedbedrockRData savedData = (SavedbedrockRData) new SavedbedrockRData().load();

        savedData.addWorkspaceMade(newWorkspaceMade);
    }

    @GetMapping("/workspaceMade")
    public ResponseEntity<WorkspaceMade[]> getWorkspacesMade() throws JsonSyntaxException, IOException {
        SavedbedrockRData savedData = (SavedbedrockRData) new SavedbedrockRData().load();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(savedData.getWorkspacesMade());
    }

    @GetMapping("/workspaceMade/{index}")
    public ResponseEntity<WorkspaceMade> getWorkspaceMadeByIndex(@PathVariable("index") int index)
            throws JsonSyntaxException, IOException {
        SavedbedrockRData savedData = (SavedbedrockRData) new SavedbedrockRData().load();
        WorkspaceMade WorkspaceMade = savedData.getWorkspaceMade(index);
        if (WorkspaceMade == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(WorkspaceMade);
    }
}
