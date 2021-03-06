package org.cyclops.integrateddynamics.block;

import net.minecraft.item.ItemBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.core.item.ItemBlockEnergyContainerAutoSupply;

/**
 * Config for {@link BlockCreativeEnergyBattery}.
 * @author rubensworks
 */
public class BlockCreativeEnergyBatteryConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static BlockCreativeEnergyBatteryConfig _instance;

    /**
     * Make a new instance.
     */
    public BlockCreativeEnergyBatteryConfig() {
        super(
            IntegratedDynamics._instance,
            true,
            "creative_energy_battery",
            null,
            BlockCreativeEnergyBattery.class
        );
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockEnergyContainerAutoSupply.class;
    }
}
