package xyz.gnarbot.gnar.commands

import xyz.gnarbot.gnar.commands.template.Description

@Description("Settings|Music|Fun|Media|General", display = "category")
enum class Category(val title: String, val show: Boolean = true) {
    SETTINGS("Settings"),
    MUSIC("Music"),
    FUN("Fun"),
    MEDIA("Media"),
    GENERAL("General"),
    NONE("", show = false),
}