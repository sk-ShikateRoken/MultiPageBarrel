package shikateroken.multipagebarrel.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MultipageBarrelBlock extends BaseEntityBlock {
    public static final MapCodec<MultipageBarrelBlock> CODEC = simpleCodec(MultipageBarrelBlock::new);

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public MultipageBarrelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    // 2. 必須の codec メソッドをオーバーライドして、上で定義した CODEC を返します
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    // 【追加】ブロックを設置する際、プレイヤーの視線を元に向きを決定するメソッド
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // バニラの樽と同じように、プレイヤーが見ている方向の「反対側（プレイヤー側）」に蓋を向ける
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    // 【追加】ストラクチャーブロックなどで建物を回転させた時に、このブロックも一緒に回転させるための処理
    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    // 【追加】ストラクチャーブロック等で反転させた時の処理
    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }



    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MultipageBarrelBlockEntity barrel) {
                player.openMenu(barrel, pos);

            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MultipageBarrelBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // 中身をばらまくループ処理は削除しました（アイテム化した樽の中に保持するため）
            level.updateNeighbourForOutputSignal(pos, this);
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    // 【追加】ブロック破壊時のドロップアイテムに、中身のデータを書き込む
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // デフォルトのドロップ（LootTableの設定など）を取得
        List<ItemStack> drops = super.getDrops(state, builder);

        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof MultipageBarrelBlockEntity barrel) {
            boolean hasAddedData = false;

            // ドロップアイテムの中にこの樽自身があった場合、データを保存する
            for (ItemStack drop : drops) {
                if (drop.is(this.asItem())) {
                    barrel.saveToItem(drop, builder.getLevel().registryAccess());
                    hasAddedData = true;
                }
            }

            // もしLootTableが未設定でドロップが空だった場合のフェールセーフ（確実にドロップさせる）
            if (!hasAddedData) {
                ItemStack stack = new ItemStack(this);
                barrel.saveToItem(stack, builder.getLevel().registryAccess());
                return java.util.List.of(stack);
            }
        }
        return drops;
    }

    // 【追加】クリエイティブモードでホイールクリック(Pick Block)した時にも中身を保持する
    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MultipageBarrelBlockEntity barrel) {
            barrel.saveToItem(stack, level.registryAccess());
        }
        return stack;
    }
}
