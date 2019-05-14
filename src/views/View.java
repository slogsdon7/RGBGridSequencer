package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

public class View extends JFrame {
    private JDesktopPane desktop;
    private ProjectListFrame projectsFrame;
    private JToolBar toolBar;
    private JButton show;
    private JButton save;
    private JButton add;
    private JPanel mainPanel = new JPanel();

    public View(){
        super("Launchpad Light Sequencer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        desktop = new JDesktopPane();
        setContentPane(mainPanel);
        toolBar = new JToolBar();
        addButtons(toolBar);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(toolBar);
        mainPanel.add(desktop);
        pack();
        setSize(1000,800);
        setLocationRelativeTo(null);
        setVisible(true);

    }
    public void addToolbarButtonListener(ActionListener actionListener){
        show.addActionListener(actionListener);
        save.addActionListener(actionListener);
        add.addActionListener(actionListener);
    }


    private void addButtons(JToolBar toolBar){
        show = makeButton("Show Projects", "SHOW_PROJECTS");
        add = makeButton("Add Sequencer", "ADD_SEQUENCER");
        save = makeButton("Save","SAVE");
        toolBar.add(save);
        toolBar.add(show);
        toolBar.add(add);
    }


    public void setProjectsFrameVisible(boolean b){
        projectsFrame.setVisible(b);
    }
    public void addSequencerView(SequencerFrame sequencer){
        desktop.add(sequencer);
        }

    public void removeSequencers(){
        for (Component component : desktop.getComponents()) {
            if (!component.equals(projectsFrame)){
                var frame = (JInternalFrame) component;
                frame.setClosable(true);
                try {
                    frame.setClosed(true);
                }
                catch (PropertyVetoException e){
                    System.err.println(e.getMessage());
                }
            }
        }
    }
    public void setProjectList(ProjectListFrame projectList){
        this.projectsFrame = projectList;
        projectList.setLocation(getWidth()/2, getHeight()/2);
        projectList.setVisible(true);
        desktop.add(projectList);
    }


    private JButton makeButton(String name, String actionCommand){
        JButton button = new JButton(name);
        button.setActionCommand(actionCommand);
        return button;
    }

}

