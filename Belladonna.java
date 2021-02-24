package plants;

import project.Organism;
import project.World;

public class Belladonna extends Plant
{
    
    public Belladonna(World world, int positionY, int positionX, String organismName)
    {
        super(world, positionY, positionX, organismName);
        this.strength = 99;
        this.initiative = 0;
        this.gridPresentation = 'b';
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void draw() {
        world.grid[positionY][positionX] = gridPresentation;
    }

    @Override
    public void addOrganism(World world, int positionY, int positionX) {
        Belladonna tmp = new Belladonna(world, positionY, positionX, "Belladonna");
        world.organisms[world.numberOfOrganism-1] = tmp;
    }
       
    //Kills any animal which eats it
    @Override
    public void collisionAttacked(Organism attacker)
    {
        attacker.comment = attacker.getOrganismName() + " ate " + organismName + " and died";
        world.comments[world.commentsCounter++] = attacker.comment;
        world.deleted[world.deletedCounter++] = indexAge;
        world.grid[positionY][positionX] = ' ';
        world.deleted[world.deletedCounter++] = attacker.getIndexAge();
        world.grid[attacker.getPositionY()][attacker.getPositionX()] = ' ';
    }
    
}

