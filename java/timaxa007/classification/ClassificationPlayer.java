package timaxa007.classification;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import timaxa007.classification.network.SyncClassificationMessage;

public class ClassificationPlayer implements IExtendedEntityProperties {

	private static final String ID = "ClassificationPlayer.v1";
	private EntityPlayer player;
	private Classification classification = Classification.NONE;

	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		if (classification == Classification.NONE) return;
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("classification", classification.getID());
		nbt.setTag(ID, tag);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		if (!nbt.hasKey(ID)) return;
		NBTTagCompound tag = nbt.getCompoundTag(ID);
		if (tag.hasKey("classification"))
			classification = Classification.getClassification(tag.getByte("classification"));
	}

	@Override
	public void init(Entity entity, World world) {
		if (entity instanceof EntityPlayer) player = (EntityPlayer)entity;
	}

	public static String reg(EntityPlayer player) {
		return player.registerExtendedProperties(ClassificationPlayer.ID, new ClassificationPlayer());
	}

	public static ClassificationPlayer get(EntityPlayer player) {
		return (ClassificationPlayer)player.getExtendedProperties(ClassificationPlayer.ID);
	}
	/*
	public boolean isClassification(Classification classification) {
		return classification >= 0 && classification <= Integer.MAX_VALUE;
	}
	 */
	public void setClassification(Classification classification) {
		if (this.classification != classification) {
			changeClassification(classification);
		}
	}

	public void changeClassification(Classification classification) {
		if (player instanceof EntityPlayerMP) {
			SyncClassificationMessage message = new SyncClassificationMessage();
			message.classification = classification.getID();
			ClassificationMod.network.sendTo(message, (EntityPlayerMP)player);
		}
		this.classification = classification;
	}

	public Classification getClassification() {
		return classification;
	}

	public static enum Classification {

		NONE	((byte)0,	"none"),
		WARRIOR	((byte)1,	"warrior"),
		MAGE	((byte)2,	"mage"),
		ROGUE	((byte)3,	"rogue"),//Разбойник
		PRIEST	((byte)4,	"priest"),
		DRUID	((byte)5,	"druid");

		private final byte id;
		private final String tag;

		Classification(final byte id, final String tag) {
			this.id = id;
			this.tag = tag;
		}

		public final byte getID() {
			return id;
		}

		public final String getTag() {
			return tag;
		}

		public static final Classification getClassification(final byte id) {
			for (Classification classification : values())
				if (classification.getID() == id) return classification;
			return Classification.NONE;
		}

		public static final Classification getClassification(final String tag) {
			for (Classification classification : values())
				if (classification.getTag().equalsIgnoreCase(tag)) return classification;
			return null;
		}

		public static final String[] getClassificationTags() {
			final String[] tags = new String[values().length];
			for (int i = 0; i < values().length; ++i) tags[i] = values()[i].getTag();
			return tags;
		}

	}

}
