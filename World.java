package project;

import animals.Human;

public class World 
{
    private final int dimensionY, dimensionX;
    public int numberOfTurn;
    
    public char[][] grid;
    public Organism[] organisms;
    public int numberOfOrganism;
    public int numberOfOrganismInTurn;
    
    public String[] comments;
    public int commentsCounter;
    
    public int[] deleted;
    public int deletedCounter;
    
    public int directionOfHumanMovement;
    public boolean humanExist;
    //constructor creates grid of specific sizes and array of pointers to organisms
    World(int dimensionY, int dimensionX)
    {
        humanExist=false;
        comments = new String[dimensionY*dimensionX];
        deleted = new int[dimensionY*dimensionX];
        commentsCounter = 0;
        deletedCounter = 0;
        numberOfOrganism = 0;
        numberOfTurn = 1;
        this.dimensionY = dimensionY;
        this.dimensionX = dimensionX;
        grid = new char[dimensionY][dimensionX];
        for(int i=0; i<grid.length; i++)
        {
            for(int j=0; j<grid[i].length; j++)
            {
                grid[i][j] = ' ';
            }
        }
        organisms = new Organism[dimensionY*dimensionX];      
    }
   
    public int getDimensionY()
    {
        return dimensionY;
    }
    public int getDimensionX()
    {
        return dimensionX;
    }
    
    //sortOrganisms function sort array of organism according to their initiative (organisms with bigger initiatives moves first)
    private void sortOrganisms()
    {
        Organism maksInitiative = null;
        int indexMaks;
        
        for(int i=0; i<numberOfOrganism-1; i++)
        {
            maksInitiative = organisms[i];
            indexMaks = i;
            
            for(int j=i+1; j<numberOfOrganism; j++)
            {
                if(organisms[j].getInitiative() > maksInitiative.getInitiative())
                {
                    maksInitiative = organisms[j];
                    indexMaks = j;
                }
            }
            for (int j=indexMaks; j>i; j--)
            {
                organisms[j] = organisms[j - 1];
		organisms[j].setIndexAge(j);
            }
	organisms[i] = maksInitiative;
	organisms[i].setIndexAge(i);
        }       
    }
    
    //makeTurn carry out one turn
    public void makeTurn()
    {
        boolean deletedOrganism = false;
        
        sortOrganisms();
 
        numberOfTurn++;
        commentsCounter = 0;
        deletedCounter = 0;

        //new created organisms do not perform action during current turn
        numberOfOrganismInTurn = numberOfOrganism;
        
        for(int i=0; i<numberOfOrganismInTurn; i++)
        {    
            deletedOrganism = false;
            for(int j=0; j<deletedCounter; j++)
            {
                if(deleted[j] == i)
                {
                    deletedOrganism = true;
                    break;
                }
            }
            if (deletedOrganism == false)
            {
                organisms[i].action();
            }
        }
        
        for(int i=0; i<deletedCounter; i++)
        {
            deleteOrganism(deleted[i]);
            for(int j=i+1; j<deletedCounter; j++)
            {
                if(deleted[j] > deleted[i])
                    deleted[j]--;
            }
        }

    }
    
    //deleteOrganism delete organism defeated in collision
    public void deleteOrganism(int index)
    {   
        if(organisms[index] instanceof Human)
            humanExist = false;
        for(int i=index; i<numberOfOrganism-1; i++)
	{
		organisms[i] = organisms[i + 1];
		organisms[i].setIndexAge(organisms[i].getIndexAge()-1);
	}
	organisms[numberOfOrganism - 1] = null;
	numberOfOrganism--;
    }
    
    //findOrganism find organism in array with specific position and returns its index in array
    public int findOrganism(int positionY, int positionX)
    {        
        for(int i=0; i<numberOfOrganism; i++)
        {      
            if(positionY == organisms[i].getPositionY() && positionX == organisms[i].getPositionX())
                return i;
        }
        return -1;
    }
}
