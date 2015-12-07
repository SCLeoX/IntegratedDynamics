package org.cyclops.integrateddynamics.core.block.cable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.core.path.CablePathElement;
import org.cyclops.integrateddynamics.core.tileentity.ITileCableFacadeable;

import javax.annotation.Nullable;

/**
 * A component for {@link ICableFacadeable}.
 * @author rubensworks
 */
public class CableNetworkFacadeableComponent<C extends Block & ICableNetwork<CablePathElement>> extends CableNetworkComponent<C> implements ICableFacadeable<CablePathElement> {

    public CableNetworkFacadeableComponent(C cable) {
        super(cable);
    }

    @Override
    public boolean hasFacade(IBlockAccess world, BlockPos pos) {
        ITileCableFacadeable tile = TileHelpers.getSafeTile(world, pos, ITileCableFacadeable.class);
        if(tile != null) {
            return tile.hasFacade();
        }
        return false;
    }

    @Override
    public IBlockState getFacade(World world, BlockPos pos) {
        ITileCableFacadeable tile = TileHelpers.getSafeTile(world, pos, ITileCableFacadeable.class);
        if(tile != null) {
            return tile.getFacade();
        }
        return null;
    }

    @Override
    public void setFacade(World world, BlockPos pos, @Nullable IBlockState blockState) {
        ITileCableFacadeable tile = TileHelpers.getSafeTile(world, pos, ITileCableFacadeable.class);
        if(tile != null) {
            tile.setFacade(blockState);
        }
    }

}