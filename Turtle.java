package animals;

import java.util.Random;
import plants.Plant;
import project.Organism;
import project.World;

public class Turtle extends Animal
{
    
    public Turtle(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 2;
        this.initiative = 1;
        this.gridPresentation = 'T';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        Turtle tmp = new Turtle(world, positionY, positionX, "Turtle");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
    //has 75% chance to stay in the same position
    @Override
    public void action()
    {
        int stayInTheSamePlace, index;
	int direction = 0, typeOfCollision = 0;
	int newPositionY = positionY, newPositionX = positionX;
        Random rand = new Random();

        comment = "";
        
	stayInTheSamePlace = rand.nextInt(4);	//randomize whether turtle perform move or not
        
        if (stayInTheSamePlace == 0)		//turtle perform move (one of four possible values = 25%)
	{
            do
            {
		direction = rand.nextInt(4) + 1;
            } while (!newPositionInsideGrid(direction));

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
            
            if (world.grid[newPositionY][newPositionX] == ' ')
		typeOfCollision = EMPTY_CELL;
            
            else if (world.grid[newPositionY][newPositionX] == gridPresentation)
		typeOfCollision = THE_SAME_SPACIES;
            
            else if (world.grid[newPositionY][newPositionX] == 'A')
            {
                typeOfCollision = MEET_ANTELOPE;
                boolean antelopeEscaped;
                index = world.findOrganism(newPositionY, newPositionX);
                antelopeEscaped = ((Antelope)world.organisms[index]).tryToEscape(true, world.organisms[index].getPositionY(), world.organisms[index].getPositionX());
                if(antelopeEscaped) 
                {
                    typeOfCollision = EMPTY_CELL;
                }
                else typeOfCollision = FIGHT;
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

            collision(typeOfCollision, newPositionY, newPositionX);
	}
	else    //turtle stayed on the same position
	{
		comment = organismName + " was to lazy to move and stayed on the same position.";
                world.comments[world.commentsCounter++] = comment;
		draw();
	}
    }
    
    //Turtle reflects attacks of animal with strength less than 5. Attacker will return to the previous cell.
    public void turtleAttacked(Organism attacker, int newPositionY, int newPositionX)
    {
        //Turtle reflects attack of animal
        if(attacker.getStrength() < 5)
        {
            attacker.comment = organismName + " reflected attack of " + attacker.getOrganismName();
            world.comments[world.commentsCounter++] = attacker.comment;
            attacker.draw();
        }
        //Turtle does not reflect attack of animal so there will be a fight
        else 
        {
            attacker.collision(FIGHT, newPositionY, newPositionX);
        }
    }
}

