package me.harsh.privategamesaddon.events;

import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import de.marcely.bedwars.api.arena.AddPlayerCause;
import de.marcely.bedwars.api.arena.AddPlayerIssue;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import de.marcely.bedwars.api.event.arena.RoundStartEvent;
import de.marcely.bedwars.api.event.player.PlayerJoinArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerQuitArenaEvent;
import de.marcely.bedwars.api.event.player.PlayerStatChangeEvent;
import me.harsh.privategamesaddon.api.events.PrivateGameCreateEvent;
import me.harsh.privategamesaddon.api.events.PrivateGameEndEvent;
import me.harsh.privategamesaddon.api.events.PrivateGameStartEvent;
import me.harsh.privategamesaddon.managers.PrivateGameManager;
import me.harsh.privategamesaddon.settings.Settings;
import me.harsh.privategamesaddon.utils.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;

import java.util.UUID;

public class PlayerListener implements Listener {
    private final PrivateGameManager manager;
    public PlayerListener(PrivateGameManager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onArenaJoin(PlayerJoinArenaEvent event){
        final Arena arena = event.getArena();
        final Player player = event.getPlayer();
        if (manager.getPrivateArenas().contains(arena)){
            if (event.getCause() == AddPlayerCause.PARTY_SWITCH_ARENA) {
                event.addIssue(AddPlayerIssue.PLUGIN);
            }
            final PartyPlayer partyPlayer = Utility.getPlayer(player);
            if (partyPlayer.isInParty()){
                final Party party = Utility.getParty(player);
                final Party p  = manager.partyMembersMangingMap.get(arena);
                if (party == p){
                    final String name = Bukkit.getPlayer(party.getLeader()).getName();
                    Utility.doStatsThing(player.getUniqueId());
                    Common.tell(player, Settings.PREFIX + " You have joined " + name + "'s private game!");
                    return;
                }
            }
            final Party p  = manager.partyMembersMangingMap.get(arena);
            Valid.checkNotNull(p);
            if (p.getMembers().contains(player.getUniqueId())){
                return;
            }
            Common.tell(player, Settings.PREFIX + "&cArena is private!");
            event.addIssue(AddPlayerIssue.PLUGIN);
        }

        if (manager.checkPlayer(player) && manager.getMode(player)){
            manager.getPrivateArenas().add(arena);
            final PartyPlayer partyPlayer = Utility.getPlayer(player);
            if (partyPlayer.isInParty()){
                final Party party = Utility.getParty(player);
                manager.partyMembersMangingMap.put(arena,party);
                Utility.doStatsThing(player.getUniqueId());
                Bukkit.getServer().getPluginManager().callEvent(new PrivateGameCreateEvent(player, arena));
                if (party.getMembers().size() == 1) {
                    Common.tell(player, Settings.PREFIX + "&c Couldn't Find Anyone in your party please invite some friend and warp them using /bwp warp :-)");
                }
            }
        }
    }

    @EventHandler
    public void onArenaEnd(RoundEndEvent event){
        final Arena arena = event.getArena();
        if (manager.getPrivateArenas().contains(arena)){
            Bukkit.getServer().getPluginManager().callEvent(new PrivateGameEndEvent(arena, event.getWinners(), event.getWinnerTeam()));
        }
        manager.getPrivateArenas().remove(arena);
        manager.partyMembersMangingMap.remove(arena);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitArenaEvent event){
        final Arena arena = event.getArena();
        final Player player = event.getPlayer();
        if (manager.checkPlayer(player) && manager.getPrivateArenas().contains(arena)){
            manager.getPrivateArenas().remove(arena);
        }
        manager.partyMembersMangingMap.remove(arena);
    }
    @EventHandler
    public void onRoundStart(RoundStartEvent event){
        final Arena arena = event.getArena();
        if (manager.getPrivateArenas().contains(arena)){
            Bukkit.getServer().getPluginManager().callEvent(new PrivateGameStartEvent(arena));
        }
    }

    @EventHandler
    public void onPlayerStatGain(PlayerStatChangeEvent event){
        final UUID uuid = event.getStats().getPlayerUUID();
        if (manager.playerStatsList.contains(uuid)){
            manager.playerStatsList.remove(uuid);
            event.setCancelled(true);
        }
    }
}
