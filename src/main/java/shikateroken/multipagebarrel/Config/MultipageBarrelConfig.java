package shikateroken.multipagebarrel.Config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MultipageBarrelConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // ページ数を保存する変数
    public static final ModConfigSpec.IntValue MAX_PAGES;

    static {
        BUILDER.push("General");

        MAX_PAGES = BUILDER.comment("Number of pages in the multipage barrel. (1 page = 104 slots)" ,
                        "Warning: When you change this config, items that overflow due to the reduction in the number of pages will be deleted." ,
                        "Warning:If you set this to the maximum, the risk of chunk data corruption increases due to NBT size limits.")
                .defineInRange("maxPages", 6, 1, 10); // デフォルト6ページ、最小1、最大100

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
