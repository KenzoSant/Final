package com.fatec.smart_parking.color;

import com.fatec.smart_parking.core.exception.ColorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public Color findByName(String name) {
        Color color = this.colorRepository.findByName(name)
                .orElseThrow(ColorNotFoundException::new);
        return color;

    }

    public List<ColorDTO> findAll() {
        List<Color> colors = this.colorRepository.findAll();
        return colors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    public ColorDTO convertToDTO(Color color) {
        return new  ColorDTO(color.getName());
    }
}
