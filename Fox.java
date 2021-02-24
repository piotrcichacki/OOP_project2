package animals;

import java.util.Random;
import plants.Plant;
import project.World;

public class Fox extends Animal
{
    
    public Fox(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 3;
        this.initiative = 7;
        this.gridPresentation = 'F';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        Fox tmp = new Fox(world, positionY, positionX, "Fox");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
    //fox will never move to a cell occupied by a stronger organism
    @Override
    public void action()
    {
        int direction=0, typeOfCollision=0, index, enemyStrength;
        int newPositionY = positionY, newPositionX = positionX;
        Random rand = new Random();
        
        comment = "";
        
        if(spaceToMove())                                                       //check if in one of the neighboring cells is weaker organism or is empty
        {
            do															//looking for empty cell or cell occupied by weaker organism
            {
		newPositionY = positionY;
		newPositionX = positionX;
        
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
                
		if (world.grid[newPositionY][newPositionX] == ' ')
                    enemyStrength = 0;
		else
		{
                    index = world.findOrganism(newPositionY, newPositionX);
			enemyStrength = world.organisms[index].getStrength();
		}
            } while (strength < enemyStrength);


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

                if(typeOfCollision == EMPTY_CELL || typeOfCollision == THE_SAME_SPACIES || typeOfCollision == FIGHT)
                    collision(typeOfCollision, newPositionY, newPositionX);
        }
        else
        {
            comment = organismName + " sensed stronger organisms next to him and stayed on the same position.";
            world.comments[world.commentsCounter++] = comment;
            draw();
        }
            
    }
    
   
    //spaceToMove function checks whether there is place to move for fox where is weaker organism than fox
    private boolean spaceToMove()
    {
        int index;
        boolean isThereSpace = false;
        
        if (isThereSpace == false && positionY > 0 && world.grid[positionY - 1][positionX] == ' ')
		isThereSpace = true;
	else if (isThereSpace == false && positionY > 0)
	{
		index = world.findOrganism(positionY - 1, positionX);
		if (strength >= world.organisms[index].getStrength())
			isThereSpace = true;
	}
	if (isThereSpace == false && positionY < world.getDimensionY() - 1 && world.grid[positionY + 1][positionX] == ' ')
		isThereSpace = true;
	else if (isThereSpace == false && positionY < world.getDimensionY() - 1)
	{
		index = world.findOrganism(positionY + 1, positionX);
		if (strength >= world.organisms[index].getStrength())
			isThereSpace = true;
	}
	if (isThereSpace == false && positionX > 0 && world.grid[positionY][positionX - 1] == ' ')
		isThereSpace = true;
	else if (isThereSpace == false && positionX > 0)
	{
		index = world.findOrganism(positionY, positionX - 1);
		if (strength >= world.organisms[index].getStrength())
			isThereSpace = true;
	}
	if (isThereSpace == false && positionX < world.getDimensionX() - 1 && world.grid[positionY][positionX + 1] == ' ')
		isThereSpace = true;
	else if (isThereSpace == false && positionX < world.getDimensionX() - 1)
	{
		index = world.findOrganism(positionY, positionX + 1);
		if (strength >= world.organisms[index].getStrength())
			isThereSpace = true;
	}
	return isThereSpace;
        
    }
    
}
