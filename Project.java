package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import animals.Antelope;
import animals.CyberSheep;
import animals.Fox;
import animals.Human;
import animals.Sheep;
import animals.Turtle;
import animals.Wolf;
import plants.Belladonna;
import plants.Grass;
import plants.Guarana;
import plants.SosnowskyHogweed;
import plants.SowThistle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Project extends JFrame
{
    public static final int dimensionY = 10;
    public static final int dimensionX = 10;
    public static World world = new World(dimensionY, dimensionX);
    
   
    JButton buttonMakeTurn = new JButton("Make turn");
    JButton buttonSaveGame = new JButton("Save game");
    JButton buttonQuitProgram = new JButton("Quit program");
    JButton buttonShowTurn = new JButton("Show turn");
    JButton buttonLoadGame = new JButton("Load game");
    JButton cells[][] = new JButton[dimensionY][dimensionX];

    JPanel head = new JPanel();
    JPanel body = new JPanel();
    JPanel footer = new JPanel();
    
    JLabel introduction = new JLabel("Piotr Cichacki    s180277");
    JLabel turnCounter = new JLabel("Turn number: " + world.numberOfTurn);
    
    

    public Project()
    {
        initWindow();      
        initComponents();
            
        buttonQuitProgram.addActionListener(new quitProgramActionListener());       
        buttonMakeTurn.addActionListener(new makeTurnActionListener());       
        buttonShowTurn.addActionListener(new showTurnActionListener());       
        buttonSaveGame.addActionListener(new saveGameActionListener());
        buttonLoadGame.addActionListener(new loadGameActionListener());
        pack();
    }
    
    
    
    //window closing settings (questions about exit and saving game)
    public void initWindow()
    {
        this.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                JFrame frame = new JFrame("Exit");
                if (JOptionPane.showConfirmDialog(frame, "Do you want to exit?","Word simulator", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                    closeFrame();
                
            }
            @Override
            public void windowClosed(WindowEvent e) 
            {
                JFrame frame = new JFrame("Save game");
                if (JOptionPane.showConfirmDialog(frame, "Do you want to save game?","Word simulator", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                {
                    saveToFile();
                    System.exit(0);
                }  
                else {
                    System.exit(0);
                }
            }       
        });
        
        this.setTitle("World simulator");         
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);                   
        this.setVisible(true);      
    }
    
    //frame look settings
    public void initComponents()
    {                
        head.setLayout(new GridLayout(3, 1, 10, 10));
        head.add(introduction);
        introduction.setFont(new Font("Monospaced", Font.BOLD, 15));
        head.add(turnCounter);
        turnCounter.setFont(new Font("Monospaced", Font.BOLD, 20));
        
        
        body.setLayout(new GridLayout(dimensionY, dimensionX));   
        for(int y=0; y<dimensionY; y++)
        {
            for(int x=0; x<dimensionX; x++)
            {
                cells[y][x] = new JButton(world.grid[y][x]+"");
                cells[y][x].setPreferredSize(new Dimension(40,40));
                cells[y][x].setFont(new Font("Monospaced", Font.BOLD, 12));
                cells[y][x].addActionListener(new cellActionListener(y,x));
                body.add(cells[y][x]);
            }
        }
        
        footer.add(buttonShowTurn);
        footer.add(buttonMakeTurn);
        footer.add(buttonSaveGame);
        footer.add(buttonLoadGame);
        footer.add(buttonQuitProgram);
         
        this.getContentPane().add(head, BorderLayout.PAGE_START);
        this.getContentPane().add(body, BorderLayout.CENTER);
        this.getContentPane().add(footer, BorderLayout.SOUTH);
    }
     
    public void closeFrame()
    {
        this.dispose();
    }
     
    private JFrame returnFrame()
    {
        return this;
    }
    
    JFrame frameMovement;
    JPanel panelMovement;
    JButton buttonMovement;
     
    //new frame which shows before making turn and forces to choose direction of human movement
    public void initComponentsMovement()
    {
        frameMovement = new JFrame();
        panelMovement = new JPanel();
        buttonMovement = new Movement("Choose direction of human movement");
        frameMovement.setTitle("World simulator"); 
        frameMovement.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);                   
        frameMovement.setVisible(true);
               
        buttonMovement.setPreferredSize(new Dimension(300,50));
        panelMovement.add(buttonMovement);
        frameMovement.getContentPane().add(panelMovement);
        frameMovement.pack();
    }
    
    //button with key listener to take direction of movement
    private class Movement extends JButton 
    {     
        public Movement(String name)
        {
            super(name);
            this.addKeyListener(new KeyAdapter() {
            
                @Override
                public void keyPressed(KeyEvent e){
                    chooseDirection(e);
                    if(world.organisms[0].newPositionInsideGrid(world.directionOfHumanMovement))
                    {
                        returnFrame().setEnabled(true);
                        frameMovement.dispose();
                        world.makeTurn();
                        turnCounter.setText("Turn number: " + world.numberOfTurn);
                        for(int y=0; y<dimensionY; y++)
                        {
                            for(int x=0; x<dimensionX; x++)
                            {
                                cells[y][x].setText(world.grid[y][x]+"");
                            }
                        }
                    }                   
                }  
            });           
        }
        
        private void chooseDirection(KeyEvent e)
        {
            if(e.getKeyCode() == KeyEvent.VK_UP)
            {
                world.directionOfHumanMovement = 1;
            }
            else if(e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                world.directionOfHumanMovement = 3;
            }
            else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                world.directionOfHumanMovement = 2;
            }
            else if(e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                world.directionOfHumanMovement = 4;
            }
        }
    }
    
    //BUTTONS ACTION LISTENERS
    private class makeTurnActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            //if human exist in the world, before making turn, you have to choose direction of movement by key arrow
            if(world.humanExist)
            {
                returnFrame().setEnabled(false);
                initComponentsMovement();
            }
            else
            {               
                        world.makeTurn();
                        turnCounter.setText("Turn number: " + world.numberOfTurn);
                        for(int y=0; y<dimensionY; y++)
                        {
                            for(int x=0; x<dimensionX; x++)
                            {
                                cells[y][x].setText(world.grid[y][x]+"");
                            }
                        }
            }
        }   
    }
    
    //similar to window close settings (questions about exit and saving game)
    private class quitProgramActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            JFrame exitFrame = new JFrame();
            if (JOptionPane.showConfirmDialog(exitFrame, "Do you want to exit?","Word simulator", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
            {
                closeFrame();
                JFrame saveFrame = new JFrame();
                if (JOptionPane.showConfirmDialog(saveFrame, "Do you want to save game?","Word simulator", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                {
                    saveToFile();
                    System.exit(0);
                } 
                else
                {
                    System.exit(0);
                }
            }
        }
    }
  
    //create new frame with informations about last turn
    private class showTurnActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            new ShowTurn(returnFrame(), world).setVisible(true);
        }       
    }
    
    //saving state of the world to the file by saveToFile function
    private class saveGameActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            JFrame saveFrame = new JFrame();
            if (JOptionPane.showConfirmDialog(saveFrame, "Do you want to save game?","Word simulator", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
            {
                saveToFile();
            }      
        }             
    }
    
    //load state of the world from file (firstly delete all current organisms and then creating new ones from file)
    private class loadGameActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            JFrame saveFrame = new JFrame();
            if (JOptionPane.showConfirmDialog(saveFrame, "Do you want to load state of World from file?","Word simulator", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
            {
                clearWorld();
                readFromFile();
            }        
        }   
    }
    
    private class cellActionListener implements ActionListener
    {
        private final int posY;
        private final int posX;
        private int index;
        
        public cellActionListener(int posY, int posX)
        {
            this.posY = posY;
            this.posX = posX;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            index = world.findOrganism(posY, posX);
            if(index!=-1)   //if cell is occupied by organism, it shows informations about it
            {   
                new InformationsAboutOrganism(returnFrame(), world.organisms[index]).setVisible(true);
            }
            else            //if cell is empty you can create new animal
            {
                JFrame frame = new JFrame("Add Organism");
                if (JOptionPane.showConfirmDialog(frame, "Do you want to create new organism?","Word simulator", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                {
                   new CreateOrganism(returnFrame(), world, posY, posX, cells[posY][posX]).setVisible(true);
                }
            }
        }   
    }
   
    public static void addOrganismToWorld(char gridPresentation, int posY, int posX, String organismName)
    {
        Organism tmp = null;
        switch(gridPresentation)
        {
            case 'W':
                tmp = new Wolf(world, posY, posX, organismName);
                break;
            case 'S':
                tmp = new Sheep(world, posY, posX, organismName);
                break;
            case 'F':
                tmp = new Fox(world, posY, posX, organismName);
                break;  
            case 'T':
                tmp = new Turtle(world, posY, posX, organismName);
                break;
            case 'A':
                tmp = new Antelope(world, posY, posX, organismName);
                break;
            case 'C':
                tmp = new CyberSheep(world, posY, posX, organismName);
                break;
            case 's':
                tmp = new Grass(world, posY, posX, "Grass");
                break;
            case 't':
                tmp = new SowThistle(world, posY, posX, "Sow_Thistle");
                break;
            case 'g':
                tmp = new Guarana(world, posY, posX, "Guarana");
                break;
            case 'b':
                tmp = new Belladonna(world, posY, posX, "Belladonna");
                break;
            case 'h':
                tmp = new SosnowskyHogweed(world, posY, posX, "Sosnowsky_hogweed");
                break;
            case 'H':
                tmp = new Human(world, posY, posX, organismName);
                break;
        }
        if(tmp!=null)
        {
            world.organisms[world.numberOfOrganism-1] = tmp;
            world.numberOfOrganismInTurn++;
        }
    }
    
    public void saveToFile()
    {
        try
        {
            FileWriter output = new FileWriter("output.txt");
            for(int i=0; i<world.numberOfOrganism; i++)
                output.write(world.organisms[i].toString()+"\n");
            output.close();
            JOptionPane.showMessageDialog(rootPane, "Game saved");
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(rootPane, "An error occured");  
        }
    }
    
    public void readFromFile()
    {
        try
        {
            File output = new File("output.txt");
            Scanner reader = new Scanner(output);
           
            
            while (reader.hasNextLine())
            {
                String data = reader.nextLine();
                loadAnimal(data);
            }
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(rootPane, "An error occured");  
        }
    }
    
    
    public void loadAnimal(String input)
    {
        String[] data = input.split("\\s");
        String organismName = data[0];
        char gridPresentation = data[1].charAt(0);
        int positionY = Integer.parseInt(data[2]);
        int positionX = Integer.parseInt(data[3]);
        int strength = Integer.parseInt(data[4]);
        int initiative = Integer.parseInt(data[5]);
        addOrganismToWorld(gridPresentation, positionY, positionX, organismName);
        world.organisms[world.findOrganism(positionY, positionX)].setStrength(strength);
        world.organisms[world.findOrganism(positionY, positionX)].setInitiative(initiative);
        cells[positionY][positionX].setText(gridPresentation+"");
    }
    
    public void clearWorld()
    {
        for(int i=world.numberOfOrganism-1; i>=0; i--)
        {
            cells[world.organisms[i].getPositionY()][world.organisms[i].getPositionX()].setText("");
            world.deleteOrganism(i);
        }
        for(int i=0; i<world.grid.length; i++)
        {
            for(int j=0; j<world.grid[i].length; j++)
            {
                world.grid[i][j] = ' ';
            }
        }
        world.numberOfOrganism=0;
        world.numberOfOrganismInTurn=0;
        world.numberOfTurn=1;
        turnCounter.setText("Turn number: " + world.numberOfTurn);
    }
    
    public static void main(String[] args) 
    {
        
        addOrganismToWorld('W', 1, 2, "WOLF_1");
        addOrganismToWorld('W', 2, 5, "WOLF_2");
        
        addOrganismToWorld('S', 4, 4, "SHEEP_1");
        
        addOrganismToWorld('F', 5, 5, "FOX_1");
        
        addOrganismToWorld('A', 2, 3, "ANTELOPE_1");
        addOrganismToWorld('A', 0, 7, "ANTELOPE_2");
        
        addOrganismToWorld('T', 8, 1, "TURTLE_1");
        addOrganismToWorld('T', 9, 2, "TURTLE_2");
        
        addOrganismToWorld('h', 6, 6, "");
        
        addOrganismToWorld('s', 7, 2, "");
        
        addOrganismToWorld('t', 8, 2, "");
        
        addOrganismToWorld('g', 9, 9, "");
        
        addOrganismToWorld('b', 0, 4, "");   
        addOrganismToWorld('H', 0, 0, "Piotr");
        
        addOrganismToWorld('h', 2, 9, "");
        addOrganismToWorld('h', 7, 5, "");
        
        addOrganismToWorld('C', 3, 4, "CYBER_SHEEP");

        new Project();
    }

}
