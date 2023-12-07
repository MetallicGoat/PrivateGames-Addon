package me.harsh.privategamesaddon.buffs;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerIngameDeathEvent;
import de.marcely.bedwars.api.event.player.PlayerIngameRespawnEvent;
import de.marcely.bedwars.api.event.player.PlayerModifyBlockPermissionEvent;
import de.marcely.bedwars.api.game.spawner.Spawner;
import de.marcely.bedwars.api.game.spawner.SpawnerDurationModifier;
import de.marcely.bedwars.api.game.upgrade.Upgrade;
import de.marcely.bedwars.api.game.upgrade.UpgradeLevel;
import de.marcely.bedwars.api.game.upgrade.UpgradeState;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.plugin.SimplePlugin;


public class PlayerBuffListener implements Listener {

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            final Player player = (Player) event.getEntity();
            final Player damager = (Player) event.getDamager();
            final Arena arena = GameAPI.get().getArenaByPlayer(player);

            if (arena == null)
                return;
            if (arena.getStatus() != ArenaStatus.RUNNING)
                return;
            if (!Utility.getManager().isPrivateArena(arena))
                return;

            final ArenaBuff buff = Utility.getBuff(player);

            if (buff == null)
                return;

            if (buff.isOneHitKill())
                player.setHealth(0);
        }
    }

    @EventHandler
    public void onPlayerTakeFallDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player){
            final Player player = (Player) event.getEntity();
            final Arena arena = GameAPI.get().getArenaByPlayer(player);

            if (arena == null)
                return;
            if (arena.getStatus() != ArenaStatus.RUNNING)
                return;
            if (!Utility.getManager().isPrivateArena(arena))
                return;

            final ArenaBuff buff = Utility.getBuff(player);

            if (buff == null)
                return;

            if (!buff.isFallDamageEnabled()){
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerIngameRespawnEvent event){
        final Player player = event.getPlayer();
        final Arena arena = event.getArena();

        if (!Utility.getManager().isPrivateArena(arena))
            return;

        final ArenaBuff buff = Utility.getBuff(arena);

        if (buff == null)
            return;

        player.setMaxHealth(buff.getHealth());
        player.setHealth(buff.getHealth());

        if (buff.isLowGravity()){
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000000, 3));
        }
        if (buff.getSpeedModifier() != 1){
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000000, buff.getSpeedModifier()));
        }
    }

    @EventHandler
    public void onStart(RoundStartEvent event){
        final Arena arena = event.getArena();

        if (!Utility.getManager().isPrivateArena(arena))
            return;

        final ArenaBuff buff = Utility.getBuff(arena);

        Valid.checkNotNull(buff);

        if (buff.getSpawnRateMultiplier() != 3){
            for (Spawner spawner: arena.getSpawners()){
                for (Team team: arena.getEnabledTeams()){
                    if (spawner.getLocation().distance(arena.getTeamSpawn(team)) <= 15){
                        spawner.addDropDurationModifier("privateMultiply", SimplePlugin.getInstance(), SpawnerDurationModifier.Operation.SET, buff.getSpawnRateMultiplier());
                    }
                }
            }
        }
        if (buff.isNoEmeralds()){
            for (Spawner spawner : arena.getSpawners()){
                for (Team team: arena.getEnabledTeams()){
                    if (spawner.getLocation().distance(arena.getTeamSpawn(team)) >= 20) {
                        spawner.addDropDurationModifier("privateStop", SimplePlugin.getInstance(), SpawnerDurationModifier.Operation.SET, 9999999999999.9);
                    }
                }
            }
        }
        arena.getPlayers().forEach(player -> {
            if (buff.isLowGravity()){
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 3));
            }
            if (buff.getSpeedModifier() != 1){
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000000, buff.getSpeedModifier()));
            }
        });
        new BukkitRunnable(){
            @Override
            public void run() {
                event.getArena().getPlayers().forEach(player -> {
                    player.setMaxHealth(buff.getHealth());
                    player.setHealth(buff.getHealth());

                });
            }
        }.runTaskLater(SimplePlugin.getInstance(), 10);

        if (buff.isMaxUpgrades()){
            Common.runLater(10, ()-> {
                for (Team team: arena.getRemainingTeams()){
                    final UpgradeState state = arena.getUpgradeState(team);
                    for (Upgrade upgrade : GameAPI.get().getUpgrades()) {
                        if (state.isMaxLevel(upgrade)){
                            continue;
                        }else {
                            state.doUpgrade(upgrade.getMaxLevel(), null);
                        }
                    }
                }
            });
        }
    }

    @EventHandler
    public void onEnd(RoundEndEvent event){
        final Arena arena = event.getArena();

        if (!Utility.getManager().isPrivateArena(arena))
            return;

        for (Player player : arena.getPlayers()){
            player.setMaxHealth(20);
            player.setHealth(20);
        }
       Utility.getManager().arenaArenaBuffMap.remove(arena);

    }
    @EventHandler
    public void onPlayerDeath(PlayerIngameDeathEvent event){
        final Arena arena = event.getArena();

        if (!Utility.getManager().isPrivateArena(arena))
            return;

        final ArenaBuff buff = Utility.getBuff(arena);

        if (buff == null)
            return;

        event.setDeathSpectateDuration(buff.getRespawnTime());
    }

    @EventHandler
    public void onPlayerBlockBreak(PlayerModifyBlockPermissionEvent event){
        final Arena arena = event.getArena();

        if (!Utility.getManager().isPrivateArena(arena))
            return;

        final ArenaBuff buff = Utility.getBuff(arena);

        if (buff == null)
            return;

        if (!buff.isBlocksProtected()){
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.INSIDE_NON_BUILD_RADIUS, false);
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.NON_PLAYER_PLACED, false);
            event.setIssuePresent(PlayerModifyBlockPermissionEvent.Issue.BLACKLISTED_MATERIAL, false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerBlockPlace(BlockPlaceEvent event){
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        final Arena arena = GameAPI.get().getArenaByPlayer(player);

        if (arena == null)
            return;

        if (!Utility.getManager().isPrivateArena(arena))
            return;

        final ArenaBuff buff = Utility.getBuff(arena);
    }
}
