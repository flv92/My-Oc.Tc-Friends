package fr.flv92.oc.tc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.util.StringTranslate;

@SideOnly(Side.CLIENT)
public class CustomGuiYesNo extends GuiScreen {

    /**
     * A reference to the screen object that created this. Used for navigating
     * between screens.
     */
    protected GuiScreen parentScreen;
    /**
     * First line of text.
     */
    private String message1;
    /**
     * The text shown for the first button in GuiYesNo
     */
    protected String buttonText1;
    /**
     * The text shown for the second button in GuiYesNo
     */
    protected String buttonText2;
    protected String strToRemove;
    protected int indexToRemove;

    public CustomGuiYesNo(GuiScreen par1GuiScreen, String par2Str, String str, int ind) {
        this.parentScreen = par1GuiScreen;
        this.message1 = par2Str;
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.buttonText1 = stringtranslate.translateKey("gui.yes");
        this.buttonText2 = stringtranslate.translateKey("gui.no");
        strToRemove = str;
        System.out.println(strToRemove);
        indexToRemove = ind;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.add(new GuiSmallButton(0, this.width / 2 - 155, this.height / 6 + 96, this.buttonText1));
        this.buttonList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.buttonText2));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton) {
        //TODO: Remove Player from list!
        if (par1GuiButton.id == 0)
        {
            removeLineFromFile(MainModClass.config.getAbsolutePath(), strToRemove);
            try
            {
                MainModClass.rereadConfigFile();
            } catch (FileNotFoundException ex)
            {
                Logger.getLogger(CustomGuiYesNo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex)
            {
                Logger.getLogger(CustomGuiYesNo.class.getName()).log(Level.SEVERE, null, ex);
            }
            removeIndexOfMyMatrix(indexToRemove);
            this.mc.displayGuiScreen(parentScreen);
        }
        if (par1GuiButton.id == 1)
        {
            this.mc.displayGuiScreen(parentScreen);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.message1, this.width / 2, 70, 16777215);
        super.drawScreen(par1, par2, par3);
    }

    public void removeLineFromFile(String file, String lineToRemove) {

        try
        {

            File inFile = new File(file);

            if (!inFile.isFile())
            {
                System.out.println("Parameter is not an existing file");
                return;
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;
            boolean flag = false;

            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null)
            {

                if (!line.trim().equals(lineToRemove) || flag)
                {

                    pw.println(line);
                    pw.flush();
                } else
                {
                    flag = true;
                }
            }
            pw.close();
            br.close();

            //Delete the original file
            if (!inFile.delete())
            {
                System.out.println("Could not delete file");
                return;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
            {
                System.out.println("Could not rename file");
            }

        } catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void removeIndexOfMyMatrix(int index) {
        String[][] temp = new String[100][2];
        for (int i = 0; i < 99; i++)
        {
            if (i < index)
            {
                for (int j = 0; j < 2; j++)
                {
                    temp[i][j] = MainModClass.ocTcInfos[i][j];
                }
            } else
            {
                for (int j = 0; j < 2; j++)
                {
                    temp[i][j] = MainModClass.ocTcInfos[i + 1][j];
                }
            }
        }
        MainModClass.ocTcInfos = temp;
    }
}
