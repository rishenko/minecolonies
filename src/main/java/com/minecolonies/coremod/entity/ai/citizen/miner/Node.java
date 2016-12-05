package com.minecolonies.coremod.entity.ai.citizen.miner;

import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

/**
 * Miner Node Data Structure.
 * <p>
 * When a node is completed we should add the surrounding nodes to level as AVAILABLE
 * also note that we don't want node (0, -1) because there will be a ladder on the back
 * wall of the initial node, and we cant put the connection through the ladder
 *
 */
public class Node
{
    /**
     * Tags used to save and retrieve data from NBT.
     */
    private static final String TAG_X                 = "idX";
    private static final String TAG_Z                 = "idZ";
    private static final String TAG_STYLE             = "Style";
    private static final String TAG_STATUS            = "Status";
    private static final String TAG_STATUS_POSITIVE_X = "positiveX";
    private static final String TAG_STATUS_NEGATIVE_X = "negativeX";
    private static final String TAG_STATUS_POSITIVE_Z = "positiveZ";
    private static final String TAG_STATUS_NEGATIVE_Z = "negativeZ";

    /**
     * X position of the Node.
     */
    private final int x;

    /**
     * Z position of the node.
     */
    private final int z;

    /**
     * Style of the node.
     */
    private NodeType   style;

    /**
     * Status of the node.
     */
    private NodeStatus status;

    /**
     * Status in positive x direction.
     */
    private NodeStatus directionPosX;

    /**
     * Status in negative x direction.
     */
    private NodeStatus directionNegX;

    /**
     * Status in positive z direction.
     */
    private NodeStatus directionPosZ;

    /**
     * Status in negative z direction.
     */
    private NodeStatus directionNegZ;

    /**
     * Initializes the node.
     * Requires a location in the node as parameters
     *
     * @param x X-coordinate in the node
     * @param z Z-coordinate in the node
     */
    public Node(final int x, final int z)
    {
        this.x = x;
        this.z = z;
        style = NodeType.CROSSROAD;
        status = NodeStatus.AVAILABLE;
        directionPosX = NodeStatus.AVAILABLE;
        directionNegX = NodeStatus.AVAILABLE;
        directionPosZ = NodeStatus.AVAILABLE;
        directionNegZ = NodeStatus.AVAILABLE;
    }

    /**
     * Creates a node from the NBT Tag.
     * Returns the created node
     *
     * @param compound Compound to read from
     * @return Node created from compound
     */
    @NotNull
    public static Node createFromNBT(@NotNull final NBTTagCompound compound)
    {
        final int x = compound.getInteger(TAG_X);
        final int z = compound.getInteger(TAG_Z);

        final NodeType style = NodeType.valueOf(compound.getString(TAG_STYLE));

        final NodeStatus status = NodeStatus.valueOf(compound.getString(TAG_STATUS));

        //Set the node status in all directions.
        final NodeStatus directionPosX = NodeStatus.valueOf(compound.getString(TAG_STATUS_POSITIVE_X));
        final NodeStatus directionNegX = NodeStatus.valueOf(compound.getString(TAG_STATUS_NEGATIVE_X));
        final NodeStatus directionPosZ = NodeStatus.valueOf(compound.getString(TAG_STATUS_POSITIVE_Z));
        final NodeStatus directionNegZ = NodeStatus.valueOf(compound.getString(TAG_STATUS_NEGATIVE_Z));

        @NotNull final Node node = new Node(x, z);
        node.setStyle(style);
        node.setStatus(status);
        node.setDirectionPosX(directionPosX);
        node.setDirectionNegX(directionNegX);
        node.setDirectionPosZ(directionPosZ);
        node.setDirectionNegZ(directionNegZ);

        return node;
    }

    /**
     * Returns the status of the positive X node.
     *
     * @return {@link NodeStatus}
     */
    public NodeStatus getDirectionPosX()
    {
        return directionPosX;
    }

    /**
     * Sets the node status of the positive x node.
     *
     * @param directionPosX {@link NodeStatus} of Positive X node
     */
    public void setDirectionPosX(final NodeStatus directionPosX)
    {
        this.directionPosX = directionPosX;
    }

    /**
     * Returns the status of the negative X node.
     *
     * @return {@link NodeStatus}
     */
    public NodeStatus getDirectionNegX()
    {
        return directionNegX;
    }

    /**
     * Sets the node status of the negative x node.
     *
     * @param directionNegX {@link NodeStatus} of Negative X node
     */
    public void setDirectionNegX(final NodeStatus directionNegX)
    {
        this.directionNegX = directionNegX;
    }

    /**
     * Returns the status of the positive Z node.
     *
     * @return {@link NodeStatus}
     */
    public NodeStatus getDirectionPosZ()
    {
        return directionPosZ;
    }

    /**
     * Sets the node status of the positive z node.
     *
     * @param directionPosZ {@link NodeStatus} of positive Z node
     */
    public void setDirectionPosZ(final NodeStatus directionPosZ)
    {
        this.directionPosZ = directionPosZ;
    }

    /**
     * Returns the status of the negative Z node.
     *
     * @return {@link NodeStatus}
     */
    public NodeStatus getDirectionNegZ()
    {
        return directionNegZ;
    }

    /**
     * Sets the node status of the negative z node.
     *
     * @param directionNegZ {@link NodeStatus} of negative Z node
     */
    public void setDirectionNegZ(final NodeStatus directionNegZ)
    {
        this.directionNegZ = directionNegZ;
    }

    /**
     * Writes the node to a NBT-compound.
     *
     * @param compound Compound to write to
     */
    public void writeToNBT(@NotNull final NBTTagCompound compound)
    {
        compound.setInteger(TAG_X, x);
        compound.setInteger(TAG_Z, z);

        compound.setString(TAG_STYLE, style.name());

        compound.setString(TAG_STATUS, status.name());

        compound.setString(TAG_STATUS_POSITIVE_X, directionPosX.name());
        compound.setString(TAG_STATUS_NEGATIVE_X, directionNegX.name());
        compound.setString(TAG_STATUS_POSITIVE_Z, directionPosZ.name());
        compound.setString(TAG_STATUS_NEGATIVE_Z, directionNegZ.name());
    }

    /**
     * Returns the x-coordinate in the node.
     *
     * @return x-coordinate
     */
    public int getX()
    {
        return x;
    }

    /**
     * Returns the z-coordinate in the node.
     *
     * @return z-coordinate
     */
    public int getZ()
    {
        return z;
    }

    /**
     * Returns the {@link NodeStatus} of the current node.
     *
     * @return {@link NodeStatus}
     */
    public NodeStatus getStatus()
    {
        return status;
    }

    /**
     * Sets the status of the current node.
     *
     * @param status {@link NodeStatus}
     */
    public void setStatus(final NodeStatus status)
    {
        this.status = status;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "Node{" + "x=" + x +
                ", z=" + z +
                ", style=" + style +
                ", status=" + status +
                ", directionPosX=" + directionPosX +
                ", directionNegX=" + directionNegX +
                ", directionPosZ=" + directionPosZ +
                ", directionNegZ=" + directionNegZ +
                '}';

    }

    /**
     * Returns the {@link NodeType} of the current node.
     *
     * @return {@link NodeType}
     */
    public NodeType getStyle()
    {
        return style;
    }

    /**
     * Sets the {@link NodeType} of the current node.
     *
     * @param style {@link NodeType}
     */
    public void setStyle(final NodeType style)
    {
        this.style = style;
    }

    /**
     * Sets the status of the node.
     * AVAILABLE means it can be mined
     * IN_PROGRESS means it is currently being mined
     * COMPLETED means it has been mined and all torches/wood structure has been placed
     * LADDER means this side has the ladder and must not be mined
     */
    enum NodeStatus
    {
        AVAILABLE,
        IN_PROGRESS,
        COMPLETED,
        WALL,
        LADDER
    }

    /**
     * Sets the node style used.
     */
    enum NodeType
    {
        //Main shaft
        SHAFT,
        //Node on the back of the ladder (Don't mine the ladder)
        LADDER_BACK,
        //Simple straight tunnle.
        TUNNEL,
        //Crossroad structure
        CROSSROAD,
        //Bending tunnle
        BEND
    }
}
