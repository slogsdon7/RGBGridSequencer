package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.*;
import views.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller {
    private final View view = new View();
    private ProjectListController projectListController;
    private static Project openProject;

    public Controller()  {
        projectListController = new ProjectListController();
        view.setProjectList(projectListController.getProjectListFrame());
        setupListeners();
    }

    private void setupListeners() {
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                    case "SHOW_PROJECTS":
                        view.setProjectList(projectListController.getProjectListFrame());
                        break;
                    case "ADD_SEQUENCER":
                        addSequencer(openProject.addSequence());
                        break;
                    case "SAVE":
                        openProject.save();
                        break;
                }
            }
        };
        view.addToolbarButtonListener(menuListener);
        ChangeListener listControlListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setProject(projectListController.getOpenProject());
            }
        };
        projectListController.setOpenProjectListener(listControlListener);

    }

    public void setProject(Project project) {
        openProject = project;
        view.removeSequencers();
        if (openProject!=null) {
            project.getSequences().forEach(sequence -> addSequencer(sequence));
        }
    }

    private void addSequencer(Sequence sequence) {
        SequencerController sc = new SequencerController(sequence);
        view.addSequencerView(sc.getSequencerFrame());
    }




}

