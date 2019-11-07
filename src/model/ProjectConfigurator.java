package model;

import java.util.List;

public class ProjectConfigurator {
    private Architecture architecture;
    private List<Component> components;
    private List<Component> selectedComponents;

    public ProjectConfigurator(Architecture architecture, List<Component> components) {
        this.architecture = architecture;
        this.components = components;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

    public List<Component> getComponents() {
        return components;
    }

    public List<Component> getSelectedComponents() {
        return selectedComponents;
    }

    public void setSelectedComponents(List<Component> selectedComponents) {
        this.selectedComponents = selectedComponents;
    }
}
