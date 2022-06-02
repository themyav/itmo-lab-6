package server.models.comparators;

import server.models.enumerations.fields.DradonField;
import server.models.elements.ServerDragon;

import java.util.Comparator;

public class SizeComparator implements Comparator<ServerDragon> {
    @Override
    public int compare(ServerDragon a, ServerDragon b){

        com.lab6.serialization.SerializationProtos.Dragon.Builder d1 = com.lab6.serialization.SerializationProtos.Dragon.newBuilder();
        com.lab6.serialization.SerializationProtos.Dragon.Builder d2 = com.lab6.serialization.SerializationProtos.Dragon.newBuilder();

        for(DradonField value : DradonField.values()){
            value.setSerialized(a, d1);
            value.setSerialized(b, d2);
        }
        Integer first = d1.build().toByteArray().length;
        Integer second = d2.build().toByteArray().length;
        return first.compareTo(second);
    }
}