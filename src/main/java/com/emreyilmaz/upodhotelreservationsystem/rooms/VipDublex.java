package com.emreyilmaz.upodhotelreservationsystem.rooms;

import com.emreyilmaz.upodhotelreservationsystem.Room;

import java.util.ArrayList;
import java.util.List;

public class VipDublex  extends Room{
    public VipDublex(int roomId, String roomName, int capacity, double price, List<String> features, int totalRooms, int availableRooms) {
        super(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
        this.setFeatures(defineFeatures());
    }

    public VipDublex() {

    }

    @Override
    protected void setTypeName() {
        this.typeName = getTypeName();
    }



    @Override
    public String getTypeName() {
        return "Vip Dublex";
    }

    @Override
    protected List<String> defineFeatures() {
        List<String> specificFeatures = new ArrayList<>();
        specificFeatures.add("Sitting area and bedroom");
        specificFeatures.add("Large double bed");
        specificFeatures.add("Mini kitchen or kitchenette");
        specificFeatures.add("Double bed or two single beds");
        specificFeatures.add("Private bathroom and toilet");
        specificFeatures.addAll(commonFeatures);

        return specificFeatures;

    }

    @Override
    protected Room createRoom(int roomId, String roomName, int capacity, double price, List<String> features, int totalRooms, int availableRooms) {
        VipDublex honeymoonRoom = new VipDublex(roomId, roomName, capacity, price, features, totalRooms, availableRooms);
        honeymoonRoom.setTypeName(roomName);
        return honeymoonRoom;
    }
}
