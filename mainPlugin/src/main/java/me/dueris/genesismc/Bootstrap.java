package me.dueris.genesismc;

import io.papermc.paper.configuration.PaperConfigurations;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import joptsimple.OptionSet;
import me.dueris.genesismc.utils.BukkitUtils;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_20_R2.bootstrap.Main;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Bootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        try {
            File datapackDir = new File(this.parseDatapackPath(context));
            copyOriginDatapack(datapackDir.toPath());
        } catch (Exception e) {
            // ignore
        }
    }

    public static void deleteDirectory(Path directory, boolean ignoreErrors) throws IOException {
        if (Files.exists(directory)) {
            Files.walk(directory)
                    .sorted((a, b) -> b.compareTo(a)) // Sort in reverse order for correct deletion
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                            Files.delete(path);
                        } catch (IOException e) {
                            if(!ignoreErrors){
                                System.err.println("Error deleting: " + path + e);
                            }
                        }
                    });
        }
    }
    public static ArrayList<String> oldDV = new ArrayList<>();
    static {
        oldDV.add("OriginsGenesis");
        oldDV.add("Origins-Genesis");
        oldDV.add("Origins-GenesisMC");
    }

    public static void copyOriginDatapack(Path datapackPath) {
        for(String string : oldDV){
            if (Files.exists(datapackPath)) {
                String path = Path.of(datapackPath + File.separator + string).toAbsolutePath().toString();
                try {
                    deleteDirectory(Path.of(path), false);
                } catch (IOException e) {

                }
            }
        }
        try {
            CodeSource src = BukkitUtils.class.getProtectionDomain().getCodeSource();
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();

                if (!name.startsWith("datapacks/")) continue;
                if (FilenameUtils.getExtension(name).equals("zip")) continue;
                if (name.equals("datapacks/")) continue;

                name = name.substring(10);
                File file = new File(datapackPath.toAbsolutePath().toString().replace(".\\", "") + File.separator + name);
                if (!file.getName().contains(".")) {
                    Files.createDirectory(Path.of(file.getAbsolutePath()));
                    continue;
                }
                Files.writeString(Path.of(file.getAbsolutePath()), new String(zip.readAllBytes()));
            }
            zip.close();
        } catch (Exception e) {
            // ignore
        }
    }

    public String parseDatapackPath(BootstrapContext context){
        OptionSet optionset;
        try {
            org.bukkit.configuration.file.YamlConfiguration bukkitConfiguration = YamlConfiguration.loadConfiguration(Paths.get("bukkit.yml").toFile());
            File container;
            container = new File(bukkitConfiguration.getString("settings.world-container", "."));
            String s = (String) Optional.ofNullable(
                    "world"
            ).orElseGet(() -> {
                Path propPath = Paths.get("server.properties");
                if (propPath.toFile().exists()) {
                    Properties properties = new Properties();
                    try (FileInputStream input = new FileInputStream(propPath.toFile())) {
                        properties.load(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return properties.getProperty("level-name", "world");
                } else {
                    return "world";
                }
            });
                    Path datapackFolder = Paths.get(container.getAbsolutePath() + File.separator + s + File.separator + "datapacks");
            return datapackFolder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}