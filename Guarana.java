package plants;

import project.Organism;
import project.World;

public class Guarana extends Plant
{
    
    public Guarana(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 0;
        this.initiative = 0;
        this.gridPresentation = 'g';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        Guarana tmp = new Guarana(world, positionY, positionX, "Guarana");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
    
    
    //animal which ate guarana, will have his strength increased by 3
    @Override
    public void collisionAttacked(Organism attacker)
    {
        attacker.comment = attacker.getOrganismName() + " ate " + organismName + " and moved on its position (" + (positionX + 1) + "," + (positionY + 1) + ")." + " His strength permanently increased by 3.";
        attacker.setStrength(attacker.getStrength()+3);
        world.deleted[world.deletedCounter++] = indexAge;
        world.grid[positionY][positionX] = ' ';
    }
    
}

