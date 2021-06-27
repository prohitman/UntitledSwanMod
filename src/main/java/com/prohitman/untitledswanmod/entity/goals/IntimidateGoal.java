package com.prohitman.untitledswanmod.entity.goals;

import com.prohitman.untitledswanmod.entity.SwanEntity;
import com.prohitman.untitledswanmod.init.ModSounds;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.util.math.vector.Vector3d;

import javax.naming.ldap.Control;
import java.util.EnumSet;
import java.util.List;

public class IntimidateGoal extends Goal {
    private static final int STARTING_DELAY = 10;
    private static final int ANIMATION_LENGTH = 25;
    private static final double INTIMIDATE_DISTANCE = 12;
    private final SwanEntity swan;
    private int animationTime;
    private int delayTime;
    //private int cooldown;
    protected RavagerEntity targetEntity;
    private Vector3d originalLocation;

    public IntimidateGoal(SwanEntity swan) {
        this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        this.swan = swan;
    }

    public boolean shouldExecute() {
        // Check if creeper nearby
        //if (cooldown-- > 0) {
        //    return false;
       // }
        if (!swan.isChild()/*swan.getGrowingAge() % 5 == 0*/) {
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
        //cooldown = 10;//was 20
        targetEntity = null;
        swan.setIntimidating(false);
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
                //scareRavagerTime = 40;
/*                List<RavagerEntity> list = swan.world.getEntitiesWithinAABB(RavagerEntity.class, swan.getBoundingBox().grow(16, 8, 16));
                for (RavagerEntity e : list) {
                    e.setAttackTarget(null);
                    e.setRevengeTarget(null);
                   // if(scareMobsTime % 5 == 0){
                        Vector3d vec = RandomPositionGenerator.findRandomTargetBlockAwayFrom(e, 20, 7, swan.getPositionVec());
                        if(vec != null){
                            e.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, 1.5D);
                        }
                    }*/
                swan.playSound(ModSounds.SWAN_AMBIENT.get(), 2.0F, 1.0F);
                swan.setIntimidating(true);
            }
            return;
        }
        animationTime--;
    }
}
