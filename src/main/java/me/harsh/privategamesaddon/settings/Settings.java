package me.harsh.privategamesaddon.settings;


import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {

    // Menu title, Prefix, Booleans
    public static String PREFIX;
    public static String MENU_TITLE;
    public static Boolean SHOULD_SAVE_STATS;
    public static Boolean AUTO_WARP;
    public static Boolean PARTIES_PRIORITY;
    public static Integer AFK_CHECK_TIME;

    // Sub menus
    public static String HEALTH_BUFF_MENU;
    public static String SPEED_BUFF_MENU;
    public static String RESPAWN_BUFF_MENU;
    public static String SPAWN_RATE_BUFF_MENU;
    public static String KNOCK_BACK_BUFF_MENU;

    // Buttons
    public static String ONE_HIT_BUFF;
    public static String CRAFTING_BUFF;
    public static String HEALTH_BUFF;
    public static String LOW_GRAVITY_BUFF;
    public static String SPEED_BUFF;
    public static String DISABLE_BLOCK_PROTECTION_BUFF;
    public static String RESPAWN_TIME_BUFF;
    public static String SPAWN_RATE_MUTIPLIER_BUFF;
    public static String FALL_DAMAGE_BUFF;
    public static String NO_SPAWNERS_BUFF;

    // Permisions
    public static String CREATE_PERM;
    public static String GLOBAL_PERM;
    public static String AUTO_WARP_PERM;
    public static String RELOAD_PERM;
    public static String PARTY_BYPASS_PERM;

    // Placeholders
    public static String IS_PRIVATE_GAME;

    // Messages

    public static String SUCCESSFUL_RELOAD;
    public static String NO_PERM_EROR;
    public static String RELOAD_NO_PERM_EROR;
    public static String NO_AUTO_WARP_PERM_EROR;
    public static String NO_PLAYER_FOUND_IN_PARTY;
    public static String ARENA_IS_PRIVATE;
    public static String NOT_IN_ARENA;
    public static String NOT_IN_PRIVATE_GAME_MODE;
    public static String PLAYER_JOIN_PRIVATE_GAME;
    public static String NOT_PRIVATE_ROOM_WARP;
    public static String NOT_IN_PARTY;
    public static String PRIVATE_GAME_MODE;
    public static String NORMAL_MODE;
    public static String AFK_TIME_REACHED;
    public static String NO_PARTY_ON_CREATE;

    @Override
    protected int getConfigVersion() {
        return 1;
    }

    private static void init(){
        pathPrefix("Features");
        SHOULD_SAVE_STATS = getBoolean("Save_Stats");
        AUTO_WARP = getBoolean("Auto_warp");
        PARTIES_PRIORITY = getBoolean("Priority_parties");
        AFK_CHECK_TIME = getInteger("Afk_check_time");
        pathPrefix("Menu");
        MENU_TITLE = getString("Title");
        pathPrefix("Menu.Sub_Menu");
        HEALTH_BUFF_MENU = getString("Health");
        SPEED_BUFF_MENU = getString("Speed");
        RESPAWN_BUFF_MENU = getString("Respawn");
        SPAWN_RATE_BUFF_MENU = getString("Spawn_rate");
        KNOCK_BACK_BUFF_MENU = getString("KnockBack");
        pathPrefix("Menu.Buffs");
        NO_SPAWNERS_BUFF = getString("No_emeralds_and_diamonds");
        CRAFTING_BUFF = getString("Crafting_allow_buff");
        ONE_HIT_BUFF = getString("One_Hit_Buff");
        HEALTH_BUFF = getString("Health_Buff");
        LOW_GRAVITY_BUFF = getString("Low_Gravity_Buff");
        SPEED_BUFF = getString("Speed_Buff");
        DISABLE_BLOCK_PROTECTION_BUFF = getString("Disable_Block_Protection_Buff");
        RESPAWN_TIME_BUFF = getString("Respawn_Time_Buff");
        SPAWN_RATE_MUTIPLIER_BUFF = getString("Spawn_Rate_Mutiplier_Buff");
        FALL_DAMAGE_BUFF = getString("Fall_damage_buff");
        pathPrefix("Perms");
        CREATE_PERM = getString("create_perm");
        GLOBAL_PERM = getString("global_perm");
        AUTO_WARP_PERM = getString("auto_warp");
        RELOAD_PERM = getString("reload_perm");
        PARTY_BYPASS_PERM = getString("party_requirement");
        pathPrefix("Placeholders");
        IS_PRIVATE_GAME = getString("Is_private_game");
        pathPrefix("Messages");
        AFK_TIME_REACHED = getString("Afk_limit_reached");
        RELOAD_NO_PERM_EROR = getString("No_reload_perm");
        SUCCESSFUL_RELOAD = getString("Successfully_reload");
        NO_PLAYER_FOUND_IN_PARTY = getString("No_players_found_in_party");
        NO_PERM_EROR = getString("No_perm");
        NO_PARTY_ON_CREATE = getString("No_party_found");
        PREFIX = getString("Prefix");
        ARENA_IS_PRIVATE = getString("Arena_private");
        NO_AUTO_WARP_PERM_EROR = getString("Auto_warp_noperm");
        NOT_IN_ARENA = getString("Arena_not_found");
        NOT_IN_PRIVATE_GAME_MODE = getString("Private_game_wrong_mode");
        PLAYER_JOIN_PRIVATE_GAME = getString("Private_game_join");
        NOT_PRIVATE_ROOM_WARP = getString("Private_game_illegal_warp");
        NOT_IN_PARTY = getString("Not_in_a_party_to_warp");
        PRIVATE_GAME_MODE = getString("Private_game_creation_mode");
        NORMAL_MODE = getString("Normal_game_mode");
    }
}
