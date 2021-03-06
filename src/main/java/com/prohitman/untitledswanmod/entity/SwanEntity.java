package com.prohitman.untitledswanmod.entity;

import com.prohitman.untitledswanmod.entity.goals.IntimidateGoal;
import com.prohitman.untitledswanmod.entity.goals.SwanHurtByTargetGoal;
import com.prohitman.untitledswanmod.init.ModEntities;
import com.prohitman.untitledswanmod.init.ModItems;
import com.prohitman.untitledswanmod.init.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Random;

public class SwanEntity extends Animal implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    private static final AnimationBuilder WALK_ANIM = new AnimationBuilder().addAnimation("animation.swan.walk", true);
    private static final AnimationBuilder SWIM_ANIM = new AnimationBuilder().addAnimation("animation.swan.swim", true);
    private static final AnimationBuilder CHARGE_ANIM = new AnimationBuilder().addAnimation("animation.swan.charge", true);
    private static final AnimationBuilder RUN_ANIM = new AnimationBuilder().addAnimation("animation.swan.run", true);
    private static final AnimationBuilder INTIMIDATION_ANIM = new AnimationBuilder().addAnimation("animation.swan.intimidation", true);
    private static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("animation.swan.idle", true);

    public static final byte ANIMATION_IDLE = 0;
    public static final byte ANIMATION_CLEAN = 1;
    public static final byte ANIMATION_INTIMIDATE = 6;


    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(SwanEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Byte> ANIMATION = EntityDataManager.createKey(SwanEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> INTIMIDATING = EntityDataManager.createKey(SwanEntity.class, DataSerializers.BOOLEAN);

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT, Items.SEAGRASS, Items.KELP);
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    public int timeUntilNextEgg = this.random.nextInt(6000) + 6000;
    public int animationTimer = 0;

    public SwanEntity(EntityType<? extends AnimalEntity> entity, World world) {
        super(entity, world);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, false, FOOD_ITEMS));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new SwimGoal(this));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(4, new SwanEntity.AvoidEntityGoal<>(this, PlayerEntity.class, 4.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D, 60));
        this.goalSelector.addGoal(7, new SwanEntity.MoveToWaterGoal(this, 0.75D));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(10, new IntimidateGoal(this));
        this.targetSelector.addGoal(4, (new SwanHurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, true, this::attackPredicate));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 10.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D).createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0.5D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 10.0D);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(INTIMIDATING, false);
        this.dataManager.register(ANIMATION, ANIMATION_IDLE);
    }

    public int getSwanType() {
        return this.dataManager.get(VARIANT);
    }

    public void setSwanType(int type) {
        this.dataManager.set(VARIANT, type);
    }

    public byte getAnimation() {
        return this.dataManager.get(ANIMATION);
    }

    public void setAnimation(byte animation) {
        this.dataManager.set(ANIMATION, animation);
    }

    public boolean isIntimidating(){ return this.dataManager.get(INTIMIDATING); }

    public void setIntimidating(boolean intimidating){ this.dataManager.set(INTIMIDATING, intimidating); }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
        Random rand = new Random();
        float variantChange = rand.nextFloat();
        if(variantChange <= 0.5F){
            this.setSwanType(1);
        }else{
            this.setSwanType(0);
        }

        this.setPosition(this.getPosX() + 0.01, this.getPosY(), this.getPosZ());
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public static boolean canSpawn(EntityType<SwanEntity> p_234314_0_, IWorld p_234314_1_, SpawnReason p_234314_2_, BlockPos p_234314_3_, Random p_234314_4_) {
        BlockPos.Mutable blockpos$mutable = p_234314_3_.toMutable();

        do {
            blockpos$mutable.move(Direction.UP);
        } while(p_234314_1_.getFluidState(blockpos$mutable).isTagged(FluidTags.WATER));

        return p_234314_1_.getBlockState(blockpos$mutable).isAir();
    }

    public boolean func_230285_a_(Fluid fluidIn) {
        return fluidIn.isIn(FluidTags.WATER);
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        this.doBlockCollisions();
        if (this.isInWater()) {
            this.fallDistance = 0.0F;
        } else {
            super.updateFallState(y, onGroundIn, state, pos);
        }
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

    @Override
    public void tick() {
        super.tick();
        this.updateFloating();
        this.doBlockCollisions();

        // Tick animation timer
        if (animationTimer > 0) {
            animationTimer--;
            if (animationTimer == 0) {
                setAnimation(ANIMATION_IDLE);
            }
        }

        if(this.isIntimidating()){
            List<RavagerEntity> list = this.world.getEntitiesWithinAABB(RavagerEntity.class, this.getBoundingBox().grow(16, 8, 16));
            for (RavagerEntity e : list) {
                e.setAttackTarget(null);
                e.setRevengeTarget(null);
                Vector3d vec = RandomPositionGenerator.findRandomTargetBlockAwayFrom(e, 20, 7, this.getPositionVec());
                if(vec != null){
                    e.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, 1.5D);
                }
            }
        }
    }

    private void updateFloating() {
        if (this.isInWater()) {
            ISelectionContext iselectioncontext = ISelectionContext.forEntity(this);
            if (iselectioncontext.func_216378_a(FlowingFluidBlock.LAVA_COLLISION_SHAPE, this.getPosition(), true) && !this.world.getFluidState(this.getPosition().up()).isTagged(FluidTags.WATER)) {
                this.onGround = true;
            } else {
                double d1 = this.isChild() ? 0.0D : 0.5D;
                this.setMotion(this.getMotion().scale(d1).add(0.0D, 0.05D, 0.0D));
            }
        }

    }

    /**
     * Returns new PathNavigateGround instance
     */
    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return new SwanEntity.WaterPathNavigator(this, worldIn);
    }

    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        if (worldIn.getBlockState(pos).getFluidState().isTagged(FluidTags.WATER)) {
            return 10.0F;
        } else {
            return this.isInWater() ? Float.NEGATIVE_INFINITY : 0.0F;
        }
    }

    public boolean isNotColliding(IWorldReader worldIn) {
        return worldIn.checkNoEntityCollision(this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @SuppressWarnings("rawtypes")
    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        float limbSwingAmount = event.getLimbSwingAmount();
        boolean isMoving = !(limbSwingAmount > -0.05F && limbSwingAmount < 0.05F);
        boolean inWater = isInWater();
        AnimationController controller = event.getController();
/*        if (isFlapping) {
            controller.setAnimation(FLY_ANIM);
            return PlayState.CONTINUE;
        }*/

        byte currentAnimation = this.getAnimation();
        switch (currentAnimation) {

            case ANIMATION_INTIMIDATE:
                controller.setAnimation(INTIMIDATION_ANIM);
                break;
            default:
                if (inWater) {
                    controller.setAnimation(isMoving ? SWIM_ANIM : IDLE_ANIM);
                } else {
                    if (isAggressive()) {
                        controller.setAnimation(CHARGE_ANIM);
                    }
                    else {
                    controller.setAnimation(isMoving ? WALK_ANIM : IDLE_ANIM);
                    }
                    return PlayState.CONTINUE;
                }
                break;
        }

        return PlayState.CONTINUE;
    }



    static class WaterPathNavigator extends GroundPathNavigator {
        WaterPathNavigator(SwanEntity p_i231565_1_, World p_i231565_2_) {
            super(p_i231565_1_, p_i231565_2_);
        }

        protected PathFinder getPathFinder(int p_179679_1_) {
            this.nodeProcessor = new WalkNodeProcessor();
            return new PathFinder(this.nodeProcessor, p_179679_1_);
        }

        protected boolean func_230287_a_(PathNodeType nodeType) {
            return nodeType == PathNodeType.WATER || super.func_230287_a_(nodeType);
        }

        public boolean canEntityStandOnPos(BlockPos pos) {
            return this.world.getBlockState(pos).matchesBlock(Blocks.WATER) || super.canEntityStandOnPos(pos);
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
        int i = this.rand.nextInt(10);
        if(i == 0 && !this.isAggressive()){
            this.playSound(ModSounds.SWAN_AMBIENT.get(), 0.25F, 1.0F);
            return null;
        }
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        this.playSound(ModSounds.SWAN_HURT.get(), 0.75F, 1.0F);
        return null;
    }

    protected SoundEvent getDeathSound() {
        this.playSound(ModSounds.SWAN_DEATH.get(), 0.5F, 1.0F);
        return null;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        if(!this.isInWater()){
            this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
        }
        else{
            this.playSwimSound(0.75f);
        }
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

    @Override
    public void applyEntityCollision(Entity entityIn) {
        super.applyEntityCollision(entityIn);
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

    static class MoveToWaterGoal extends MoveToBlockGoal {
        private final SwanEntity swan;

        private MoveToWaterGoal(SwanEntity swan, double p_i241913_2_) {
            super(swan, p_i241913_2_, 8, 2);
            this.swan = swan;
        }

        public BlockPos func_241846_j() {
            return this.destinationBlock;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return !this.swan.isInWater() && this.shouldMoveTo(this.swan.world, this.destinationBlock);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return !this.swan.isInWater() && super.shouldExecute();
        }

        public boolean shouldMove() {
            return this.timeoutCounter % 20 == 0;
        }

        /**
         * Return true to set given position as destination
         */
        protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
            return worldIn.getBlockState(pos).matchesBlock(Blocks.WATER) && worldIn.getBlockState(pos.up()).allowsMovement(worldIn, pos, PathType.LAND);
        }
    }
}
