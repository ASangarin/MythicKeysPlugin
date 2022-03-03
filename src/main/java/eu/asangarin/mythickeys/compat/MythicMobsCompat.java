package eu.asangarin.mythickeys.compat;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class MythicMobsCompat {
	public static void runSkill(String id, Player player) {
		if (id == null || id.isEmpty()) return;

		MythicMobs.inst().getSkillManager().getSkill(id).ifPresent(skill -> {
			AbstractPlayer trigger = BukkitAdapter.adapt(player);
			GenericCaster genericCaster = new GenericCaster(trigger);
			SkillMetadata skillMeta = new SkillMetadata(SkillTrigger.API, genericCaster, trigger, BukkitAdapter.adapt(player.getLocation()),
					new HashSet<>(), null, 1.0F);
			if (skill.usable(skillMeta, SkillTrigger.API)) skill.execute(skillMeta);
		});
	}
}
