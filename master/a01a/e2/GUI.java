package a01a.e2;

import javax.swing.*;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    
    private final List<JButtonSuperiore> cells = new ArrayList<>();
    private final LogicImpl logic;
    public GUI(int size) {
        this.logic = new LogicImpl();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(100*size, 100*size);
        
        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(panel);
        
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e){
        	    var button = (JButtonSuperiore)e.getSource();
                logic.pressButton(button.getCord());
                button.setText(logic.getValue(button.getx(), button.gety()));
            }
        };
                
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                final JButtonSuperiore jb = new JButtonSuperiore(j+" "+i,j,i);
                this.cells.add(jb);
                jb.addActionListener(al);
                panel.add(jb);
            }
        }
        this.setVisible(true);
    } 
    
    private class JButtonSuperiore extends JButton {
        private final int x;
        private final int y;
        private final Pair<Integer,Integer> cord; 
        
        public int getx() {
            return x;
        }

        public int gety() {
            return y;
        }

        public Pair<Integer,Integer> getCord(){
            return cord;
        }



        public JButtonSuperiore(final String text,final int x, final int y){
            super(text);
            this.x = x;
            this.y = y;
            this.cord = new Pair<Integer,Integer>(x, y);
        }

        public JButtonSuperiore(final String text,final Pair<Integer,Integer> cord){
            super(text);
            this.cord = cord;
            this.x = cord.getX();
            this.y = cord.getY();
        }
    }
}

