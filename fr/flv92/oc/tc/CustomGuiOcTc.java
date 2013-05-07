package fr.flv92.oc.tc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StringTranslate;


@SideOnly(Side.CLIENT)
public class CustomGuiOcTc extends GuiScreen {

    /**
     * A reference to the screen object that created this. Used for navigating
     * between screens.
     */
    public GuiScreen parentScreen;
    /**
     * The title string that is displayed in the top-center of the screen.
     */
    protected String screenTitle = "Controls";
    /**
     * The ID of the button that has been pressed.
     */
    private int buttonId = -1;
    private CustomGuiControlsScrollPanel scrollPane;

    public CustomGuiOcTc(GuiScreen par1GuiScreen) {
        this.parentScreen = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {

        scrollPane = new CustomGuiControlsScrollPanel(this, mc);
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 28, stringtranslate.translateKey("gui.done")));
        this.buttonList.add(new GuiButton(201, this.width - 40, this.height - 28, 20, 20, "+"));
        //this.buttonList.add(new GuiButton(202, 10, this.height - 28, 70, 20, "Update skins"));
        scrollPane.registerScrollButtons(buttonList, 7, 8);
        this.screenTitle = "Oc.Tc Friends";
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton) {
        if (par1GuiButton.id == 200)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (par1GuiButton.id == 201)
        {
            this.mc.displayGuiScreen(new GuiAddFriend(this, this.mc));
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        scrollPane.drawScreen(par1, par2, par3);
        drawCenteredString(fontRenderer, screenTitle, width / 2, 4, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
