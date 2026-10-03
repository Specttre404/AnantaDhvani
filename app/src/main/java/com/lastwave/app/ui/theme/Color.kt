package com.lastwave.app.ui.theme

import androidx.compose.ui.graphics.Color

data class AudiophileAccentTheme(
    val id: String,
    val name: String,
    val primaryHex: String,
    val lightHex: String,
    val primaryColor: Color,
)

object AudiophilePalettes {
    val DYNAMIC_MONET = AudiophileAccentTheme(
        id = "monet",
        name = "Dynamic Monet",
        primaryHex = "#6750A4",
        lightHex = "#D0BCFF",
        primaryColor = Color(0xFF6750A4),
    )

    val CATPPUCCIN_MOCHA = AudiophileAccentTheme(
        id = "catppuccin_mocha",
        name = "Catppuccin Mocha",
        primaryHex = "#CBA6F7",
        lightHex = "#F5E0DC",
        primaryColor = Color(0xFFCBA6F7),
    )

    val NORD = AudiophileAccentTheme(
        id = "nord",
        name = "Nord Polar",
        primaryHex = "#88C0D0",
        lightHex = "#81A1C1",
        primaryColor = Color(0xFF88C0D0),
    )

    val DRACULA = AudiophileAccentTheme(
        id = "dracula",
        name = "Dracula",
        primaryHex = "#BD93F9",
        lightHex = "#FF79C6",
        primaryColor = Color(0xFFBD93F9),
    )

    val TOKYO_NIGHT = AudiophileAccentTheme(
        id = "tokyo_night",
        name = "Tokyo Night",
        primaryHex = "#7DCFFF",
        lightHex = "#7AA2F7",
        primaryColor = Color(0xFF7DCFFF),
    )

    val GRUVBOX_DARK = AudiophileAccentTheme(
        id = "gruvbox_dark",
        name = "Gruvbox Dark",
        primaryHex = "#FABD2F",
        lightHex = "#FE8019",
        primaryColor = Color(0xFFFABD2F),
    )

    val OLED_BLACK = AudiophileAccentTheme(
        id = "oled_black",
        name = "OLED Pitch Black",
        primaryHex = "#000000",
        lightHex = "#222222",
        primaryColor = Color(0xFF000000),
    )

    val ELECTRIC_CYAN = AudiophileAccentTheme(
        id = "electric_cyan",
        name = "Electric Cyan",
        primaryHex = "#00E5FF",
        lightHex = "#80F2FF",
        primaryColor = Color(0xFF00E5FF),
    )

    val NEON_AMETHYST = AudiophileAccentTheme(
        id = "neon_amethyst",
        name = "Neon Amethyst",
        primaryHex = "#9D4EDD",
        lightHex = "#C77DFF",
        primaryColor = Color(0xFF9D4EDD),
    )

    val ALL_THEMES = listOf(
        DYNAMIC_MONET,
        CATPPUCCIN_MOCHA,
        NORD,
        DRACULA,
        TOKYO_NIGHT,
        GRUVBOX_DARK,
        OLED_BLACK,
        ELECTRIC_CYAN,
        NEON_AMETHYST,
    )
}
