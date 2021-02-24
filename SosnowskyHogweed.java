package plants;

import animals.CyberSheep;
import project.Organism;
import project.World;

public class SosnowskyHogweed extends Plant
{
    
    public SosnowskyHogweed(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 10;
        this.initiative = 0;
        this.gridPresentation = 'h';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        SosnowskyHogweed tmp = new SosnowskyHogweed(world, positionY, positionX, "Sosnowsky_hogweed");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
    //Kills every animal in its immediate neighborhood except cyber-sheep.
    @Override
    public void action()
    {
        int index;
	int newPositionY = positionY, newPositionX = positionX;
	boolean killAnimal = false;

        comment = "";
        
	for (int i = 1; i <= 4; i++)	//go through 4 neighboring cells (up, left, right, down)
	{
            newPositionY = positionY;
            newPositionX = positionX;
            index = 0;

            if (newPositionInsideGrid(i))
            {
                switch(i)
                {
                    case UP:
                        newPositionY = positionY-1;
                        break;
                    case RIGHT:
                        newPositionX = positionX+1;
                        break;
                    case DOWN:
                        newPositionY = positionY+1;
                        break;
                    case LEFT:
                        newPositionX = positionX-1;
                        break;
                }
       
                if (world.grid[newPositionY][newPositionX] == 'W' || world.grid[newPositionY][newPositionX] == 'S' || world.grid[newPositionY][newPositionX] == 'F' || world.grid[newPositionY][newPositionX] == 'A' || world.grid[newPositionY][newPositionX] == 'T' || world.grid[newPositionY][newPositionX] == 'H') //checks whether in neighboring cell is animal
                {
                    index = world.findOrganism(newPositionY, newPositionX);
                    comment += organismName + " killed " + world.organisms[index].getOrganismName() + ". ";
                    world.grid[newPositionY][newPositionX] = ' ';
                    world.deleted[world.deletedCounter++] = index;
                    killAnimal = true;
                }
            }
	}

	if (!killAnimal)
	{
		comment = organismName + " didn't kill any neighboring animal";
	}
        world.comments[world.commentsCounter++] = comment;
	draw();
    }
    
    //Kills any animal which eats it, apart from cyber-sheep
    @Override
    public void collisionAttacked(Organism attacker)
    {
        if(attacker instanceof CyberSheep)
        {
            attacker.comment = attacker.getOrganismName() + " ate " + organismName + ". ";
            world.deleted[world.deletedCounter++] = indexAge;
            world.grid[positionY][positionX] = ' ';
        }
        else
        {
        attacker.comment = attacker.getOrganismName() + " ate " + organismName + " and died";
        world.comments[world.commentsCounter++] = attacker.comment;
        world.deleted[world.deletedCounter++] = indexAge;
        world.grid[positionY][positionX] = ' ';
        world.deleted[world.deletedCounter++] = attacker.getIndexAge();
        world.grid[attacker.getPositionY()][attacker.getPositionX()] = ' ';
        }
    }
    
}

