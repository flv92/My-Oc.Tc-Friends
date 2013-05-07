package fr.flv92.oc.tc;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;

/**
 * @author Flv92
 * @TODO Debug less than 4 char names!
 * @TODO Fix bug of Edit button
 *
 */
@Mod(modid = "oc.tc Friends", name = "My Oc.tc friends", version = "Release: 1.0")
public class MainModClass {

    public static String[][] ocTcFriends;
    public static String[][] ocTcInfos;
    BufferedReader buffer = null;
    FileOutputStream fos = null;
    InputStream is = null;
    public static File config;
    //2 for debug
    //0 for realease
    public static int debugOrRelease = 0;
    public static int stupidInt = 0;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
        ocTcFriends = new String[100][2];
        ocTcInfos = new String[100][2];
        config = event.getSuggestedConfigurationFile();
        if (!config.exists())
        {
            try
            {
                config.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            MainModClass.rereadConfigFile();
            String location = Minecraft.getMinecraftDir().getAbsolutePath();
            location = location.substring(0, location.length() - debugOrRelease) + "/resources/skinsHead/";
            File skinsHead = new File(location);
            if (!skinsHead.exists())
            {
                skinsHead.mkdir();
            }

        } catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        MainModClass.updateAllInfos();

    }

    @Mod.Init
    public void init(FMLInitializationEvent event) {

        FMLClientHandler.instance().getClient().guiAchievement = new CustomGuiAchievement(FMLClientHandler.instance().getClient());
        TickRegistry.registerTickHandler(new MenuTickHandler(), Side.CLIENT);
        for (int i = 0; ocTcFriends[i][0] != null; i++)
        {
            tryToDownloadFile(i);
        }


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true)
                {
                    if (ocTcFriends[i][0] == null)
                    {
                        i = 0;
                    }
                    try
                    {
                        updateInfoForIndexPlayer(i);
                        Thread.sleep(500L);
                    } catch (InterruptedException ex)
                    {
                        Logger.getLogger(MainModClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    i++;
                }
            }
        };
        Thread t1 = new Thread(runnable);
        t1.start();
        updateAllInfos();
    }

    public void tryToDownloadFile(int index) {
        System.out.println("Trying to download skin head of " + ocTcFriends[index][0]);
        String location = Minecraft.getMinecraftDir().getAbsolutePath();
        location = location.substring(0, location.length() - debugOrRelease) + "/resources/skinsHead/" + ocTcFriends[index][0] + ".png";
        System.out.println(location);
        File image = new File(location);//this.mc.getAppDir("minecraft") + File.separator + "resources" + File.separator + "player.png");
        if (!image.exists())
        {
            try
            {

                URL url = new URL("https://minotar.net/helm/" + ocTcFriends[index][0] + "/16.png");
                URLConnection conn = url.openConnection();
                int FileLenght = conn.getContentLength();
                if (FileLenght == -1)
                {
                    throw new IOException("Error when downloading the skin of " + ocTcFriends[index][0]);
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
                fos.close();
                buffer.close();
                is.close();

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void addPseudoToConfig(String pseudo) {
        try
        {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(config, true)));
            out.println(pseudo);
            out.close();
            MainModClass.rereadConfigFile();

        } catch (IOException ex)
        {
            Logger.getLogger(MainModClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void rereadConfigFile() throws FileNotFoundException, IOException {
        ocTcFriends = new String[100][2];
        FileReader reader = new FileReader(config);
        BufferedReader br = new BufferedReader(reader);
        String s;
        int i = 0;
        while ((s = br.readLine()) != null)
        {
            String s1 = "";
            if (s.length() > 4)
            {
                s1 = s.substring(s.length() - 4, s.length());
            }
            if ("*fav".equals(s1))
            {
                System.out.println("*fav");
                ocTcFriends[i][0] = s.substring(0, s.length() - 4);
                ocTcFriends[i][1] = "1";
            } else
            {
                ocTcFriends[i][0] = s;
                ocTcFriends[i][1] = "0";
            }
            i++;
        }
        reader.close();
        br.close();
        //Organize array, fav first
        String[] fav = new String[100];
        String[] noFav = new String[100];
        for (i = 0; ocTcFriends[i][0] != null; i++)
        {
            if ("1".equals(ocTcFriends[i][1]))
            {
                //Add this man to fav
                int j;
                for (j = 0; fav[j] != null; j++)
                {
                }
                fav[j] = ocTcFriends[i][0];
                System.out.println("Adding " + fav[j] + " to the favorite");
            } else
            {
                //Add this man to noFav
                int j;
                for (j = 0; noFav[j] != null; j++)
                {
                }
                noFav[j] = ocTcFriends[i][0];
                System.out.println("Adding " + noFav[j] + " to the non favorite");
            }
        }
        ocTcFriends = new String[100][2];

        int length = 0;
        for (i = 0; fav[i] != null; i++)
        {
            ocTcFriends[i][0] = fav[i];
            ocTcFriends[i][1] = "1";
            length++;
        }
        //Get length

        for (i = 0; noFav[i] != null; i++)
        {
            ocTcFriends[i + length][0] = noFav[i];
            ocTcFriends[i + length][1] = "0";
        }
    }

    public static String[] getOcTcInfosForPlayer(String pseudo) {
        String returnCode = "2";
        String info = null;
        try
        {
            BufferedReader reader = SourceCodeDownloader.Connect("https://oc.tc/" + pseudo);
            String aux;
            int i = 0;
            String line134 = null;
            String line135 = null;

            while ((aux = reader.readLine()) != null)
            {
                if (i == 133)
                {
                    line134 = aux;
                }
                if (i == 134)
                {
                    line135 = aux;
                }
                i++;
            }
            if (line134.contains("Seen"))
            {
                //Offline
                returnCode = "1";
                info = line134 + " " + line135;
            } else if (line134.contains("href"))
            {
                returnCode = "0";
                info = "Online on: " + line135.substring(7, line135.length() - 8);
            }
            reader.close();

        } catch (Exception e)
        {
            info = "An error occured!";
        }
        //Must return 0 at String[0] to tell if player is online
        //Green: Online

        //Must return 1 at String[0] to tell if player is offline
        //White: Offline (Seen ___ ago on ___)

        //Must return 2 at String[0] to tell if an error occured!
        //Red 
        return new String[]
                {
                    returnCode, info
                };
    }

    public static void updateAllInfos() {
        int length = 0;
        for (int i = 0; ocTcFriends[i][0] != null; i++)
        {
            length++;
            ocTcInfos[i] = new String[]
            {
                "3", "Please wait..."
            };
        }
        for (int i = 0; ocTcFriends[i][0] != null; i++)
        {
            stupidInt = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ocTcInfos[stupidInt] = getOcTcInfosForPlayer(ocTcFriends[stupidInt][0]);
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }

    }

    public static void updateInfoForIndexPlayer(int i) {
        boolean wasOnline = false;
        if (ocTcInfos[i] != null && "0".equals(ocTcInfos[i][0]))
        {
            wasOnline = true;
        } else if (ocTcInfos[i] != null && "1".equals(ocTcInfos[i][0]))
        {
            wasOnline = false;
        } else
        {
            if (ocTcFriends[i][0] != null)
            {
                ocTcInfos[i] = getOcTcInfosForPlayer(ocTcFriends[i][0]);

            }
            return;
        }

        if (ocTcFriends[i][0] != null)
        {
            ocTcInfos[i] = getOcTcInfosForPlayer(ocTcFriends[i][0]);
        }
        if (!wasOnline && ocTcInfos[i][0] == "0")
        {
            Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
            ((CustomGuiAchievement) FMLClientHandler.instance().getClient().guiAchievement).addFakeAchievementToMyList(custom, ocTcFriends[i][0], ocTcInfos[i][1].substring(ocTcInfos[i][1].lastIndexOf(" ") + 1, ocTcInfos[i][1].length()), true);
        }
        if (wasOnline && ocTcInfos[i][0] == "1")
        {
            Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
            ((CustomGuiAchievement) FMLClientHandler.instance().getClient().guiAchievement).addFakeAchievementToMyList(custom, ocTcFriends[i][0], ocTcInfos[i][1].substring(ocTcInfos[i][1].lastIndexOf(" ") + 1, ocTcInfos[i][1].length()), false);
        }
    }
}
