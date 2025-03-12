package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum HexCode {
    GREEN("#00ff00"),
    DARK_GREEN("#0bda51"),
    LIGHT_GREEN("#33cc33"),
    YELLOW("#ffff00"),
    GOLD("#fdda0d"),
    ORANGE("#ffbf00"),
    DARK_ORANGE("#ff9900"),
    RED_ORANGE("#ff5f15"),
    LIGHT_RED("#ff3d00"),
    RED("#ff0000"),
    DEEP_RED("#d2042d"),
    GRAY("#808080");

    private final String hexCode;
}
