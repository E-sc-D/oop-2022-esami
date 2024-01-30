package a02b.e2;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    
    private final List<JButtonMigliore> cells = new ArrayList<>();
    private final Logic logic = new LogicImpl();
    public GUI(int size) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(100*size, 100*size);
        
        JPanel main = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(size,size));
        JButton check = new JButton("check");
        this.getContentPane().add(main);
        main.add(BorderLayout.CENTER, panel);
        main.add(BorderLayout.SOUTH, check);
        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
            }
            
        });
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e){
        	    var button = (JButtonMigliore)e.getSource();
                logic.press(button.coord);
        	    button.setText(logic.getValue(button.getCoord()));
            }
        };
                
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                final JButtonMigliore jb = new JButtonMigliore(" ",new Pair<Integer,Integer>(j, i));
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

        public JButtonMigliore(String text,Pair<Integer,Integer> coord){
            super(text);
            this.coord = coord;
        }
    }
}
