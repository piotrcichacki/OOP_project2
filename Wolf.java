package animals;

import project.World;

public class Wolf extends Animal
{
    
    public Wolf(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 9;
        this.initiative = 5;
        this.gridPresentation = 'W';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        Wolf tmp = new Wolf(world, positionY, positionX, "Wolf");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
}
