package project;

import animals.Human;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InformationsAboutOrganism extends JDialog
{
    public Organism organism;
    
    public InformationsAboutOrganism(JFrame parent, Organism organism) 
    {
        super(parent, true);
        
        this.organism = organism;
        
        initComponents();
        
        int width = (int)parent.getBounds().getWidth();
        int height = (int)parent.getBounds().getHeight();  
        int frameWidth = this.getSize().width;
        int frameHeight = this.getSize().height;     
        
        this.setLocation(parent.getBounds().x+((width-frameWidth)/4), parent.getBounds().y+((height-frameHeight)/4));
                
    }
    
    public int numberOfLabels = 5;
    JPanel panel = new JPanel();
        
    public void initComponents()
    {
        JLabel organismName = new JLabel("Organism name: "+organism.getOrganismName());
        JLabel gridPresentation = new JLabel("Grid presentation: "+organism.getGridPresentation());
        JLabel position = new JLabel("Position: ("+(organism.getPositionX()+1)+","+(organism.getPositionY()+1)+")");
        JLabel strength = new JLabel("Strength: "+organism.getStrength());
        JLabel comment = new JLabel("Last move: "+organism.getComment());
        
        panel.add(organismName);
        panel.add(gridPresentation);
        panel.add(position);
        panel.add(strength);
               
        if(organism instanceof Human)
        {
            humanExtension();
        }
        
        panel.add(comment);
        
        this.setTitle("Organism");
        
        panel.setLayout(new GridLayout(numberOfLabels, 1, 10, 10));
           
        this.getContentPane().add(panel, BorderLayout.NORTH);
        pack();
    }
    
    public void humanExtension()
    {
        String activatedAbility, activeTurn;
        if(((Human)organism).activatedAbility()) 
        {
            activatedAbility = "YES";
            activeTurn = "Active for next " + ((Human)organism).getTurnActivated() + " turns";
        }
        else
        {
            activatedAbility = "NO";
            if(((Human)organism).getTurnDisactivated()>0)
                activeTurn = "You cannot activate for next " + ((Human)organism).getTurnDisactivated() + " turns";
            else
            {
                activeTurn = "Press button to activate special ability";
                JPanel ability = new JPanel();
                JButton activateAbility = new JButton("Activate ability");
                activateAbility.addActionListener(new activateAbility());
                ability.add(activateAbility);
                this.getContentPane().add(ability, BorderLayout.SOUTH);
            }
        }
            
        JLabel ability = new JLabel("Purification active: "+activatedAbility);
        JLabel active = new JLabel(activeTurn);
        panel.add(ability);
        panel.add(active);
        numberOfLabels = 7;
    }
    
    public void closeFrame()
    {
        this.dispose();
    }
     
    
    private class activateAbility implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            ((Human)organism).activatedSpecialAbility = true;
            ((Human)organism).turnActivated = 5;
            closeFrame();
            JOptionPane.showMessageDialog(rootPane, "Purification activated");
        }             
    }
}
