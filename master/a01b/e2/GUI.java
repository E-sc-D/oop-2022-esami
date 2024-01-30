package a01b.e2;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    
    private final List<JButtonMigliore> cells = new ArrayList<>();
    private final Logic logic;
    
    public GUI(int size) {
        this.logic = new LogicImpl(size);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(100*size, 100*size);
        
        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(panel);
        
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e){
        	    var button = (JButtonMigliore)e.getSource();

                if(!logic.pressButton(button.getCoord())){
                    System.exit(1);
                }

                for (JButtonMigliore jButtonMigliore : cells) {
                    jButtonMigliore.setText(logic.getValue(jButtonMigliore.coord));
                }
            }
        };
                
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                final JButtonMigliore jb = new JButtonMigliore(" ",j,i);
                this.cells.add(jb);
                jb.addActionListener(al);
                panel.add(jb);
            }
        }
        this.setVisible(true);
    }    

    private class JButtonMigliore extends JButton{
        private final Pair<Integer,Integer> coord;

        public Pair<Integer, Integer> getCoord() {
            return coord;
        }

        public JButtonMigliore (String text,Pair<Integer,Integer> coord){
            super(text);
            this.coord = coord;
        }
        
        public JButtonMigliore (String text,int x, int y){
            super(text);
            this.coord = new Pair<>(x,y);
        }

        public String PosToString(){
            return this.coord.toString();
        }
    }
}
