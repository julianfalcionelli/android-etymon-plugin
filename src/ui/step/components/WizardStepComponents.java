package ui.step.components;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.wizard.CommitStepException;
import model.Component;
import model.ProjectConfigurator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

public class WizardStepComponents extends ModuleWizardStep {
    private ProjectConfigurator projectConfigurator;
    private List<Component> selectedComponents;

    public WizardStepComponents(ProjectConfigurator projectConfigurator) {
        this.projectConfigurator = projectConfigurator;
    }

    @Override
    public JComponent getComponent() {
        selectedComponents = new ArrayList();

        // Parent View
        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        // UI Creation
        JLabel title = new JLabel("Project Components");
        title.setFont(title.getFont().deriveFont(25f));

        JLabel subtitle = new JLabel("Select the components that you want to add to your project.");

        // Arrange buttons vertically in a panel
        JPanel checkBoxes = new JPanel();
        checkBoxes.setLayout(new GridLayout(projectConfigurator.getComponents().size(), 1));

        for (Component component: projectConfigurator.getComponents()) {
            JCheckBox myCheckBox = new JCheckBox(component.getName() + ": " + component.getDescription());
            myCheckBox.addItemListener(e ->
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedComponents.add(component);
                } else {
                    selectedComponents.remove(component);
                }
            });
            checkBoxes.add(myCheckBox);
        }

        // Add child views
        contentPane.add(title);
        contentPane.add(subtitle);
        contentPane.add(checkBoxes);

        // Constrains
        // Title
        layout.putConstraint(SpringLayout.WEST, title,
                5,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.EAST, title,
                5,
                SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, title,
                5,
                SpringLayout.NORTH, contentPane);

        // Subtitle
        layout.putConstraint(SpringLayout.WEST, subtitle,
                5,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, subtitle,
                5,
                SpringLayout.SOUTH, title);

        // Description
        layout.putConstraint(SpringLayout.WEST, checkBoxes,
                5,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, checkBoxes,
                15,
                SpringLayout.SOUTH, subtitle);

        // Create View
        frame.pack();
        frame.setVisible(true);

        return frame.getRootPane();
    }

    @Override
    public void updateDataModel() {
        System.out.println("updateDataModel");
        projectConfigurator.setSelectedComponents(selectedComponents);
    }

    @Override
    public void onWizardFinished() throws CommitStepException {
        System.out.println("onWizardFinished");
        // configurator.setPepe(configurator.getPepe() + 1);
        super.onWizardFinished();
    }
}