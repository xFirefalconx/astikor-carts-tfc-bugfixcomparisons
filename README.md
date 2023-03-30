# AstikorCarts TFC 1.18.2

#### This is a fork of [Astikor Carts](https://github.com/issork/astikor-carts/tree/1.18.2) with the purpose of adding compatibility with [TerraFirmaCraft](https://github.com/TerraFirmaCraft/TerraFirmaCraft).

[![GitHub](https://img.shields.io/github/license/MennoMax/astikor-carts.svg)](https://github.com/MennoMax/astikor-carts/blob/master/LICENSE.md)
[![](http://cf.way2muchnoise.eu/full_astikorcarts_downloads.svg)](https://minecraft.curseforge.com/projects/astikorcarts)

AstikorCarts is a Minecraft mod which you can travel, transport goods, and plow fields with using horse drawn carts!

#### Maven

A maven artifact of this project can now be found on '**mvn.mennomax.de**' - first, add the maven repository to your build.gradle:

```
// Do NOT paste this into the 'buildscript' block.
repositories {
    maven { url = 'https://mvn.mennomax.de' }
}
```
Then, add the artifact to your build.gradles dependencies:

```
dependencies {
    // compile 'de.mennomax:astikorcarts-<Minecraft-Version>:<AstikorCarts-Version>'
    // For Minecraft 1.15.2 and AstikorCarts 1.0.2, you would have to add:
    compile 'de.mennomax:astikorcarts-1.15.2:1.0.2'
    
}
```
