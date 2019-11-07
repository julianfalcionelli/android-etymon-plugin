package ui.step.architecture;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.wizard.CommitStepException;
import model.ProjectConfigurator;

import javax.swing.*;
import java.awt.*;

public class WizardStepArchitecture extends ModuleWizardStep {
    private ProjectConfigurator projectConfigurator;

    public WizardStepArchitecture(ProjectConfigurator projectConfigurator) {
        this.projectConfigurator = projectConfigurator;
    }

    @Override
    public JComponent getComponent() {
        // Parent View
        JFrame frame = new JFrame("");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        // UI Creation
        JLabel title = new JLabel("Project Architecture");
        title.setFont(title.getFont().deriveFont(25f));

        JLabel subtitle = new JLabel("Select your desired project architecture.");

        JRadioButton mvpRadioButton = new JRadioButton("MVP: Model View Presenter ", true);
        JRadioButton mvvmRadioButton = new JRadioButton("MVVM: Model View - View Model");
        mvvmRadioButton.setEnabled(false);
        JRadioButton mviRadioButton = new JRadioButton("MVI: Model View Intent");
        mviRadioButton.setEnabled(false);

        ButtonGroup architecturesButtonGroup = new ButtonGroup();
        architecturesButtonGroup.add(mvpRadioButton);
        architecturesButtonGroup.add(mvvmRadioButton);
        architecturesButtonGroup.add(mviRadioButton);

        JLabel wipLabel = new JLabel("We still working on adding support for multiple cool architectures.");

        // Arrange buttons vertically in a panel
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(3, 1));
        radioPanel.add(mvpRadioButton);
        radioPanel.add(mvvmRadioButton);
        radioPanel.add(mviRadioButton);

        // Add child views
        contentPane.add(title);
        contentPane.add(subtitle);
        contentPane.add(radioPanel);
        contentPane.add(wipLabel);
        
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

        // Radio Panel
        layout.putConstraint(SpringLayout.WEST, radioPanel,
                5,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, radioPanel,
                15,
                SpringLayout.SOUTH, subtitle);

        // WIP
        layout.putConstraint(SpringLayout.WEST, wipLabel,
                5,
                SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, wipLabel,
                15,
                SpringLayout.SOUTH, radioPanel);

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
        // configurator.setPepe(configurator.getPepe() + 1);
        super.onWizardFinished();
    }
}