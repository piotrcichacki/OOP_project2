
package plants;

import java.util.Random;
import project.Organism;
import project.World;


public abstract class Plant extends Organism
{

    public static final int SOW_PROPABILITY = 25;
    
    public Plant(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName); 
    }
    
    @Override
    public void action() 
    {
        int sowing, direction;
	int newPlantPositionY = positionY, newPlantPositionX = positionX;
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

		comment = organismName + " sowned new plant.";
		world.comments[world.commentsCounter++] = comment;
                addOrganism(world, newPlantPositionY, newPlantPositionX);
            }
            else
            {
			comment = organismName + " didn't sow new plant.";
                        world.comments[world.commentsCounter++] = comment;
            }
	}
	else
	{
		comment = organismName + " didn't sow new plant.";
                world.comments[world.commentsCounter++] = comment;
	}
	draw();
    } 
    
    @Override
    public void collision(int typeOfCollision, int newPositionY, int newPositionX)
    {
        //plants don't move so they will never cause collision, that's why this function is empty
    }
    
    public void collisionAttacked(Organism attacker)
    {
        attacker.comment = attacker.getOrganismName() + " ate " + organismName + " and moved on its position (" + (positionX + 1) + "," + (positionY + 1) + ").";
        world.deleted[world.deletedCounter++] = indexAge;
        world.grid[positionY][positionX] = ' ';
    }
            
}



