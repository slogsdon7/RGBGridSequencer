package model;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable {
    private String name;
    private List<Sequence> sequences = new ArrayList<>();
    private int id;

     public String getName() {
         return name;
     }

     public Sequence addSequence(){
         return addSequence(new Sequence());

     }
     public Sequence addSequence(Sequence sequence){
         sequence.setParent(this);
         sequences.add(sequence);
         return sequence;
     }
     public Project getChildren(){
         try {
             DB.setProjectSequences(this);
         }
         catch (SQLException e){
             System.err.println(e.getMessage());
         }
         return this;
     }


     public static List<Project> getProjects()  {
         return DB.getProjectList();
     }

    public static Project create(String name){
         Project project = new Project(name);
         project.addSequence();
         project.save();
         return project;
    }

    public void save(){
         DB.save(this);
    }
    public void delete(){
         DB.delete(this);
    }

     public void setName(String name) {
         this.name = name;
     }

     int getId() {
         return id;
     }

     void setId(int id) {
         this.id = id;
     }

    public List<Sequence> getSequences() {
        return sequences;
    }



    public Project() {

    }

     public Project(String name) {
         this.name = name;

     }



    @Override
    public String toString() {
        return name;
    }
}
