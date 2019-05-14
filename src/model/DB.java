package model;


import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    static String db = "jdbc:sqlite:db.sqlite";
    static Connection conn;
    public static void init(){
        try {
            conn = DriverManager.getConnection(db);
            createDB();
            Statement s = conn.createStatement();
            s.executeUpdate("PRAGMA foreign_keys = ON; ");
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
    private static void createDB(){
        try {
            Statement s = conn.createStatement();
            s.executeUpdate(Constants.create_schema);
        }
        catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    static private int handleInsert(String SQL){
        try {
            System.out.println(SQL);
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.execute();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
            return 0;
    }
    private static ResultSet handleSelect(String SQL){
        try{
            PreparedStatement stmt = conn.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            return rs;
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    static List<Project> getProjectList() {
        String SQL = "SELECT * from project";
        List<Project> projects = new ArrayList();
        ResultSet rs = handleSelect(SQL);
        try {
            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setName(rs.getString("name"));
                projects.add(project);
            }
        }
        catch (SQLException e){

        }

        return projects;
    }

    static Project setProjectSequences(Project project) throws SQLException{
        String SQL = "SELECT * from sequence WHERE project_id = " + project.getId();
        ResultSet rs = handleSelect(SQL);
        if (!project.getSequences().isEmpty()){
            project.getSequences().clear();
        }
        while(rs.next()){
            Sequence sequence = new Sequence();
            sequence.setParent(project);
            sequence.setID(rs.getInt("id"));
            project.addSequence(sequence);
            setSequencerSteps(sequence);

        }
        return project;


    }
    static Sequence setSequencerSteps(Sequence sequence) throws SQLException{
        String SQL = "SELECT * FROM step WHERE sequence_id =" + sequence.getID() + " ORDER BY position ASC";
        ResultSet rs = handleSelect(SQL);
        List<Step> steps = new ArrayList<>();
        while (rs.next()){
            Step step = new Step();
            step.setParent(sequence);
            step.setId(rs.getInt("id"));
            step.setPosition(rs.getInt("position"));
            setStepPads(step);
            steps.add(step);
        }
        sequence.setSteps(steps);
        return sequence;


    }
    static Step setStepPads(Step step) throws SQLException{
        String SQL = "SELECT * FROM pad WHERE step_id="+ step.getId();
        ResultSet rs = handleSelect(SQL);
        List<Pad> pads = new ArrayList<>();
        while (rs.next()){
            Pad pad = new Pad();
            pad.setId(rs.getInt("id"));
            pad.setIndex(rs.getInt("pos"));
            pad.setColor(new Color(rs.getInt("color")));
            pad.setFlashing(rs.getBoolean("flashing"));
            pad.setPulsing((rs.getBoolean("pulsing")));
            pad.setParent(step);
            pads.add(pad);

        }
        step.setPads(pads);
        return step;
    }
    static void save(Sequence sequence) {
        if (sequence.getID() == 0) {
            String SQL = "INSERT INTO sequence (project_id) VALUES("+ sequence.getParent().getId() + ");";
            sequence.setID(handleInsert(SQL));
        }

        sequence.getSteps().forEach(DB::save);
    }
    static void save(Project project) {
        String SQL;
        if (project.getId()==0) {
            SQL = "INSERT INTO project (name) VALUES('" + project.getName() + "')";
            project.setId(handleInsert(SQL));
        }
        else {
            SQL = "UPDATE project SET name = '" + project.getName()  +"' WHERE id="+project.getId();
            handleInsert(SQL);
        }
        project.getSequences().forEach(DB::save);
    }
    static void save(Step step) {
        String SQL;
        if (step.getId()==0) {
            SQL = "INSERT INTO step (sequence_id, position) VALUES ("
                    + step.getParent().getID()
                    + ","
                    + step.getPosition()
                    + ");";
            int id = handleInsert(SQL);
            if (id>0){
                step.setId(id);
            }
        }
        step.getPads().forEach(DB::save);

    }
    static void save(Pad pad){
        String SQL = "INSERT OR REPLACE INTO " +
                "pad (pos, step_id, color, flashing, pulsing ) VALUES ("
                + pad.getIndex()
                + ","
                + pad.getParent().getId()
                +","
                +pad.getColor().getRGB()
                +","
                +pad.isFlashing()
                +","
                +pad.isPulsing()
                +");";
        int id = handleInsert(SQL);
        if (id>0){
            pad.setId(id);
        }
    }
    static void delete(Sequence sequence){
        String SQL = "DELETE FROM sequence where id =" + sequence.getID();
        handleInsert(SQL);
    }
    static void delete(Project project) {
        String SQL = "DELETE FROM project where id =" + project.getId();
        handleInsert(SQL);
        //stmt.execute();
    }


}
