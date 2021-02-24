package project;

import project.World;

public abstract class Organism 
{
    //direction of movement
    protected static final int UP = 1;
    protected static final int RIGHT = 2;
    protected static final int DOWN = 3;
    protected static final int LEFT = 4;
    
    //cases of collisions
    protected static final int EMPTY_CELL = 1;
    protected static final int THE_SAME_SPACIES = 2;
    protected static final int FIGHT = 3;
    protected static final int MEET_TURTLE = 4;
    protected static final int MEET_ANTELOPE = 5;
    protected static final int MEET_EATABLE_PLANT = 6;
    protected static final int MEET_POISON_PLANT = 7;
  
    protected int strength, initiative;
    protected int positionY, positionX, indexAge;
    protected char gridPresentation;
    protected String organismName;
    public String comment;
    protected World world;
    
    
    public Organism(World world, int positionY, int positionX, String organismName)
    {
        this.positionY = positionY;
        this.positionX = positionX;
        this.organismName = organismName;
        this.world = world;
        this.indexAge = world.numberOfOrganism;
        this.comment = organismName+" has been just created at (" + (positionX+1) + "," + (positionY+1)+").";
        world.numberOfOrganism++;
        world.comments[world.commentsCounter++] = this.comment;    
    }
   
    public abstract void action();
    public abstract void collision(int typeOfCollision, int newPositionY, int newPositionX);
    public abstract void draw();
    public abstract void addOrganism(World world, int positionY, int positionX);
    
    public int getStrength() {
        return strength;
    }
    public int getInitiative() {
        return initiative;
    }
    public int getPositionY() {
        return positionY;
    }
    public int getPositionX() {
        return positionX;
    }
    public int getIndexAge() {
        return indexAge;
    }
    public String getOrganismName() {
	return organismName;
    }
    public String getComment(){
        return comment;
    }
    public char getGridPresentation() {
	return gridPresentation;
    }
    public void setPositionY(int positionY) {
	this.positionY = positionY;
    }
    public void setPositionX(int positionX) {
	this.positionX = positionX;
    }
    public void setIndexAge(int indexAge) {
        this.indexAge = indexAge;
    }
    public void setStrength(int strength) {
	this.strength = strength;
    }
    public void setInitiative(int initiative) {
	this.initiative = initiative;
    }
    
    //newPositionInsideGrid function checks whether possible new coordinates are inside grid
    protected boolean newPositionInsideGrid(int direction)
    {
        switch(direction)
        {
            case UP:
                return (positionY-1>=0);
            case RIGHT:
                return (positionX+1<world.getDimensionX());
            case DOWN:
                return (positionY+1<world.getDimensionY());
            case LEFT:
                return (positionX-1>=0);
            default:
                return false;
        }
    }
    
    //spaceForNewOrganism function checks whether it is possibility to create new organism next to organism
    //if one of the neighboring cells is empty, then function return true
    protected boolean spaceForNewOrganism()
    {
        if (positionY > 0 && world.grid[positionY - 1][positionX] == ' ') return true;
	if (positionY < world.getDimensionY() - 1 && world.grid[positionY + 1][positionX] == ' ') return true;
	if (positionX > 0 && world.grid[positionY][positionX - 1] == ' ') return true;
	if (positionX < world.getDimensionX() - 1 && world.grid[positionY][positionX + 1] == ' ') return true;
	return false;
    }
    
    @Override
    public String toString()
    {
        return organismName + " " + gridPresentation + " " + positionY + " " + positionX + " " + strength + " " + initiative;       
    }
}
