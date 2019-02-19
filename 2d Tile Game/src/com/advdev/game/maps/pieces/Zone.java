package com.advdev.game.maps.pieces;

import com.advdev.display.interfaces.handlers.DialogueHandler;
import com.advdev.game.entities.Entity;
import com.advdev.game.entities.npcs.Npc;
import com.advdev.game.items.Item;
import com.advdev.game.maps.Zoneable;
import com.advdev.game.objects.GameObject;
import com.advdev.misc.Constants;

public class Zone {
	private GameObject baseGem;
	private final GameObject blackGem = new GameObject(4, 2);
	private final GameObject blueGem = new GameObject(4, 0);
	private final GameObject bluePortal = new GameObject(3, 1);
	private boolean containsPortal;
	private Zoneable contents;
	private GameObject enterance, gem;
	private int idleProfits;
	private double inZone;
	private long last;
	private int minToCollect;
	private Entity owner;
	private final GameObject pinkPortal = new GameObject(3, 0);
	private final GameObject yellowGem = new GameObject(4, 5);
	private final GameObject yellowPortal = new GameObject(3, 2);

	public Zone() {
		this.containsPortal = true;
		this.idleProfits = 25;
		this.enterance = this.pinkPortal;
	}

	public Zone(Zoneable contents, double rarity) {
		this.contents = contents;
		setIdleProfits(rarity);
		this.enterance = contents instanceof GameObject ? this.bluePortal : this.yellowPortal;
		this.gem = contents instanceof GameObject ? this.blueGem : this.yellowGem;
		this.baseGem = this.gem;
	}

	public void click() {
		//TODO: Check if next to tile then walk to or enter
	}

	public void collect(Entity entity) {
		if (entity != this.owner) return;// TODO send message about not taking peoples shit
		int toTake = (int) this.inZone;
		this.inZone -= toTake;
		entity.addCoins(toTake);
	}

	public boolean containsPortal() {
		return this.containsPortal;
	}

	public void enterZone(Entity entering, Region region) {
		if (!((this.owner == entering) || (this.owner == null))) return;
		this.owner = entering;
		region.checkZones();
		if (hasObject()) {
			((GameObject) this.contents).harvest();
		} else if (hasNpc()) {
			((Npc) this.contents).kill();
		} else {
			DialogueHandler.displayTwoOptions("Teleport to Home Map", "Teleport to new Map");
		}
	}

	/**
	 *
	 * @return Npc else GameObject
	 */
	public String getContentType() {
		return (hasNpc() ? "Hunting" : "Gathering") + " Zone";
	}

	public GameObject getEnterance() {
		return isComplete() ? this.gem : this.enterance;
	}

	public int getIdleProfits() {
		return (int) (this.inZone);
	}

	public int getMinToCollect() {
		return this.minToCollect;
	}

	public Npc getNpc() {
		if (!(this.contents instanceof Npc)) return null;
		return ((Npc) this.contents);
	}

	public GameObject getObject() {
		if (!(this.contents instanceof GameObject)) return null;
		return ((GameObject) this.contents);
	}

	public Entity getOwner() {
		return this.owner;
	}

	public int getProfitPerMinute() {
		return this.idleProfits;
	}

	public boolean hasNpc() {
		return (this.contents != null) && (this.contents instanceof Npc);
	}

	public boolean hasObject() {
		return (this.contents != null) && (this.contents instanceof GameObject);
	}

	public boolean isComplete() {
		return this.containsPortal ? false
				: hasNpc() ? ((Npc) this.contents).isDead() : ((GameObject) this.contents).harvested();
	}

	public void process() {
		this.enterance.process();
		if (this.gem != null) {
			this.gem.process();
		}
		if (isComplete()) {
			double percOfMinute = ((System.currentTimeMillis() - this.last) / 60000.0);
			double profitSinceLast = percOfMinute * this.idleProfits;
			this.inZone += profitSinceLast;
			if (this.inZone < this.minToCollect) {
				this.gem = this.blackGem;
			} else {
				this.gem = this.baseGem;
			}
		}
		this.last = System.currentTimeMillis();
	}

	private void setIdleProfits(double rarity) {
		if (rarity < 0) {
			rarity = 0;
		}
		if (rarity > 10) {
			rarity = 10;
		}
		this.minToCollect = (int) ((rarity + 1) * 15);
		this.idleProfits = 5 + Constants.getRandomInt((int) (15 * rarity));
	}

	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	public void useItemOn(Item selected) {
		// TODO: Use Item On Zone
	}
}
