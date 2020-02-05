package com.aaronhowser1.biggerblackerlotus;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import java.util.List;

public class BiggerBlackerLotusItem extends Item implements IManaDissolvable {
    public BiggerBlackerLotusItem() {
        super(new Item.Properties()
                .group(BotaniaCreativeTab.INSTANCE));
    }

    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    public void onDissolveTick(IManaPool pool, ItemStack stack, ItemEntity item) {
        if (!pool.isFull() && pool.getCurrentMana() != 0) {
            TileEntity tile = (TileEntity)pool;
            if (!item.world.isRemote) {
//              Gives double the amount of Mana as a puny, normal Blacker Lotus
                pool.recieveMana(200000);
                stack.shrink(1);
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(item.world, tile.getPos());
                PacketHandler.sendToNearby(item.world, item, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.BLACK_LOTUS_DISSOLVE, item.posX, (double)tile.getPos().getY() + 0.5D, item.posZ, new int[0]));
            }

            item.playSound(ModSounds.blackLotus, 0.5F,0.05F);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        list.add((new TranslationTextComponent("bbl.desc", new Object[0])).applyTextStyle(TextFormatting.RED));
    }
}
