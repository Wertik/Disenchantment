package me.wertik.disenchant;

import space.devport.utils.DevportPlugin;
import space.devport.utils.text.language.LanguageDefaults;

public class DisenchantLanguage extends LanguageDefaults {

    public DisenchantLanguage(DevportPlugin plugin) {
        super(plugin);
    }

    @Override
    public void setDefaults() {
        addDefault("Commands.Invalid-Player", "&cPlayer &f%param% &cis not online.");

        addDefault("Commands.Disenchant.No-Air", "&cYou have to hold an item.");
        addDefault("Commands.Disenchant.Done", "&7Disenchanted &f%count% &7enchants.");
    }
}
