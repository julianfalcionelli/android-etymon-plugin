package model;

import java.util.List;

public class Component {
    private String marker;
    private String name;
    private String description;
    private List<String> paths;
    private List<String> sharedPaths;

    public Component(String marker, String name, String description, List<String> paths, List<String> sharedPaths) {
        this.marker = marker;
        this.name = name;
        this.description = description;
        this.paths = paths;
        this.sharedPaths = sharedPaths;
    }

    public String getMarker() {
        return marker;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPaths() {
        return paths;
    }

    public List<String> getSharedPaths() {
        return sharedPaths;
    }
}
