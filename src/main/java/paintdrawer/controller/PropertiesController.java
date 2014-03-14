package paintdrawer.controller;

import paintdrawer.model.commands.*;
import paintdrawer.model.interfaces.ICommand;
import paintdrawer.model.shapes.Shape;
import paintdrawer.view.PropertiesTile;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.*;
import java.util.List;

/**
 * Created by joel on 2014-03-12.
 */
public class PropertiesController implements ActionListener, PopupMenuListener
{
    private FrontController front;
    private PropertiesTile propertiesTile;
    private Shape markedShape;
    private JComboBox sizeBox, lineBox, colorBox;

    private final int SIZE = 0;
    private final int LINEWIDTH = 1;
    private final int COLOR = 2;
    private final int DELETE = 3;
    private final int CLOSE = 4;
    private final int FILL = 5;

    public PropertiesController(FrontController front, PropertiesTile propertiesTile)
    {
        this.front = front;
        this.propertiesTile = propertiesTile;

        initListeners(propertiesTile);
    }

    private void initListeners(PropertiesTile propertiesTile)
    {
        sizeBox = (JComboBox) propertiesTile.getComponent(SIZE);
        sizeBox.setActionCommand(PropertiesTile.Components.SIZE.name());
        sizeBox.addPopupMenuListener(this);

        lineBox = (JComboBox) propertiesTile.getComponent(LINEWIDTH);
        lineBox.setActionCommand(PropertiesTile.Components.LINEWIDTH.name());
        lineBox.addPopupMenuListener(this);

        colorBox = (JComboBox) propertiesTile.getComponent(COLOR);
        colorBox.setActionCommand(PropertiesTile.Components.COLOR.name());
        colorBox.addPopupMenuListener(this);

        JButton closeButton = (JButton) propertiesTile.getComponent(CLOSE);
        closeButton.setActionCommand(PropertiesTile.Components.CLOSE.name());
        closeButton.addActionListener(this);

        JButton deleteButton = (JButton) propertiesTile.getComponent(DELETE);
        deleteButton.setActionCommand(PropertiesTile.Components.DELETE.name());
        deleteButton.addActionListener(this);

        JToggleButton fillButton = (JToggleButton) propertiesTile.getComponent(FILL);
        fillButton.setActionCommand(PropertiesTile.Components.FILL.name());
        fillButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Shape shape = front.getModel().getActiveShape();
        System.out.println(e.getActionCommand());

        if (e.getActionCommand().equals(PropertiesTile.Components.CLOSE.name())) {
            propertiesTile.setVisible(false);

            if (shape != null) {
                shape.setMarked(false);
            }
        } else if (shape != null) {
            if (e.getActionCommand().equals(PropertiesTile.Components.DELETE.name())) {
                front.getModel().executeCommand(new RemoveAction(shape, front.getModel()));
            } else if (e.getActionCommand().equals(PropertiesTile.Components.FILL.name())) {
                AbstractButton button = (AbstractButton)e.getSource();
                System.out.println("Fill action: " + button.getModel().isArmed());

                if (button.getModel().isArmed()) {
                    front.getModel().executeCommand(new FillAction(shape, button.getModel().isSelected()));
                }
            }
        }
    }

    public Shape getIntersectingShape(MouseEvent e)
    {
        int x = e.getX(), y = e.getY();
        List<Shape> shapes = front.getModel().getShapes();
        Shape s;

        // Start from latest added shape, if they are stacked as layers
        for (int i = shapes.size() - 1; i >= 0; i--) {
            s = shapes.get(i);

            if (s.intersects(x, y)) {
                return s;
            }
        }

        return null;
    }

    public void togglePropertyBoard(MouseEvent e)
    {
        Shape s = getIntersectingShape(e);

        if (markedShape != null) {
            markedShape.setMarked(false);
            markedShape = null;
        }

        if (s != null) {
            if (s.isMarked()) {
                s.setMarked(false);
            } else {
                markedShape = s;
                s.setMarked(true);
                front.getModel().setActiveShape(s);
            }
        }

        propertiesTile.setVisible(true);
        front.update();
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
    {
        System.out.println();
        Shape shape = front.getModel().getActiveShape();

        if (shape != null) {
            if (e.getSource().equals(sizeBox)) {
                front.getModel().executeCommand(new ResizeAction(shape, propertiesTile.getShapeSize()));
            } else if (e.getSource().equals(lineBox)) {
                front.getModel().executeCommand(new LineWidthAction(shape, propertiesTile.getLineSize()));
            } else if (e.getSource().equals(colorBox)) {
                front.getModel().executeCommand(new ColorAction(shape, propertiesTile.getColor()));
            }

            front.update();
        }
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {

    }
}
