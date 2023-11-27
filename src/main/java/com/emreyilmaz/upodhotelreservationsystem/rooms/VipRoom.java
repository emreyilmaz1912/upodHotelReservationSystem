package com.emreyilmaz.upodhotelreservationsystem.rooms;
import com.emreyilmaz.upodhotelreservationsystem.Room;

import java.util.ArrayList;
import java.util.List;
public class VipRoom extends Room {
    public VipRoom(int roomId, String roomName, int capacity, double price, List<String> features, int totalRooms,int availableRooms) {
        super(roomId, roomName, capacity, price, features,totalRooms,availableRooms);
        this.setFeatures(defineFeatures());
    }

    public VipRoom() {

    }

    @Override
    protected void setTypeName() {
        this.typeName = getTypeName();
    }


    @Override
    protected List<String> defineFeatures() {
        List<String> specificFeatures = new ArrayList<>();
        specificFeatures.add("Sitting area and bedroom" );
        specificFeatures.add("Large double bed");
        specificFeatures.add("Mini kitchen or kitchenette");
        specificFeatures.add("Private bathroom and toilet");
        specificFeatures.addAll(commonFeatures);
        return specificFeatures;
    }
    @Override
    protected Room createRoom(int roomId, String roomName, int capacity, double price, List<String> features, int totalRooms,int availableRooms) {
        VipRoom juniorSuite = new VipRoom(roomId, roomName, capacity, price,features ,totalRooms, availableRooms);
        juniorSuite.setTypeName(roomName);
        return juniorSuite;
    }

    @Override
    protected String getTypeName() {
        return "Vip room";
    }
}
