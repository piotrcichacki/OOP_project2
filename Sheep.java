package animals;

import project.World;

public class Sheep extends Animal
{
    
    public Sheep(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 4;
        this.initiative = 4;
        this.gridPresentation = 'S';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        Sheep tmp = new Sheep(world, positionY, positionX, "Sheep");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
}

