package views;

import model.Project;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.Serializable;

public class ProjectListFrame extends JInternalFrame implements Serializable {
    private JList projectList;
    private JMenuBar menuBar = new JMenuBar();
    private JButton newBtn = new JButton("New");
    private JButton delBtn = new JButton("Delete");
    private JButton openBtn = new JButton("Open");
    private JPanel panel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();
    public ProjectListFrame(){

        super("Projects",
                true,
                true,
                true,
                true);
        initComponents();
        pack();

    }
    private void initComponents(){
        setContentPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        menuBar.add(newBtn);
        menuBar.add(delBtn);
        menuBar.add(openBtn);
        panel.add(menuBar);
        scrollPane.setPreferredSize(new Dimension(100,100));
        panel.add(scrollPane);

    }
    public void addNewButtonListener(ActionListener l){
        newBtn.addActionListener(l);
    }
    public void addDeleteButtonListener(ActionListener l){
        delBtn.addActionListener(l);
    }
    public void addOpenButtonListener(ActionListener l){
        openBtn.addActionListener(l);
    }
    public void setList(List<Project> list){
        projectList = new JList();
        projectList.setListData(list.toArray());
        scrollPane.setViewportView(projectList);
    }
    public int getSelected(){
        return projectList.getSelectedIndex();
    }
    public String openDialog(){
        return JOptionPane.showInputDialog("Enter Project Name");
    }

}
