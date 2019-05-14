package controller;

import model.Project;
import views.ProjectListFrame;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProjectListController {
    private List<Project> projects;
    private ProjectListFrame projectListFrame;
    private Project openProject;
    private ChangeListener listener;

    public ProjectListFrame getProjectListFrame() {
        return projectListFrame;
    }

    public ProjectListController(){
        projectListFrame = new ProjectListFrame();
        initProjectList();
        setupListeners();
    }


    public Project getOpenProject() {
        return openProject;
    }
    private void initProjectList() {
        projects = Project.getProjects();
        projectListFrame.setList(projects);
    }

    private void setupListeners() {
        ActionListener addListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project project;
                String name = projectListFrame.openDialog();
                if (name != null) {
                    project = Project.create(name);
                    projects.add(project);
                    openProject = project;
                    projectListFrame.setList(projects);
                    listener.stateChanged(new ChangeEvent(this));
                }

            }
        };
        ActionListener delListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Project project = projects.remove(projectListFrame.getSelected());
                project.delete();
                openProject = null;
                listener.stateChanged(new ChangeEvent(this));
                initProjectList();

            }
        };
        ActionListener openListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProject = projects.get(projectListFrame.getSelected()).getChildren();
                listener.stateChanged(new ChangeEvent(this));

            }
        };
        projectListFrame.addNewButtonListener(addListener);
        projectListFrame.addDeleteButtonListener(delListener);
        projectListFrame.addOpenButtonListener(openListener);

    }

    public void setOpenProjectListener(ChangeListener l) {
        listener = l;
    }

}
