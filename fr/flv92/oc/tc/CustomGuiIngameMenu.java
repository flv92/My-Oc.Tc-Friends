package fr.flv92.oc.tc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.stats.StatList;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class CustomGuiIngameMenu extends GuiScreen
{
    /** Also counts the number of updates, not certain as to why yet. */
    private int updateCounter2 = 0;

    /** Counts the number of screen updates. */
    private int updateCounter = 0;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.updateCounter2 = 0;
        this.buttonList.clear();
        byte b0 = -16;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + b0, StatCollector.translateToLocal("menu.returnToMenu")));

        if (!this.mc.isIntegratedServerRunning())
        {
            ((GuiButton)this.buttonList.get(0)).displayString = StatCollector.translateToLocal("menu.disconnect");
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + b0, StatCollector.translateToLocal("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + b0, 98, 20, StatCollector.translateToLocal("menu.options")));
        GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + b0, 98, 20, StatCollector.translateToLocal("menu.shareToLan")));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + b0, 98, 20, StatCollector.translateToLocal("gui.achievements")));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + b0, 98, 20, StatCollector.translateToLocal("gui.stats")));
        this.buttonList.add(new GuiButton(8, this.width / 2 - 100, this.height / 4 + 72 + b0, "My Oc.Tc Friends"));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                par1GuiButton.enabled = false;
                this.mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);
                this.mc.displayGuiScreen(new GuiMainMenu());
            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                this.mc.sndManager.resumeAllSounds();
                break;
            case 5:
                this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
                break;
            case 6:
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
                break;
            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            case 8:
                this.mc.displayGuiScreen(new CustomGuiOcTc(this));
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.updateCounter;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 16777215);
        super.drawScreen(par1, par2, par3);
    }
}