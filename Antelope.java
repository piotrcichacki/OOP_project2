package animals;

import java.util.Random;
import plants.Plant;
import project.World;

public class Antelope extends Animal
{
    
    public Antelope(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 4;
        this.initiative = 4;
        this.gridPresentation = 'A';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        Antelope tmp = new Antelope(world, positionY, positionX, "Antelope");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
    @Override
    public void action()
    {
        int direction = 0, rangeOfMovement = 1, typeOfCollision = 0;
	int newPositionY = positionY, newPositionX = positionX;
        boolean escape;
        int index; 
        Random rand = new Random();
        
        comment = "";
        
        do
	{
		direction = rand.nextInt(4) + 1;
		rangeOfMovement = rand.nextInt(2) + 1;					//antelope can move one or two field	
	} while (!newPositionInsideGrid(direction, rangeOfMovement));
        
        switch(direction)
        {
            case UP:
                newPositionY = positionY-rangeOfMovement;
                break;
            case RIGHT:
                newPositionX = positionX+rangeOfMovement;
                break;
            case DOWN:
                newPositionY = positionY+rangeOfMovement;
                break;
            case LEFT:
                newPositionX = positionX-rangeOfMovement;
                break;
        }
        
        escape = tryToEscape(false, newPositionY, newPositionX);            //whether antelope escapes from fight or not (if there is enemy)
        
        if(escape==false)
        {
                if (world.grid[newPositionY][newPositionX] == ' ')
                        typeOfCollision = EMPTY_CELL;
            
                else if (world.grid[newPositionY][newPositionX] == gridPresentation)
                        typeOfCollision = THE_SAME_SPACIES;
                
                else if (world.grid[newPositionY][newPositionX] == 'T')
                {
                    typeOfCollision = MEET_TURTLE;
                    index = world.findOrganism(newPositionY, newPositionX);
                    ((Turtle)world.organisms[index]).turtleAttacked(this, newPositionY, newPositionX);
                }
                
                else if (world.grid[newPositionY][newPositionX] == 's' || world.grid[newPositionY][newPositionX] == 't' || world.grid[newPositionY][newPositionX] == 'g')
                {
                    typeOfCollision = MEET_EATABLE_PLANT;
                    index = world.findOrganism(newPositionY, newPositionX);
                    ((Plant)world.organisms[index]).collisionAttacked(this);
                    typeOfCollision = EMPTY_CELL;
                }
                
                else if (world.grid[newPositionY][newPositionX] == 'b' || world.grid[newPositionY][newPositionX] == 'h')
                {
                    typeOfCollision = MEET_POISON_PLANT;
                    index = world.findOrganism(newPositionY, newPositionX);
                    ((Plant)world.organisms[index]).collisionAttacked(this);     
                }
                
                else
                        typeOfCollision = FIGHT;

                if(typeOfCollision == EMPTY_CELL || typeOfCollision == THE_SAME_SPACIES || typeOfCollision == FIGHT)
                    collision(typeOfCollision, newPositionY, newPositionX);
        }
                
    }
    
    //antelope has 50% chance to escape from fight with other animals
    public boolean tryToEscape(boolean attackted, int newPositionY, int newPositionX)
    {
        int escapeFromFight = 0, direction = 0;
        Random rand = new Random();
        boolean freeNeighboringCell = false;
	int freeCellPositionY, freeCellPositionX;
        
        //if she jumped to the cell occupied by other animal or if she was attacked
        if((!attackted && (world.grid[newPositionY][newPositionX] == 'W' || world.grid[newPositionY][newPositionX] == 'S' ||world.grid[newPositionY][newPositionX] == 'F' ||world.grid[newPositionY][newPositionX] == 'T' ||world.grid[newPositionY][newPositionX] == 'H'))||attackted)
            escapeFromFight = rand.nextInt(2) + 1;
        else
            return false;
        
        if(escapeFromFight == 1)    //antelope want to escape
        {
            freeNeighboringCell = spaceToEscape(newPositionY, newPositionX);
            if(!freeNeighboringCell)
                return false;
            else
            {
                freeCellPositionY = newPositionY;
                freeCellPositionX = newPositionX;

		while (world.grid[freeCellPositionY][freeCellPositionX] != ' ')
		{
                    freeCellPositionY = newPositionY;
                    freeCellPositionX = newPositionX;

                    do
                    {
			direction = rand.nextInt(4) + 1;
                    } while (!newPositionInsideGrid(direction));
                    
                    switch(direction)
                    {
                        case UP:
                            freeCellPositionY = newPositionY-1;
                            break;
                        case RIGHT:
                            freeCellPositionX = newPositionX+1;
                            break;
                        case DOWN:
                            freeCellPositionY = newPositionY+1;
                            break;
                        case LEFT:
                            freeCellPositionX = newPositionX-1;
                            break;
                    }
		}
                comment = organismName + " escaped from fight and moved from (" + (positionX+1) + "," + (positionY+1) + ") to ("+ (freeCellPositionX+1) + "," + (freeCellPositionY+1) + ").";
                world.comments[world.commentsCounter++] = comment;
                world.grid[positionY][positionX] = ' ';
                positionY = freeCellPositionY;
                positionX = freeCellPositionX;
                draw();
                return true;
            }
        } 
        return false;   //antelope didn't escape
    }
    
    public boolean spaceToEscape(int newPositionY, int newPositionX)
    {
        if (newPositionY > 0 && world.grid[newPositionY - 1][newPositionX] == ' ') return true;
	if (newPositionY < world.getDimensionY() - 1 && world.grid[newPositionY + 1][newPositionX] == ' ') return true;
	if (newPositionX > 0 && world.grid[newPositionY][newPositionX - 1] == ' ') return true;
	if (newPositionX < world.getDimensionX() - 1 && world.grid[newPositionY][newPositionX + 1] == ' ') return true;
	return false;
    }
    
    public boolean newPositionInsideGrid(int direction, int rangeOfMovement)
    {
        switch(direction)
        {
            case UP:
                return (positionY-rangeOfMovement>=0);
            case RIGHT:
                return (positionX+rangeOfMovement<world.getDimensionX());
            case DOWN:
                return (positionY+rangeOfMovement<world.getDimensionY());
            case LEFT:
                return (positionX-rangeOfMovement>=0);
            default:
                return false;
        }
    }
}



