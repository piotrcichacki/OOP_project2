package plants;

import java.util.Random;
import project.World;

public class SowThistle extends Plant
{
    
    public SowThistle(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 0;
        this.initiative = 0;
        this.gridPresentation = 't';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        SowThistle tmp = new SowThistle(world, positionY, positionX, "Sow_Thistle");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
    @Override
    public void action()
    {
        int sowing, direction;
	boolean sowNewPlant = false;
	int newPlantPositionY = positionY, newPlantPositionX = positionX;
        
        comment ="";
        
        //sow thistle performs 3 attempts at spreading in each turn
        for(int i=0; i<3; i++)
        {
           
            Random rand = new Random();
        
            sowing = rand.nextInt(100);
	
            if (sowing < SOW_PROPABILITY)		//plant will sow to free neighboring cell
            {
                if (spaceForNewOrganism())
                {
                    while (world.grid[newPlantPositionY][newPlantPositionX] != ' ')
                    {
                        newPlantPositionY = positionY;
                        newPlantPositionX = positionX;

                        do
                        {
                        direction = rand.nextInt(4) + 1;
                        } while (!newPositionInsideGrid(direction));

                        switch(direction)
                        {
                            case UP:
                                newPlantPositionY = positionY-1;
                                break;
                            case RIGHT:
                                newPlantPositionX = positionX+1;
                                break;
                            case DOWN:
                                newPlantPositionY = positionY+1;
                                break;
                            case LEFT:
                                newPlantPositionX = positionX-1;
                                break;
                        }            
                    }

		comment = organismName + " sowned new plant. ";
		world.comments[world.commentsCounter++] = comment;
                addOrganism(world, newPlantPositionY, newPlantPositionX);
                sowNewPlant = true;
                }
                else
                {
                    continue;
                }
            }
            else
            {
                continue;
            }
	 
        }
        if(!sowNewPlant)
        {
            comment = organismName + " didn't sow new plant.";
            world.comments[world.commentsCounter++] = comment;
        }
        draw();
    }
            
    
}

