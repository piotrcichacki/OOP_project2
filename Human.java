package animals;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import plants.Plant;
import project.World;


public class Human extends Animal
{
    public boolean activatedSpecialAbility;
    public int turnActivated;
    public int turnDisactivated;
    
    public Human(World world, int positionY, int positionX, String organismName) 
    {
        super(world, positionY, positionX, organismName);
        this.turnActivated = 0;
        this.turnDisactivated = 0;
        this.activatedSpecialAbility = false;
        this.strength = 5;
        this.initiative = 4;
        this.gridPresentation = 'H';
        world.grid[positionY][positionX] = gridPresentation;
        world.humanExist = true;
    }

    @Override
    public void draw() 
    {
        world.grid[positionY][positionX] = gridPresentation;
    }
    
    public int getTurnActivated()
    {
        return turnActivated;
    }
    public int getTurnDisactivated()
    {
        return turnDisactivated;
    }
    public boolean activatedAbility()
    {
        return activatedSpecialAbility;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) 
    {
    //there won't be another human so empty function
    }  
    
   
    
    @Override
    public void action()
    {
        comment = "";
        int typeOfCollision = 0, index;
        int newPositionY = positionY, newPositionX = positionX;
        
        if(activatedSpecialAbility)
        {
            purification();
        }
        else if (!activatedSpecialAbility && turnDisactivated > 0)
	{
		turnDisactivated--;
	}
        if (activatedSpecialAbility)
	{
		turnActivated--;
	}
       
        switch(world.directionOfHumanMovement)
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
        
        if(activatedSpecialAbility)
        {
            purification();
        }
        
        if (turnActivated == 0 && activatedSpecialAbility == true)						//end of special ability
	{
		activatedSpecialAbility = false;
		turnDisactivated = 5;
        }
    }
    
     private void purification()
    {
        int newPositionY=positionY, newPositionX=positionX, index;
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
		    if (world.grid[newPositionY][newPositionX] != ' ') //checks whether in neighboring cell is animal or plant
		    {
			index = world.findOrganism(newPositionY, newPositionX);
			if (world.organisms[index].getGridPresentation() <= 'Z' && world.organisms[index].getGridPresentation() >= 'A')
				comment += organismName + " killed " + world.organisms[index].getOrganismName() + ". ";
			else
				comment += organismName + " ate " + world.organisms[index].getOrganismName() + ". ";
                        
			world.grid[newPositionY][newPositionX] = ' ';
			world.deleteOrganism(index);
		    }
		}
            }
    }
    
}
