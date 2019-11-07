package ui.step.intial;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.wizard.CommitStepException;

import javax.swing.*;
import java.awt.*;

public class WizardStepInitial extends ModuleWizardStep {
    @Override
    public JComponent getComponent() {
        // Parent View
        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        // UI Creation
        JLabel title = new JLabel("Android Etymon");
        title.setFont(title.getFont().deriveFont(40f));

        JLabel subtitle = new JLabel("by BARDO (www.bybardo.co) - with love J.F.");
        JLabel description = new JLabel("Create a New Android Project with a complete base code according your project requirements.");

        // Add child views
        contentPane.add(title);
        contentPane.add(subtitle);
        contentPane.add(description);

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
                2,
                SpringLayout.SOUTH, title);

        // Description
        layout.putConstraint(SpringLayout.WEST, description,
                5,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, description,
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
    }

    @Override
    public void onWizardFinished() throws CommitStepException {
        System.out.println("onWizardFinished");
        super.onWizardFinished();
    }
}