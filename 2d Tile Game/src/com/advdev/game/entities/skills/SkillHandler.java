package com.advdev.game.entities.skills;

import java.lang.reflect.InvocationTargetException;

import com.advdev.game.entities.Entity;

public class SkillHandler {
	private Skill[] skills;

	public SkillHandler(Entity entity) {
		this.skills = new Skill[classes.length];
		for (int i = 0; i < this.skills.length; i++) {
			try {
				Skill skill = (Skill) classes[i].getDeclaredConstructor(Entity.class).newInstance(entity);
				this.skills[i] = skill;
			} catch (InstantiationException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public Skill getSkill(String name) {
		for (Skill s : this.skills) {
			if (s.getName().equals(name)) return s;
		}
		return null;
	}

	private static Class<?>[] classes = {
			Firemaking.class
	};
}
