package animals;

import java.util.Random;
import plants.Plant;
import project.World;

public class CyberSheep extends Animal
{
    
    public CyberSheep(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 11;
        this.initiative = 4;
        this.gridPresentation = 'C';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        CyberSheep tmp = new CyberSheep(world, positionY, positionX, "Cyber_sheep");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
    private int hogweedPositionY;
    private int hogweedPositionX;
    private double hogweedDistance;
    
    public void action()
    {
        comment = "";
        boolean hogweedFound = false;
        int direction = 0, typeOfCollision = 0, index = 0;
        int newPositionY = positionY, newPositionX = positionX;
        
        hogweedFound = findHogweed();
        
        if(hogweedFound)
        {
            comment = organismName + " moves towards the closes hogweed on (" + (hogweedPositionX+1) + "," + (hogweedPositionY+1)+"). ";
            
            if(hogweedPositionX>positionX)
                direction = RIGHT;
            else if (hogweedPositionX<positionX)
                direction = LEFT;
            else if (hogweedPositionY>positionY)
                direction = DOWN;
            else if (hogweedPositionY<positionY)
                direction = UP;
            
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
            
            else if (world.grid[newPositionY][newPositionX] == 'b')
            {
                typeOfCollision = MEET_POISON_PLANT;
                index = world.findOrganism(newPositionY, newPositionX);
                ((Plant)world.organisms[index]).collisionAttacked(this);     
            }
            
            else if (world.grid[newPositionY][newPositionX] == 'h')
            {
                typeOfCollision = MEET_EATABLE_PLANT;
                index = world.findOrganism(newPositionY, newPositionX);
                ((Plant)world.organisms[index]).collisionAttacked(this);
                typeOfCollision = EMPTY_CELL;
            }
            
            else
                    typeOfCollision = FIGHT;

            
            if(typeOfCollision == EMPTY_CELL || typeOfCollision == THE_SAME_SPACIES || typeOfCollision == FIGHT)
                collision(typeOfCollision, newPositionY, newPositionX);
                 
        }
        else                                                        //there is no hogweed and it behaves as normal sheep
        {
            super.action();
        }
    }
    
    //finding the nearest hogweed
    public boolean findHogweed()
    {
        hogweedPositionY=-1;
        hogweedPositionX=-1;
        hogweedDistance=0.0;
        
        double tmpDistance;
        
        for(int i=0; i<world.getDimensionY(); i++)
        {
            for(int j=0; j<world.getDimensionX(); j++)
            {
                if(world.grid[i][j]=='h')
                {
                    tmpDistance = Math.sqrt(((positionY-i)*(positionY-i))+((positionX-j)*(positionX-j)));
                    if(tmpDistance<hogweedDistance || hogweedDistance==0.0) 
                    {
                        hogweedDistance = tmpDistance;
                        hogweedPositionY=i;
                        hogweedPositionX=j;
                    }
                }
            }
        }
        
        if(hogweedDistance>0)
            return true;
        
        else return false;
    }
    
}

