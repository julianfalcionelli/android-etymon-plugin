import com.intellij.icons.AllIcons;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import di.Configurator;
import manager.file.FileManager;
import model.Component;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AndroidEtymonModuleType extends ModuleType<AndroidEtymonModuleBuilder> {
    private static final String ID = "AndroidEtymonModuleType";

    private FileManager fileManager;

    public AndroidEtymonModuleType() {
        super(ID);

        // Dependencies
        fileManager = Configurator.getFileManager();
    }

    public static AndroidEtymonModuleType getInstance() {
        return (AndroidEtymonModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    @NotNull
    @Override
    public AndroidEtymonModuleBuilder createModuleBuilder() {
        AndroidEtymonModuleBuilder moduleBuilder = new AndroidEtymonModuleBuilder();

        moduleBuilder.addListener(module -> {
            System.out.println("moduleCreated");
            System.out.println("Project Name: " + module.getProject().getName());

            // 1 - Copy All Base Project Files to the Module
            // TODO: Download Base Project from Github
            File baseProjectResFile = fileManager.getResourceFile(AndroidEtymonConfiguration.getProjectResourceRoot());

            for (File baseProjectResFileChild : baseProjectResFile.listFiles()) {
                fileManager.copyFilesToDir(baseProjectResFileChild, module.getModuleFile().getParent().getPath());
            }

            // 2 - Add copied Base Project Files to the source of the Module
            fileManager.addFilesToSource("./", module);

            // 3 - Remove Files of Non-Selected Components
            String baseProjectRootPath = module.getModuleFile().getParent().getPath() + "/";
            List<Component> selectedComponents = moduleBuilder.getProjectConfigurator().getSelectedComponents();
            for (Component component: moduleBuilder.getProjectConfigurator().getComponents()) {
                if (!selectedComponents.contains(component)) {
                    // Remove Non-Shared Files
                    for (String componentNonSharedFile: component.getPaths()) {
                        File fileToRemove = new File(baseProjectRootPath + componentNonSharedFile);

                        if (fileToRemove.exists()) {
                            fileToRemove.delete();
                        }

                        // Check if Parent Dir is empty
                        File parentFile = fileToRemove.getParentFile();
                        if(parentFile.isDirectory() &&
                                parentFile.list().length == 0){
                            parentFile.delete();
                        }
                    }

                    String startMark = AndroidEtymonConfiguration.COMPONENT_START_MARK_PREFIX + component.getMarker();
                    String endMark = AndroidEtymonConfiguration.COMPONENT_END_MARK_PREFIX + component.getMarker();

                    // Remove Content on Shared Files
                    for (String componentSharedFile: component.getSharedPaths()) {
                        File fileToEdit = new File(baseProjectRootPath + componentSharedFile);
                        if (fileToEdit.exists()) {
                            try {
                                List<String> lines = new ArrayList<>();
                                String line;
                                String previousLine = "";

                                FileReader fr = new FileReader(fileToEdit);
                                BufferedReader br = new BufferedReader(fr);
                                while ((line = br.readLine()) != null) {

                                    // Component Block Detected
                                    if (line.contains(startMark)) {
                                        boolean isASharedBlock = previousLine.contains(AndroidEtymonConfiguration.COMPONENT_START_MARK_PREFIX);
                                        while ((line = br.readLine()) != null) {
                                            // All lines processed
                                            if (line.contains(endMark)) {
                                                break; // Continue to next block
                                            }

                                            if (line.contains(AndroidEtymonConfiguration.COMPONENT_START_MARK_PREFIX)) {
                                                isASharedBlock = true;
                                            }

                                            if (isASharedBlock) {
                                                addLine(previousLine, line, lines);
                                            }

                                            previousLine = line;
                                        }
                                    } else {
                                        addLine(previousLine, line, lines);
                                    }

                                    previousLine = line;
                                }

                                fr.close();
                                br.close();

                                // Write new Lines
                                FileWriter fw = new FileWriter(fileToEdit);
                                BufferedWriter out = new BufferedWriter(fw);
                                int count = 0;
                                for (String s : lines) {
                                    if (count > 0) {
                                        // Add new line since where removed in the previous step
                                        // out.write("\n");
                                    }
                                    out.write(s);
                                    count++;
                                }

                                out.flush();
                                out.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    // Refresh Project
                    module.getModuleFile().refresh(false, true);
                }
            }
        });

        return moduleBuilder;
    }

    private void addLine(String previousLine, String newLine, List<String> lines) {
        if ((newLine.equals("") && previousLine.equals("")) ||
                (newLine.equals("\n") && previousLine.equals("\n"))) {
            return; // Ignore line (To prevent multiple spaces)
        }

        lines.add(newLine + "\n");
    }

    @NotNull
    @Override
    public String getName() {
        return "Android Etymon";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Create a base Android project according to the requirements of your project and the structure that best suits your way of working.";
    }

    @Override
    public Icon getNodeIcon(@Deprecated boolean b) {
        return AllIcons.General.Information;
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(
            @NotNull WizardContext wizardContext,
            @NotNull AndroidEtymonModuleBuilder moduleBuilder,
            @NotNull ModulesProvider modulesProvider
    ) {
        System.out.println("createWizardSteps");

        wizardContext.addContextListener(new WizardContext.Listener() {
            @Override
            public void buttonsUpdateRequested() {
                System.out.println("buttonsUpdateRequested");
            }

            @Override
            public void nextStepRequested() {
                System.out.println("nextStepRequested");
            }
        });

        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider);
    }
}