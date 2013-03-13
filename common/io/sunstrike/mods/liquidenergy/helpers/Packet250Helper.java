package io.sunstrike.mods.liquidenergy.helpers;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.*;

/*
 * Packet250Helper
 * io.sunstrike.mods.liquidenergy.helpers
 * LiquidEnergy
 *
 * The MIT License (MIT)
 * Copyright (c) 2013 Sunstrike <sunstrike@azurenode.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */

/**
 * Helper for constructing and using custom payload packets (Packet250CustomPayload).
 *
 * @author Sunstrike <sunstrike@azurenode.net>
 */
public class Packet250Helper {

    /**
     * Packet builder - Location (with dimID) + String
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param dim Dimension ID
     * @param str String to include
     * @return Final packet
     */
    public static Packet250CustomPayload constructLocationWithString(int x, int y, int z, int dim, String str) {
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(bos);
        try {
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeInt(z);
            stream.writeInt(dim);
            stream.writeUTF(str);
        } catch (Exception ex) {
            FMLCommonHandler.instance().raiseException(ex, "Issue constructing Packet 250 instance", true);
        }

        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        return pkt;
    }

    /**
     * Packet parser - Location (with dimID) + String
     * </p>
     * Returns array containing: int[4] (x, y, z, dimID) and String (from packet)
     *
     * @param pkt Packet to parse
     * @return Array as described above.
     */
    public static Object[] parseLocationWithString(Packet250CustomPayload pkt) {
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(pkt.data));

        int[] location = new int[4];
        String str = "";

        try {
            location[0] = input.readInt();
            location[1] = input.readInt();
            location[2] = input.readInt();
            location[3] = input.readInt();
            str = input.readUTF();
        } catch (IOException ex) {
            FMLCommonHandler.instance().raiseException(ex, "Issue parsing Packet 250 instance", true);
        }

        return new Object[]{location, str};
    }

}
