package fr.flv92.oc.tc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StringTranslate;

/**
 * @author Flv92
 */
public class CustomGuiEditingFriend extends GuiScreen {

    public GuiScreen parentGui;
    public String pseudoToEdit;
    public int index;

    public CustomGuiEditingFriend(GuiScreen parent, String pseudo, int ind) {
        parentGui = parent;
        pseudoToEdit = pseudo;
        index = ind;
    }

    public void initGui() {

        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.buttonList.add(new GuiButton(0, this.width / 4 - 70, this.height / 2 - 10, 100, 20, "Remove"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 70, this.height / 2 - 10, 140, 20, "1".equals(MainModClass.ocTcFriends[index][1]) ? "Remove from Favorites" : "Add to Favorites"));
        this.buttonList.add(new GuiButton(2, 3 * this.width / 4 - 30, this.height / 2 - 10, 100, 20, "Cancel"));
    }

    protected void actionPerformed(GuiButton i) {
        if (i.id == 2)
        {
            mc.displayGuiScreen(parentGui);
        }
        if (i.id == 1)
        {
            if (MainModClass.ocTcFriends[index][1] == "1")
            {
                this.removeFavFromList(pseudoToEdit);
            } else
            {
                this.addFavFromList(pseudoToEdit);
            }
            mc.displayGuiScreen(parentGui);
        }
        if (i.id == 0)
        {
            mc.displayGuiScreen(new CustomGuiYesNo(parentGui, "Would you really like to remove " + pseudoToEdit + "?", pseudoToEdit + ("1".equals(MainModClass.ocTcFriends[index][1]) ? "*fav" : ""), index));
        }
    }

    public void addFavFromList(String pseudo) {
        try
        {
            File temp = new File(MainModClass.config.getAbsolutePath() + ".tmp");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(temp, false)));
            FileReader reader = new FileReader(MainModClass.config);
            BufferedReader br = new BufferedReader(reader);
            String s;
            int i = 0;
            while ((s = br.readLine()) != null)
            {
                s = s.replaceAll(pseudo, pseudo + "*fav");
                out.println(s);
            }
            out.close();
            MainModClass.config.delete();
            temp.renameTo(MainModClass.config);
            MainModClass.rereadConfigFile();
        } catch (IOException ex)
        {
            Logger.getLogger(MainModClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        MainModClass.updateAllInfos();

    }

    private void removeFavFromList(String pseudo) {
        try
        {
            File temp = new File(MainModClass.config.getAbsolutePath() + ".tmp");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(temp, false)));
            FileReader reader = new FileReader(MainModClass.config);
            BufferedReader br = new BufferedReader(reader);
            String s;
            int i = 0;
            while ((s = br.readLine()) != null)
            {
                if (s.contains(pseudo))
                {
                    s = s.substring(0, s.length() - 4);
                }
                System.out.println(s);
                System.out.println(pseudo + "*fav");
                out.println(s);
            }
            out.close();
            MainModClass.config.delete();
            temp.renameTo(MainModClass.config);
            MainModClass.rereadConfigFile();
        } catch (IOException ex)
        {
            Logger.getLogger(MainModClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        MainModClass.updateAllInfos();
    }
}
