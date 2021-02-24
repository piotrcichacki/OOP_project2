package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShowTurn extends JDialog
{
    JPanel panel = new JPanel();
    JLabel[] comments;

    public ShowTurn(JFrame parent, World world)
    {
        super(parent, true);
        
        int width = (int)parent.getBounds().getWidth();
        int height = (int)parent.getBounds().getHeight();  
        int frameWidth = this.getSize().width;
        int frameHeight = this.getSize().height;     
        
        this.setTitle("Last turn process");
        this.setLocation(parent.getBounds().x+((width-frameWidth)/4), parent.getBounds().y+((height-frameHeight)/4));
        
        initComponents(world);
    }
    
    public void initComponents(World world)
    {
        JPanel panel = new JPanel();
        
        JLabel[] comments = new JLabel[world.commentsCounter];
        
        if(world.commentsCounter>40)
            panel.setLayout(new GridLayout((world.commentsCounter/2) + 1, 2, 5, 5));
        else
            panel.setLayout(new GridLayout(world.commentsCounter, 1, 5, 5));

        
        for(int i=0; i<world.commentsCounter; i++)
        {
            comments[i] = new JLabel((i+1)+". "+world.comments[i]);
            panel.add(comments[i]);
        }
        
        this.getContentPane().add(panel);
        pack();

    }
    
}
