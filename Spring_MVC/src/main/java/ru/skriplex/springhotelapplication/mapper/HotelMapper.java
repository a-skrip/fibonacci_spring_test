package ru.skriplex.springhotelapplication.mapper;

import org.mapstruct.Mapper;
import ru.skriplex.springhotelapplication.entity.Hotel;
import ru.skriplex.springhotelapplication.model.HotelModel;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    Hotel toEntity(HotelModel hotelModel);

    HotelModel toModel(Hotel hotel);

}
