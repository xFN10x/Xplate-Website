package dev.xplate.api.bedrockr.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import dev.xplate.api.util.SavedFile;
import jakarta.annotation.Nullable;

public class SavedbedrockRData extends SavedFile {

    public record ElementMade(Date timeMade, @Nullable Object elementData, int bedrockRVersion,
            @Nullable String workspaceName) implements Comparable<ElementMade> {

        @Override
        public int compareTo(ElementMade o) {
            return timeMade.compareTo(o.timeMade);
        }
    }

    public record WorkspaceMade(Date timeMade, @Nullable Object workspaceData, int bedrockRVersion,
            @Nullable Object[] elementDatas) implements Comparable<WorkspaceMade> {

        @Override
        public int compareTo(WorkspaceMade o) {
            return timeMade.compareTo(o.timeMade);
        }
    }

    private ArrayList<ElementMade> ElementsMade = new ArrayList<ElementMade>();
    private ArrayList<WorkspaceMade> WorkspacesMade = new ArrayList<WorkspaceMade>();

    public ElementMade getElementMade(int index) {
        try {
            return ElementsMade.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    public WorkspaceMade getWorkspaceMade(int index) {
        try {
            return WorkspacesMade.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    public ElementMade[] getElementsMade() {
        return ElementsMade.toArray(new ElementMade[0]);
    }

    public WorkspaceMade[] getWorkspacesMade() {
        return WorkspacesMade.toArray(new WorkspaceMade[0]);
    }

    public ElementMade addElementMade(ElementMade elementMade) {
        ElementsMade.add(elementMade);
        Collections.sort(ElementsMade);
        dirty();
        return elementMade;
    }

    public WorkspaceMade addWorkspaceMade(WorkspaceMade workspaceMade) {
        WorkspacesMade.add(workspaceMade);
        Collections.sort(WorkspacesMade);
        dirty();
        return workspaceMade;
    }

    @Override
    public Path saveLocation() {
        return getDataFolder().resolve("bedrockRData.json");
    }

}
