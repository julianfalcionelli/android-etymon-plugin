import com.google.gson.reflect.TypeToken;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import di.Configurator;
import manager.file.FileManager;
import manager.parser.ParserManager;
import model.Architecture;
import model.Component;
import model.ProjectConfigurator;
import org.jetbrains.annotations.NotNull;
import ui.step.architecture.WizardStepArchitecture;
import ui.step.components.WizardStepComponents;
import ui.step.intial.WizardStepInitial;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AndroidEtymonModuleBuilder extends ModuleBuilder {
    private ProjectConfigurator projectConfigurator;
    private FileManager fileManager;
    private ParserManager parserManager;

    public AndroidEtymonModuleBuilder() {
        super();
        // Dependencies
        fileManager = Configurator.getFileManager();
        parserManager = Configurator.getParserManager();

        Type componentsType = new TypeToken<ArrayList<Component>>(){}.getType();

        File componentsJsonFile = fileManager.getResourceFile(AndroidEtymonConfiguration.getProjectComponentsJson());

        projectConfigurator = new ProjectConfigurator(
                Architecture.MVP,
                parserManager.fromJson(fileManager.getFileContent(componentsJsonFile), componentsType)
        );
    }

    @Override
    public void setupRootModel(ModifiableRootModel model) throws ConfigurationException {
        System.out.println("setupRootModel");
        // TODO Pass configurator to DemoModuleType ?
    }

    public ProjectConfigurator getProjectConfigurator() {
        return projectConfigurator;
    }

    @Override
    public ModuleType getModuleType() {
        return AndroidEtymonModuleType.getInstance();
    }

    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new WizardStepInitial(); // First Step
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        ModuleWizardStep[] list = new ModuleWizardStep[2];
        list[0] = new WizardStepArchitecture(projectConfigurator); // 2 Step
        list[1] = new WizardStepComponents(projectConfigurator); // 3 Step

        // TODO Step for Firebase and APIs Configuration (Base URL and More)
        // TODO Step for Flavors / Environments creation

        return list;
    }
}

