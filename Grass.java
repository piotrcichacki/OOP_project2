package plants;

import project.World;

public class Grass extends Plant
{
    
    public Grass(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 0;
        this.initiative = 0;
        this.gridPresentation = 's';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        Grass tmp = new Grass(world, positionY, positionX, "Grass");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
}

