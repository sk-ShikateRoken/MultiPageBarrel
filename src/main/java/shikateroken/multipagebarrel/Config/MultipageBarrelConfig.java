package shikateroken.multipagebarrel.Config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MultipageBarrelConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // ページ数を保存する変数
    public static final ModConfigSpec.IntValue MAX_PAGES;

    static {
        BUILDER.push("General");

        MAX_PAGES = BUILDER.comment("マルチページ樽のページ数 (1ページ = 27スロット)")
                .defineInRange("maxPages", 6, 1, 100); // デフォルト6ページ、最小1、最大100

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
