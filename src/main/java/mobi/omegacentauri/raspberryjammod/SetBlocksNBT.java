package mobi.omegacentauri.raspberryjammod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;

public class SetBlocksNBT extends SetBlocksState {
	NBTTagCompound nbt;
	
	public SetBlocksNBT(BlockPos corner1, BlockPos corner2, short id, short meta, NBTTagCompound nbt) {
		super(corner1, corner2, id, meta);
		this.nbt = nbt;
	}
	
	@Override
	public void execute(World world) {
		int y1 = pos.getY();
		int z1 = pos.getZ();
		IBlockState state = Block.getBlockById(id).getStateFromMeta(meta);
		
		for (int x = pos.getX() ; x <= x2 ; x++)
			for (int y = y1 ; y <= y2 ; y++)
				for (int z = z1 ; z <= z2 ; z++) {
					if (! RaspberryJamMod.active)
						break;

					BlockPos here = new BlockPos(x,y,z);
					
					world.setBlockState(here, state, 2);

					TileEntity tileEntity = world.getTileEntity(here);
					if (tileEntity != null) {
						nbt.setInteger("x", here.getX());
						nbt.setInteger("y", here.getY());
						nbt.setInteger("z", here.getZ());
						try {
							tileEntity.readFromNBT(nbt);
						}
						catch(Exception e){}
						tileEntity.markDirty();
					}
				}
		
	}
	
	@Override
	public boolean contains(int x, int y, int z) {
		return x <= x2 && y <= y2 && z <= z2 && pos.getX() <= x && pos.getY() <= y && pos.getZ() <= z;
	}

	@Override
	public String describe() {
		SetBlockNBT.scrubNBT(nbt);
		return super.describe()+nbt.toString();
	}
}