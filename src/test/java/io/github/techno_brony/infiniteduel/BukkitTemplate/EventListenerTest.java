/*
package io.github.techno_brony.infiniteduel.BukkitTemplate;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.plugin.Plugin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BlockBreakEvent.class)
public class EventListenerTest {

    @Test
    public void testOnBlockBreak() throws Exception {
        BlockBreakEvent mockEvent = PowerMockito.mock(BlockBreakEvent.class);
        Block mockBlock = mock(Block.class);
        Player mockPlayer = mock(Player.class);
        Main mockPlugin = mock(Main.class);
        EventListener listener = new EventListener(mockPlugin);

        when(mockEvent.getPlayer()).thenReturn(mockPlayer);
        when(mockEvent.getBlock()).thenReturn(mockBlock);
        when(mockBlock.getType()).thenReturn(Material.STONE);

        listener.onBlockBreak(mockEvent);

        verify(mockPlayer).sendMessage("You broke a block : STONE");
    }
}*/
