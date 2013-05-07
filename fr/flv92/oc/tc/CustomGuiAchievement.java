package fr.flv92.oc.tc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class CustomGuiAchievement extends GuiAchievement {

    /**
     * Holds the instance of the game (Minecraft)
     */
    private Minecraft theGame;
    /**
     * Holds the latest width scaled to fit the game window.
     */
    private int achievementWindowWidth;
    /**
     * Holds the latest height scaled to fit the game window.
     */
    private int achievementWindowHeight;
    private String achievementGetLocalText;
    private String achievementStatName;
    /**
     * Holds the achievement that will be displayed on the GUI.
     */
    private Achievement theAchievement;
    private long achievementTime;
    /**
     * Holds a instance of RenderItem, used to draw the achievement icons on
     * screen (is based on ItemStack)
     */
    private RenderItem itemRender;
    private boolean haveAchiement;
    public boolean isFakeAchievement;
    public String customPseudoToDraw;
    public String stringToDraw;
    public boolean isOnline;
    BufferedReader buffer = null;
    FileOutputStream fos = null;
    InputStream is = null;
    private ImageLoader imgLoader;
    private BufferedImage buffered;

    public CustomGuiAchievement(Minecraft par1Minecraft) {
        super(par1Minecraft);
        this.theGame = par1Minecraft;
        this.itemRender = new RenderItem();
        isFakeAchievement = false;
        isOnline = false;
        stringToDraw = null;
        this.imgLoader = new ImageLoader(par1Minecraft.texturePackList, par1Minecraft.gameSettings);
        this.customPseudoToDraw = "nocxx";
        stringToDraw = "sdfgf";



    }

    /**
     * Queue a taken achievement to be displayed.
     */
    public void queueTakenAchievement(Achievement par1Achievement) {
        this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
        this.achievementStatName = StatCollector.translateToLocal(par1Achievement.getName());
        this.achievementTime = Minecraft.getSystemTime();
        this.theAchievement = par1Achievement;
        this.haveAchiement = false;
        this.isFakeAchievement = false;


    }

    public void addFakeAchievementToMyList(Achievement par1Achievement, String pseudo, String str, boolean isO) {
        this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
        this.achievementStatName = StatCollector.translateToLocal(par1Achievement.getName());
        this.achievementTime = Minecraft.getSystemTime();
        this.theAchievement = par1Achievement;
        this.haveAchiement = false;
        this.isFakeAchievement = true;
        this.customPseudoToDraw = pseudo;
        stringToDraw = str;
        this.isOnline = isO;
    }

    /**
     * Queue a information about a achievement to be displayed.
     */
    public void queueAchievementInformation(Achievement par1Achievement) {
        this.achievementGetLocalText = StatCollector.translateToLocal(par1Achievement.getName());
        this.achievementStatName = par1Achievement.getDescription();
        this.achievementTime = Minecraft.getSystemTime() - 2500L;
        this.theAchievement = par1Achievement;
        this.haveAchiement = true;
        this.isFakeAchievement = false;

    }

    /**
     * Update the display of the achievement window to match the game window.
     */
    private void updateAchievementWindowScale() {
        GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.achievementWindowWidth = this.theGame.displayWidth;
        this.achievementWindowHeight = this.theGame.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.achievementWindowWidth = scaledresolution.getScaledWidth();
        this.achievementWindowHeight = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double) this.achievementWindowWidth, (double) this.achievementWindowHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    /**
     * Updates the small achievement tooltip window, showing a queued
     * achievement if is needed.
     */
    public void updateAchievementWindow() {
        if (this.theAchievement != null && this.achievementTime != 0L)
        {
            double d0 = (double) (Minecraft.getSystemTime() - this.achievementTime) / 3000.0D;

            if (!this.haveAchiement && (d0 < 0.0D || d0 > 1.0D))
            {
                this.achievementTime = 0L;
            } else
            {
                this.updateAchievementWindowScale();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                double d1 = d0 * 2.0D;

                if (d1 > 1.0D)
                {
                    d1 = 2.0D - d1;
                }

                d1 *= 4.0D;
                d1 = 1.0D - d1;

                if (d1 < 0.0D)
                {
                    d1 = 0.0D;
                }

                d1 *= d1;
                d1 *= d1;
                int i = this.achievementWindowWidth - 160;
                int j = 0 - (int) (d1 * 36.0D);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                this.theGame.renderEngine.bindTexture("/achievement/bg.png");
                GL11.glDisable(GL11.GL_LIGHTING);
                this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

                if (this.haveAchiement)
                {
                    this.theGame.fontRenderer.drawSplitString(this.achievementStatName, i + 30, j + 7, 120, -1);
                } else if (this.isFakeAchievement)
                {

                    this.theGame.fontRenderer.drawString(this.isOnline ? this.customPseudoToDraw + " is online" : this.customPseudoToDraw + " just", i + 30, j + 7, this.isOnline ? 52224 : 13369344);
                    this.theGame.fontRenderer.drawString(this.isOnline ? "on " +this.stringToDraw : "left " + this.stringToDraw, i + 30, j + 18, this.isOnline ? 52224 : 13369344);
                } else
                {
                    this.theGame.fontRenderer.drawString(this.achievementGetLocalText, i + 30, j + 7, -256);
                    this.theGame.fontRenderer.drawString(this.achievementStatName, i + 30, j + 18, -1);
                }

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                if (!this.isFakeAchievement)
                {
                    this.itemRender.renderItemAndEffectIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, this.theAchievement.theItemStack, i + 8, j + 8);
                } else
                {
                    String location = Minecraft.getMinecraftDir().getAbsolutePath();
                    location = location.substring(0, location.length() - MainModClass.debugOrRelease) + "/resources/skinsHead/" + this.customPseudoToDraw + ".png";
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
                    this.imgLoader.setupTexture(this.buffered, 20, 16, 16);// on charge l'image à partit du buffer (20 est le numéro de texture, 64 et 32 car la texture d'un skin fait 64/32 pixels
                    GL11.glPushMatrix(); // on ouvre une matrice pour que les changements suivants n'affectent pas le reste du gui
                    GL11.glColor4f(1, 1, 1, 1); // on applique une lumière blanche sur l'image
                    GL11.glScalef(1F / 16F, 1F / 16F, 1F);// ici j'ai du redimentionner l'image car avec ma class elle est déformée
                    GL11.glTranslatef((i + 8) * 16F, (j + 8) * 16F, 0);// ici tu changera les 2 premier paramètres (position finale en x et y de l'image sur le gui)
                    this.drawTexturedModalRect(0, 0, 0, 0, 256, 256); // et on affiche l'image
                    GL11.glPopMatrix();
                }
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
}
