package org.cyclops.integrateddynamics.capability.dynamicredstone;

import net.minecraft.util.EnumFacing;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;
import org.cyclops.integrateddynamics.api.block.IDynamicRedstone;
import org.cyclops.integrateddynamics.core.tileentity.TileMultipartTicking;

/**
 * Default implementation of {@link IDynamicRedstone}.
 * @author rubensworks
 */
public class DynamicRedstoneTileMultipartTicking implements IDynamicRedstone {

    private final TileMultipartTicking tile;
    private final EnumFacing side;

    public DynamicRedstoneTileMultipartTicking(TileMultipartTicking tile, EnumFacing side) {
        this.tile = tile;
        this.side = side;
    }

    protected EnumFacingMap<Integer> getRedstoneLevels() {
        return tile.getRedstoneLevels();
    }

    protected EnumFacingMap<Boolean> getRedstoneInputs() {
        return tile.getRedstoneInputs();
    }

    @Override
    public void setRedstoneLevel(int level) {
        if(!tile.getWorld().isRemote) {
            EnumFacingMap<Integer> redstoneLevels = getRedstoneLevels();
            boolean sendUpdate = false;
            if(redstoneLevels.containsKey(side)) {
                if(redstoneLevels.get(side) != level) {
                    sendUpdate = true;
                    redstoneLevels.put(side, level);
                }
            } else {
                sendUpdate = true;
                redstoneLevels.put(side, level);
            }
            if(sendUpdate) {
                tile.updateRedstoneInfo(side);
            }
        }
    }

    @Override
    public int getRedstoneLevel() {
        EnumFacingMap<Integer> redstoneLevels = getRedstoneLevels();
        if(redstoneLevels.containsKey(side)) {
            return redstoneLevels.get(side);
        }
        return -1;
    }

    @Override
    public void setAllowRedstoneInput(boolean allow) {
        EnumFacingMap<Boolean> redstoneInputs = getRedstoneInputs();
        redstoneInputs.put(side, allow);
    }

    @Override
    public boolean isAllowRedstoneInput() {
        EnumFacingMap<Boolean> redstoneInputs = getRedstoneInputs();
        if(redstoneInputs.containsKey(side)) {
            return redstoneInputs.get(side);
        }
        return false;
    }
}