package com.prohitman.untitledswanmod.entity.goals;

import com.prohitman.untitledswanmod.entity.SwanEntity;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.util.math.vector.Vector3d;

import javax.naming.ldap.Control;
import java.util.EnumSet;

public class IntimidateGoal extends Goal {
    private static final int STARTING_DELAY = 10;
    private static final int ANIMATION_LENGTH = 25;
    private static final double INTIMIDATE_DISTANCE = 8;
    private final SwanEntity swan;
    private int animationTime;
    private int delayTime;
    private int cooldown;
    protected Entity targetEntity;
    private Vector3d originalLocation;

    public IntimidateGoal(SwanEntity swan) {
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        this.swan = swan;
    }

    public boolean shouldExecute() {
        // Check if creeper nearby
        if (cooldown-- > 0) {
            return false;
        }
        if (swan.getGrowingAge() % 5 == 0) {
            targetEntity = swan.world.getClosestEntity(RavagerEntity.class, new EntityPredicate(), swan, swan.getPosX(), swan.getPosY(), swan.getPosZ(), swan.getBoundingBox().expand(INTIMIDATE_DISTANCE, 3, INTIMIDATE_DISTANCE));
            return targetEntity != null;
        }
        return false;
    }

    @Override
    public void startExecuting() {
        swan.getNavigator().clearPath();
        animationTime = ANIMATION_LENGTH;
        delayTime = STARTING_DELAY;

        originalLocation = swan.getPositionVec();
        swan.getNavigator().tryMoveToEntityLiving(targetEntity, 1.2);
    }

    @Override
    public void resetTask() {
        swan.setAnimation(SwanEntity.ANIMATION_IDLE);
        swan.getNavigator().tryMoveToXYZ(originalLocation.x, originalLocation.y, originalLocation.z, 1.2);
        cooldown = 20;
        targetEntity = null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetEntity.isAlive() && swan.getDistanceSq(originalLocation) <= INTIMIDATE_DISTANCE*INTIMIDATE_DISTANCE && animationTime >= 0;
    }

    @Override
    public void tick() {
        swan.getLookController().setLookPosition(targetEntity.getPosX(), targetEntity.getPosYEye(), targetEntity.getPosZ());
        if (delayTime > 0) {
            delayTime--;
            if (delayTime == 0) {
                swan.getNavigator().clearPath();
                swan.setAnimation(SwanEntity.ANIMATION_INTIMIDATE);
                //swan.playSound(ModSoundEvents.getswanHonkSound(), 1.0f, 1.0f);
            }
            return;
        }
        animationTime--;
    }
}
