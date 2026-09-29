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

    val AMBER_TUBE = AudiophileAccentTheme(
        id = "amber_tube",
        name = "Amber Tube Warmth",
        primaryHex = "#FFB703",
        lightHex = "#FFD166",
        primaryColor = Color(0xFFFFB703),
    )

    val OBSIDIAN_GOLD = AudiophileAccentTheme(
        id = "obsidian_gold",
        name = "Obsidian Gold",
        primaryHex = "#D4AF37",
        lightHex = "#F3E5AB",
        primaryColor = Color(0xFFD4AF37),
    )

    val EMERALD_HIFI = AudiophileAccentTheme(
        id = "emerald_hifi",
        name = "Emerald Hifi",
        primaryHex = "#06D6A0",
        lightHex = "#80ED99",
        primaryColor = Color(0xFF06D6A0),
    )

    val CYBER_MAGENTA = AudiophileAccentTheme(
        id = "cyber_magenta",
        name = "Cyber Magenta",
        primaryHex = "#FF007F",
        lightHex = "#FF66B2",
        primaryColor = Color(0xFFFF007F),
    )

    val CRIMSON_PEAK = AudiophileAccentTheme(
        id = "crimson_peak",
        name = "Crimson Peak",
        primaryHex = "#E63946",
        lightHex = "#FF6B6B",
        primaryColor = Color(0xFFE63946),
    )

    val ALL_THEMES = listOf(
        DYNAMIC_MONET,
        ELECTRIC_CYAN,
        NEON_AMETHYST,
        AMBER_TUBE,
        OBSIDIAN_GOLD,
        EMERALD_HIFI,
        CYBER_MAGENTA,
        CRIMSON_PEAK,
    )
}
