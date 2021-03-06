package net.theexceptionist.coherentvillages.main.entity.spells;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class SpellTransmuteFoot extends Spell{
	private Block toChange;
	private Block changeTo;
	private int radius;
	private boolean allBlocks;
	private BlockPos offset;
	private boolean snowLayer = false;
	
	public SpellTransmuteFoot(String name, int type, Block toChange, Block changeTo, int radius, boolean allBlocks) {
		super(name, type);
		
		this.toChange = toChange;
		this.changeTo = changeTo;
		this.radius = radius;
		this.allBlocks = allBlocks;
		//this.offset = offset;
		// TODO Auto-generated constructor stub
	}
	
	public SpellTransmuteFoot(String name, int type, Block toChange, Block changeTo, int radius, boolean allBlocks, boolean snow) {
		super(name, type);
		
		this.toChange = toChange;
		this.changeTo = changeTo;
		this.radius = radius;
		this.allBlocks = allBlocks;
		this.snowLayer = snow;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(EntityLivingBase caster) {
		BlockPos blockToChange = caster.getPosition().down();
		
		boolean standingOnBlock = false;
		boolean blockChanged = false;
		
		//System.out.println("Execute: "+this.name);
		
		if(caster.world.getBlockState(blockToChange).getBlock() == toChange) 
		{
			standingOnBlock = true;		
		}
		
		for(int x = -radius; x < radius; x++)
		{
			for(int z = -radius; z < radius; z++)
			{
				BlockPos currentBlock = blockToChange.add(x, 0, z);
				
				
				if(caster.world.getBlockState(currentBlock).getBlock() == toChange) 
				{
					if(snowLayer)
					{
						currentBlock = caster.getPosition();
						
						if(!Blocks.SNOW_LAYER.canPlaceBlockAt(caster.world, currentBlock))
						{
							return;
						}
					}
					
					caster.world.setBlockState(currentBlock, changeTo.getDefaultState());	
					blockChanged = true;
				}
				else if(standingOnBlock && allBlocks)
				{
					if(snowLayer)
					{
						currentBlock = caster.getPosition();
						
						if(!Blocks.SNOW_LAYER.canPlaceBlockAt(caster.world, currentBlock))
						{
							return;
						}
					}
					
					caster.world.setBlockState(currentBlock, changeTo.getDefaultState());	
					blockChanged = true;
				}
			}
		}
		
		/*if(blockChanged)
		{
			BlockPos topBlock = blockToChange.up();
			
			caster.setPosition(topBlock.getX(), topBlock.getY(), topBlock.getZ());
		}*/
		
		
	}

}
