package net.haraxx.preventblockspawn;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class PreventBlockSpawnPlugin extends JavaPlugin implements Listener {
    private HashMap<Material, Material> preventReplacementMap;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        preventReplacementMap = new HashMap<>();

        for (String materialUnparsed : getConfig().getConfigurationSection("prevent-generation").getKeys(false)) {
            preventReplacementMap.put(
                    Material.getMaterial(materialUnparsed),
                    Material.getMaterial(
                            getConfig().getString("prevent-generation." + materialUnparsed + ".replacement-material")
                    )
            );
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onChunkLoad(ChunkPopulateEvent chunkPopulateEvent) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunkPopulateEvent.getWorld().getMinHeight(); y < chunkPopulateEvent.getWorld().getMaxHeight(); y++) {
                    Block block = chunkPopulateEvent.getChunk().getBlock(x, y, z);
                    if (preventReplacementMap.keySet().contains(block.getType())) {
                        block.setType(preventReplacementMap.get(block.getType()));
                    }
                }
            }
        }
    }
}
