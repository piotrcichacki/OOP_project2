package animals;

import project.Organism;
import project.World;
import java.util.Random;
import plants.Plant;

public abstract class Animal extends Organism
{
    public Animal(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName); 
    }
    
    //default for animals
    @Override
    public void action()
    {
        int direction = 0, typeOfCollision = 0, index = 0;
        int newPositionY = positionY, newPositionX = positionX;
        Random rand = new Random();
        
        comment = "";
        
        //animal moves to  randomly selected neighboring field
        do
        {
            direction = rand.nextInt(4)+1;
        } while(!newPositionInsideGrid(direction));
        
        switch(direction)
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
        
        //cell is not occupied by other organism
        if (world.grid[newPositionY][newPositionX] == ' ')
		typeOfCollision = EMPTY_CELL;
        
        //cell is occupied by the same spacies and instead of fighting with each other, both animals remain in their original positions, and next to them a new animal of their species is created.

        else if (world.grid[newPositionY][newPositionX] == gridPresentation)
		typeOfCollision = THE_SAME_SPACIES;
        
        //cell is occupied by turtle
        else if (world.grid[newPositionY][newPositionX] == 'T')
        {
            typeOfCollision = MEET_TURTLE;
            index = world.findOrganism(newPositionY, newPositionX);                                 //finding this turtle in the array of organisms
            ((Turtle)world.organisms[index]).turtleAttacked(this, newPositionY, newPositionX);      //calling special method from Turtle class
        }
        
        //cell is occupied by antelope
        else if (world.grid[newPositionY][newPositionX] == 'A')
        {
            typeOfCollision = MEET_ANTELOPE;
            boolean antelopeEscaped;
            index = world.findOrganism(newPositionY, newPositionX);                                 //finding this antelope in the array of organisms
            //calling special method from Antelope class and checking if she escape from fight or not
            antelopeEscaped = ((Antelope)world.organisms[index]).tryToEscape(true, world.organisms[index].getPositionY(), world.organisms[index].getPositionX());
            if(antelopeEscaped) 
            {
                typeOfCollision = EMPTY_CELL;
            }
            else typeOfCollision = FIGHT;
        }
        
        //cell is occupied by eatable plant
        else if (world.grid[newPositionY][newPositionX] == 's' || world.grid[newPositionY][newPositionX] == 't' || world.grid[newPositionY][newPositionX] == 'g')
        {
            typeOfCollision = MEET_EATABLE_PLANT;
            index = world.findOrganism(newPositionY, newPositionX);
            ((Plant)world.organisms[index]).collisionAttacked(this);            //plant is eaten by organism
            typeOfCollision = EMPTY_CELL;
        }
        
        //cell is occupied by plant which will kill animal
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
    
    @Override
    public void collision(int typeOfCollision, int newPositionY, int newPositionX)
    {
        Random rand = new Random();
        int direction = 0, index = 0;
	int newAnimalPositionY = positionY, newAnimalPositionX = positionX;
        
        switch(typeOfCollision)
        {
            case EMPTY_CELL:
                if(comment=="")
                    comment = (organismName + " moved from (" + (positionX+1) + "," + (positionY+1) + ") to ("+ (newPositionX+1) + "," + (newPositionY+1) + ").");
                world.grid[positionY][positionX] = ' ';
                positionY = newPositionY;
                positionX = newPositionX;
                draw();
                world.comments[world.commentsCounter++] = comment;
                break;
            case THE_SAME_SPACIES:
                comment = (organismName + " met the same spacies at ("+ (newPositionX+1) + "," + (newPositionY+1) + "). ");
                if(spaceForNewOrganism())
                {
                    while (world.grid[newAnimalPositionY][newAnimalPositionX] != ' ')
                    {
			newAnimalPositionY = positionY;
			newAnimalPositionX = positionX;

                        do
                        {
                            direction = rand.nextInt(4)+1;
                        } while(!newPositionInsideGrid(direction));

                        switch(direction)
                        {
                        case UP:
                            newAnimalPositionY = positionY-1;
                            break;
                        case RIGHT:
                            newAnimalPositionX = positionX+1;
                            break;
                        case DOWN:
                            newAnimalPositionY = positionY+1;
                            break;
                        case LEFT:
                            newAnimalPositionX = positionX-1;
                            break;
                        }
                    }
                    comment += ("New animal at ("+ (newAnimalPositionX+1) + "," + (newAnimalPositionY+1) + ") was created.");
                    world.comments[world.commentsCounter++] = comment;
                    addOrganism(world, newAnimalPositionY, newAnimalPositionX);
                }
                else
                {
                    world.comments[world.commentsCounter++] = comment;
                }
                draw();
                break;
            case FIGHT:
                index = world.findOrganism(newPositionY, newPositionX);
                comment = (organismName + " met " + world.organisms[index].getOrganismName() + ". There will be a fight!   ");
                if(strength >= world.organisms[index].getStrength())
                {
                    comment += (organismName + " won the fight! " + world.organisms[index].getOrganismName() + " is dead!");
                    world.deleted[world.deletedCounter++] = index;
                    world.grid[positionY][positionX] = ' ';
                    positionY = newPositionY;
                    positionX = newPositionX;
                    draw();
                }
                else
                {
                    comment += (world.organisms[index].getOrganismName() + " won the fight! " + organismName + " is dead!");
                    world.grid[positionY][positionX] = ' ';
                    world.deleted[world.deletedCounter++] = indexAge;             
                }
                world.comments[world.commentsCounter++] = comment;
                break;
        }
    }
}
