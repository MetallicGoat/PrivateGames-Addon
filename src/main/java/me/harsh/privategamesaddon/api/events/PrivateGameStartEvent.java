package me.harsh.privategamesaddon.api.events;

import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.ArenaEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrivateGameStartEvent extends Event implements ArenaEvent {
  private static final HandlerList list = new HandlerList();
  Arena arena;

  public PrivateGameStartEvent(Arena arena) {
    this.arena = arena;
  }

  @Override
  public HandlerList getHandlers() {
    return list;
  }

  public Arena getArena() {
    return arena;
  }
}
