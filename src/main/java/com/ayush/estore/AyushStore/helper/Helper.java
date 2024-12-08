package com.ayush.estore.AyushStore.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.ayush.estore.AyushStore.dtos.PageableResponse;
import com.ayush.estore.AyushStore.dtos.UserDtos;
import com.ayush.estore.AyushStore.entities.User;

public class Helper {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> userspage, Class<V> type) {
        List<U> entity = userspage.getContent();

        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(
                object, type))
                .collect(Collectors.toList());
        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(userspage.getNumber());
        response.setPageSize(userspage.getSize());
        response.setTotalElements(userspage.getTotalElements());
        response.setTotalPages(userspage.getTotalPages());
        response.setLastPage(userspage.isLast());
        return response;
    }

}
