package game.script.ui.game;

import game.cycle.scene.game.world.creature.Player;
import game.cycle.scene.game.world.skill.Skill;
import game.cycle.scene.ui.list.UIGame;
import game.script.Script;
import game.script.game.event.GameEvents;

public class ui_GameSkill implements Script {

	private UIGame ui;
	private Player player;
	private Skill skill;
	
	public ui_GameSkill(UIGame ui, Player player, Skill skill) {
		this.ui = ui;
		this.player = player;
		this.skill = skill;
	}

	@Override
	public void execute() {
		if(skill.range == 0.0f){ // self cast
			GameEvents.useSkillSelfTarget(player, skill);
		}
		else{ // target cast
			switch (skill.type) {
				case Skill.typeMelee:
					ui.setMode(UIGame.modeSkillMelee);
					break;

				case Skill.typeRange:
					ui.setMode(UIGame.modeSkillRange);
					break;
					
				case Skill.typeSpell:
					ui.setMode(UIGame.modeSkillSpell);
					break;
					
				case Skill.typeNull:
				default:
					ui.setMode(UIGame.modeSkillNull);
					break;
			}
			
			player.setUsedSkill(skill);
		}
	}
}