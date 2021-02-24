package project;

import animals.Antelope;
import animals.CyberSheep;
import animals.Fox;
import animals.Human;
import animals.Sheep;
import animals.Turtle;
import animals.Wolf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import plants.Belladonna;
import plants.Grass;
import plants.Guarana;
import plants.SosnowskyHogweed;
import plants.SowThistle;

public class CreateOrganism extends JFrame
{
    int posY, posX;
    World world;
    JButton cell;
    
    public CreateOrganism(JFrame parent, World world, int posY, int posX, JButton cell)
    {
        this.world = world;
        this.posY = posY;
        this.posX = posX;
        this.cell = cell;
        
        int width = (int)parent.getBounds().getWidth();
        int height = (int)parent.getBounds().getHeight();  
        int frameWidth = this.getSize().width;
        int frameHeight = this.getSize().height;     
        
        this.setTitle("Creating new organism");
        this.setLocation(parent.getBounds().x+((width-frameWidth)/4), parent.getBounds().y+((height-frameHeight)/4));
        this.setSize(300,300);
        
        initComponents();
    }
    
    int option;
    ButtonGroup options = new ButtonGroup();

    public void initComponents()
    {
        JPanel head = new JPanel();
        JPanel typeOfOrganism = new JPanel();
        JPanel footer = new JPanel();
        
        JButton confirm = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new cancelButton());
        confirm.addActionListener(new confirmButton());
        
        JLabel question = new JLabel("Choose type of new organism:");
        
        JRadioButton[] organism = new JRadioButton[12];
        organism[0] = new JRadioButton("Wolf", false);
        organism[1] = new JRadioButton("Sheep", false);
        organism[2] = new JRadioButton("Fox", false);
        organism[3] = new JRadioButton("Turtle", false);
        organism[4] = new JRadioButton("Antelope", false);
        organism[5] = new JRadioButton("Cyber_Sheep", false);
        organism[6] = new JRadioButton("Grass", false);
        organism[7] = new JRadioButton("Sow_thistle", false);
        organism[8] = new JRadioButton("Guarana", false);
        organism[9] = new JRadioButton("Belladonna", false);
        organism[10] = new JRadioButton("Sosnowsky's_hogweed", false);
        organism[11] = new JRadioButton("Human", false);
        
        
        head.add(question);
        for(int i=0; i<12; i++)
        {
            typeOfOrganism.add(organism[i]);
            organism[i].addActionListener(new radioButton(i));
            options.add(organism[i]);
        }
        
        footer.add(cancel);
        footer.add(confirm);
        
        typeOfOrganism.setLayout(new GridLayout(12, 1));
        this.getContentPane().add(head, BorderLayout.NORTH);
        this.getContentPane().add(typeOfOrganism, BorderLayout.CENTER);
        this.getContentPane().add(footer, BorderLayout.SOUTH);
        
    }
    
    private class radioButton implements ActionListener
    {
        int index;
        public radioButton(int index)
        {
            this.index = index;
        }
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            option = index;
        }       
    }
    
    private class confirmButton implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            JFrame frame = new JFrame("Organism");
            Organism tmp = null;
            switch(option)
            {
                case 0:
                    tmp = new Wolf(world, posY, posX, "Wolf");
                    break;
                case 1:
                    tmp = new Sheep(world, posY, posX, "Sheep");
                    break;
                case 2:
                    tmp = new Fox(world, posY, posX, "Fox");
                    break;  
                case 3:
                    tmp = new Turtle(world, posY, posX, "Turtle");
                    break;
                case 4:
                    tmp = new Antelope(world, posY, posX, "Antelope");
                    break;
                case 5:
                    tmp = new CyberSheep(world, posY, posX, "Cyber_sheep");
                    break;
                case 6:
                    tmp = new Grass(world, posY, posX, "Grass");
                    break;
                case 7:
                    tmp = new SowThistle(world, posY, posX, "Sow_Thistle");
                    break;
                case 8:
                    tmp = new Guarana(world, posY, posX, "Guarana");
                    break;
                case 9:
                    tmp = new Belladonna(world, posY, posX, "Belladonna");
                    break;
                case 10:
                    tmp = new SosnowskyHogweed(world, posY, posX, "Sosnowsky_hogweed");
                    break;
                case 11:
                    if(world.humanExist)
                    {
                        JOptionPane.showMessageDialog(rootPane, "You cannot create another human");
                    }
                    else
                        tmp = new Human(world, posY, posX, "Human");
                    break;
            }
            if(tmp!=null)
            {
                world.organisms[world.numberOfOrganism-1] = tmp;
                cell.setText(tmp.getGridPresentation()+"");
            }
            closeFrame();
        }    
    }
    
    public void closeFrame()
    {
        this.dispose();
    }
    
    private class cancelButton implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            returnFrame().dispose();
        }      
    }
    
    private JFrame returnFrame()
    {
        return this;
    }
}
