package fr.flv92.oc.tc;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CustomGuiControlsScrollPanel extends GuiSlot {

    private CustomGuiOcTc controls;
    private Minecraft mc;
    private String[] message;
    private int _mouseX;
    private int _mouseY;
    private int selected = -1;
    BufferedReader buffer = null;
    FileOutputStream fos = null;
    InputStream is = null;
    private ImageLoader imgLoader;
    private BufferedImage buffered;

    public CustomGuiControlsScrollPanel(CustomGuiOcTc controls, Minecraft mc1) {
        super(mc1, controls.width, controls.height, 16, (controls.height - 32) + 4, 25);
        mc = mc1;
        this.imgLoader = new ImageLoader(this.mc.texturePackList, this.mc.gameSettings);
        this.controls = controls;
    }

    @Override
    protected int getSize() {
        int length = 0;
        for (int i = 0; MainModClass.ocTcFriends[i][0] != null; i++)
        {
            length++;
        }
        return length + 1;// + 2;
    }

    @Override
    protected void elementClicked(int i, boolean flag) {
        if (!flag)
        {
            if (selected == -1)
            {
                selected = i;
            } else
            {
            }
        }
    }

    @Override
    protected boolean isSelected(int i) {
        return false;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int index, int xPosition, int yPosition, int l, Tessellator tessellator) {

        if (index == 0)
        {
            return;
        }
        index--;
        /*int realLength = 0;
         for (int j = 0; j < MainModClass.ocTcFriends.length; j++)
         {
         if (MainModClass.ocTcFriends[j] != null)
         {
         realLength++;
         }
         }
         if(index > realLength - 1)
         {
         return;
         }*/

        int width = 70;
        int height = 20;
        xPosition -= 100;

        int skinWidth = 20;
        int skinHeight = 20;
        //Drawing some buttons
        boolean flag = _mouseX >= (-30 + 38) && _mouseY >= yPosition - 2 && _mouseX < (-30 + 38) + 20 && _mouseY < yPosition - 2 +20;
        int k = (flag ? 2 : 1);

        mc.renderEngine.bindTexture("/gui/gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //controls.drawTexturedModalRect(xPosition + 3, yPosition - 4, 0, 46 + k * 20, width / 2, height + 1);
        //controls.drawTexturedModalRect(xPosition + width / 2 + 3, yPosition - 4, 200 - width / 2, 46 + k * 20, width / 2, height + 1);

        //Drawing player name in gui!
        controls.drawString(mc.fontRenderer, MainModClass.ocTcFriends[index][0], -40 + width + 4, yPosition + 6, "1".equals(MainModClass.ocTcFriends[index][1]) ? 0xFFFFFF55 : 0xFFFFFFFF);

        //Drawing inventory slot behind skin Head
        mc.renderEngine.bindTexture("/gui/gui.png");
        this.drawTexturedModalRectForBackground((-30 + 38), yPosition - 2, 1, 1, 20, 20);

        //Drawing skin Head of the player
        String location = Minecraft.getMinecraftDir().getAbsolutePath();
        location = location.substring(0, location.length() - MainModClass.debugOrRelease) + "/resources/skinsHead/" + MainModClass.ocTcFriends[index][0] + ".png";
        File image = new File(location);
        Image img = null;
        try
        {
            img = ImageIO.read(image);
        } catch (IOException e)
        {
            e.printStackTrace();
        }//et on charge notre image dans un buffer... oui je sais tu es perdu mais sa fonctionne comme ça ;)
        this.buffered = (BufferedImage) img;
        if(buffered != null)
        {
            this.drawSprite(-30, yPosition, 16, 16);
        }


        //Drawing information (Online(green), Seen x [time] ago(White), Error occured(Red)
        //String[] infosForPseudo = MainModClass.getOcTcInfosForPlayer(MainModClass.ocTcFriends[index]);
        controls.drawString(mc.fontRenderer, MainModClass.ocTcInfos[index][1], -40 + width + 4 + 75, yPosition + 6, "2".equals(MainModClass.ocTcInfos[index][0]) ? 0xFFFF555F : "1".equals(MainModClass.ocTcInfos[index][0]) ? 0xFFFFFFFF : "0".equals(MainModClass.ocTcInfos[index][0]) ? 0xF55FFF55 : 0xFFFFFF55);

        if (flag)
        {
            /*this.mc.renderEngine.bindTexture("/gui/beacon.png");
             this.drawTexturedModalRect(_mouseX - 5, _mouseY - 20, 130, 60, 48, 15);
             controls.drawString(mc.fontRenderer, "Remove", _mouseX, _mouseY - 15, 0xFFFFFFFF);*/
            int var8 = this.mc.fontRenderer.getStringWidth("Edit");
            drawGradientRect(_mouseX - 5, _mouseY - 20, _mouseX + 5 + var8, _mouseY - 2, -1073741824, -1073741824);
            this.mc.fontRenderer.drawStringWithShadow("Edit", _mouseX, _mouseY - 15, -1);
        }
        if (flag && Mouse.isButtonDown(0))
        {
            this.mc.displayGuiScreen(new CustomGuiEditingFriend(controls, MainModClass.ocTcFriends[index][0], index));
        }
    }

    public void drawSprite(int par1, int par2, int par3, int par4) {
        // début ajout
        this.imgLoader.setupTexture(this.buffered, 20, 16, 16);// on charge l'image à partit du buffer (20 est le numéro de texture, 64 et 32 car la texture d'un skin fait 64/32 pixels

        GL11.glPushMatrix(); // on ouvre une matrice pour que les changements suivants n'affectent pas le reste du gui
        GL11.glScalef(1F / 16F, 1F / 16F, 1F);// ici j'ai du redimentionner l'image car avec ma class elle est déformée
        GL11.glTranslatef((par1 + 40) * 16, (par2) * 16, 0);// ici tu changera les 2 premier paramètres (position finale en x et y de l'image sur le gui)
        GL11.glColor4f(1, 1, 1, 1); // on applique une lumière blanche sur l'image
        this.drawTexturedModalRect(0, 0, 0, 0, 256, 256); // et on affiche l'image
        GL11.glPopMatrix(); // on refermen toujoure une matrice ouverte !!!
        // fin ajout
    }

    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (par1 + 0), (double) (par2 + par6), 0.0D, (double) ((float) (par3 + 0) * f), (double) ((float) (par4 + par6) * f1));
        tessellator.addVertexWithUV((double) (par1 + par5), (double) (par2 + par6), 0.0D, (double) ((float) (par3 + par5) * f), (double) ((float) (par4 + par6) * f1));
        tessellator.addVertexWithUV((double) (par1 + par5), (double) (par2 + 0), 0.0D, (double) ((float) (par3 + par5) * f), (double) ((float) (par4 + 0) * f1));
        tessellator.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0), 0.0D, (double) ((float) (par3 + 0) * f), (double) ((float) (par4 + 0) * f1));
        tessellator.draw();
    }

    public void drawTexturedModalRectForBackground(int par1, int par2, int par3, int par4, int par5, int par6) {
        mc.renderEngine.bindTexture("/gui/gui.png");
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (par1 + 0), (double) (par2 + par6), 0.0D, (double) ((float) (par3 + 0) * f), (double) ((float) (par4 + par6) * f1));
        tessellator.addVertexWithUV((double) (par1 + par5), (double) (par2 + par6), 0.0D, (double) ((float) (par3 + par5) * f), (double) ((float) (par4 + par6) * f1));
        tessellator.addVertexWithUV((double) (par1 + par5), (double) (par2 + 0), 0.0D, (double) ((float) (par3 + par5) * f), (double) ((float) (par4 + 0) * f1));
        tessellator.addVertexWithUV((double) (par1 + 0), (double) (par2 + 0), 0.0D, (double) ((float) (par3 + 0) * f), (double) ((float) (par4 + 0) * f1));
        tessellator.draw();
    }

    protected void drawContainerBackground(Tessellator tess) {
        return;
    }

    @Override
    public void drawScreen(int mX, int mY, float f) {
        _mouseX = mX;
        _mouseY = mY;

        if (selected != -1 && !Mouse.isButtonDown(0) && Mouse.getDWheel() == 0)
        {
            if (Mouse.next() && Mouse.getEventButtonState())
            {
                System.out.println(Mouse.getEventButton());
            }
        }

        super.drawScreen(mX, mY, f);
    }

    protected void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float f = (float) (par5 >> 24 & 255) / 255.0F;
        float f1 = (float) (par5 >> 16 & 255) / 255.0F;
        float f2 = (float) (par5 >> 8 & 255) / 255.0F;
        float f3 = (float) (par5 & 255) / 255.0F;
        float f4 = (float) (par6 >> 24 & 255) / 255.0F;
        float f5 = (float) (par6 >> 16 & 255) / 255.0F;
        float f6 = (float) (par6 >> 8 & 255) / 255.0F;
        float f7 = (float) (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex((double) par3, (double) par2, 0.0D);
        tessellator.addVertex((double) par1, (double) par2, 0.0D);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double) par1, (double) par4, 0.0D);
        tessellator.addVertex((double) par3, (double) par4, 0.0D);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
