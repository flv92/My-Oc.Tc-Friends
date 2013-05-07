package fr.flv92.oc.tc;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author Flv92
 */
public class MenuTickHandler implements ITickHandler {

    public MenuTickHandler() {
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (type.equals(EnumSet.of(TickType.CLIENT)))
        {
            GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
            if (guiscreen instanceof GuiIngameMenu)
            {
                Minecraft.getMinecraft().displayGuiScreen(new CustomGuiIngameMenu());
            }
            if (guiscreen instanceof GuiCrafting)
            {
                ItemStack is = (ItemStack) ((GuiCrafting) guiscreen).inventorySlots.inventoryItemStacks.get(1);
                if (is != null)
                {
                    if (is.itemID == Block.blockSteel.blockID)
                    {
                        System.out.println("sdf");
                        Slot s = (Slot) ((GuiCrafting) guiscreen).inventorySlots.inventorySlots.get(18);
                        FMLClientHandler.instance().getClient().playerController.windowClick(((GuiCrafting) guiscreen).inventorySlots.windowId, s.slotNumber, s.xDisplayPosition, s.yDisplayPosition, FMLClientHandler.instance().getClient().thePlayer);
                        Slot s2 = (Slot) ((GuiCrafting) guiscreen).inventorySlots.inventorySlots.get(5);
                        FMLClientHandler.instance().getClient().playerController.windowClick(((GuiCrafting) guiscreen).inventorySlots.windowId, s2.slotNumber, s2.xDisplayPosition, s2.yDisplayPosition, FMLClientHandler.instance().getClient().thePlayer);

                    }
                } else
                {
                }
            }

        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return null;
    }
}
