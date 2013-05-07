package fr.flv92.oc.tc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StringTranslate;
import org.lwjgl.input.Keyboard;

/**
 * @author Flv92
 */
public class GuiAddFriend extends GuiScreen {

    public Minecraft mc;
    private GuiScreen parentGui;
    private GuiTextField serverName;
    BufferedReader buffer = null;
    FileOutputStream fos = null;
    InputStream is = null;

    public GuiAddFriend(GuiScreen gui, Minecraft mineC) {
        parentGui = gui;
        mc = mineC;

    }

    public void updateScreen() {
        this.serverName.updateCursorCounter();
        ((GuiButton) this.buttonList.get(1)).enabled = this.serverName.getText().length() > 0 && !this.serverName.getText().contains(" ") && !doesPseudoAlreadyExist(this.serverName.getText());

    }

    public void drawScreen(int par1, int par2, float par3) {
        this.serverName.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }

    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 80 + 12, "Add"));
        this.serverName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 66, 200, 20);
        this.serverName.setFocused(true);
        ((GuiButton) this.buttonList.get(1)).enabled = this.serverName.getText().length() > 0 && !this.serverName.getText().contains(" ") && !doesPseudoAlreadyExist(this.serverName.getText());


    }

    public boolean doesPseudoAlreadyExist(String pseudo)
    {
        boolean retour = false;
        for(int i = 0; MainModClass.ocTcFriends[i][0] != null; i++)
        {
            if((MainModClass.ocTcFriends[i][0].toLowerCase()).equals(pseudo.toLowerCase()))
            {
                retour = true;
            }
        }
        return retour;
    }
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        if (par1GuiButton.id == 0)
        {
            this.mc.displayGuiScreen(parentGui);

        }
        if (par1GuiButton.id == 1)
        {

            this.downloadFileFromPseudo(this.serverName.getText());
            MainModClass.addPseudoToConfig(this.serverName.getText());
            boolean flag = false;
            int length = 0;
            for (int i = 0; MainModClass.ocTcFriends[i][0] != null; i++)
            {
                length++;
            }
            MainModClass.updateAllInfos();
            this.mc.displayGuiScreen(parentGui);
        }
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.serverName.mouseClicked(par1, par2, par3);
    }

    protected void keyTyped(char par1, int par2) {
        this.serverName.textboxKeyTyped(par1, par2);
    }

    public void downloadFileFromPseudo(String pseudo) {
        System.out.println("Trying to download skin head of " + pseudo);
        String location = Minecraft.getMinecraftDir().getAbsolutePath();
        location = location.substring(0, location.length() - MainModClass.debugOrRelease) + "/resources/skinsHead/" + pseudo + ".png";
        File image = new File(location);//this.mc.getAppDir("minecraft") + File.separator + "resources" + File.separator + "player.png");
        if (!image.exists())
        {
            try
            {

                URL url = new URL("https://minotar.net/helm/" + pseudo + "/16.png");
                URLConnection conn = url.openConnection();
                int FileLenght = conn.getContentLength();
                if (FileLenght == -1)
                {
                    throw new IOException("Erreur au téléchargement du skin de " + pseudo);
                }
                this.is = conn.getInputStream();
                this.buffer = new BufferedReader(new InputStreamReader(is));
                this.fos = new FileOutputStream(image);
                byte[] buff = new byte[1024];
                int l = this.is.read(buff);
                int percents = 0;

                while (l > 0)
                {
                    percents = (int) image.length() * 100 / FileLenght;
                    this.fos.write(buff, 0, l);
                    l = this.is.read(buff);
                }

            } catch (Exception e)
            {
            }
        }
    }
}
