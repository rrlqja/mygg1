package song.mygg1.domain.riot.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class StringListConvertor implements AttributeConverter<List<String>, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 직렬화 실패", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        try {
            return mapper.readValue(
                    json, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON 역직렬화 실패", e);
        }
    }
}
