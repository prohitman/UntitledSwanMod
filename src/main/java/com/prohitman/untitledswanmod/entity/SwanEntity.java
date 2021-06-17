package com.prohitman.untitledswanmod.entity;

import com.prohitman.untitledswanmod.init.ModEntities;
import com.prohitman.untitledswanmod.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class SwanEntity extends AnimalEntity {
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(SwanEntity.class, DataSerializers.VARINT);

    private static final Ingredient FOOD_ITEMS = Ingredient.fromItems(Items.CARROT, Items.POTATO, Items.BEETROOT, Items.GRASS);
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    public int timeUntilNextEgg = this.rand.nextInt(6000) + 6000;

    public SwanEntity(EntityType<? extends AnimalEntity> entity, World world) {
        super(entity, world);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    protected void registerGoals() {
        //this.goalSelector.addGoal(0, new SwimGoal(this));
        //this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, false, FOOD_ITEMS));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 0.75D, false));
        this.goalSelector.addGoal(3, new SwimGoal(this));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new SwanEntity.AvoidEntityGoal<>(this, PlayerEntity.class, 4.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new SwanEntity.GoToWaterGoal(this, 0.75D));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, true, this::attackPredicate));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 10.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D).createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0.5D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 10.0D);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, 0);
    }

    public int getSwanType() {
        return this.dataManager.get(VARIANT);
    }

    public void setSwanType(int p_175529_1_) {
        this.dataManager.set(VARIANT, p_175529_1_);
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
        Random rand = new Random();
        float variantChange = rand.nextFloat();
        if(variantChange <= 0.5F){
            this.setSwanType(1);
        }else{
            this.setSwanType(0);
        }
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean attackPredicate(LivingEntity player){
        return !this.isChild() && EntityPredicates.CAN_HOSTILE_AI_TARGET.test(player);
    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        return super.getExperiencePoints(player);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("EggLayTime")) {
            this.timeUntilNextEgg = compound.getInt("EggLayTime");
        }
        this.setSwanType(compound.getInt("SwanType"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("EggLayTime", this.timeUntilNextEgg);
        compound.putInt("SwanType", this.getSwanType());
    }

    public void livingTick() {
        super.livingTick();
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }

        this.wingRotDelta = (float)((double)this.wingRotDelta * 0.9D);
        Vector3d vector3d = this.getMotion();
        if (!this.onGround && vector3d.y < 0.0D) {
            this.setMotion(vector3d.mul(1.0D, 0.6D, 1.0D));
        }

        this.wingRotation += this.wingRotDelta * 2.0F;
        if (!this.world.isRemote && this.isAlive() && !this.isChild() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.entityDropItem(ModItems.SWAN_EGG.get());
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean canBreed() {
        return true;
    }

    protected float getStandingEyeHeight(Pose pose, EntitySize size) {
        return  size.height * 0.92F;
    }

    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return ModEntities.SWAN_ENTITY.get().create(world);
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    static class AvoidEntityGoal<T extends LivingEntity> extends net.minecraft.entity.ai.goal.AvoidEntityGoal<T> {
        private final SwanEntity swan;

        public AvoidEntityGoal(SwanEntity swan, Class<T> entity, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
            super(swan, entity, avoidDistanceIn, farSpeedIn, nearSpeedIn);
            this.swan = swan;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return this.swan.isChild() && super.shouldExecute();
        }
    }

    static class GoToWaterGoal extends MoveToBlockGoal {
        private final SwanEntity swan;

        private GoToWaterGoal(SwanEntity swan, double speedIn) {
            super(swan, swan.isChild() ? 2.0D : speedIn, 24);
            this.swan = swan;
            this.field_203112_e = 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return !this.swan.isInWater() && this.timeoutCounter <= 100 && this.shouldMoveTo(this.swan.world, this.destinationBlock);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (this.swan.isChild() && !this.swan.isInWater()) {
                return super.shouldExecute();
            } else {
                return !this.swan.isInWater() ? super.shouldExecute() : false;
            }
        }

        public boolean shouldMove() {
            return this.timeoutCounter % 50 == 0;
        }

        /**
         * Return true to set given position as destination
         */
        protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
            return worldIn.getBlockState(pos).matchesBlock(Blocks.WATER);
        }
    }
}
