% Факты с одним аргументом: герои, роли, атрибуты
% Герои
hero(antimage).
hero(axe).
hero(crystal_maiden).
hero(pudge).
hero(invoker).
hero(sven).
hero(windranger).
hero(lina).
hero(earthshaker).
hero(lion).
hero(spectre).
hero(faceless_void).
hero(rubick).
hero(naga_siren).
hero(batrider).
hero(drow_ranger).
hero(juggernaut).
hero(zeus).
hero(slark).
hero(lich).
hero(tiny).

% Роли
role(carry).
role(support).
role(disabler).
role(nuker).
role(initator).
role(durable).

% Атрибуты (силовые, ловкие, интеллект)
attribute(strength).
attribute(agility).
attribute(intelligence).

% Факты с двумя аргументами: роли и атрибуты героев, способности героев
% Связь героя с его основной ролью
hero_role(antimage, carry).
hero_role(axe, durable).
hero_role(crystal_maiden, support).
hero_role(pudge, durable).
hero_role(invoker, nuker).
hero_role(sven, carry).
hero_role(windranger, carry).
hero_role(lina, nuker).
hero_role(earthshaker, initator).
hero_role(lion, disabler).
hero_role(spectre, carry).
hero_role(faceless_void, carry).
hero_role(rubick, support).
hero_role(naga_siren, carry).
hero_role(batrider, initiator).
hero_role(drow_ranger, carry).
hero_role(juggernaut, carry).
hero_role(zeus, nuker).
hero_role(slark, carry).
hero_role(lich, support).
hero_role(tiny, initiator).

% Связь героя с атрибутом
hero_attribute(antimage, agility).
hero_attribute(axe, strength).
hero_attribute(crystal_maiden, intelligence).
hero_attribute(pudge, strength).
hero_attribute(invoker, intelligence).
hero_attribute(sven, strength).
hero_attribute(windranger, agility).
hero_attribute(lina, intelligence).
hero_attribute(earthshaker, strength).
hero_attribute(lion, intelligence).
hero_attribute(spectre, agility).
hero_attribute(faceless_void, agility).
hero_attribute(rubick, intelligence).
hero_attribute(naga_siren, agility).
hero_attribute(batrider, intelligence).
hero_attribute(drow_ranger, agility).
hero_attribute(juggernaut, agility).
hero_attribute(zeus, intelligence).
hero_attribute(slark, agility).
hero_attribute(lich, intelligence).
hero_attribute(tiny, strength).

% Способности героев
hero_ability(antimage, mana_void).
hero_ability(axe, berserkers_call).
hero_ability(crystal_maiden, freezing_field).
hero_ability(pudge, rot).
hero_ability(invoker, sun_strike).
hero_ability(sven, god_strength).
hero_ability(windranger, focus_fire).
hero_ability(lina, laguna_blade).
hero_ability(earthshaker, echo_slam).
hero_ability(lion, finger_of_death).
hero_ability(spectre, haunt).
hero_ability(faceless_void, chronosphere).
hero_ability(rubick, spell_steal).
hero_ability(naga_siren, song_of_the_siren).
hero_ability(batrider, flaming_lasso).
hero_ability(drow_ranger, marksmanship).
hero_ability(juggernaut, omnislash).
hero_ability(zeus, thunder_god_wrath).
hero_ability(slark, shadow_dance).
hero_ability(lich, chain_frost).
hero_ability(tiny, avalanche).

% Правила
% Правило: герой считается ловким, если его атрибут - agility
agile_hero(Hero) :- hero(Hero), hero_attribute(Hero, agility).

% Правило: герой является керри, если его основная роль - carry
is_carry(Hero) :- hero(Hero), hero_role(Hero, carry).

% Проверка, является ли герой магом
is_mage(Hero) :-
    hero(Hero),
    (hero_role(Hero, nuker); hero_role(Hero, support); hero_role(Hero, disabler)).

% Проверка, является ли герой танком
is_tank(Hero) :-
    hero(Hero),
    (hero_role(Hero, durable); hero_role(Hero, initiator)).

% Правило: герой имеет ультимативную способность, если у него есть конкретное умение
has_ultimate(Hero, Ability) :- hero_ability(Hero, Ability).

% Правило: герой считается сильным в командных боях, если он инициатор
team_fighter(Hero) :- hero_role(Hero, initiator).

% Найти героя, который не имеет указанной роли
hero_no_role(Hero, Role) :-
    hero(Hero),
    \+ hero_role(Hero, Role).

% Найти героя, который не имеет указанного атрибута
hero_no_attribute(Hero, Attribute) :-
    hero(Hero),
    \+ hero_attribute(Hero, Attribute).

