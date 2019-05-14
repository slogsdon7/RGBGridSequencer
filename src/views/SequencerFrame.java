package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SequencerFrame extends JInternalFrame {

    private JMenuBar menuBar = new JMenuBar();
    private JPanel panel = new JPanel();
    private JPanel gPanel = new JPanel();
    private List<Grid> grids = new ArrayList<>();
    private JPanel editor = new JPanel();
    private JToolBar controls;
    private int selectedStep;
    private JPanel stepSelector = new JPanel();
    private List<JToggleButton> steps = new ArrayList<>();
    private JButton play;
    private JButton pause;
    private JButton stop;
    private PadButton colorBtn;

    public SequencerFrame(String title) {

        super("Sequencer", false, true, false, true);
        initComponents();
        pack();
        setSelectedStep(0);
        setVisible(true);
    }

    void initComponents() {
        setContentPane(panel);
        createMenu();
        setJMenuBar(menuBar);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        makeEditor();
        makeStepSelector();
        makeGrids();
        panel.add(gPanel);
        panel.add(stepSelector);


    }

    public void setSelectedStep(int i) {
        CardLayout card = (CardLayout) gPanel.getLayout();
        card.show(gPanel, String.valueOf(i));
        selectedStep = i;
        steps.get(i).setSelected(true);

    }
    public void setPaintColor(Color color){
        this.colorBtn.setBackground(color);

    }

    public int getSelectedStep() {
        return selectedStep;
    }

    public void addStepListener(ActionListener listener) {
        steps.forEach(b -> b.addActionListener(listener));
    }

    public void addPadListener(ActionListener l) {
        grids.forEach(grid -> grid.setPadListener(l));
    }

    public void addControlsListener(ActionListener l) {
        play.addActionListener(l);
        stop.addActionListener(l);
        pause.addActionListener(l);
        colorBtn.addActionListener(l);


    }

    public void addMenuListener(ActionListener l) {
        for (Component component : menuBar.getMenu(0).getMenuComponents()) {
            var item = (JMenuItem) component;
            item.addActionListener(l);
        }
    }

    private void makeGrids() {
        gPanel.setLayout(new CardLayout());
        for (int i = 0; i < 16; i++) {
            Grid grid = new Grid(8);
            grids.add(grid);
            gPanel.add(grid, String.valueOf(i));
        }
    }

    public void setPadColor(int step, int index, Color color) {
        grids.get(step).setColorAt(color, index);
    }

    public void setPadColor(int index, Color color) {
        grids.get(selectedStep).setColorAt(color, index);
    }

    public void setPadColorChangeListener(PropertyChangeListener propertyChangeListener, int step, int index) {
        grids.get(step).setColorChangeListenerAt(propertyChangeListener, index);
    }

    private void createMenu() {
        JMenu menu = new JMenu("Edit");
        JMenuItem copy = new JMenuItem("Copy Step");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.META_MASK));
        copy.setActionCommand("COPY");
        JMenuItem paste = new JMenuItem("Paste Step");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.META_MASK));
        paste.setActionCommand("PASTE");
        JMenuItem delete = new JMenuItem("Delete");
        delete.setActionCommand("DELETE");
        menu.add(copy);
        menu.add(paste);
        menu.add(delete);
        menuBar.add(menu);

    }


    private void createButtons() {
        controls = new JToolBar();
        controls.setOrientation(1);
        play = new JButton("Play");
        pause = new JButton("Pause");
        stop = new JButton("Stop");
        play.setActionCommand("PLAY");
        pause.setActionCommand("PAUSE");
        stop.setActionCommand("STOP");
        controls.add(play);
        controls.add(pause);
        controls.add(stop);

    }

    void makeEditor() {
        colorBtn = new PadButton(Color.white);
        colorBtn.setActionCommand("COLOR");
        Dimension dim = new Dimension(50,50);
        colorBtn.setPreferredSize(dim);
        colorBtn.setMinimumSize(dim);
        colorBtn.setMaximumSize(dim);
        editor.setLayout(new BoxLayout(editor, BoxLayout.X_AXIS));
        editor.add(colorBtn);


    }

    void makeStepSelector() {
        createButtons();
        JPanel steps = new JPanel(new GridLayout(2, 8));
        stepSelector = new JPanel();
        stepSelector.setLayout(new BoxLayout(stepSelector, BoxLayout.X_AXIS));
        stepSelector.add(steps);
        stepSelector.add(controls);
        stepSelector.add(editor);
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 16; i++) {
            JToggleButton btn = new JToggleButton();
            btn.setActionCommand(String.valueOf(i));
            btn.setPreferredSize(new Dimension(30, 30));
            this.steps.add(btn);
            steps.add(btn);
            group.add(btn);

        }

    }


}

class Grid extends JPanel {
    private int gridSize;
    private List<PadButton> pads = new ArrayList<>();

    Grid() {
        super(new GridLayout(8, 8));
        gridSize = 8;
        init();
    }

    Grid(int size) {
        super(new GridLayout(size, size));
        gridSize = size;
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            colors.add(Color.darkGray);

        }
        setPads(colors);
        setupGrid();
    }


    public void setPadListener(ActionListener l) {
        pads.forEach(pad -> pad.addActionListener(l));
    }

    public void setColorAt(Color color, int index) {
        pads.get(index).setBackground(color);

    }

    public void setColorChangeListenerAt(PropertyChangeListener propertyChangeListener, int index) {
        pads.get(index).addPropertyChangeListener("background", propertyChangeListener);
    }

    private void init() {
        setupGrid();
    }

    public void setPads(List<Color> padColors) {
        for (Color color : padColors) {
            pads.add(new PadButton(color));
        }

    }

    private void setupGrid() {
        for (AbstractButton pad : pads) {
            pad.setPreferredSize(new Dimension(50, 50));
            add(pad);

        }
    }


}

class PadButton extends JToggleButton implements Serializable {
    PadButton(Color color) {
        super();
        setContentAreaFilled(true);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(color);
        createPopupMenu();
    }
    private void createPopupMenu(){
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copy = new JMenuItem("Copy");
        copy.setActionCommand("COPY");
        popupMenu.add(copy);
        setComponentPopupMenu(popupMenu);

    }

    @Override
    public void addActionListener(ActionListener l) {
        super.addActionListener(l);
        JMenuItem menuItem = (JMenuItem) getComponentPopupMenu().getComponent(0);
        menuItem.addActionListener(l);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth() - 5, getHeight() - 5);
        /*if (isSelected()) {
            g2d.setColor(Color.BLUE);
            g2d.drawRect(0, 0, getWidth() - 5, getHeight() - 5);
        }*/

    }

    @Override
    public void setBackground(Color bg) {
        firePropertyChange("background", getBackground(), bg);
        super.setBackground(bg);

    }
}


