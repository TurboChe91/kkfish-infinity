package me.kkfish.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class XSeriesUtil {

    private static boolean initialized = false;
    private static Class<?> xMaterialClass;
    private static Class<?> xSoundClass;

    private static void initialize() {
        if (!initialized) {
            try {
                try {
                    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                    xMaterialClass = contextClassLoader.loadClass("com.cryptomorin.xseries.XMaterial");
                    xSoundClass = contextClassLoader.loadClass("com.cryptomorin.xseries.XSound");
                    initialized = true;
                    return;
                } catch (ClassNotFoundException e) {
                }
                
                try {
                    xMaterialClass = Class.forName("com.cryptomorin.xseries.XMaterial");
                    xSoundClass = Class.forName("com.cryptomorin.xseries.XSound");
                    initialized = true;
                    return;
                } catch (ClassNotFoundException e) {
                }
            } catch (Exception e) {
            }
        }
    }

    public static Material parseMaterial(String materialName) {
        if (materialName == null || materialName.isEmpty()) {
            return null;
        }
        
        try {
            Material material = Material.getMaterial(materialName);
            if (material != null) {
                return material;
            }
        } catch (Exception e) {
        }
        
        try {
            Material material = Material.getMaterial(materialName.toUpperCase());
            if (material != null) {
                return material;
            }
        } catch (Exception e) {
        }
        
        initialize();
        if (initialized && xMaterialClass != null) {
            try {
                Method matchMethod = xMaterialClass.getMethod("matchXMaterial", String.class);
                Object optionalXMaterial = matchMethod.invoke(null, materialName);
                
                Method isPresentMethod = optionalXMaterial.getClass().getMethod("isPresent");
                if (!(Boolean) isPresentMethod.invoke(optionalXMaterial)) {
                    return null;
                }
                
                Method getMethod = optionalXMaterial.getClass().getMethod("get");
                Object xMaterial = getMethod.invoke(optionalXMaterial);
                
                Method parseMaterialMethod = xMaterial.getClass().getMethod("parseMaterial");
                Material material = (Material) parseMaterialMethod.invoke(xMaterial);
                if (material != null) {
                    return material;
                }
            } catch (Exception e) {
            }
        }
        
        return getAlternativeMaterial(materialName);
    }

    public static Material getMaterial(String materialEnumName) {
        if (materialEnumName == null || materialEnumName.isEmpty()) {
            return getFallbackMaterial();
        }
        
        try {
            Material material = Material.getMaterial(materialEnumName);
            if (material != null) {
                return material;
            }
        } catch (Exception e) {
        }
        
        initialize();
        if (initialized && xMaterialClass != null) {
            try {
                Field field = xMaterialClass.getField(materialEnumName);
                Object xMaterial = field.get(null);
                
                Method parseMaterialMethod = xMaterial.getClass().getMethod("parseMaterial");
                Material material = (Material) parseMaterialMethod.invoke(xMaterial);
                if (material != null) {
                    return material;
                }
            } catch (Exception e) {
            }
        }
        
        return getAlternativeMaterial(materialEnumName);
    }

    private static Material getMaterialByName(String materialName) {
        try {
            return Material.getMaterial(materialName);
        } catch (Exception e) {
            return null;
        }
    }

    private static Material getAlternativeMaterial(String materialName) {
        try {
            Material material = Material.getMaterial(materialName);
            if (material != null) {
                return material;
            }
            
            if (materialName.contains("_WOOL")) {
                Material woolMaterial = Material.getMaterial("WOOL");
                if (woolMaterial != null) {
                    return woolMaterial;
                }
            }
            
            if (materialName.contains("_STAINED_GLASS_PANE")) {
                Material glassPaneMaterial = Material.getMaterial("GLASS_PANE");
                if (glassPaneMaterial != null) {
                    return glassPaneMaterial;
                }
            }
            
            if (materialName.contains("_STAINED_GLASS")) {
                Material glassMaterial = Material.getMaterial("GLASS");
                if (glassMaterial != null) {
                    return glassMaterial;
                }
            }
            
            if (materialName.contains("_LOG")) {
                Material logMaterial = Material.getMaterial("LOG");
                if (logMaterial != null) {
                    return logMaterial;
                }
            }
            
            if (materialName.contains("_WOOD")) {
                Material oakWoodMaterial = Material.getMaterial("OAK_WOOD");
                if (oakWoodMaterial != null) {
                    return oakWoodMaterial;
                }
                Material woodMaterial = Material.getMaterial("WOOD");
                if (woodMaterial != null) {
                    return woodMaterial;
                }
            }
            
            if (materialName.contains("_LEAVES")) {
                Material oakLeavesMaterial = Material.getMaterial("OAK_LEAVES");
                if (oakLeavesMaterial != null) {
                    return oakLeavesMaterial;
                }
                Material leavesMaterial = Material.getMaterial("LEAVES");
                if (leavesMaterial != null) {
                    return leavesMaterial;
                }
            }
            
            if (materialName.contains("_PLANKS")) {
                Material oakPlanksMaterial = Material.getMaterial("OAK_PLANKS");
                if (oakPlanksMaterial != null) {
                    return oakPlanksMaterial;
                }
                Material woodMaterial = Material.getMaterial("WOOD");
                if (woodMaterial != null) {
                    return woodMaterial;
                }
            }
            
            Material[] commonMaterials = {
                Material.STONE,
                Material.DIRT,
                Material.GRASS,
                getMaterialByName("OAK_WOOD"),
                getMaterialByName("WOOD"),
                Material.COAL,
                Material.IRON_INGOT,
                Material.GOLD_INGOT,
                Material.DIAMOND
            };
            
            for (Material commonMaterial : commonMaterials) {
                if (commonMaterial != null) {
                    return commonMaterial;
                }
            }
            
            return getFallbackMaterial();
        } catch (Exception e) {
            return getFallbackMaterial();
        }
    }

    private static Material getFallbackMaterial() {
        try {
            return Material.STONE;
        } catch (Exception e) {
            return null;
        }
    }

    public static void playSound(Location location, String soundName, float volume, float pitch) {
        initialize();
        if (!initialized || xSoundClass == null) {
            try {
                Sound sound = Sound.valueOf(soundName);
                if (location.getWorld() != null) {
                    location.getWorld().playSound(location, sound, volume, pitch);
                }
            } catch (Exception e) {
            }
            return;
        }

        try {
            Method matchMethod = xSoundClass.getMethod("matchXSound", String.class);
            Object optionalXSound = matchMethod.invoke(null, soundName);
            
            Method isPresentMethod = optionalXSound.getClass().getMethod("isPresent");
            if (!(Boolean) isPresentMethod.invoke(optionalXSound)) {
                return;
            }
            
            Method getMethod = optionalXSound.getClass().getMethod("get");
            Object xSound = getMethod.invoke(optionalXSound);
            
            Method playMethod = xSound.getClass().getMethod("play", Location.class, float.class, float.class);
            playMethod.invoke(xSound, location, volume, pitch);
        } catch (Exception e) {
            try {
                Sound sound = Sound.valueOf(soundName);
                if (location.getWorld() != null) {
                    location.getWorld().playSound(location, sound, volume, pitch);
                }
            } catch (Exception ex) {
            }
        }
    }

    public static boolean isXSeriesLoaded() {
        initialize();
        return initialized;
    }
}
